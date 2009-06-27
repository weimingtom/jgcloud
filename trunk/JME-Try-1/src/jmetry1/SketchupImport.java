package jmetry1;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.collada.ExtraPluginManager;
import com.jmex.model.collada.GoogleEarthPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SketchupImport extends SimpleGame {

    private static Logger logger = Logger.getLogger(SketchupImport.class.toString());
    private boolean positiveYDirection = true;

    public static void main(String[] args) {
        SketchupImport app = new SketchupImport();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        PointLight pl = new PointLight();
        pl.setLocation(new Vector3f(100, 100, 100));

        Node n = getSketchupNode();
        n.setModelBound(new BoundingBox());
        n.updateModelBound();
        n.updateRenderState();
        rootNode.attachChild(n);
    }

    private Node getSketchupNode() {
        // So far to get this to work, I have had to update the .kmz file to .zip
        // and extract the house.dae in it. Now place the textureX.jpg files in
        // the textures sub-directory. Finally, update the .dae file, changing
        // path to ./textureX.jpg for each file (ie no path details).
        // That seems to do the job!

        try {
//            File f = new File("C:\\Temp\\house.dae");
            File f = new File("C:\\Users\\Richard\\Documents\\Sketchup\\exporttest01.dae");
//            File f = new File("C:\\Users\\Richard\\Documents\\Sketchup\\ferris-wheel.dae");
//            File f = new File("C:\\Users\\Richard\\Documents\\Sketchup\\amusement-park.dae");
            SimpleResourceLocator loc = new SimpleResourceLocator(new File("./textures").toURI());
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc);

            ExtraPluginManager.registerExtraPlugin("GOOGLEEARTH", new GoogleEarthPlugin());
            InputStream gamemap = new FileInputStream(f);
            ColladaImporter.load(gamemap, "model");
            Node modelNode = ColladaImporter.getModel();
            logger.log(Level.INFO, "Loaded node " + modelNode.getName() + ", children=" + modelNode.getChildren().size());

            final Spatial box1 = modelNode.getChild("MyBox1");
            logger.log(Level.INFO, "XXX" + box1);

            box1.addController(new Controller() {
                public void update(float time) {
                    float moveY;

                    if (positiveYDirection) {
                        moveY = box1.getLocalTranslation().y + tpf;
                    } else {
                        moveY = box1.getLocalTranslation().y - tpf;
                    }

                    box1.setLocalTranslation(0, moveY, 0);

                    if (box1.getLocalTranslation().y > 20 || box1.getLocalTranslation().y < (0 - 20)) {
                        positiveYDirection = !positiveYDirection;
                    }
                }
            });
//            showChildren(modelNode,0);

            return modelNode;
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return null;
        } finally {
            ColladaImporter.cleanUp();
        }
    }

    private void showChildren(Node node, int level) {
        if (node.getChildren() == null) {
            return;
        }

        for (Spatial child : node.getChildren()) {
            String indent = "";
            for (int i = 0; i < level; i++) {
                indent += "  ";
            }
            logger.log(Level.INFO, indent + "child=" + child.getName());
            if (child instanceof Node) {
                showChildren((Node) child, level + 1);
            }
        }
    }
}
