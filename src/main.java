import java.io.File;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class main {
	
	static Piece tabPieces[];
	
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
		
		tabPieces = new Piece[doc.getElementsByTagName("path").getLength()];

		creationPieces(0, new double[2], doc.getDocumentElement());
	}

	private static void creationPieces(int i, double[] coordParent, Node node) {
		if(node.getNodeName().equals("g")) {
			Node tmp = node.getAttributes().getNamedItem("transform");
			if(tmp != null) {
				String valeurs = tmp.getNodeValue().replaceAll("[^0-9,.-]+", "");
				String[] tabValeurs = valeurs.split(",");
				coordParent[0] += Double.parseDouble(tabValeurs[0]);
				coordParent[1] += Double.parseDouble(tabValeurs[1]);
			}
		} else if (node.getNodeName().equals("path")) {
			// Maintenant il faut traiter les différentes données, qui sont ajoutées dans chaque pièce
        	tabPieces[i] = new Piece(coordParent, node.getAttributes().getNamedItem("d").getNodeValue());
        	i++;
		}
	    NodeList nodeList = node.getChildNodes();
	    for (int j = 0; j < nodeList.getLength(); j++) {
	        Node currentNode = nodeList.item(j);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	        	creationPieces(i, coordParent, currentNode);
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
}
