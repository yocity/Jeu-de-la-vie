package tests;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.modele.Arbre;
import src.modele.NoeudBase;

/**
 * Tests pour la classe NoeudBase.
 * On vérifie les cas de base : population, niveau, état de vie, etc.
 * 
 * Compilation :
 * javac -cp .:build:junit-platform-console-standalone-1.8.1.jar -d build tests_unitaire/*.java
 *
 * Exécution :
 * java -jar junit-platform-console-standalone-1.8.1.jar --class-path build --scan-class-path
 */
public class NoeudBaseTest {

    private NoeudBase noeudVide;
    private NoeudBase noeudVivant;

    /**
     * Initialise un noeud mort et un noeud vivant avant chaque test.
     */
    @BeforeEach
    void setUp() {
        noeudVide = new NoeudBase(false); 
        noeudVivant = new NoeudBase(true);
    }

    /**
     * Un noeud mort doit avoir une population de 0, un vivant de 1.
     */
    @Test
    void testGetPopulation() {
        assertEquals(0, noeudVide.getPopulation());
        assertEquals(1, noeudVivant.getPopulation());
    }

    /**
     * Tous les noeuds de base doivent être de niveau 0.
     */
    @Test
    void testGetNiveau() {
        assertEquals(0, noeudVide.getNiveau());
        assertEquals(0, noeudVivant.getNiveau());
    }

    /**
     * Vérifie l'état vivant/mort selon le constructeur.
     */
    @Test
    void testGetEstVivant() {
        assertFalse(noeudVide.getEstVivant());
        assertTrue(noeudVivant.getEstVivant());
    }

    /**
     * Après setCellule, le noeud doit devenir vivant.
     */
    @Test
    void testSetCellule() {
        NoeudBase noeudChanger = (NoeudBase) noeudVide.setCellule(0, 0);
        assertTrue(noeudChanger.getEstVivant());
    }

    /**
     * Crée un arbre vide de niveau 5 et vérifie sa validité.
     */
    @Test
    void testArbreVide() {
        Arbre arbreVide = noeudVide.arbreVide(5);
        assertNotNull(arbreVide);
        assertEquals(5, arbreVide.getNiveau());
        assertEquals(0, arbreVide.getPopulation());
    }

    /**
     * L'univers doit s'agrandir d'un niveau.
     */
    @Test
    void testAgrandirUniverse() {
        Arbre arbre = noeudVide.arbreVide(5);
        Arbre univers = arbre.agrandirUniverse();
        assertNotNull(univers);
        assertEquals(6, univers.getNiveau());
    }
}

