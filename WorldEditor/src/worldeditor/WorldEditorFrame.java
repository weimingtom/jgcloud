/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WorldEditorFrame.java
 *
 * Created on 21-Apr-2010, 08:13:33
 */
package worldeditor;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.scene.Spatial;
import com.jme.util.export.xml.XMLImporter;
import java.io.File;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Richard
 */
public class WorldEditorFrame extends JFrame
        implements TranslationChangeListener, RotationChangeListener, ScaleChangeListener {

    private InspectableGame game;
    private XMLImporter xmlImporter = XMLImporter.getInstance();
    File defaultJMEFilesDirectory;

    /** Creates new form WorldEditorFrame */
    public WorldEditorFrame(InspectableGame game) {
        this.game = game;
        initComponents();

        translationPanel1.addTranslationChangeListener(this);
        rotationPanel1.addRotationChangeListener(this);
        scalePanel1.addScaleChangeListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spacialSettingsTabbedPane = new javax.swing.JTabbedPane();
        rotationPanel1 = new worldeditor.RotationPanel();
        translationPanel1 = new worldeditor.TranslationPanel();
        scalePanel1 = new worldeditor.ScalePanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        spatialsList = new javax.swing.JList();
        reloadSpatialsButton = new javax.swing.JButton();
        loadJMEXMLButton = new javax.swing.JButton();
        sceneTreePanel1 = new SceneTreePanel(game);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("World Editor");

        spacialSettingsTabbedPane.addTab("Rotation", rotationPanel1);
        spacialSettingsTabbedPane.addTab("Translation", translationPanel1);
        spacialSettingsTabbedPane.addTab("Scale", scalePanel1);

        spatialsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                spatialsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(spatialsList);

        reloadSpatialsButton.setText("Reload");
        reloadSpatialsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadSpatialsButtonActionPerformed(evt);
            }
        });

        loadJMEXMLButton.setText("Load JME-XML");
        loadJMEXMLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadJMEXMLButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spacialSettingsTabbedPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(reloadSpatialsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadJMEXMLButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sceneTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(sceneTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reloadSpatialsButton)
                    .addComponent(loadJMEXMLButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spacialSettingsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reloadSpatialsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadSpatialsButtonActionPerformed
        if (game != null && game.getRootNode() != null) {
            sceneTreePanel1.loadTreeModel();
            spatialsList.setListData(game.getRootNode().getChildren().toArray(new Spatial[0]));
        } else {
            spatialsList.clearSelection();
        }
    }//GEN-LAST:event_reloadSpatialsButtonActionPerformed

    private void loadJMEXMLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadJMEXMLButtonActionPerformed
        File xmlFile = xmlFileChooser();

        if (xmlFile != null) {
            loadJMEXMLFile(xmlFile);
        }
    }//GEN-LAST:event_loadJMEXMLButtonActionPerformed

    private File xmlFileChooser() {
        JFileChooser jmexmlFileChooser = new JFileChooser();

        if (defaultJMEFilesDirectory != null && defaultJMEFilesDirectory.isDirectory()) {
            jmexmlFileChooser.setCurrentDirectory(defaultJMEFilesDirectory);
        }

        jmexmlFileChooser.setMultiSelectionEnabled(false);
        jmexmlFileChooser.setAcceptAllFileFilterUsed(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        jmexmlFileChooser.setFileFilter(filter);

        int returnVal = jmexmlFileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            defaultJMEFilesDirectory = jmexmlFileChooser.getSelectedFile().getParentFile();
            return jmexmlFileChooser.getSelectedFile();

        } else {
            return null;
        }
    }

    private void loadJMEXMLFile(File xmlFile) {
        try {
            URL xmlFileURL = xmlFile.toURI().toURL();
            Spatial loadedSpatial = (Spatial)xmlImporter.load(xmlFileURL);
            loadedSpatial.setModelBound(new BoundingBox());
            loadedSpatial.updateModelBound();
            game.getRootNode().attachChild(loadedSpatial);
            game.getRootNode().updateRenderState();
            reloadSpatialsButtonActionPerformed(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load/parse JME XML File:\n" + ex , "JME XML Load/Parse Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void spatialsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_spatialsListValueChanged
        if (spatialsList.getSelectedValue() != null) {
            Spatial selectedSpatial = (Spatial) spatialsList.getSelectedValue();

            scalePanel1.setValues(
                    selectedSpatial.getLocalScale().getX(),
                    selectedSpatial.getLocalScale().getY(),
                    selectedSpatial.getLocalScale().getZ());

            translationPanel1.setValues(
                    selectedSpatial.getLocalTranslation().getX(),
                    selectedSpatial.getLocalTranslation().getY(),
                    selectedSpatial.getLocalTranslation().getZ());

            Quaternion rotation = selectedSpatial.getLocalRotation();

            float[] radians = rotation.toAngles(null);

            rotationPanel1.setValues(
                    SceneController.radiansToDegrees(radians[0]),
                    SceneController.radiansToDegrees(radians[1]),
                    SceneController.radiansToDegrees(radians[2]));
        }
    }//GEN-LAST:event_spatialsListValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        runWorldEditorFrame(null);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadJMEXMLButton;
    private javax.swing.JButton reloadSpatialsButton;
    private worldeditor.RotationPanel rotationPanel1;
    private worldeditor.ScalePanel scalePanel1;
    private worldeditor.SceneTreePanel sceneTreePanel1;
    private javax.swing.JTabbedPane spacialSettingsTabbedPane;
    private javax.swing.JList spatialsList;
    private worldeditor.TranslationPanel translationPanel1;
    // End of variables declaration//GEN-END:variables

    public static void runWorldEditorFrame(InspectableGame game) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Failed to load system look and feel: " + ex);
        }

        JFrame worldEditorFrame = new WorldEditorFrame(game);
        worldEditorFrame.setVisible(true);
    }

    @Override
    public void translationValueChanged(float xVal, float yVal, float zVal) {
        if (spatialsList.getSelectedValue() != null) {
            SceneController.updateTranslation((Spatial) spatialsList.getSelectedValue(), xVal, yVal, zVal);
        }
    }

    @Override
    public void rotationValueChanged(float xAngle, float yAngle, float zAngle) {
        if (spatialsList.getSelectedValue() != null) {
            SceneController.updateRotation((Spatial) spatialsList.getSelectedValue(), xAngle, yAngle, zAngle);
        }
    }

    @Override
    public void scaleValueChanged(float xVal, float yVal, float zVal) {
        if (spatialsList.getSelectedValue() != null) {
            SceneController.updateScale((Spatial) spatialsList.getSelectedValue(), xVal, yVal, zVal);
        }
    }
}
