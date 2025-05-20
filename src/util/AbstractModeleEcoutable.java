package src.util;

import java.util.ArrayList;

/**
 * Classe abstraite représentant un modèle pouvant notifier ses écouteurs lors
 * d'un changement.
 * <p>
 * Elle est utilisée dans le cadre du pattern Observateur (Observer), où des
 * vues ou contrôleurs peuvent s'abonner à un modèle pour être notifiés des
 * changements d'état.
 * </p>
 */
public class AbstractModeleEcoutable {

    private ArrayList<EcouteurModele> ecouteurs = new ArrayList<>();

    /**
     * Ajoute un écouteur qui sera notifié lorsque le modèle change.
     *
     * @param e l'écouteur à ajouter
     */
    public void ajoutEcouteur(EcouteurModele e) {
        ecouteurs.add(e);
    }

    /**
     * Retire un écouteur de la liste des écouteurs du modèle.
     *
     * @param e l'écouteur à retirer
     */
    public void retraitEcouteur(EcouteurModele e) {
        ecouteurs.remove(e);
    }

    /**
     * Notifie tous les écouteurs que le modèle a changé. Appelle la méthode
     * {@code actualise()} sur chaque écouteur inscrit.
     */
    public void fireChangement() {
        for (EcouteurModele e : ecouteurs) {
            e.actualise();
        }
    }
}
