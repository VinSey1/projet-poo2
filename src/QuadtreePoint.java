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
