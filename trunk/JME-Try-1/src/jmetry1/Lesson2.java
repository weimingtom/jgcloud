package jmetry1;

import com.jme.app.*;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.*;
import com.jme.math.*;
import com.jme.renderer.*;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.system.*;
import com.jme.util.*;

public class Lesson2 extends BaseGame {
    //display attributes for the window. We will keep these values
    //to allow the user to change them

    private int width,  height,  depth,  freq;
    private boolean fullscreen;
    private Camera cam;

    //the root node of the scene graph
    private Node scene;
    //TextureState to show the monkey on the sphere.
    private TextureState ts;
    private Timer timer;

    public static void main(String[] args) {
        Lesson2 app = new Lesson2();
        //We will load our own "fantastic" Flag Rush logo. Yes, I'm an artist.
        app.setConfigShowMode(ConfigShowMode.AlwaysShow, Lesson2.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"));
        app.start();
    }

    protected void update(float interpolation) {
        //update the time to get the framerate
        timer.update();
        interpolation = timer.getTimePerFrame();
        //if escape was pressed, we exit
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
            finished = true;
        }

    }

    protected void render(float interpolation) {
        //Clear the screen
        display.getRenderer().clearBuffers();

        display.getRenderer().draw(scene);

    }

    protected void initSystem() {
        //store the properties information
        width = this.settings.getWidth();
        height = this.settings.getHeight();
        depth = this.settings.getDepth();
        freq = this.settings.getFrequency();
        fullscreen = this.settings.isFullscreen();

        try {
            display = DisplaySystem.getDisplaySystem(this.getNewSettings().getRenderer());
            display.createWindow(width, height, depth, freq, fullscreen);

            cam = display.getRenderer().createCamera(width, height);
        } catch (JmeException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //set the background to blue
        display.getRenderer().setBackgroundColor(ColorRGBA.blue);

        //initialize the camera
        cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1, 1000);
        Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
        // Move our camera to a correct place and orientation.
        cam.setFrame(loc, left, up, dir);
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();

        /** Get a high resolution timer for FPS updates. */
        timer = Timer.getTimer();

        display.getRenderer().setCamera(cam);

        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);

    }

    protected void initGame() {
        scene = new Node("Scene graph node");

        //Create our Sphere
        Sphere s = new Sphere("Sphere", 30, 30, 25);

        s.setLocalTranslation(new Vector3f(0, 0, -40));
        s.setModelBound(new BoundingBox());
        s.updateModelBound();

        ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(TextureManager.loadTexture(
                Lesson2.class.getClassLoader().getResource("res/Monkey.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));

        s.setRenderState(ts);

        scene.attachChild(s);

        //update the scene graph for rendering
        scene.updateGeometricState(0.0f, true);
        scene.updateRenderState();

    }

    protected void reinit() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void cleanup() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
