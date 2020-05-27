import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    final static List<Piece> listPieces = new ArrayList<>();
    final static List<Element> listNodesPieces = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Entrez le chemin du fichier à optimiser : ");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        sc.close();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Le fichier n'existe pas. Fin du programme.");
            System.exit(1);
        }

        JFrame f = new JFrame("Affichage pré-optimisation");
        AffichageSVG app = new AffichageSVG(f);

        f.getContentPane().add(app.afficherFichier(file));

        f.setVisible(true);

        Document doc = createXML(file);

        creationPieces(new Point(), doc.getDocumentElement());

        // Nous allons choisir une profondeur de 6, soit 4^6 = 4096 arbres (grille de 64x64)
        optimisation(6, f.getWidth(), f.getHeight());

        // Test des collisions (preuve que ça ne marche pas)
		/*
		for(int i = 0; i<listPieces.size(); i++) {
			for(int j = i+1; j<listPieces.size(); j++) {
				System.out.println("[" + i + ", " + j + "] : " + listPieces.get(i).croise(listPieces.get(j)));
			}
		}
		*/

        moveTo(listNodesPieces.get(3), new Point(500000, 1000));

        try {
            affichageOpti(fileName, doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void creationPieces(Point coordParent, Node node) {
        if (node.getNodeName().equals("g")) {
            Node tmp = node.getAttributes().getNamedItem("transform");
            if (tmp != null) {
                String valeurs = tmp.getNodeValue().replaceAll("[^0-9,.-]+", "");
                String[] tabValeurs = valeurs.split(",");
                coordParent.addX(Double.parseDouble(tabValeurs[0]));
                coordParent.addY(Double.parseDouble(tabValeurs[1]));
            }
        } else if (node.getNodeName().equals("path")) {
            listNodesPieces.add((Element) node);
            listPieces.add(new Piece(coordParent, node.getAttributes().getNamedItem("d").getNodeValue()));
        }
        NodeList nodeList = node.getChildNodes();
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node currentNode = nodeList.item(j);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                creationPieces(coordParent, currentNode);
            }
        }
    }

    private static Document createXML(File file) {
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Optimisation du placement des pièces
     *
     * @param depth  La profondeur de l'arbre à utiliser
     * @param width  La largeur de la grille
     * @param height La hauteur de la grille
     */
    private static void optimisation(int depth, double width, double height) {
        if (depth < 1)
            throw new UnsupportedOperationException("Depth should be at least one!");
        // Creation de la racine du quadtree
        final Quadtree<QuadtreePoint> root = createTree(depth, 0, 0, width, height);
        // Trouver la première feuille qui a assez de place
        Quadtree<QuadtreePoint> quadtreePosition;
        for (final Piece listPiece : listPieces) {
            quadtreePosition = findFirstPlace(root, listPiece);
            if (quadtreePosition == null) {
                System.err.println("Pas assez de place pour y mettre toutes les pièces!");
                System.exit(1);
            }
            movePiece(listPiece, new Point(quadtreePosition.getValue().getX(), quadtreePosition.getValue().getY()));
        }
    }

    /**
     * Création d'un quadtree d'une profondeur fixe
     *
     * @param depth  La profondeur
     * @param x      La valeur du noeud en X
     * @param y      La valeur du noeud en Y
     * @param width  La largeur de la grille
     * @param height La longeur de la grille
     * @return La quadtree créer
     */
    private static Quadtree<QuadtreePoint> createTree(int depth, double x, double y, double width, double height) {
        Quadtree<QuadtreePoint> node = new Quadtree<>(new QuadtreePoint(x, y, width, height));
        if (depth == 1) {
            return node;
        }
        node.addChild(createTree(depth - 1, x, y, width / 2, height / 2));
        node.addChild(createTree(depth - 1, width / 2, y, width / 2, height / 2));
        node.addChild(createTree(depth - 1, x, height / 2, width / 2, height / 2));
        node.addChild(createTree(depth - 1, width / 2, height / 2, width / 2, height / 2));
        return node;
    }

    private static void movePiece(Piece listPiece, Point point) {
        final int index = listPieces.indexOf(listPiece);
        if (index >= 0) {
            Element e = listNodesPieces.get(index);
            if (e != null) {
                moveTo(e, new Point(point.getX(), point.getY()));
            }
        }
    }

    /**
     * Trouver le meilleur emplacement d'une pièce dans l'arbre
     *
     * @param tree  L'abre
     * @param piece La pièce à placer
     * @return L'arbre dans laquelle elle devrait être placer, pouvant être {@literal null}
     */
    private static Quadtree<QuadtreePoint> findFirstPlace(Quadtree<QuadtreePoint> tree, Piece piece) {
        Quadtree<QuadtreePoint> candidate = null;
        // Si ce quadtree a assez de place pour accueillir la pièce
        if (hasEnoughPlaceForPiece(piece, tree.getValue().getLongX(), tree.getValue().getLongY())) {
            // Ce arbre pourrait correspondre pour accueillir la pièce
            candidate = tree;
            if (tree.getChilds() == null || tree.getChilds().isEmpty()) {
                // Si il n'a pas de fils, il va accueillir la pièce
                return candidate;
            } else {
                // Vérifions si un de ses fils (plus petit), pourrait aussi l'accueillir
                for (int i = 0; i < 4 && tree.getChilds().size() > i; i++) {
                    candidate = findFirstPlace(tree.getChilds().get(0), piece);
                    if (candidate != null)
                        return candidate;
                }
                // Si tous les fils sont trop petit, c'est cet arbre qui va accueillir la pièce
                return tree;
            }
        } else {
            return null;
        }
    }

    /**
     * Vérifier si une pièce satisfait une hauteur et largeur
     *
     * @param piece  La pièce
     * @param width  La largeur
     * @param height La hauteur
     * @return {@literal true} si la pièce satisfait les conditions
     */
    private static boolean hasEnoughPlaceForPiece(Piece piece, double width, double height) {
        return ((piece.getDiagonale().getB().x - piece.getDiagonale().getA().x) <= width) &&
                ((piece.getDiagonale().getB().y - piece.getDiagonale().getA().y) <= height);
    }

    private static void moveTo(Element n, Point a) {
        n.setAttribute("transform", "translate(" + a.getX() + "," + a.getY() + ")");
    }

    private static void affichageOpti(String fileName, Document doc) throws IOException, TransformerException {
        DOMSource source = new DOMSource(doc);
        FileWriter writer;
        fileName = fileName.replace(".svg", "_opti.svg");
        writer = new FileWriter(new File(fileName));
        StreamResult result = new StreamResult(writer);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);

        System.out.println("Fichier sortie : " + fileName);
        File file = new File(fileName);

        JFrame f = new JFrame("Affichage post-optimisation");
        AffichageSVG app = new AffichageSVG(f);

        f.getContentPane().add(app.afficherFichier(file));

        f.setVisible(true);
    }
}
