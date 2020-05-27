
public class Trace {
	protected Point a;
	protected Point b;
	
	public Trace(Point a, Point b) {
		this.a = a;
		this.b = b;
	}
	
	public Point getA() {
		return a;
	}

	public Point getB() {
		return b;
	}
	
	public boolean croiseTrace(Trace trace) {
		Point E = substract(this.b, this.a);
		Point F = substract(trace.getB(), trace.getA());
		Point P = new Point(-E.getY(), E.getX());
		double result = scal(substract(this.a, trace.getB()), P) / scal(F, P);
		return (result >= 0 && result <= 1);
	}
	
	private double scal(Point a, Point b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}
	
	private Point substract(Point a, Point b) {
		return new Point(a.getX() - b.getX(), a.getY() - b.getY());
	}
}
