package src.modele;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Classe représentant un arbre (noeud interne) du quadtree pour le jeu de la
 * vie elle permet de modéliser , faire évoluer et optimiser l'univers avec
 * mémoire et parallélisme
 */
public class Arbre extends NoeudBase {

    private static final HashMap<Arbre, Arbre> memoizedCache = new HashMap<>();

    /**
     * Calcule l'etat d'une cellule selon les régle du jeu de la vie
     *
     * @param bitmask un entier représentant l'état d'une grille 3*3 encodé sur
     * 9 bits
     * @return un arbre contenant une cellule vivant ou mort selon le nombre de
     * voisins
     */
    public Arbre oneGen(int bitmask) {
        if (bitmask == 0) {
            return creer(false);
        }
        int self = (bitmask >> 5) & 1;
        bitmask &= 0x757;
        int neighborCount = 0;
        while (bitmask != 0) {
            neighborCount++;
            bitmask &= bitmask - 1;
        }
        if (neighborCount == 3 || (neighborCount == 2 && self != 0)) {
            return creer(true); 
        }else {
            return creer(false);
        }
    }

    /**
     * Simule lentement l’évolution d’un nœud de niveau 2. Balaye les cellules
     * dans un carré 4x4 centré, calcule les voisins, et applique la règle du
     * jeu pour générer un nouvel arbre.
     *
     * @return un nouvel arbre représentant l’évolution de la génération
     * actuelle
     */
    public Arbre slowSimulation() {
        int allbits = 0;
        for (int y = -2; y < 2; y++) {
            for (int x = -2; x < 2; x++) {
                allbits = (allbits << 1) + getCellule(x, y);
            }
        }
        return creer(oneGen(allbits >> 5), oneGen(allbits >> 4),
                oneGen(allbits >> 1), oneGen(allbits));
    }

    /**
     * Extrait la partie centrale du quadtree courant. Il s’agit du sous-arbre
     * formé par les centres de ses quatre quadrants.
     *
     * @return un nouvel arbre représentant le centre de l’arbre actuel
     */
    public Arbre centeredSubArbre() {
        return creer(hg.bd, hd.bg, bg.hd, bd.hg);
    }

    /**
     * Construit un sous-arbre horizontal centré à partir de deux arbres
     * adjacents. Combine la partie droite de l’arbre de gauche (w) et la partie
     * gauche de l’arbre de droite (e), pour former une nouvelle bande verticale
     * au centre
     *
     * @param w arbre à gauche (west)
     * @param e arbre à droite (east)
     * @return un nouvel arbre représentant la bande verticale centrée entre w
     * et e
     */
    public Arbre centeredHorizontal(Arbre w, Arbre e) {
        return creer(w.hd.bd, e.hg.bg, w.bd.hd, e.bg.hg);
    }

    /**
     * Construit un sous-arbre vertical centré à partir de deux arbres empilés.
     * Combine la partie basse de l’arbre du haut (n) et la partie haute de
     * l’arbre du bas (s), pour former une nouvelle bande horizontale centrée
     *
     * @param n arbre du haut (north)
     * @param s arbre du bas (south)
     * @return un nouvel arbre représentant la bande horizontale centrée entre n
     * et s
     */
    public Arbre centeredVertical(Arbre n, Arbre s) {
        return creer(n.bg.bd, n.bd.bg, s.hg.hd, s.hd.hg);
    }

    /**
     * Extrait le sous-sous-arbre central du quadtree. C’est le cœur du cœur :
     * on prend le centre de chaque quadrant du centre.
     *
     * @return un nouvel arbre représentant la zone centrale profonde de l’arbre
     * courant
     */
    public Arbre centeredSubSubArbre() {
        return creer(hg.bd.bd, hd.bg.bg, bg.hd.hd, bd.hg.hg);
    }

