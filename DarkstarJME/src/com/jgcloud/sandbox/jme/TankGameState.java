package com.jgcloud.sandbox.jme;

import com.jgcloud.sandbox.darkstar.DarkstarConstants;
import com.jgcloud.sandbox.darkstar.DarkstarUpdater;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Capsule;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


/**
 * This is the main JME class to setup and view the scene. It also handles any
 * updates sent from the server, and positions the remote tanks as required.
 *
 * @author Richard Hawkes
 */
public class TankGameState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(TankGameState.class.getName());

    protected static int FLOOR_WIDTH = 200;
    protected static int FLOOR_LENGTH = 400;
    protected static int WALL_HEIGHT = 10;

    private Node myTank;
    private int currentSpeed;

    private Node walls;
    private Node remotePlayersNode; // Node that contains the remote players.

    private Camera cam;
    private ChaseCamera chaseCamera;
    private Skybox skybox;
    private static StandardGame standardGame;

   
   /**
     * A map of remote players and their "interpolated" position.
      */
     private Map<String,Interpolator> remotePlayers = new ConcurrentHashMap<String,Interpolator>();

    public static void main(String[] args) {
        standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
        standardGame.getSettings().setSamples(0);
        standardGame.start();

        GameState client = new TankGameState();
        GameStateManager.getInstance().attachChild(client);
        client.setActive(true);

        logger.info("TankGameState.main complete");
    }


    /**
     * Main constructor.
     */
    public TankGameState() {
        super("Main Client");

        cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        init();

        Thread darkstarThread = new Thread(DarkstarUpdater.getInstance(myTank));
        darkstarThread.setName("DarkstarUpdater");
        darkstarThread.start();
    }


    /**
     * Sets up the arena, tank, controller etc. Basically gets everything
     * visible for us.
     */
    protected void init() {
        createSkybox();
        createArena();
        createMyTank();
        createChaseCamera();
        createLighting();
        addController();
        createRemotePlayersNode();
        
        getRootNode().updateRenderState();
    }


    public Map<String,Interpolator> getRemotePlayers() {
        return remotePlayers;
    }


    /**
     * Creates a simple arena...
     */
    private void createArena() {
        // Set up the 'yaw' (90 degrees) for the left and right walls.
        Quaternion yaw90 = new Quaternion();
        yaw90.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));

        // Set up the 'pitch' (90 degrees) for the floor.
        Quaternion pitch90 = new Quaternion();
        pitch90.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));


        Quad floor = new Quad("Floor", FLOOR_WIDTH, FLOOR_LENGTH);
        floor.setLocalRotation(pitch90);

        //load a texture for the floor
        TextureState floorTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture floorTexture = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        floorTexture.setWrap(Texture.WrapMode.Repeat);
        floorTexture.setScale(new Vector3f(10,10,10));

        floorTextureState.setTexture(floorTexture);
        floor.setRenderState(floorTextureState);

        rootNode.attachChild(floor);
        
        walls = new Node("Walls");

        // load a texture for the walls
        TextureState wallTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture wallTexture = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        wallTexture.setWrap(Texture.WrapMode.Repeat);
        wallTexture.setScale(new Vector3f(50,1,0));

        // By setting the render state on the 'walls' node, all its childre will
        // also use the same texture.
        wallTextureState.setTexture(wallTexture);
        walls.setRenderState(wallTextureState);

        // The back wall does not need to be rotated. It defaults to vertical.
        Quad backWall = new Quad("Back Wall", FLOOR_WIDTH, WALL_HEIGHT);
        walls.attachChild(backWall);
        backWall.setLocalTranslation(0, WALL_HEIGHT/2, FLOOR_LENGTH/2);
        backWall.setModelBound(new BoundingBox());
        backWall.updateModelBound();

        // The front wall does not need to be rotated. It defaults to vertical.
        Quad frontWall = new Quad("Front Wall", FLOOR_WIDTH, WALL_HEIGHT);
        walls.attachChild(frontWall);
        frontWall.setLocalTranslation(0, WALL_HEIGHT/2, 0 - (FLOOR_LENGTH/2));
        frontWall.setModelBound(new BoundingBox());
        frontWall.updateModelBound();

        Quad leftWall = new Quad("Left Wall", FLOOR_LENGTH, WALL_HEIGHT);
        walls.attachChild(leftWall);
        leftWall.setLocalRotation(yaw90);
        leftWall.setLocalTranslation(0-(FLOOR_WIDTH/2), WALL_HEIGHT/2, 0);
        leftWall.setModelBound(new BoundingBox());
        leftWall.updateModelBound();

        Quad rightWall = new Quad("Right Wall", FLOOR_LENGTH, WALL_HEIGHT);
        walls.attachChild(rightWall);
        rightWall.setLocalRotation(yaw90);
        rightWall.setLocalTranslation((FLOOR_WIDTH/2), WALL_HEIGHT/2, 0);
        rightWall.setModelBound(new BoundingBox());
        rightWall.updateModelBound();

        rootNode.attachChild(walls);
    }

    private void createMyTank() {
        myTank = createPlayer("MyTank");
        getRootNode().attachChild(myTank);
    }

    /**
     * Returns a tank node. Although this is meant for the player on this
     * computer, it can also be used to create a remote player as well.
     *
     * @return A tank node
     */
    private Node createPlayer(String playerName) {
        Node tank = new Node(playerName);

        //load a texture for the myTank
        TextureState tankTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture tankTexture = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/tanktexture.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        tankTextureState.setTexture(tankTexture);
        tank.setRenderState(tankTextureState);

        // Create a simple box that will be our myTank
        Box tankBox = new Box("TankBox", new Vector3f(0.0F, 5F, 0.0F), 5, 5, 5);
        tank.attachChild(tankBox);

        // Now create a capsule that will be our gun
        Capsule tankGun = new Capsule("TankGun", 10, 10, 10, 1, 8);

        // Set up the 'pitch' (90 degrees) for the gun
        Quaternion pitch90 = new Quaternion();
        pitch90.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
        tankGun.setLocalRotation(pitch90);
        tankGun.setLocalTranslation(0, 5, -8);
        tank.attachChild(tankGun);

        tank.setModelBound(new BoundingBox());
        tank.updateModelBound();

        tank.updateWorldBound();

        return tank;
    }


    @Override
    public void update(float tpf) {
        super.update(tpf);

        // See if there are any player location updates.
        updateRemotePlayerLocations();

        // Keep the chase camera behaving itself :)
        chaseCamera.update(tpf);

        //we want to keep the skybox around our eyes, so move it with the camera.
        skybox.setLocalTranslation(cam.getLocation());
    }


    /**
     * Not a very good implementatin of a chase camera. Works though.
     */
    private void createChaseCamera() {
        Vector3f targetOffset = new Vector3f();

        targetOffset.y = ((BoundingBox)myTank.getWorldBound()).yExtent * 3F;
        targetOffset.z = ((BoundingBox)myTank.getWorldBound()).zExtent * 1F;

        HashMap props = new HashMap();
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);

        chaseCamera = new ChaseCamera(DisplaySystem.getDisplaySystem().getRenderer().getCamera(), myTank, props);
    }


    /**
     * Create a node that will hold all of the remote players. This should
     * give us quicker retrieval of the tank nodes when we need to update them.
     */
    private void createRemotePlayersNode() {
        remotePlayersNode = new Node("RemotePlayers");
        getRootNode().attachChild(remotePlayersNode);
    }


    /**
     * A little unnecessary this, but I wanted to play around with skyboxes :)
     */
    private void createSkybox() {
        skybox = new Skybox("skybox", 10, 10, 10);

        Texture skyboxTextureNorth = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/north.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureSouth = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/south.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureEast = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/east.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureWest = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/west.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureUp = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/up.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureDown = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/down.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        skybox.setTexture(Skybox.Face.North, skyboxTextureNorth);
        skybox.setTexture(Skybox.Face.South, skyboxTextureSouth);
        skybox.setTexture(Skybox.Face.East, skyboxTextureEast);
        skybox.setTexture(Skybox.Face.West, skyboxTextureWest);
        skybox.setTexture(Skybox.Face.Up, skyboxTextureUp);
        skybox.setTexture(Skybox.Face.Down, skyboxTextureDown);

        rootNode.attachChildAt(skybox, 0);
    }


    /**
     * Some simple lighting for our scene.
     */
    private void createLighting() {
        //Spot on!
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        final LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        getRootNode().setRenderState(lightState);
    }


    /**
     * Sets up the keyboard/mouse controller for our tank.
     */
    private void addController() {
        getRootNode().addController(new TankController(myTank, walls));
    }

    private void interpolateRemotePlayerLocations() {
        for (String remotePlayer : remotePlayers.keySet()) {
            Spatial remotePlayerTank = remotePlayersNode.getChild(remotePlayer);
            // remotePlayerTank should not be null, but putting a check in
            // here, just in case.
            if (remotePlayerTank != null) {
                Interpolator dr = remotePlayers.get(remotePlayer);
                remotePlayerTank.setLocalTranslation(dr.getCurrentTranslation());
                remotePlayerTank.setLocalRotation(dr.getCurrentRotation());
            } else {
                // Like I say, we should never get here.
                logger.severe("Player " + remotePlayer + " exists in remotPlayers but is not in remotePlayersNode");
            }
        }
    }


    /**
     * Poll the DarkstarUpdater.playerDetailsQueue for any new PlayerDetails
     * information.
     *
     * Note that this is run once per frame in the "update(...) method.
     * Therefore, only one player can get  updated per frame. My thinking (for
     * now) is that we can always increase the number of calls per frame if
     * necessary.
     */
    private void updateRemotePlayerLocations() {
        // First poll the playerDetailsQueue, if there's nothing there, then
        // it will return null, otherwise we'll get some details about a
        // remote player.
        PlayerDetails playerDetails = DarkstarUpdater.playerDetailsQueue.poll();

        // If there are some details, then we will process them now.
        if (playerDetails != null) {
            // Check to see if this is details of an existing player on our
            // "remotePlayers" map, or whether it's a player we haven't seen
            // before.
            Interpolator dr = remotePlayers.get(playerDetails.getPlayerName());

            // If the "get" returned null, then this is a new player. Set them
            // up.
            if (dr == null) {
                Spatial remotePlayerTank = createPlayer(playerDetails.getPlayerName());
                remotePlayersNode.attachChild(remotePlayerTank);
                getRootNode().updateRenderState();

                // Set up a new "Interpolator" with the same start and end
                // point (because we don't know where they're going yet).
                dr = new Interpolator();
                dr.setStartTranslation(playerDetails.getLocation());
                dr.setFinishTranslation(playerDetails.getLocation());
                dr.setStartRotation(playerDetails.getRotation());
                dr.setFinishRotation(playerDetails.getRotation());
                dr.setStartTimeMilis(System.currentTimeMillis());
                dr.setFinishTimeMilis(System.currentTimeMillis());
                remotePlayers.put(playerDetails.getPlayerName(), dr);
            } else {
                // If we're here it's because the player already existed. We
                // should therefore update their "Interpolator" instance.
                dr.setStartTranslation(dr.getCurrentTranslation());
                dr.setFinishTranslation(playerDetails.getLocation());
                dr.setStartRotation(dr.getCurrentRotation());
                dr.setFinishRotation(playerDetails.getRotation());
                dr.setStartTimeMilis(System.currentTimeMillis());
                dr.setFinishTimeMilis(System.currentTimeMillis()+DarkstarConstants.LOCATION_UPDATE_INTERVAL_MS+25);
            }
        }

        // Right, with our Interpolation out of the way, we can loop through
        // the players in our map, and calculate their new interpolated
        // positions.
        interpolateRemotePlayerLocations();
    }

    /**
     * @return the currentSpeed
     */
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * @param currentSpeed the currentSpeed to set
     */
    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
