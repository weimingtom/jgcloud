package jmetry1;


import javax.swing.ImageIcon;

import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/**
 * Tutorial 3 Loads a random terrain for uses at the game level.
 * framework for Flag Rush. For Flag Rush Tutorial Series.
 *
 * @author Mark Powell
 */
public class Lesson3 extends BaseGame {
	private TerrainBlock tb;

	protected Timer timer;

	// Our camera object for viewing the scene
	private Camera cam;

	// the root node of the scene graph
	private Node scene;

	// display attributes for the window. We will keep these values
	// to allow the user to change them
	private int width, height, depth, freq;

	private boolean fullscreen;

	/**
	 * Main entry point of the application
	 */
	public static void main(String[] args) {
		Lesson3 app = new Lesson3();
		// We will load our own "fantastic" Flag Rush logo. Yes, I'm an artist.
		app.setConfigShowMode(ConfigShowMode.AlwaysShow, Lesson3.class.getClassLoader().getResource("jmetest/data/images/FlagRush.png"));
		app.start();
	}

	/**
	 * During an update we only look for the escape button and update the timer
	 * to get the framerate.
	 *
	 * @see com.jme.app.SimpleGame#update()
	 */
	protected void update(float interpolation) {
		// update the time to get the framerate
		timer.update();
		interpolation = timer.getTimePerFrame();
		// if escape was pressed, we exit
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}
	}

	/**
	 * draws the scene graph
	 *
	 * @see com.jme.app.SimpleGame#render()
	 */
	protected void render(float interpolation) {
		// Clear the screen
		display.getRenderer().clearBuffers();

		display.getRenderer().draw(scene);

	}

	/**
	 * initializes the display and camera.
	 *
	 * @see com.jme.app.SimpleGame#initSystem()
	 */
	protected void initSystem() {
		// store the properties information
		width = settings.getWidth();
		height = settings.getHeight();
		depth = settings.getDepth();
		freq = settings.getFrequency();
		fullscreen = settings.isFullscreen();

		try {
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
			display.createWindow(width, height, depth, freq, fullscreen);

			cam = display.getRenderer().createCamera(width, height);
		} catch (JmeException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// set the background to black
		display.getRenderer().setBackgroundColor(ColorRGBA.black);

		// initialize the camera
		cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1,
				1000);
		Vector3f loc = new Vector3f(500.0f, 150.0f, 500.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0.0f, -1.0f);
		// Move our camera to a correct place and orientation.
		cam.setFrame(loc, left, up, dir);
		/** Signal that we've changed our camera's location/frustum. */
		cam.update();

		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);
	}

	/**
	 * initializes the scene
	 *
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void initGame() {
		scene = new Node("Scene graph node");
		buildTerrain();
		scene.attachChild(tb);

	    buildLighting();

		// update the scene graph for rendering
		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();
	}

	/**
	 * creates a light for the terrain.
	 */
	private void buildLighting() {
		/** Set up a basic, default light. */
	    DirectionalLight light = new DirectionalLight();
	    light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    light.setDirection(new Vector3f(1,-1,0));
	    light.setEnabled(true);

	      /** Attach the light to a lightState and the lightState to rootNode. */
	    LightState lightState = display.getRenderer().createLightState();
	    lightState.setEnabled(true);
	    lightState.attach(light);
	    scene.setRenderState(lightState);
	}

	/**
	 * build the height map and terrain block.
	 */
	private void buildTerrain() {
		// Generate a random terrain data
		MidPointHeightMap heightMap = new MidPointHeightMap(64, 1f);
		// Scale the data
		Vector3f terrainScale = new Vector3f(20, 0.5f, 20);
		// create a terrainblock
		tb = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,	heightMap.getHeightMap(), new Vector3f(0, 0, 0));

		tb.setModelBound(new BoundingBox());
		tb.updateModelBound();

		// generate a terrain texture with 3 textures
		ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
		pt.addTexture(new ImageIcon(Lesson3.class.getClassLoader().getResource("jmetest/data/texture/grassb.png")), -128, 0, 1000);
//		pt.addTexture(new ImageIcon(Lesson3.class.getClassLoader().getResource("jmetest/data/texture/dirt.jpg")), 0, 128, 255);
//		pt.addTexture(new ImageIcon(Lesson3.class.getClassLoader().getResource("jmetest/data/texture/highest.jpg")), 128, 255, 384);
		pt.createTexture(32);

		// assign the texture to the terrain
		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t1, 0);

		tb.setRenderState(ts);
	}

	/**
	 * will be called if the resolution changes
	 *
	 * @see com.jme.app.SimpleGame#reinit()
	 */
	protected void reinit() {
		display.recreateWindow(width, height, depth, freq, fullscreen);
	}

	/**
	 * clean up the textures.
	 *
	 * @see com.jme.app.SimpleGame#cleanup()
	 */
	protected void cleanup() {

	}
}