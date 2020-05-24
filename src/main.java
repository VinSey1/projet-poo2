import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class main {

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

		Document doc = createXML(file);
		NodeList objList = doc.getElementsByTagName("path");
		System.out.println("Nombre d'objets : " + objList.getLength());

		// Afficher la fenêtre du fichier de base
		// Récupérer les coordonnées de chacun des objets
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
