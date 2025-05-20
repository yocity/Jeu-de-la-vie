package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.modele.Arbre;
import src.modele.NoeudBase;

/**
 * Tests pour la classe Arbre.
 * On vérifie l'évolution, les sous-arbres, l'égalité et le hashCode.
 */
public class ArbreTest {

    private Arbre arbreMort;
    private Arbre arbreVivant;

    /**
     * Crée un arbre mort et un arbre vivant avant chaque test.
     */
    @BeforeEach
    void setUp() {
        NoeudBase noeudMort = new NoeudBase(false);
        NoeudBase noeudVivant = new NoeudBase(true);
        arbreMort = noeudMort.creer(false);
        arbreVivant = noeudVivant.creer(true);
    }

    /**
     * Vérifie que oneGen() respecte les règles de vie en fonction du bitmask.
     */
    @Test
    void testOneGen() {
        Arbre newGen1 = arbreMort.oneGen(0);
        Arbre newGen2 = arbreVivant.oneGen(0b000111010);

        assertFalse(newGen1.getEstVivant());
        assertTrue(newGen2.getEstVivant());
    }

    /**
     * Vérifie que slowSimulation() retourne bien un nouvel arbre.
     */
    @Test
    void testSlowSimulation() {
        Arbre simulated = arbreVivant.slowSimulation();
        assertNotNull(simulated);
    }

    /**
     * Teste la récupération du sous-arbre centré.
     */
    @Test
    void testCenteredSubArbre() {
        Arbre arbre = new NoeudBase(false).arbreVide(3);
        Arbre subArbre = arbre.centeredSubArbre();
        assertNotNull(subArbre);
    }

    /**
     * Vérifie que nextGeneration() retourne une génération suivante valide.
     */
    @Test
    void testNextGeneration() {
        Arbre arbre = new NoeudBase(false).arbreVide(3);
        Arbre nextGen = arbre.nextGeneration();
        assertNotNull(nextGen);
    }

    /**
     * Vérifie que equals() compare bien les arbres.
     */
    @Test
    void testEquals() {
        Arbre autreArbre = new NoeudBase(true).creer(true);
        assertEquals(arbreVivant, autreArbre);
        assertNotEquals(arbreVivant, arbreMort);
    }

    /**
     * Vérifie que deux arbres identiques ont le même hashCode().
     */
    @Test
    void testHashCode() {
        Arbre autreArbre = new NoeudBase(true).creer(true);
        assertEquals(arbreVivant.hashCode(), autreArbre.hashCode());
    }
}

