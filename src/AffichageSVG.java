import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;

import javax.swing.*;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;

public class AffichageSVG {

    protected JFrame fenetre;
    
    protected JSVGCanvas svgCanvas = new JSVGCanvas();

    public AffichageSVG(JFrame f) {
        fenetre = f;
    }

    public JComponent afficherFichier(File file) {

        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add("Center", svgCanvas);

        try {
			svgCanvas.setURI(file.toURL().toString());
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