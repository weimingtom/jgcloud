package worldeditor;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.intersection.CollisionData;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.intersection.TriangleCollisionResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.light.PointLight;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.QuadMesh;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.xml.XMLImporter;
import com.jmex.game.StandardGame;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Richard Hawkes
 */
public class TestGameState extends BasicGameState implements InspectableGame {

    private Spatial character;
    private float characterHorizontalAngle = 0F;
    private ChaseCamera chaseCamera;
    private KeyBindingManager kbm = KeyBindingManager.getKeyBindingManager();
    private static final float FORWARD_SPEED = 5f;
    private static final float TURN_SPEED = 2f;
    private Skybox skybox;
    private XMLImporter xmlImporter = XMLImporter.getInstance();
    private Logger logger = Logger.getLogger(TestGameState.class.toString());


    public static void main(String[] args) {
        StandardGame standardGame = new StandardGame("GameControl", StandardGame.GameType.GRAPHICAL, null);
        standardGame.getSettings().setWidth(640);
        standardGame.getSettings().setHeight(480);
        standardGame.getSettings().setFullscreen(false);
        standardGame.getSettings().setSamples(8);
        standardGame.start();

        GameState client = new TestGameState();
        GameStateManager.getInstance().attachChild(client);
        client.setActive(true);
    }

    public TestGameState() {
        super("Test Game State");
        WorldEditorFrame.runWorldEditorFrame(this);
        init();
    }

    private void init() {
        loadSceneObjects();
        createCharacter();
        createSkybox();
        createChaseCamera();
        setKeyBinding();
    }

    private void loadSceneObjects() {
        loadSceneObject(new File("E:/Blender Work/castle/castle-jme.xml"), Vector3f.ZERO, 15f);
        loadSceneObject(new File("E:/Blender Work/catapult/catapult-jme.xml"), new Vector3f(68.0f, -72.4f, -165f), 5f);
    }

    private void loadSceneObject(File xmlFile, Vector3f translation, float scale) {
        try {
            long startTime = System.currentTimeMillis();

            Spatial spatial = (Spatial)xmlImporter.load(xmlFile);
            spatial.setLocalTranslation(translation);
            spatial.setLocalScale(scale);
            spatial.setModelBound(new BoundingBox());
            spatial.updateModelBound();
            getRootNode().attachChild(spatial);
            getRootNode().updateRenderState();

            logger.info("Loaded " + xmlFile.getName() + " in " + (System.currentTimeMillis()-startTime) + "ms");
        } catch (Exception ex) {
            logger.severe("Unable to load scene object: " + ex);
        }
    }

    private void createCharacter() {
        character = new Box("character-cube", new Vector3f(), 2f, 2f, 2f);
        character.setModelBound(new BoundingBox());
        character.updateModelBound();
        getRootNode().attachChild(character);
        getRootNode().updateWorldBound();
    }

