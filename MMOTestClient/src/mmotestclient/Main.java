/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmotestclient;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Richard
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GUIFrame gf = new GUIFrame();
        gf.setTitle("Crappy XTest Client");
        gf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreFrame(gf);
        gf.setVisible(true);

        Runnable csi = new ServerLocationUpdater(gf);
        Thread locationUpdaterThread = new Thread(csi);
        locationUpdaterThread.start();
    }

    private static void centreFrame(JFrame f) {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = f.getSize().width;
        int h = f.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        f.setLocation(x, y);
    }
}
