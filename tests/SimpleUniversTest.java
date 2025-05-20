package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.modele.Cellule;
import src.modele.SimpleUnivers;

/**
 * Tests pour la classe SimpleUnivers.
 * On vérifie la manipulation des cellules, l'évolution et les statistiques.
 */
public class SimpleUniversTest {

    private SimpleUnivers s;

    /**
     * Initialise un nouvel univers avant chaque test.
     */
    @BeforeEach
    void setUp() {
        s = new SimpleUnivers();
    }

    /**
     * Vérifie que setCellule() active bien une cellule,
     * et que les autres restent mortes.
     */
    @Test
    void testSetEtGetCellule() {
        s.setCellule(1, 1);
        assertEquals(1, s.getCell(1, 1));
        assertEquals(0, s.getCell(1, 2));
    }

    /**
     * Simule une génération pour vérifier que les cellules
     * évoluent correctement selon les règles du jeu.
     */
    @Test
    void testRunStep() {
        s.setCellule(0, 1);
        s.setCellule(1, 0);
        s.setCellule(1, 1);
        s.runStep();

        assertEquals(1, s.getCell(0, 0));
        assertEquals(1, s.getCell(1, 1));
        assertEquals(1, s.getCell(0, 1));
        assertEquals(1, s.getCell(1, 0));
    }

    /**
     * Vérifie que stats() retourne le bon nombre de cellules vivantes.
     */
    @Test
    void testStats() {
        s.setCellule(0, 1);
        s.setCellule(1, 0);
        s.setCellule(1, 1);
        assertTrue(s.stats().contains("Nombre de cellules vivantes : 3"));
    }
}

