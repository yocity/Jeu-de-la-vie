package src.modele;

import java.util.Objects;

/**
 * classe représentant une cellule dans un jeu de la vie
 */
public class Cellule {

    int x, y;

    /**
     * constructeur de la cellule
     *
     * @param x Coordonnée x horizontale
     * @param y Coordonnée y verticale
     */
    public Cellule(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * vérifier si deux cellules sont égales
     *
     * @param o Objet à comparer avec la cellule
     * @return true Si les coordonnées des deux cellules sont égales , sinon
     * false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cellule cellule = (Cellule) o;
        return x == cellule.x && y == cellule.y;
    }

    /**
     * générer un code de hachage basé sur les coordonnées de la cellule
     *
     * @return la code de hachage de la cellule
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
