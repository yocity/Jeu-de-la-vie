package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.modele.HashLifeUnivers;

/**
 * Tests pour la classe HashLifeUnivers.
 * On vérifie l'activation des cellules, leur état et l'évolution après un runStep.
 */
public class HashLifeUniversTest {

    private HashLifeUnivers h;

    /**
     * Initialise un univers vide avant chaque test.
     */
    @BeforeEach
    void setUp() {
        h = new HashLifeUnivers();
    }

    /**
     * Vérifie que getCellule() retourne 1 pour une cellule activée,
     * et 0 pour une cellule inactive.
     */
    @Test
    void testGetCellule() {
        h.setCellule(1, 1);
        assertEquals(1, h.getCellule(1, 1));
        assertEquals(0, h.getCellule(1, 2));
    }

    /**
     * Vérifie que setCellule() active correctement une cellule.
     */
    @Test
    void testSetCellule () {
        h.setCellule(0, 0);
        assertEquals(1, h.getCellule(0, 0));
    }

    /**
     * Vérifie que runStep() applique bien les règles du jeu
     * et fait évoluer l'univers comme attendu.
     */
    @Test
    void testRunStep() {
        h.setCellule(0, 1);
        h.setCellule(1, 0);
        h.setCellule(1, 1);
        h.runStep();

        assertEquals(1, h.getCellule(0, 0));
        assertEquals(1, h.getCellule(1, 1));
        assertEquals(1, h.getCellule(0, 1));
        assertEquals(1, h.getCellule(1, 0));
    }
}

