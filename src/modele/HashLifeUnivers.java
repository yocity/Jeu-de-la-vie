package src.modele;

/**
 * Implémentation du modèle d'univers basée sur l'algorithme HashLife. Utilise
 * une structure quadtree optimisée pour simuler le Jeu de la Vie efficacement.
 */
public class HashLifeUnivers extends UniversModel {

    protected Arbre racine = Arbre.creer();
    protected double nbgeneration = 0;

    /**
     * Active une cellule aux coordonnées (x, y). Si nécessaire, agrandit
     * récursivement l'univers pour inclure cette cellule.
     *
     * @param x Coordonnée horizontale
     * @param y Coordonnée verticale
     */
    @Override
    public void setCellule(int x, int y) {
        while (true) {
            int maxCoordinate = 1 << (racine.getNiveau() - 1);
            if (-maxCoordinate <= x && x <= maxCoordinate - 1 && -maxCoordinate <= y && y <= maxCoordinate - 1) {
                break;
            }
            racine = racine.agrandirUniverse();
        }

        racine = racine.setCellule(x, y);
    }

    /**
     * Exécute une étape de simulation. Agrandit l'univers si besoin, puis
     * calcule la génération suivante.
     */
    public void runStep() {
        while (racine.getNiveau() < 3 || racine.hg.population != racine.hg.bd.bd.population
                || racine.hd.population != racine.hd.bg.bg.population
                || racine.bg.population != racine.bg.hd.hd.population
                || racine.bd.population != racine.bd.hg.hg.population) {
            racine = racine.agrandirUniverse();
        }
        racine = racine.nextGeneration();
        nbgeneration++;
        this.fireChangement();
    }

    /**
     * Récupère l'état de la cellule à la position donnée.
     *
     * @param x Coordonnée horizontale
     * @param y Coordonnée verticale
     * @return 1 si vivante, 0 sinon
     */
    public int getCellule(int x, int y) {
        return racine.getCellule(x, y);
    }

    /**
     * Identique à {@link #getCellule(int, int)}.
     */
    public int getCell(int x, int y) {
        return racine.getCellule(x, y);
    }

    /**
     * Renvoie les statistiques courantes : génération et population.
     *
     * @return Chaîne descriptive des statistiques
     */
    public String stats() {
        return "Generation: " + (int) nbgeneration + ", Population: " + (int) racine.getPopulation();
    }

    /**
     * Réinitialise l'univers à une grille vide. Conserve le niveau courant mais
     * vide toutes les cellules.
     */
    public void initialiseGrille() {
        nbgeneration = 0;
        int niv = racine.getNiveau();
        racine = racine.arbreVide(niv);
    }
}
