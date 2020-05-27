import java.lang.Math;

public class Piece {
	
	Point hautGauche = new Point();
	Point basDroit = new Point();
	
	public Piece(Point coordParent, String trace) {
		Point coordActuelle = coordParent;
		String[] d = trace.split(" ");
		Point max = new Point(coordParent.getX(), coordParent.getY());
		Point min = new Point(coordParent.getX(), coordParent.getY());
		for(int i = 0; i<d.length; i++) {
			if(!d[i].replaceAll("[^0-9\\,.\\-e]", "").equals("")) {
				System.out.println(d[i]);
				String[] tmpString = d[i].split(",");
				coordActuelle.addX(Double.parseDouble(tmpString[0]));
				coordActuelle.addY(Double.parseDouble(tmpString[1]));
				if(coordActuelle.getX() > max.getX()) max.setX(coordActuelle.getX());
				if(coordActuelle.getY() > max.getY()) max.setY(coordActuelle.getY());
				if(coordActuelle.getX() < min.getX()) min.setX(coordActuelle.getX());
				if(coordActuelle.getY() < min.getY()) min.setY(coordActuelle.getY());
			} else {
				if(d[i].matches("[hH]")) {
					coordActuelle.addX(Double.parseDouble(d[i+1].split(",")[0]));
					i++;
				}
				if(d[i].matches("[vV]")) {
					coordActuelle.addY(Double.parseDouble(d[i+1].split(",")[0]));
					i++;
				}
				if(d[i].matches("[zZ]")) {
					// car Z "reset" la coordonnée
					coordActuelle = coordParent;
				}
				if(d[i].matches("[qQcCtTsS]")) {
					Point traceBezier[] = new Point[10];
					if(d[i].matches("[C]")) {
						traceBezier = calculCubicTraceBezier(
								coordParent,
								coordParent.getX() + Double.parseDouble(d[i+1].split(",")[0]),
								coordParent.getY() + Double.parseDouble(d[i+1].split(",")[1]),
								coordParent.getX() + Double.parseDouble(d[i+2].split(",")[0]),
								coordParent.getY() + Double.parseDouble(d[i+2].split(",")[1]));
						traceBezier[9] = new Point(
								coordParent.getX() + Double.parseDouble(d[i+3].split(",")[0]),
								coordParent.getY() + Double.parseDouble(d[i+3].split(",")[1]));
						i = i+3;
					}
					if(d[i].matches("[c]")) {
						traceBezier = calculCubicTraceBezier(
								coordActuelle,
								coordActuelle.getX() + Double.parseDouble(d[i+1].split(",")[0]),
								coordActuelle.getY() + Double.parseDouble(d[i+1].split(",")[1]),
								coordActuelle.getX() + Double.parseDouble(d[i+2].split(",")[0]),
								coordActuelle.getY() + Double.parseDouble(d[i+2].split(",")[1]));
						traceBezier[9] = new Point(
								coordActuelle.getX() + Double.parseDouble(d[i+3].split(",")[0]),
								coordActuelle.getY() + Double.parseDouble(d[i+3].split(",")[1]));
						i = i+3;
					}
					if(d[i].matches("[qQtTsS]")) {
						// Je ne vois pas comment le traiter
					}
					Point maxTrace = getMax(traceBezier);
					Point minTrace = getMin(traceBezier);
					if(maxTrace.getX() > max.getX()) max.setX(maxTrace.getX());
					if(maxTrace.getY() > max.getY()) max.setY(maxTrace.getY());
					if(minTrace.getX() > min.getX()) min.setX(minTrace.getX());
					if(minTrace.getY() > min.getY()) min.setY(minTrace.getY());

					// Faire les fonctions restantes
					// Sauvegarder toutes les fonctions de tracé dans une variable pour détecter les collisions
					// Faire l'optimisation en rajoutant un attribut "translate" à chaque path
					// Prendre en compte les groupes de paths
					// Afficher l'optimisation
				}
			}
		}
	}
	
	private Point[] calculCubicTraceBezier(Point start, double p0, double p1, double p2, double p3) {
		Point[] result = new Point[10];
		for(int i = 0; i < result.length-1; i++) {
			double y = Math.pow(1-i, 3)*p0 + 
					3*Math.pow(1-i, 2)*i*p1 +
					3*(1-i)*Math.pow(i, 2)*p2 +
					Math.pow(i, 3)*p3;
			result[i] = new Point(start.getX() + i, y);
		}
		return result;
	}
	
	private Point getMin(Point[] tab) {
		Point result = tab[0];
		for(int i = 1; i<tab.length; i++) {
			if(tab[i].getX() < result.getX()) result.setX(tab[i].getX());
			if(tab[i].getY() < result.getY()) result.setY(tab[i].getY());
		}
		return result;
	}
	
	private Point getMax(Point[] tab) {
		Point result = tab[0];
		for(int i = 1; i<tab.length; i++) {
			if(tab[i].getX() > result.getX()) result.setX(tab[i].getX());
			if(tab[i].getY() > result.getY()) result.setY(tab[i].getY());
		}
		return result;
	}
}