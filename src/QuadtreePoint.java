/**
 * Représente un point qui sera élément de notre {@link Quadtree}. Contient une coordonnée x, y ainsi qu'une longueur et une largeur.
 */
public class QuadtreePoint {

    private final double x, y, longX, longY;

    public QuadtreePoint(double x, double y, double longX, double longY) {
        this.x = x;
        this.y = y;
        this.longX = longX;
        this.longY = longY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLongX() {
        return longX;
    }

    public double getLongY() {
        return longY;
    }
}
