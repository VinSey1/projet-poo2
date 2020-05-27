import java.util.ArrayList;
import java.util.List;

/**
 * Piece cr��e � partir de coordonn�es d'un fichier SVG
 */
public class Piece {

    /**
     * La sauvegarde des diff�rentes lignes trac�es pour former la pi�ce
     */
    final List<Trace> tracesPiece = new ArrayList<>();

    /**
     * La diagonale de la pi�ce avec son point le plus proche et le plus loin de l'origine
     * pour former une hitbox rectangle.
     */
    Trace diagonale;

    /**
     * Constructeur Piece qui traduit l'argument d du XML en coordonn�es
     *
     * @param coordParent les coordonn�es du parent � ajouter aux coordonn�es de base
     * @param trace       La valeur du champ d de la pi�ce � traduire en coordonn�es
     */
    public Piece(Point coordParent, String trace) {
        // coordActuelle est le curseur pour savoir o� on est
        Point coordActuelle = coordParent;
        String[] d = trace.split(" ");
        // Points pour remplir la diagonale
        Point max = new Point(coordParent.getX(), coordParent.getY());
        Point min = new Point(coordParent.getX(), coordParent.getY());
        for (int i = 0; i < d.length; i++) {
            if (!d[i].replaceAll("[^0-9\\,.\\-e]", "").equals("")) {
                // Si c'est seulement un couple de double
                String[] tmpString = d[i].split(",");
                Point valeurs = new Point(
                        Double.parseDouble(tmpString[0]),
                        Double.parseDouble(tmpString[1]));
                Point arrivee = new Point(
                        coordActuelle.getX() + valeurs.getX(),
                        coordActuelle.getY() + valeurs.getY());
                // Ajout de la ligne du curseur vers le point d'arriv�e (LigneTo)
                tracesPiece.add(new Trace(coordActuelle, arrivee));
                // Maj du curseur
                coordActuelle.setX(arrivee.getX());
                coordActuelle.setY(arrivee.getY());
            } else {
                // Si c'est une commande non explicite
                if (d[i].matches("[m]")) {
                    // On place le curseur de mani�re relative
                    coordActuelle.addX(Double.parseDouble(d[i + 1].split(",")[0]));
                    coordActuelle.addY(Double.parseDouble(d[i + 1].split(",")[1]));
                    i++;
                }
                if (d[i].matches("[M]")) {
                    // On place le curseur de mani�re absolue (en fonction du parent)
                    coordActuelle.setX(coordParent.getX() + Double.parseDouble(d[i + 1].split(",")[0]));
                    coordActuelle.setY(coordParent.getY() + Double.parseDouble(d[i + 1].split(",")[1]));
                    i++;
                }
                if (d[i].matches("[l]")) {
                    // On trace une ligne de mani�re relative
                    Point arrivee = new Point(
                            coordActuelle.getX() + Double.parseDouble(d[i + 1].split(",")[0]),
                            coordActuelle.getY() + Double.parseDouble(d[i + 1].split(",")[1])
                    );
                    // Sauvegarde
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    // Maj curseur
                    coordActuelle.setX(arrivee.getX());
                    coordActuelle.setY(arrivee.getY());
                    i++;
                }
                if (d[i].matches("[L]")) {
                    // On trace une ligne de mani�re absolue (par rapport au parent)
                    Point arrivee = new Point(
                            coordParent.getX() + Double.parseDouble(d[i + 1].split(",")[0]),
                            coordParent.getY() + Double.parseDouble(d[i + 1].split(",")[1])
                    );
                    // Sauvegarde
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    // Maj curseur
                    coordActuelle.setX(arrivee.getX());
                    coordActuelle.setY(arrivee.getY());
                    i++;
                }
                if (d[i].matches("[h]")) {
                    // Ligne horizontale relative
                    Point arrivee = new Point(
                            coordActuelle.getX() + Double.parseDouble(d[i + 1].split(",")[0]),
                            coordActuelle.getY());
                    // ...
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    coordActuelle.setX(arrivee.getX());
                    i++;
                }
                if (d[i].matches("[H]")) {
                    // Ligne horizontale absolue
                    Point arrivee = new Point(
                            coordParent.getX() + Double.parseDouble(d[i + 1].split(",")[0]),
                            coordParent.getY());
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    coordActuelle.setX(arrivee.getX());
                    i++;
                }
                if (d[i].matches("[v]")) {
                    // Verticale relative
                    Point arrivee = new Point(
                            coordActuelle.getX(),
                            coordActuelle.getY() + Double.parseDouble(d[i + 1].split(",")[0]));
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    coordActuelle.setY(arrivee.getY());
                    i++;
                }
                if (d[i].matches("[V]")) {
                    // Verticale absolue
                    Point arrivee = new Point(
                            coordParent.getX(),
                            coordParent.getY() + Double.parseDouble(d[i + 1].split(",")[0]));
                    tracesPiece.add(new Trace(coordActuelle, arrivee));
                    coordActuelle.setY(arrivee.getY());
                    i++;
                }
                if (d[i].matches("[zZ]")) {
                    // Reset du curseur vers la coordonn�e du parent
                    tracesPiece.add(new Trace(coordActuelle, coordParent));
                    coordActuelle = coordParent;
                }
                if (d[i].matches("[cC]")) {
                    // Cr�ation de la courbe de B�zier
                    Point[] traceBezier = new Point[10];
                    if (d[i].matches("[C]")) {
                        // Absolue
                        traceBezier = calculCubicTraceBezier(
                                coordParent,
                                Double.parseDouble(d[i + 1].split(",")[0]),
                                Double.parseDouble(d[i + 1].split(",")[1]),
                                Double.parseDouble(d[i + 2].split(",")[0]),
                                Double.parseDouble(d[i + 2].split(",")[1]));
                        traceBezier[9] = new Point(
                                coordParent.getX() + Double.parseDouble(d[i + 3].split(",")[0]),
                                coordParent.getY() + Double.parseDouble(d[i + 3].split(",")[1]));
                        i = i + 3;
                    }
                    if (d[i].matches("[c]")) {
                        // Relative
                        traceBezier = calculCubicTraceBezier(
                                coordActuelle,
                                Double.parseDouble(d[i + 1].split(",")[0]),
                                Double.parseDouble(d[i + 1].split(",")[1]),
                                Double.parseDouble(d[i + 2].split(",")[0]),
                                Double.parseDouble(d[i + 2].split(",")[1]));
                        traceBezier[9] = new Point(
                                coordActuelle.getX() + Double.parseDouble(d[i + 3].split(",")[0]),
                                coordActuelle.getY() + Double.parseDouble(d[i + 3].split(",")[1]));
                        i = i + 3;
                    }

                    // Sauvegarde de la liaison du curseur vers la premi�re valeur de la courbe
                    tracesPiece.add(new Trace(coordActuelle, traceBezier[0]));
                    // Sauvegarde liaison entre tous les points de la courbe
                    for (int k = 1; k < traceBezier.length - 1; k++) {
                        tracesPiece.add(new Trace(traceBezier[k], traceBezier[k + 1]));
                    }

                    // Maj du curseur
                    coordActuelle = traceBezier[9];

                    // R�cup�ration maximum de la courbe pour v�rifier max et min
                    Point maxTrace = getMax(traceBezier);
                    Point minTrace = getMin(traceBezier);

                    // V�rification max et min b�zier
                    if (maxTrace.getX() > max.getX()) max.setX(maxTrace.getX());
                    if (maxTrace.getY() > max.getY()) max.setY(maxTrace.getY());
                    if (minTrace.getX() > min.getX()) min.setX(minTrace.getX());
                    if (minTrace.getY() > min.getY()) min.setY(minTrace.getY());
                } else {
                    // V�rification max et min � chaque boucle
                    if (coordActuelle.getX() > max.getX()) max.setX(coordActuelle.getX());
                    if (coordActuelle.getY() > max.getY()) max.setY(coordActuelle.getY());
                    if (coordActuelle.getX() < min.getX()) min.setX(coordActuelle.getX());
                    if (coordActuelle.getY() < min.getY()) min.setY(coordActuelle.getY());
                }
                if (d[i].matches("[qQtTsS]")) {
                    // On ne traite pas ces cas l�
                }
            }
        }
        // Cr�ation de la diagonale
        diagonale = new Trace(min, max);
    }

