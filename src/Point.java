/**
 * Point avec coordonn�es x y
 */
public class Point {

    /**
     * Coordonn�es x
     */
    protected double x;

    /**
     * Coordonn�es y
     */
    protected double y;

    /**
     * Constructeur vide
     */
    public Point() {
    }

    /**
     * Constructeur avec x y
     *
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * this.x += x
     *
     * @param x valeur � ajouter
     */
    public void addX(double x) {
        this.x += x;
    }

    /**
     * this.y += y
     *
     * @param y valeur � ajouter
     */
    public void addY(double y) {
        this.y += y;
    }

    /**
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * setter X
     *
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * setter Y
     *
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }
}
