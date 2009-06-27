package jmetry1;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionResults;
import com.jme.math.Vector3f;
import com.jme.scene.*;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import java.io.*;

public class CollisionFun extends SimpleGame {

    private Node scene = new Node("3D Scene");
    private TriMesh box1;
    private TriMesh box2;
    private Node box1Node;
    private Node box2Node;
    private Node camNode;
    private Vector3f max = new Vector3f(5, 5, 5);
    private Vector3f min = new Vector3f(-5, -5, -5);
    private Text infoText;
    private boolean positiveXDirection = true;
    private boolean positiveYDirection = true;
    private CollisionResults results;

    public static void main(String[] args) {
        CollisionFun cf = new CollisionFun();

        File f = new File("c:\\temp\\logo.png");

        try {
            cf.setConfigShowMode(ConfigShowMode.AlwaysShow, f.toURI().toURL());
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        cf.samples = 2;
        System.out.println(cf.settings.DEFAULT_SAMPLES);

        cf.start();
    }

    @Override
    protected void simpleInitGame() {
        display.setTitle("Understanding collision... Maybe!");

        results = new BoundingCollisionResults() {
            @Override
            public void processCollisions() {
                if (getNumber() > 0) {
//                    infoText.print("Collision: YES");
                } else {
//                    infoText.print("Collision: NO");
                }
            }
        };

        camNode = new Node("Camera Node");
        camNode.setIsCollidable(true);
        camNode.updateModelBound();

        addBox1();

        addBox2();

        addFloor();

        rootNode.attachChild(scene);

        infoText = Text.createDefaultTextLabel("Text Label", ":-)");
        infoText.setLocalTranslation(new Vector3f(1, 60, 0));
        statNode.attachChild(infoText);

//        cam.setLeft(new Vector3f(-1,0,0));
//        cam.setUp(new Vector3f(0,0,-1));
    }

    @Override
    protected void simpleUpdate() {
        animateBox1();
        animateBox2();
        handleCollisions();
    }

    private void addBox1() {
        box1 = new Box("Box 1", min, max);
        box1.setModelBound(new BoundingBox());
        box1.updateModelBound();
//        box1.setLocalTranslation(new Vector3f(0, 30, 0));
        box1Node = new Node("Box 1 Node");
        box1Node.attachChild(box1);
        scene.attachChild(box1Node);
    }

    private void addBox2() {
        box2 = new Box("Box 2", min, max);
        box2.setModelBound(new BoundingBox());
        box2.updateModelBound();
//        box2.setLocalTranslation(new Vector3f(0, 30, 0));
        box2Node = new Node("Box 2 Node");
        box2Node.attachChild(box2);
        scene.attachChild(box2Node);
    }

    private void addFloor() {
        Quad floor = new Quad("Floor", 100, 100);
        floor.setModelBound(new BoundingBox());
        floor.updateModelBound();

        scene.attachChild(floor);

        File f = new File("C:\\Temp\\floor.jpg");
        try {
            TextureState ts = display.getRenderer().createTextureState();
            ts.setEnabled(true);
            Texture texture = TextureManager.loadTexture(f.toURI().toURL(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, 0, true);
            texture.setScale(new Vector3f(20, 20, 0));
            texture.setWrap(Texture.WrapMode.Repeat);
            ts.setTexture(texture);
            floor.setRenderState(ts);
        } catch (Exception ex) {
            System.out.println("Couldn't load texture :(");
        }
    }

    private void animateBox1() {
        float moveX;
        if (positiveXDirection) {
            moveX = box1.getLocalTranslation().x + tpf;
        } else {
            moveX = box1.getLocalTranslation().x - tpf;
        }
        box1.setLocalTranslation(moveX, 0, 0);
        if (box1.getLocalTranslation().x > 20 || box1.getLocalTranslation().x < (0 - 20)) {
            positiveXDirection = !positiveXDirection;
        }
    }

    private void animateBox2() {
        float moveY;

        if (positiveYDirection) {
            moveY = box2.getLocalTranslation().y + tpf;
        } else {
            moveY = box2.getLocalTranslation().y - tpf;
        }

        box2.setLocalTranslation(0, moveY, 0);

        if (box2.getLocalTranslation().y > 20 || box2.getLocalTranslation().y < (0 - 20)) {
            positiveYDirection = !positiveYDirection;
        }
    }

    private void handleCollisions() {
        results.clear();
        camNode.setLocalTranslation(cam.getLocation());
        camNode.calculateCollisions(scene, results);


//        infoText.print(camNode.getLocalTranslation().toString() + "\n" + cam.getLocation().toString());
        infoText.print(cam.getLeft().toString() + " " + cam.getUp().toString());
    }
}
