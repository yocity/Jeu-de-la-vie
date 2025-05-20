package src.vuecontroleur;

import src.modele.UniversModel;
import src.util.EcouteurModele;

/**
 * Vue console permettant d'afficher l'évolution d'un univers sur la grille.
 * Cette classe observe un modèle et l'affiche en texte dans la console.
 */
public class UniversViewConsole implements EcouteurModele {

    /**
     * Référence au modèle de l'univers.
     */
    private UniversModel model;

    /**
     * Constructeur de la vue console. Initialise la référence au modèle et s'y
     * enregistre comme écouteur.
     *
     * @param model Le modèle de l'univers à observer.
     */
    public UniversViewConsole(UniversModel model) {
        this.model = model;
        this.model.ajoutEcouteur(this);
    }

    /**
     * Affiche une seule génération de l'univers dans la console. La grille
     * affichée est de 80x80, avec les cellules actives ou inactives. Les
     * cellules actives sont représentées par "●", les inactives par "○".
     */
    public void afficheOneGen() {
        int size = 80;
        for (int y = 0; y < size; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < size; x++) {
                int cell = model.getCell(x, y);
                line.append(cell == 1 ? "● " : "○ ");
            }
            System.out.println(line);
        }
    }

    /**
     * Méthode appelée lorsque le modèle notifie un changement. Cette méthode
     * actualise l'affichage en console à chaque mise à jour.
     */
    @Override
    public void actualise() {
        afficheOneGen();
    }

    /**
     * Lance une simulation en continu dans la console. À chaque itération, elle
     * fait avancer le modèle d'une génération, affiche les statistiques et
     * attend une seconde.
     */
    public void runSimulation() {
        String message = "Bienvenue dans le super jeu de la vie!\n\n"
                + "Le Jeu de la Vie est un automate cellulaire inventé par John Conway.\n";
        System.out.println(message);
        while (true) {
            long startTime = System.nanoTime();
            model.runStep();
            long endTime = System.nanoTime();

            double seconds = (endTime - startTime) / 1_000_000_000.0;
            System.out.println(model.stats());
            System.out.println("Temps écoulé en secondes: " + seconds);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
