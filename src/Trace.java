
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
	
	public boolean croiseTrace(Trace b) {
		return false;
	}
}
