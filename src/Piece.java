import java.util.ArrayList;
import java.util.List;

/**
 * Piece créée à partir de coordonnées d'un fichier SVG
 */
public class Piece {

    /**
     * La sauvegarde des différentes lignes tracées pour former la pièce
     */
    final List<Trace> tracesPiece = new ArrayList<>();

    /**
     * La diagonale de la pièce avec son point le plus proche et le plus loin de l'origine
     * pour former une hitbox rectangle.
     */
    Trace diagonale;

    /**
     * Constructeur Piece qui traduit l'argument d du XML en coordonnées
     *
     * @param coordParent les coordonnées du parent à ajouter aux coordonnées de base
     * @param trace       La valeur du champ d de la pièce à traduire en coordonnées
     */
    public Piece(Point coordParent, String trace) {
        // coordActuelle est le curseur pour savoir où on est
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
                // Ajout de la ligne du curseur vers le point d'arrivée (LigneTo)
                tracesPiece.add(new Trace(coordActuelle, arrivee));
                // Maj du curseur
                coordActuelle.setX(arrivee.getX());
                coordActuelle.setY(arrivee.getY());
            } else {
                // Si c'est une commande non explicite
                if (d[i].matches("[m]")) {
                    // On place le curseur de manière relative
                    coordActuelle.addX(Double.parseDouble(d[i + 1].split(",")[0]));
                    coordActuelle.addY(Double.parseDouble(d[i + 1].split(",")[1]));
                    i++;
                }
                if (d[i].matches("[M]")) {
                    // On place le curseur de manière absolue (en fonction du parent)
                    coordActuelle.setX(coordParent.getX() + Double.parseDouble(d[i + 1].split(",")[0]));
                    coordActuelle.setY(coordParent.getY() + Double.parseDouble(d[i + 1].split(",")[1]));
                    i++;
                }
                if (d[i].matches("[l]")) {
                    // On trace une ligne de manière relative
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
                    // On trace une ligne de manière absolue (par rapport au parent)
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
                    // Reset du curseur vers la coordonnée du parent
                    tracesPiece.add(new Trace(coordActuelle, coordParent));
                    coordActuelle = coordParent;
                }
                if (d[i].matches("[cC]")) {
                    // Création de la courbe de Bézier
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

                    // Sauvegarde de la liaison du curseur vers la première valeur de la courbe
                    tracesPiece.add(new Trace(coordActuelle, traceBezier[0]));
                    // Sauvegarde liaison entre tous les points de la courbe
                    for (int k = 1; k < traceBezier.length - 1; k++) {
                        tracesPiece.add(new Trace(traceBezier[k], traceBezier[k + 1]));
                    }

                    // Maj du curseur
                    coordActuelle = traceBezier[9];

                    // Récupération maximum de la courbe pour vérifier max et min
                    Point maxTrace = getMax(traceBezier);
                    Point minTrace = getMin(traceBezier);

                    // Vérification max et min bézier
                    if (maxTrace.getX() > max.getX()) max.setX(maxTrace.getX());
                    if (maxTrace.getY() > max.getY()) max.setY(maxTrace.getY());
                    if (minTrace.getX() > min.getX()) min.setX(minTrace.getX());
                    if (minTrace.getY() > min.getY()) min.setY(minTrace.getY());
                } else {
                    // Vérification max et min à chaque boucle
                    if (coordActuelle.getX() > max.getX()) max.setX(coordActuelle.getX());
                    if (coordActuelle.getY() > max.getY()) max.setY(coordActuelle.getY());
                    if (coordActuelle.getX() < min.getX()) min.setX(coordActuelle.getX());
                    if (coordActuelle.getY() < min.getY()) min.setY(coordActuelle.getY());
                }
                if (d[i].matches("[qQtTsS]")) {
                    // On ne traite pas ces cas là
                }
            }
        }
        // Création de la diagonale
        diagonale = new Trace(min, max);
    }

    /**
     * Méthode pour calculer la courbe de bézier
     *
     * @param start Point de départ de la courbe
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
     * Récupère le minimum d'un tableau de points
     *
     * @param tab tableau de points à traiter
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
     * Récupère le maximum d'un tableau de points
     *
     * @param tab tableau de points à traiter
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
     * Vérifie si l'ensemble des traces sauvegardées dans la pièce croisent à un moment les traces de la pièce
     * a
     *
     * @param a Pièce à vérifier pour l'intersection
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