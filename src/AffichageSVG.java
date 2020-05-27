import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Affichage d'un SVG avec la librairie batik
 *
 * @author aaaaa
 */
public class AffichageSVG {

    /**
     * Fenêtre d'affichage
     */
    protected JFrame fenetre;

    /**
     * Canvas SVG
     */
    protected JSVGCanvas svgCanvas = new JSVGCanvas();

    /**
     * Constructeur avec JFrame
     *
     * @param f
     */
    public AffichageSVG(JFrame f) {
        fenetre = f;
    }

    /**
     * Permet d'afficher un fichier dans la fenêtre
     *
     * @param file fichier à afficher
     * @return
     */
    public JComponent afficherFichier(File file) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.add("Center", svgCanvas);

        try {
            svgCanvas.setURI(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                fenetre.pack();
            }
        });

        return panel;
    }
}