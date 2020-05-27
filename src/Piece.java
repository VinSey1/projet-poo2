import java.lang.Math;

public class Piece {
	
	String trace;
	double hautGauche[] = new double[2];
	double basDroit[] = new double[2];;
	
	public Piece(double pos[], String trace) {
		
		String[] d = trace.split(" ");
		for(int i = 0; i<d.length; i++) {
			if(d[i].equals("M") || d[i].equals("m")) {
				
			}
			String[] tmpString = d[i].split(",");
			if(tmpString.length == 2) {
				double tmp1 = Double.parseDouble(tmpString[0]);
				double tmp2 = Double.parseDouble(tmpString[1]);
			}
		}
	}
	
	private double[] calculTraceBezier(double p0, double p1, double p2, double p3) {
		double[] result = new double[10];
		for(int i = 0; i < result.length; i++) {
			result[i] =
			Math.pow((1-i), 3*p0) + 
			Math.pow(3*(1-i), 2*i*p1) +
			Math.pow(3*(1-i)*i, 2*p2) +
			Math.pow(i, 3*p3);
		}
		return result;
	}
}