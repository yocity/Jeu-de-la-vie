package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.modele.Cellule;

import java.util.HashSet;

/**
 * Tests pour la classe Cellule.
 * On vérifie l'égalité, le hashCode, et le comportement dans les HashSet.
 */
public class CelluleTest {

    private Cellule c1;
    private Cellule c2;
    private Cellule c3;

    /**
     * Initialise trois cellules pour les tests :
     * deux identiques (1,1), une différente (1,2).
     */
    @BeforeEach
    void setUp() {
        c1 = new Cellule(1, 1);
        c2 = new Cellule(1, 1);
        c3 = new Cellule(1, 2);
    }

    /**
     * Vérifie que deux cellules avec les mêmes coordonnées sont égales.
     * Et que des coordonnées différentes donnent des objets non égaux.
     */
    @Test
    void testEquals() {
        assertEquals(c1, c2);
        assertNotEquals(c2, c3);
    }

    /**
     * Vérifie que les hashCodes suivent bien equals().
     * Et que le HashSet peut retrouver les objets.
     */
    @Test
    void testHashCode() {
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c2.hashCode(), c3.hashCode());

        HashSet<Cellule> celluleSet = new HashSet<>();
        celluleSet.add(c1);
        assertTrue(celluleSet.contains(c2));
        assertFalse(celluleSet.contains(c3));
    }
}