    /**
     * Calcule la prochaine génération de l’arbre courant. Utilise la mémoire
     * pour éviter de recalculer les arbres déjà vus.
     *
     * @return un nouvel arbre représentant la génération suivante
     */
    public Arbre nextGeneration() {
        if (memoizedCache.containsKey(this)) {
            return memoizedCache.get(this);
        }

        Arbre nextGen;
        if (population == 0) {
            nextGen = hg;
        } else if (niveau == 2) {
            nextGen = slowSimulation();
        } else {
            // Créer les sous-arbres de manière parallèle
            CompletableFuture<Arbre> f00 = CompletableFuture.supplyAsync(() -> hg.centeredSubArbre());
            CompletableFuture<Arbre> f01 = CompletableFuture.supplyAsync(() -> centeredHorizontal(hg, hd));
            CompletableFuture<Arbre> f02 = CompletableFuture.supplyAsync(() -> hd.centeredSubArbre());
            CompletableFuture<Arbre> f10 = CompletableFuture.supplyAsync(() -> centeredVertical(hg, bg));
            CompletableFuture<Arbre> f11 = CompletableFuture.supplyAsync(() -> centeredSubSubArbre());
            CompletableFuture<Arbre> f12 = CompletableFuture.supplyAsync(() -> centeredVertical(hd, bd));
            CompletableFuture<Arbre> f20 = CompletableFuture.supplyAsync(() -> bg.centeredSubArbre());
            CompletableFuture<Arbre> f21 = CompletableFuture.supplyAsync(() -> centeredHorizontal(bg, bd));
            CompletableFuture<Arbre> f22 = CompletableFuture.supplyAsync(() -> bd.centeredSubArbre());

            // Attendre les résultats des calculs
            Arbre n00 = f00.join();
            Arbre n01 = f01.join();
            Arbre n02 = f02.join();
            Arbre n10 = f10.join();
            Arbre n11 = f11.join();
            Arbre n12 = f12.join();
            Arbre n20 = f20.join();
            Arbre n21 = f21.join();
            Arbre n22 = f22.join();

            // Créer la prochaine génération
            nextGen = creer(
                    creer(n00, n01, n10, n11).nextGeneration(),
                    creer(n01, n02, n11, n12).nextGeneration(),
                    creer(n10, n11, n20, n21).nextGeneration(),
                    creer(n11, n12, n21, n22).nextGeneration()
            );
        }

        memoizedCache.put(this, nextGen);
        return nextGen;
    }

    public Arbre(boolean living) {
        super(living);
    }

    /**
     * Construit un arbre interne à partir de ses quatre sous-arbres.
     *
     * @param hg quadrant supérieur gauche
     * @param hd quadrant supérieur droit
     * @param bg quadrant inférieur gauche
     * @param bd quadrant inférieur droit
     */
    public Arbre(Arbre hg, Arbre hd, Arbre bg, Arbre bd) {
        super(hg, hd, bg, bd);
    }

    /**
     * Calcule le code de hachage de l’arbre. Pour un nœud feuille, le hash
     * dépend du niveau et de l’état (vivant ou non), pour un nœud interne, il
     * dépend de ses sous-arbres
     *
     * @return un entier représentant le hash de l’arbre
     */
    @Override
    public int hashCode() {
        if (niveau == 0) {
            return Objects.hash(niveau, estVivant);
        } else {
            return Objects.hash(niveau, hg, hd, bg, bd);
        }
    }

    /**
     * Compare deux arbres pour vérifier s’ils sont structurellement
     * équivalents. Deux arbres sont égaux s’ils ont le même niveau et les mêmes
     * sous-arbres (ou même état si niveau 0).
     *
     * @param o l’objet à comparer
     * @return true si les arbres sont identiques, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Arbre t = (Arbre) o;
        if (niveau != t.niveau) {
            return false;
        }
        if (niveau == 0) {
            return estVivant == t.estVivant;
        } else {
            return hg.equals(t.hg) && hd.equals(t.hd) && bg.equals(t.bg) && bd.equals(t.bd);
        }
    }

}
