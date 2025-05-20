package src.modele;

/**
 * classe représentant un nœud de base d'un quadtree
 */
public class NoeudBase {

    protected Arbre hg, hd, bg, bd;
    protected double population;
    protected boolean estVivant;
    protected int niveau;

    /**
     * Construire un nouveau NoeudBase,composé de quatre sous-arbres,chaque
     * sous-arbre représente l’un des quatre quadrants du nœud courant.
     *
     * @param hg sous arbre représentant le quadrant supérieur gauche
     * @param hd sous-arbre représentant le quadrant supérieur droit
     * @param bg sous-arbre représentant le quadrant inférieur gauche
     * @param bd sous-arbre représentant le quadrant inférieur droit
     */
    public NoeudBase(Arbre hg, Arbre hd, Arbre bg, Arbre bd) {
        this.hg = hg;
        this.hd = hd;
        this.bg = bg;
        this.bd = bd;
        this.population = hg.population + hd.population + bg.population + bd.population;
        this.estVivant = population > 0;
        this.niveau = hg.niveau + 1;
    }

    /**
     * constructeur de la noeud de base qui peut etre vivant ou mort
     *
     * @param estVivant si true signifie ce noeud est vivant ,sinon signifie ce
     * noued est mort
     */
    public NoeudBase(boolean estVivant) {
        this.hg = this.hd = this.bg = this.bd = null;
        this.population = estVivant ? 1 : 0;
        this.estVivant = estVivant;
        this.niveau = 0;
    }

    /**
     * obtient la population d'un noeud
     *
     * @return la nombre de cellule vivant dans ce noeud
     */
    public int getPopulation() {
        return (int) population;
    }

    /**
     * Renvoie le niveau du noeud dans le quadtree
     *
     * @return le niveau du noeud
     */
    public int getNiveau() {
        return niveau;
    }

    /**
     * Renvoie l'etat du noeud dans le quadtree
     *
     * @return {@code true} si le noeud est vivant, sinon {@code false}
     *
     */
    public boolean getEstVivant() {
        return estVivant;
    }

    /**
     * Créer un arbre qui avoir juste une cellue
     *
     * @param vivant si {@code ture}, l'arbre contient une cellule vivant,sinon
     * contient une cellule morte
     * @return un nouvel arbre de niveau 0
     */
    public Arbre creer(boolean vivant) {
        return new Arbre(vivant);
    }

    /**
     * Créer un arbre à partir de quatre sous-arbre
     *
     * @param hg sous arbre représentant le quadrant supérieur gauche
     * @param hd sous-arbre représentant le quadrant supérieur droit
     * @param bg sous-arbre représentant le quadrant inférieur gauche
     * @param bd sous-arbre représentant le quadrant inférieur droit
     * @return un nouvel arbre contenant les quatre quadrants donnés
     */
    public Arbre creer(Arbre hg, Arbre hd, Arbre bg, Arbre bd) {
        return new Arbre(hg, hd, bg, bd);
    }

    /**
     * Créer un arbre vide de niveau 3 avec des cellules mortes.
     *
     * @return un nouvel arbre vide de niveau 3
     */
    static Arbre creer() {
        return new Arbre(false).arbreVide(3);
    }

    /*
     * L’offset représente la distance (en termes d'unités de coordonnées) du centre
     * du nœud courant au centre de chacun de ses quadrants. L’expression 1 <<
     * (level - 2) correspond à 2^(niveau-2) .
     * Cette valeur permet d'ajuster correctement les coordonnées lors de la
     * descente dans l'arbre.
     */
    public int getCellule(int x, int y) {
        if (niveau == 0) {
            return estVivant ? 1 : 0;
        }
        int offset = 1 << (this.niveau - 2);
        if (x < 0) {
            if (y < 0) {
                return hg.getCellule(x + offset, y + offset); 
            }else {
                return bg.getCellule(x + offset, y - offset); 
            }
        }else if (y < 0) {
            return hd.getCellule(x - offset, y + offset); 
        }else {
            return bd.getCellule(x - offset, y - offset);
        }
    }

    /**
     * Mettre une cellule vivante aux coordonées (x,y) dans l'arbre. si le noeud
     * est niveau 0 , il devient une cellule vivantte sinon , la cellue est
     * ajoutée récursivement dans le sous-arbre correspondant
     *
     * @param x la coordonnée horizontale
     * @param y la coordonnée verticale
     * @return un nouvel arbre reflétant le changement
     */
    public Arbre setCellule(int x, int y) {
        if (niveau == 0) {
            return new Arbre(true);
        }

        int offset = 1 << (niveau - 2);
        if (x < 0) {
            if (y < 0) {
                return creer(hg.setCellule(x + offset, y + offset), hd, bg, bd); 
            }else {
                return creer(hg, hd, bg.setCellule(x + offset, y - offset), bd); 
            }
        }else if (y < 0) {
            return creer(hg, hd.setCellule(x - offset, y + offset), bg, bd); 
        }else {
            return creer(hg, hd, bg, bd.setCellule(x - offset, y - offset));
        }
    }

    /**
     * Créer un arbre vide d'un niveau donné tous les sous-arbres seront vides
     *
     * @param niv le niveau souhaité pour l'arbre
     * @return un arbre composé seulement de cellules mortes,de niveau
     * {@code niv}
     */
    public Arbre arbreVide(int niv) {
        if (niv == 0) {
            return creer(false);
        }
        Arbre n = arbreVide(niv - 1);
        return creer(n, n, n, n);
    }

    /**
     * Étend l'univers pour éviter qu'il ne devinne trop étroit quand
     * l'évolution approche des bords,cette méthod ajoute une couche vide tout
     * autour laisser de la place à la vie de s'étendre librement
     *
     * @return un arbre élargi d'un niveau, pret à acceillir les génération
     * futures
     *
     */
    public Arbre agrandirUniverse() {
        Arbre border = arbreVide(niveau - 1);
        return creer(creer(border, border, border, hg),
                creer(border, border, hd, border),
                creer(border, bg, border, border),
                creer(bd, border, border, border));
    }
}
