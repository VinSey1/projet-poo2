import java.lang.Math;

public class Piece {
	
	String trace;
	double hautGauche[] = new double[2];
	double basDroit[] = new double[2];;
	
	public Piece(double coordParent[], String trace) {
		double coordActuelle[] = coordParent;		
		String[] d = trace.split(" ");
		double maxX = coordParent[0];
		double maxY = coordParent[1];
		double minX = coordParent[0];
		double minY = coordParent[1];
		for(int i = 0; i<d.length; i++) {
			if(!d[i].replaceAll("[^0-9,.-]", "").equals("")) {
				String[] tmpString = d[i].split(",");
				coordActuelle[0] += Double.parseDouble(tmpString[0]);
				coordActuelle[1] += Double.parseDouble(tmpString[1]);
				if(coordActuelle[0] > maxX) maxX = coordActuelle[0];
				if(coordActuelle[1] > maxY) maxY = coordActuelle[1];
				if(coordActuelle[0] < minX) minX = coordActuelle[0];
				if(coordActuelle[1] < minY) minY = coordActuelle[1];
			} else {
				if(d[i].matches("[hH]")) {
					coordActuelle[0] += Double.parseDouble(d[i+1].split(",")[0]);
					i++;
				}
				if(d[i].matches("[vV]")) {
					coordActuelle[1] += Double.parseDouble(d[i+1].split(",")[0]);
					i++;
				}
				if(d[i].matches("[zZ]")) {
					// car Z "reset" la coordonnée
					coordActuelle = coordParent;
				}
				if(d[i].matches("[qQcCtTsS]")) {
					double traceBezier[] = new double[10];
					if(d[i].matches("[q]")) {
						traceBezier = calculTraceBezier(
								coordActuelle[0] + Double.parseDouble(d[i+1].split(",")[0]),
								coordActuelle[1] + Double.parseDouble(d[i+1].split(",")[1]),
								Double.parseDouble(d[i+2].split(",")[0]),
								Double.parseDouble(d[i+2].split(",")[1]));
						i = i+2;
					}
					if(d[i].matches("[Q]")) {
						traceBezier = calculTraceBezier(
								coordParent[0] + Double.parseDouble(d[i+1].split(",")[0]),
								coordParent[1] + Double.parseDouble(d[i+1].split(",")[1]),
								Double.parseDouble(d[i+2].split(",")[0]),
								Double.parseDouble(d[i+2].split(",")[1]));
						i = i+2;
					}
					if(d[i].matches("[cCtTsS]")) {
						// Je ne vois pas comment le traiter
					}
					// Trouver le minimum des points du tableau traceBezier et le max pour maxX/Y minX/Y
					// Faire les fonctions restantes
					// Sauvegarder toutes les fonctions de tracé dans une variable pour détecter les collisions
					// Faire l'optimisation en rajoutant un attribut "translate" à chaque path
					// Prendre en compte les groupes de paths
					// Afficher l'optimisation
				}
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