    /**
     * M�thode pour calculer la courbe de b�zier
     *
     * @param start Point de d�part de la courbe
     * @param p0
     * @param p1
     * @param p2
     * @param p3
     * @return Tableau de 10 points
     */
    private Point[] calculCubicTraceBezier(Point start, double p0, double p1, double p2, double p3) {
        Point[] result = new Point[10];
        for (int i = 0; i < result.length - 1; i++) {
            double y = Math.pow(1 - i, 3) * p0 +
                    3 * Math.pow(1 - i, 2) * i * p1 +
                    3 * (1 - i) * Math.pow(i, 2) * p2 +
                    Math.pow(i, 3) * p3;
            result[i] = new Point(start.getX() + i, start.getY() + y);
        }
        return result;
    }

    /**
     * R�cup�re le minimum d'un tableau de points
     *
     * @param tab tableau de points � traiter
     * @return minimum
     */
    private Point getMin(Point[] tab) {
        Point result = tab[0];
        for (int i = 1; i < tab.length; i++) {
            if (tab[i].getX() < result.getX()) result.setX(tab[i].getX());
            if (tab[i].getY() < result.getY()) result.setY(tab[i].getY());
        }
        return result;
    }

    /**
     * R�cup�re le maximum d'un tableau de points
     *
     * @param tab tableau de points � traiter
     * @return maximum
     */
    private Point getMax(Point[] tab) {
        Point result = tab[0];
        for (int i = 1; i < tab.length; i++) {
            if (tab[i].getX() > result.getX()) result.setX(tab[i].getX());
            if (tab[i].getY() > result.getY()) result.setY(tab[i].getY());
        }
        return result;
    }

    /**
     * V�rifie si l'ensemble des traces sauvegard�es dans la pi�ce croisent � un moment les traces de la pi�ce
     * a
     *
     * @param a Pi�ce � v�rifier pour l'intersection
     * @return
     */
    public boolean croise(Piece a) {
        for (int i = 0; i < this.tracesPiece.size(); i++) {
            for (int j = 0; j < a.getTracesPiece().size(); j++) {
                if (tracesPiece.get(i).croiseTrace(a.getTracesPiece().get(j))) return false;
            }
        }
        return true;
    }

    /**
     * @return tracesPiece
     */
    public List<Trace> getTracesPiece() {
        return tracesPiece;
    }

    /**
     * @return diagonale
     */
    public Trace getDiagonale() {
        return diagonale;
    }
}