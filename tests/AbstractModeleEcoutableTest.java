package tests;

import org.junit.jupiter.api.Test;

import src.util.AbstractModeleEcoutable;
import src.util.EcouteurModele;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour la classe AbstractModeleEcoutable.
 * On vérifie que les écouteurs sont bien notifiés ou ignorés selon les cas.
 */
public class AbstractModeleEcoutableTest {

    /**
     * Implémentation factice d'un écouteur, utilisée pour tester les notifications.
     */
    static class FakeEcouteur implements EcouteurModele {
        boolean wasCalled = false;

        @Override
        public void actualise() {
            wasCalled = true;
        }
    }

    /**
     * Vérifie que l'ajout d'un écouteur permet bien d'être notifié via fireChangement().
     */
    @Test
    void testAjoutEcouteurEtNotification() {
        AbstractModeleEcoutable modele = new AbstractModeleEcoutable();
        FakeEcouteur ecouteur = new FakeEcouteur();

        modele.ajoutEcouteur(ecouteur);
        modele.fireChangement();

        assertTrue(ecouteur.wasCalled);
    }

    /**
     * Vérifie qu'un écouteur retiré ne reçoit plus de notification.
     */
    @Test
    void testRetraitEcouteur() {
        AbstractModeleEcoutable modele = new AbstractModeleEcoutable();
        FakeEcouteur ecouteur = new FakeEcouteur();

        modele.ajoutEcouteur(ecouteur);
        modele.retraitEcouteur(ecouteur);
        modele.fireChangement();

        assertFalse(ecouteur.wasCalled);
    }
}

