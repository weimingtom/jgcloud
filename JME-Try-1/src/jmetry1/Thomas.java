/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetry1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import java.io.File;

/**
 * TO-DO: Get this to work from a GoogleCode download!
 * @author Richard Hawkes
 */
public class Thomas extends SimpleGame {

    private Spatial map;
    TerrainBlock tb;

    public static void main(String[] args) {
        Thomas app = new Thomas();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        Logger.getLogger("").setLevel(Level.SEVERE);
        app.alphaBits = 2;
        app.start();
    }

    protected void simpleInitGame() {
//        buildTerrain();
//        rootNode.attachChild(tb);

        // update the scene graph for rendering
        rootNode.updateGeometricState(0.0f, true);
        rootNode.updateRenderState();

        display.setTitle("Thomas in a box!");
        URL folder = Thomas.class.getClassLoader().getResource(".");
        URL model = Thomas.class.getClassLoader().getResource("./crap-face.obj");
//        URL model = Thomas.class.getClassLoader().getResource("./fenc.obj");
        FormatConverter converter = new ObjToJme();
        converter.setProperty("mtllib", folder);
        converter.setProperty("texdir", folder);
//
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        try {
            converter.convert(model.openStream(), BO);
            map = (Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            map.setLocalScale(4f);

            File file1 = new File("C:\\Users\\Richard\\Pictures\\Thomas\\CIMG1318.jpg");
            Texture texture1 = TextureManager.loadTexture(file1.toURI().toURL(), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            TextureState ts1 = display.getRenderer().createTextureState();
            ts1.setEnabled(true);
            ts1.setTexture(texture1);
            map.setRenderState(ts1);





            map.setIsCollidable(true);
//
            map.setModelBound(new BoundingBox());
            map.updateModelBound();
            rootNode.attachChild(map);

            int z = -10;
            int counter = 1;

            File dir = new File("C:\\Users\\Richard\\Pictures\\Thomas");

            for (File file : dir.listFiles()) {
                if (file.getName().toUpperCase().contains(".JPG")) {
                    int x = (counter % 2 == 0 ? -10 : 10);

                    Texture texture = TextureManager.loadTexture(file.toURI().toURL(), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
                    TextureState ts = display.getRenderer().createTextureState();
                    float ratio = (float) texture.getImage().getHeight() / (float) texture.getImage().getWidth();
                    System.out.println("" + texture.getImage().getWidth() + "," + texture.getImage().getHeight() + " " + ratio);
                    Box box1 = new Box("Box", new Vector3f(x, 0, z), 5, (5.0f * ratio), 5);

                    counter++;
                    System.out.println(file.toURI().toURL());

                    ts.setEnabled(true);
                    ts.setTexture(texture);
                    box1.setRenderState(ts);

                    box1.setModelBound(new BoundingBox());
                    box1.updateModelBound();
                    rootNode.attachChild(box1);
                    z -= 20;
                }
            }
        } catch (Exception e) {
            System.out.println("Damn exceptions! O_o \n" + e);
            e.printStackTrace();
            System.exit(0);
        }

        PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(200f, 200f, 200f, 1.0f));
        light.setLocation(new Vector3f(0, 0, 0));
        light.setEnabled(true);
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState);

        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
        CameraNode camNode = new CameraNode("", cam);
        camNode.setLocalTranslation(0, 1.5f, 0);
    }

    protected void simpleUpdate() {
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", true)) {
            System.exit(0);
        }
    }

    public void buildTerrain() {
        MidPointHeightMap heightMap = new MidPointHeightMap(64, 0.1f);
        Vector3f terrainScale = new Vector3f(1, 0.5f, 1);
        // create a terrainblock
        float[] points = new float[1024000];
//        tb = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale, heightMap.getHeightMap(), new Vector3f(0, 0, 0));
        tb = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale, points, new Vector3f(0, 0, 0));

        try {
            File f = new File("C:\\Users\\Richard\\Pictures\\Thomas\\p1.jpg");
            Texture texture = TextureManager.loadTexture(f.toURI().toURL(), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            TextureState ts = display.getRenderer().createTextureState();
            ts.setEnabled(true);
            ts.setTexture(texture);
            tb.setRenderState(ts);
        } catch (Exception e) {
            System.out.println("EX:" + e);
        }

        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();
    }
}