import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		
		optimisation();
		
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
		if(node.getNodeName().equals("g")) {
			Node tmp = node.getAttributes().getNamedItem("transform");
			if(tmp != null) {
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
	
	private static void optimisation() {
		
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
