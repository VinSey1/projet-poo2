import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Piece {

	final List<Trace> tracesPiece = new ArrayList<>();

	public Piece(Point coordParent, String trace) {
		Point coordActuelle = coordParent;
		String[] d = trace.split(" ");
		for(int i = 0; i<d.length; i++) {
			if(!d[i].replaceAll("[^0-9\\,.\\-e]", "").equals("")) {
				String[] tmpString = d[i].split(",");
				Point valeurs = new Point(
						Double.parseDouble(tmpString[0]),
						Double.parseDouble(tmpString[1]));
				Point arrivee = new Point(
						coordActuelle.getX() + valeurs.getX(),
						coordActuelle.getY() + valeurs.getY());
				tracesPiece.add(new Trace(coordActuelle, arrivee));
				coordActuelle.setX(arrivee.getX());
				coordActuelle.setY(arrivee.getY());
			} else {
				if(d[i].matches("[m]")) {
					coordActuelle.addX(Double.parseDouble(d[i+1].split(",")[0]));
					coordActuelle.addY(Double.parseDouble(d[i+1].split(",")[1]));
					i++;
				}
				if(d[i].matches("[M]")) {
					coordActuelle.setX(coordParent.getX() + Double.parseDouble(d[i+1].split(",")[0]));
					coordActuelle.setY(coordParent.getY() + Double.parseDouble(d[i+1].split(",")[1]));
					i++;
				}
				if(d[i].matches("[l]")) {
					Point arrivee = new Point(
							coordActuelle.getX() + Double.parseDouble(d[i+1].split(",")[0]),
							coordActuelle.getY() + Double.parseDouble(d[i+1].split(",")[1])
							);
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setX(arrivee.getX());
					coordActuelle.setY(arrivee.getY());
					i++;
				}
				if(d[i].matches("[L]")) {
					Point arrivee = new Point(
							coordParent.getX() + Double.parseDouble(d[i+1].split(",")[0]),
							coordParent.getY() + Double.parseDouble(d[i+1].split(",")[1])
							);
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setX(arrivee.getX());
					coordActuelle.setY(arrivee.getY());
					i++;
				}
				if(d[i].matches("[h]")) {
					Point arrivee = new Point(
							coordActuelle.getX() + Double.parseDouble(d[i+1].split(",")[0]),
							coordActuelle.getY());
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setX(arrivee.getX());
					i++;
				}
				if(d[i].matches("[H]")) {
					Point arrivee = new Point(
							coordParent.getX() + Double.parseDouble(d[i+1].split(",")[0]),
							coordParent.getY());
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setX(arrivee.getX());
					i++;
				}
				if(d[i].matches("[v]")) {
					Point arrivee = new Point(
							coordActuelle.getX(),
							coordActuelle.getY() + Double.parseDouble(d[i+1].split(",")[0]));
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setY(arrivee.getY());
					i++;
				}
				if(d[i].matches("[V]")) {
					Point arrivee = new Point(
							coordParent.getX(),
							coordParent.getY() + Double.parseDouble(d[i+1].split(",")[0]));
					tracesPiece.add(new Trace(coordActuelle, arrivee));
					coordActuelle.setY(arrivee.getY());
					i++;
				}
				if(d[i].matches("[zZ]")) {
					// car Z "reset" la coordonnée
					tracesPiece.add(new Trace(coordActuelle, coordParent));
					coordActuelle = coordParent;
				}
				if(d[i].matches("[cC]")) {
					Point traceBezier[] = new Point[10];
					if(d[i].matches("[C]")) {
						traceBezier = calculCubicTraceBezier(
								coordParent,
								Double.parseDouble(d[i+1].split(",")[0]),
								Double.parseDouble(d[i+1].split(",")[1]),
								Double.parseDouble(d[i+2].split(",")[0]),
								Double.parseDouble(d[i+2].split(",")[1]));
						traceBezier[9] = new Point(
								coordParent.getX() + Double.parseDouble(d[i+3].split(",")[0]),
								coordParent.getY() + Double.parseDouble(d[i+3].split(",")[1]));
						i = i+3;
					}
					if(d[i].matches("[c]")) {
						traceBezier = calculCubicTraceBezier(
								coordActuelle,
								Double.parseDouble(d[i+1].split(",")[0]),
								Double.parseDouble(d[i+1].split(",")[1]),
								Double.parseDouble(d[i+2].split(",")[0]),
								Double.parseDouble(d[i+2].split(",")[1]));
						traceBezier[9] = new Point(
								coordActuelle.getX() + Double.parseDouble(d[i+3].split(",")[0]),
								coordActuelle.getY() + Double.parseDouble(d[i+3].split(",")[1]));
						i = i+3;
					}
					
					tracesPiece.add(new Trace(coordActuelle, traceBezier[0]));
					for(int k = 1; k < traceBezier.length-1; k++) {
						tracesPiece.add(new Trace(traceBezier[k], traceBezier[k+1]));
					}
										
					coordActuelle = traceBezier[9];
				}
				if(d[i].matches("[qQtTsS]")) {
						// On ne traite pas ces cas là
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
			result[i] = new Point(start.getX() + i, start.getY() + y);
		}
		return result;
	}
	
	public boolean croise(Piece a) {
		for(int i = 0; i<this.tracesPiece.size(); i++) {
			for(int j = 0; j<a.getTracesPiece().size(); j++) {
				if(tracesPiece.get(i).croiseTrace(a.getTracesPiece().get(j))) return false;
			}
		}
		return true;
	}

	public List<Trace> getTracesPiece() {
		return tracesPiece;
	}
}