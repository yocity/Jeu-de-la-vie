package src.modele;

import src.util.AbstractModeleEcoutable;

/**
 * Classe abstraite représentant un modèle d'univers dans une simulation.
 * Fournit les méthodes essentielles à implémenter pour gérer l'état d'une
 * grille.
 */
public abstract class UniversModel extends AbstractModeleEcoutable {

    /**
     * Modifie l'état de la cellule à la position (x, y).
     *
     * @param x La position horizontale de la cellule.
     * @param y La position verticale de la cellule.
     */
    public abstract void setCellule(int x, int y);

    /**
     * Exécute une étape de la simulation (une évolution de l'univers).
     */
    public abstract void runStep();

    /**
     * Renvoie des statistiques sur l'état actuel de la simulation.
     *
     * @return Une chaîne de caractères représentant les statistiques.
     */
    public abstract String stats();

    /**
     * Obtient l'état actuel de la cellule située en (x, y).
     *
     * @param x La position horizontale de la cellule.
     * @param y La position verticale de la cellule.
     * @return L'état de la cellule (valeur entière).
     */
    public abstract int getCell(int x, int y);

    /**
     * Initialise ou réinitialise la grille de l'univers.
     */
    public abstract void initialiseGrille();
}
