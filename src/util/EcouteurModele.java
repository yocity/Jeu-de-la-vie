package src.util;

/**
 * Interface du pattern Observateur utilisée pour notifier les vues lorsqu'une
 * modification survient dans le modèle.
 *
 * Toute classe qui implémente cette interface peut s'abonner à un modèle (comme
 * UniversModel) pour être avertie des changements et mettre à jour son
 * affichage ou son état.
 */
public interface EcouteurModele {

    /**
     * Méthode appelée par le modèle lorsqu'une mise à jour est nécessaire. Les
     * vues qui écoutent le modèle doivent implémenter cette méthode pour
     * actualiser leur affichage.
     */
    void actualise();

}
