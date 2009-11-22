package com.jgcloud.sandbox.jme;

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
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
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
 *
 * @author Richard Hawkes
 */
public class TankGameState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(TankGameState.class.getName());

    private int FLOOR_WIDTH = 200;
    private int FLOOR_HEIGHT = 400;
    private int WALL_HEIGHT = 10;

    private Node myTank;
    private Node remotePlayersNode; // Node that contains the remote players.

    private Camera cam;
    private ChaseCamera chaseCamera;
    private Skybox skybox;
    private static StandardGame standardGame;
    private static Map<String,PlayerDetails> remotePlayers = new ConcurrentHashMap<String,PlayerDetails>();

    public static void main(String[] args) {
        standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
        standardGame.getSettings().setSamples(8);
        standardGame.start();

        GameState client = new TankGameState();
        GameStateManager.getInstance().attachChild(client);
        client.setActive(true);

        logger.info("TankGameState.main complete");
    }


    public TankGameState() {
        super("Main Client");

        cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        init();

        Thread t = new Thread(DarkstarUpdater.getInstance(myTank));
        t.start();
    }


    protected void init() {
        configureGameTaskQueueManager();
        createArena();
        createMyTank();
        createChaseCamera();
        createSkybox();
        createLighting();
        addController();
        createRemotePlayersNode();
//        createTestPlayer();
        
        getRootNode().updateRenderState();
    }


    public static Map<String,PlayerDetails> getRemotePlayers() {
        return remotePlayers;
    }

    private void configureGameTaskQueueManager() {
        // Set it so that each "execute" statement only executes one task...
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).setExecuteAll(false);
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
        TextureState floorTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture floorTexture = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        floorTexture.setWrap(Texture.WrapMode.Repeat);
        floorTexture.setScale(new Vector3f(10,10,10));

        floorTextureState.setTexture(floorTexture);
        floor.setRenderState(floorTextureState);

        rootNode.attachChild(floor);
        
        Node walls = new Node("Walls");

        //load a texture for the walls
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
        backWall.setLocalTranslation(0, WALL_HEIGHT/2, FLOOR_HEIGHT/2);
        backWall.setModelBound(new BoundingBox());
        backWall.updateModelBound();

        // The front wall does not need to be rotated. It defaults to vertical.
        Quad frontWall = new Quad("Front Wall", FLOOR_WIDTH, WALL_HEIGHT);
        walls.attachChild(frontWall);
        frontWall.setLocalTranslation(0, WALL_HEIGHT/2, 0 - (FLOOR_HEIGHT/2));
        frontWall.setModelBound(new BoundingBox());
        frontWall.updateModelBound();

        Quad leftWall = new Quad("Left Wall", FLOOR_HEIGHT, WALL_HEIGHT);
        walls.attachChild(leftWall);
        leftWall.setLocalRotation(yaw90);
        leftWall.setLocalTranslation(0-(FLOOR_WIDTH/2), WALL_HEIGHT/2, 0);
        leftWall.setModelBound(new BoundingBox());
        leftWall.updateModelBound();

        Quad rightWall = new Quad("Right Wall", FLOOR_HEIGHT, WALL_HEIGHT);
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

        // Execute any tasks on the queue...
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

        updateRemotePlayerLocations();

        chaseCamera.update(tpf);

        //we want to keep the skybox around our eyes, so move it with the camera.
        skybox.setLocalTranslation(cam.getLocation());
    }


    private void createChaseCamera() {
        Vector3f targetOffset = new Vector3f();

        targetOffset.y = ((BoundingBox)myTank.getWorldBound()).yExtent * 3F;
        targetOffset.z = ((BoundingBox)myTank.getWorldBound()).zExtent * 1F;

        HashMap props = new HashMap();
//        props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "10");
//        props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "2");
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
//        props.put(ThirdPersonMouseLook.PROP_MAXASCENT, ""+(45*FastMath.DEG_TO_RAD));
//        props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD));
//        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        chaseCamera = new ChaseCamera(DisplaySystem.getDisplaySystem().getRenderer().getCamera(), myTank, props);
//        chaseCamera.setMaxDistance(10);
//        chaseCamera.setMinDistance(6);
    }

    private void createRemotePlayersNode() {
        remotePlayersNode = new Node("RemotePlayers");
        getRootNode().attachChild(remotePlayersNode);
    }


    private void createSkybox() {
        skybox = new Skybox("skybox", 512, 512, 512);

        Texture skyboxTextureNorth = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/north.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureSouth = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/south.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureEast = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/east.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureWest = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/west.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureUp = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/up.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture skyboxTextureDown = TextureManager.loadTexture(TankGameState.class.getClassLoader().getResource("images/down.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

        skybox.setTexture(Skybox.Face.North, skyboxTextureNorth);
        skybox.setTexture(Skybox.Face.South, skyboxTextureSouth);
        skybox.setTexture(Skybox.Face.East, skyboxTextureEast);
        skybox.setTexture(Skybox.Face.West, skyboxTextureWest);
        skybox.setTexture(Skybox.Face.Up, skyboxTextureUp);
        skybox.setTexture(Skybox.Face.Down, skyboxTextureDown);

        rootNode.attachChild(skybox);
    }

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

    private void addController() {
        getRootNode().addController(new TankController(myTank));
    }

    private void createTestPlayer() {
        // PN=testUser001,CT=1258839563002,TX=-6.7676134,TY=0.0,TZ=-18.557644,RX=0.0,RY=0.050041594,RZ=0.0,RW=0.9987471
        PlayerDetails pd = new PlayerDetails("Richard", new Vector3f(-6.7676134F,0.0F,-18.557644F), new Quaternion(0.0F,0.050041594F,0.0F,0.9987471F), 1258839563002L);
        remotePlayers.put(pd.getPlayerName(), pd);
    }

    private void updateRemotePlayerLocations() {
        for (String remotePlayerName : remotePlayers.keySet()) {
            PlayerDetails playerDetails = remotePlayers.get(remotePlayerName);
            // Node is a sub-class of spatial. The getChild() returns a spatial,
            // but that's ok for what we need to do here.
            Spatial remotePlayerTank = remotePlayersNode.getChild(remotePlayerName);
            // This might be a new player, so create it if it is.
            if (remotePlayerTank == null) {
                remotePlayerTank = createPlayer(remotePlayerName);
                remotePlayersNode.attachChild(remotePlayerTank);
                getRootNode().updateRenderState();
            }
            remotePlayerTank.setLocalTranslation(playerDetails.getLocation());
            remotePlayerTank.setLocalRotation(playerDetails.getRotation());
        }
    }
}
