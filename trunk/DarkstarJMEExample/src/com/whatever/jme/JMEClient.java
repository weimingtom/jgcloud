package com.whatever.jme;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import java.util.logging.Logger;
import jmetest.renderer.TestAnisotropic;

/**
 * Creates (and runs) the main JME client.
 *
 * @author Richard Hawkes
 */
public class JMEClient extends SimpleGame {

    private static Logger logger = Logger.getLogger(JMEClient.class.getName());

    /**
     * Startup and entry point for the client.
     *
     * @param args Command-line arguments (none required yet)
     */
    public static void main(String[] args) {
        JMEClient app = new JMEClient();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    /**
     *  Replace this with "initGame" (I think) once this extends BaseGame.
     */
    @Override
    protected void simpleInitGame() {
        createArena();
        this.samples = 2;
//        createPlayer();
//        createPlayerKeyboardBindings();
//        createChaseCamera();
//        createSkybox();
//        startServerInteractionThread();
    }

    private void createArena() {
        Quad floor = new Quad("Floor", 200, 200);
//        floor.setLocalRotation(???); // Command to rotate the floor 90 degrees on the X so it's flat.

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture texture = TextureManager.loadTexture(TestAnisotropic.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, 0, true);
        texture.setWrap(Texture.WrapMode.Repeat);
        texture.setScale(new Vector3f(50, 50, 50));

        ts.setTexture(texture);
        floor.setRenderState(ts);

        rootNode.attachChild(floor);
    }

    private void createChaseCamera() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createPlayer() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createPlayerKeyboardBindings() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createSkybox() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startServerInteractionThread() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
