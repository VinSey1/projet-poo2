/**
 * Ligne entre deux points
 */
public class Trace {

    /**
     * Origine du segment
     */
    protected Point a;

    /**
     * Fin du segment
     */
    protected Point b;

    /**
     * Constructeur avec a b
     *
     * @param a
     * @param b
     */
    public Trace(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    /**
     * @return a
     */
    public Point getA() {
        return a;
    }

    /**
     * @return b
     */
    public Point getB() {
        return b;
    }

    /**
     * Permet de v�rifier si deux traces se croisent
     *
     * @param trace trace � v�rifier
     * @return boolean
     */
    public boolean croiseTrace(Trace trace) {
        Point E = substract(this.b, this.a);
        Point F = substract(trace.getB(), trace.getA());
        Point P = new Point(-E.getY(), E.getX());
        double result = scal(substract(this.a, trace.getB()), P) / scal(F, P);
        return (result >= 0 && result <= 1);
    }

    /**
     * Produit scaleur de points
     *
     * @param a
     * @param b
     * @return
     */
    private double scal(Point a, Point b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    /**
     * Soustraction de points
     *
     * @param a
     * @param b
     * @return
     */
    private Point substract(Point a, Point b) {
        return new Point(a.getX() - b.getX(), a.getY() - b.getY());
    }
}
