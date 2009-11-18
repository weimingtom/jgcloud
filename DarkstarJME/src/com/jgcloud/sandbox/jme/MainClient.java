package com.jgcloud.sandbox.jme;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import java.util.logging.Logger;


/**
 *
 * @author Richard Hawkes
 */
public class MainClient extends SimpleGame {
    private static final Logger logger = Logger.getLogger(MainClient.class.getName());

    private int FLOOR_WIDTH = 200;
    private int FLOOR_HEIGHT = 400;
    private int WALL_HEIGHT = 10;

    public static void main(String[] args) {
        MainClient client = new MainClient();
//        client.samples = 4;

        client.setConfigShowMode(ConfigShowMode.AlwaysShow);
        client.start();
    }

    @Override
    protected void simpleInitGame() {
        createArena();
        createPlayer();
    }

    private void createArena() {
        // Set up the 'yaw' (90 degrees) for the left and right walls.
        Quaternion yaw90 = new Quaternion();
        yaw90.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));

        // Set up the 'pitch' (90 degrees) for the floor.
        Quaternion pitch90 = new Quaternion();
        pitch90.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));


        Quad floor = new Quad("Floor", FLOOR_WIDTH, FLOOR_HEIGHT);
        floor.setLocalRotation(pitch90);

        //load a texture for the floor
        TextureState floorTextureState = display.getRenderer().createTextureState();
        Texture floorTexture = TextureManager.loadTexture(MainClient.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        floorTextureState.setTexture(floorTexture);
        floor.setRenderState(floorTextureState);

        rootNode.attachChild(floor);
        
        Node walls = new Node("Walls");

        //load a texture for the walls
        TextureState wallTextureState = display.getRenderer().createTextureState();
        Texture wallTexture = TextureManager.loadTexture(MainClient.class.getClassLoader().getResource("jmetest/data/texture/rust.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        // By setting the render state on the 'walls' node, all its childre will
        // also use the same texture.
        wallTextureState.setTexture(wallTexture);
        walls.setRenderState(wallTextureState);

        // The back wall does not need to be rotated. It defaults to vertical.
        Quad backWall = new Quad("Back Wall", FLOOR_WIDTH, WALL_HEIGHT);
        walls.attachChild(backWall);
        backWall.setLocalTranslation(0, WALL_HEIGHT/2, FLOOR_HEIGHT/2);

        // The front wall does not need to be rotated. It defaults to vertical.
        Quad frontWall = new Quad("Front Wall", FLOOR_WIDTH, WALL_HEIGHT);
        walls.attachChild(frontWall);
        frontWall.setLocalTranslation(0, WALL_HEIGHT/2, 0 - (FLOOR_HEIGHT/2));

        Quad leftWall = new Quad("Left Wall", FLOOR_HEIGHT, WALL_HEIGHT);
        walls.attachChild(leftWall);
        leftWall.setLocalRotation(yaw90);
        leftWall.setLocalTranslation(0-(FLOOR_WIDTH/2), WALL_HEIGHT/2, 0);

        Quad rightWall = new Quad("Right Wall", FLOOR_HEIGHT, WALL_HEIGHT);
        walls.attachChild(rightWall);
        rightWall.setLocalRotation(yaw90);
        rightWall.setLocalTranslation((FLOOR_WIDTH/2), WALL_HEIGHT/2, 0);

        rootNode.attachChild(walls);
    }

    private void createPlayer() {
        Box b = new Box("Player", new Vector3f(0.0F, 5F, 0.0F), 5, 5, 5);
        rootNode.attachChild(b);
    }
}
