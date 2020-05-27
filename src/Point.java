
/**
 * Point avec coordonnées x y
 *
 */
public class Point {
	
	/**
	 * Coordonnées x
	 */
	protected double x;
	
	/**
	 * Coordonnées y
	 */
	protected double y;
	
	/**
	 * Constructeur vide
	 */
	public Point() {};
	
	/**
	 * Constructeur avec x y
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * setter X
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * this.x += x 
	 * @param x valeur à ajouter
	 */
	public void addX(double x) {
		this.x += x;
	}

	/**
	 * setter Y
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * this.y += y
	 * @param y valeur à ajouter
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
	 * @return y
	 */
	public double getY() {
		return y;
	}
}