    private void createSkybox() {
        skybox = new Skybox("skybox", 100, 100, 100);

        try {
            URL skyboxFileNorth = new File("images/north.png").toURI().toURL();
            URL skyboxFileSouth = new File("images/south.png").toURI().toURL();
            URL skyboxFileEast = new File("images/east.png").toURI().toURL();
            URL skyboxFileWest = new File("images/west.png").toURI().toURL();
            URL skyboxFileUp = new File("images/up.png").toURI().toURL();
            URL skyboxFileDown = new File("images/down.png").toURI().toURL();

            Texture skyboxTextureNorth = TextureManager.loadTexture(skyboxFileNorth, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            Texture skyboxTextureSouth = TextureManager.loadTexture(skyboxFileSouth, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            Texture skyboxTextureEast = TextureManager.loadTexture(skyboxFileEast, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            Texture skyboxTextureWest = TextureManager.loadTexture(skyboxFileWest, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            Texture skyboxTextureUp = TextureManager.loadTexture(skyboxFileUp, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
            Texture skyboxTextureDown = TextureManager.loadTexture(skyboxFileDown, Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);

            skybox.setTexture(Skybox.Face.North, skyboxTextureNorth);
            skybox.setTexture(Skybox.Face.South, skyboxTextureSouth);
            skybox.setTexture(Skybox.Face.East, skyboxTextureEast);
            skybox.setTexture(Skybox.Face.West, skyboxTextureWest);
            skybox.setTexture(Skybox.Face.Up, skyboxTextureUp);
            skybox.setTexture(Skybox.Face.Down, skyboxTextureDown);

            rootNode.attachChildAt(skybox, 0);
            rootNode.updateRenderState();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load skybox: " + ex);
        }
    }

    private void createChaseCamera() {
        Vector3f targetOffset = new Vector3f();

        targetOffset.y = ((BoundingBox) character.getWorldBound()).yExtent * 1F;
        targetOffset.z = ((BoundingBox) character.getWorldBound()).zExtent * 1F;

        HashMap props = new HashMap();
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);

        chaseCamera = new ChaseCamera(DisplaySystem.getDisplaySystem().getRenderer().getCamera(), character, props);
    }

    private void createLighting() {
        //Spot on!
        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        getRootNode().setRenderState(lightState);
    }

    public void update(float tpf) {
        super.update(tpf);
        handleKeyInput(tpf);
        collisionCheck(tpf);
        rayCheck(tpf);
        chaseCamera.update(tpf);
        skybox.setLocalTranslation(chaseCamera.getCamera().getLocation());
    }

    private void handleKeyInput(float tpf) {
        float forward = 0;
        float turn = 0; // 1 is right, -1 is left

        if (kbm.isValidCommand("forward")) {
            forward++;
        }

        if (kbm.isValidCommand("back")) {
            forward--;
        }

        if (kbm.isValidCommand("turn-left")) {
            turn++;
        }

        if (kbm.isValidCommand("turn-right")) {
            turn--;
        }

        if (forward == 0 && turn == 0) {
            // nothing to do!
            return;
        }

        Vector3f newLocation = character.getLocalTranslation().clone();
        Quaternion newRotation = character.getLocalRotation().clone();

        float turnAmount = TURN_SPEED * tpf * turn;
        characterHorizontalAngle += turnAmount;
        newRotation.fromAngles(0f, characterHorizontalAngle, 0f);
        character.getLocalRotation().set(newRotation);

        // I don't get this next line. What is getRotationColumn? value must be
        // between 0 and 2. I think 0=x, 1=y and 2=z... But I is baffled!
        Vector3f direction = new Vector3f(character.getLocalRotation().getRotationColumn(2));

        float speed = tpf * FORWARD_SPEED * forward;
        newLocation.addLocal(direction.mult(speed));

        character.getLocalTranslation().set(newLocation);
        character.updateGeometricState(0, true);
    }

    private void collisionCheck(float tpf) {
        if (character.hasCollision(getRootNode(), false)) {
            CollisionResults collisionResults = new TriangleCollisionResults();

            character.calculateCollisions(getRootNode(), collisionResults);
            setCharacterYValue(collisionResults);
        }
    }

    private float getMaxTriangleYValue(TriMesh triMesh, List<Integer> trianglesToCheck, float currentMaxY) {
        float maxY = currentMaxY;

        for (int triangleIndex : trianglesToCheck) {
            Vector3f[] triangleVertices = new Vector3f[3];
            triMesh.getTriangle(triangleIndex, triangleVertices);

            if (triangleVertices[0].getY() > maxY) {
                maxY = triangleVertices[0].getY();
            }
            if (triangleVertices[1].getY() > maxY) {
                maxY = triangleVertices[1].getY();
            }
            if (triangleVertices[2].getY() > maxY) {
                maxY = triangleVertices[2].getY();
            }
        }

        return maxY;
    }

    private void rayCheck(float tpf) {
        Ray characterDownRay = new Ray(character.getLocalTranslation(), new Vector3f(0, -1, 0));
        PickResults results = new TrianglePickResults();
        results.setCheckDistance(true);
        rootNode.findPick(characterDownRay, results);

        // ERROR: This isn't working properly. It's finding the character (most of the time)
        if (results.getNumber() > 0) {
            PickData closest = results.getPickData(0);
//            System.out.println("Closest=" + closest.getTargetMesh().getName() + " distance=" + closest.getDistance());
        }
    }

    private void setCharacterYValue(CollisionResults collisionResults) {
        for (int i = 0; i < collisionResults.getNumber(); i++) {
            float maxY = character.getLocalTranslation().getY();

            CollisionData collisionData = collisionResults.getCollisionData(i);
            Geometry targetMesh = collisionData.getTargetMesh();
            
            if (targetMesh instanceof QuadMesh) {
                logger.severe("Geometry '" + targetMesh.getName() + "' is of type QuadMesh. It should be a TriMesh. In Blender: Select Object -> Edit -> Select All (A) - Convert to Triangles (Ctrl+T)");
            } else if (targetMesh instanceof TriMesh) {
                maxY = getMaxTriangleYValue((TriMesh) targetMesh, collisionData.getTargetTris(), maxY);
            } 

            if (maxY < 100) {
                character.getLocalTranslation().setY(maxY);
            }
        }
    }

    private void setKeyBinding() {
        kbm.set("forward", KeyInput.KEY_W);
        kbm.set("back", KeyInput.KEY_S);
        kbm.set("turn-left", KeyInput.KEY_A);
        kbm.set("turn-right", KeyInput.KEY_D);
    }
}
