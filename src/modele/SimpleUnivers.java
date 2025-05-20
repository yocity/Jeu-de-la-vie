package src.modele;

import java.util.*;

/**
 * Implémentation simple du Jeu de la Vie basée sur une grille avec des
 * coordonnées explicites. Utilise un ensemble de cellules vivantes, sans
 * structure de quadtree.
 */
public class SimpleUnivers extends UniversModel {

    private Set<Cellule> grille;
    private double nbgeneration = 0;

    /**
     * Crée un nouvel univers vide sans aucune cellule vivante.
     */
    public SimpleUnivers() {
        grille = new HashSet<>();
    }

    /**
     * Mettre une cellule vivante aux coordonnées données.
     *
     * @param x position horizontale
     * @param y position verticale
     */
    @Override
    public void setCellule(int x, int y) {
        grille.add(new Cellule(x, y));
    }

    /**
     * Vérifie si une cellule aux coordonnées données est vivante.
     *
     * @param x position horizontale
     * @param y position verticale
     * @return 1 si la cellule est vivante, 0 sinon
     */
    @Override
    public int getCell(int x, int y) {
        return grille.contains(new Cellule(x, y)) ? 1 : 0;
    }

    /**
     * Calcule les huit voisins autour d’une cellule donnée.
     *
     * @param cellule la cellule dont on cherche les voisins
     * @return une liste des cellules voisines
     */
    private List<Cellule> getVoisins(Cellule cellule) {
        List<Cellule> voisins = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    voisins.add(new Cellule(cellule.x + dx, cellule.y + dy));
                }
            }
        }
        return voisins;
    }

    /**
     * Effectue une étape d’évolution de l’univers selon les règles du Jeu de la
     * Vie. Met à jour les cellules vivantes et incrémente le nombre de
     * générations.
     */
    @Override
    public void runStep() {
        Map<Cellule, Integer> voisinsComptes = new HashMap<>();

        for (Cellule cellule : grille) {
            for (Cellule voisin : getVoisins(cellule)) {
                voisinsComptes.put(voisin, voisinsComptes.getOrDefault(voisin, 0) + 1);
            }
        }

        Set<Cellule> nouvellesCellules = new HashSet<>();
        for (Map.Entry<Cellule, Integer> entry : voisinsComptes.entrySet()) {
            Cellule cellule = entry.getKey();
            int count = entry.getValue();
            if (count == 3 || (grille.contains(cellule) && (count == 2 || count == 3))) {
                nouvellesCellules.add(cellule);
            }
        }

        grille = nouvellesCellules;
        nbgeneration++;
        this.fireChangement();
    }

    /**
     * Retourne un résumé de l’état actuel de l’univers : génération et nombre
     * de cellules vivantes.
     *
     * @return une chaîne descriptive de l’univers
     */
    @Override
    public String stats() {
        return "Generation: " + (int) nbgeneration + ", Nombre de cellules vivantes : " + grille.size();

    }

    /**
     * Réinitialise l’univers : vide la grille et remet le compteur de
     * génération à zéro.
     */
    public void initialiseGrille() {
        nbgeneration = 0;
        grille = new HashSet<>();
    }
}
