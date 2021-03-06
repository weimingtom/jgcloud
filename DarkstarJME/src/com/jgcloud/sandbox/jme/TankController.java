package com.jgcloud.sandbox.jme;

import com.jgcloud.sandbox.darkstar.DarkstarUpdater;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

import com.jme.system.DisplaySystem;
import java.util.logging.Logger;
import static com.jgcloud.sandbox.jme.TankController.CubeAction.*;
import static com.jme.input.KeyInput.*;
import static com.jme.input.controls.binding.MouseButtonBinding.*;

/**
 * Basic Controller implementation for our tank.
 *
 * @author Richard Hawkes
 */
public class TankController extends Controller {
    Logger logger = Logger.getLogger(TankController.class.getName());

    enum CubeAction {FAST, LEFT, RIGHT, UP, DOWN, SNAPSHOT, EXIT};

    private final static float TURN_SPEED = 2F;
    private final static float FORWARD_SPEED = 16F;


    private final Node node;
    private Node walls;

    private final GameControlManager manager;
    private float vAngle = 0F;
    private float hAngle = 0F;

    public TankController(Node node, Node walls) {
        this.node = node;
        this.walls = walls;

        this.manager = new GameControlManager();

        //create all actions
        for (CubeAction action : CubeAction.values()) {
            manager.addControl(action.name());
        }
        //bind keys
        bindKey(EXIT, KEY_X);
        bindKey(UP, KEY_UP);
        bindKey(DOWN, KEY_DOWN);
        bindKey(LEFT, KEY_LEFT);
        bindKey(RIGHT, KEY_RIGHT);
        bindKey(FAST, KEY_LSHIFT);
        bindKey(SNAPSHOT, KEY_F12);

        //bind mouse buttons
        bindMouseButton(LEFT, LEFT_BUTTON);
        bindMouseButton(RIGHT, RIGHT_BUTTON);
    }


    private void bindKey(CubeAction action, int... keys) {
        final GameControl control = manager.getControl(action.name());
        for (int key : keys) {
          control.addBinding(new KeyboardBinding(key));
        }
    }

    private void bindMouseButton(CubeAction action, int mouseButton) {
        final GameControl control = manager.getControl(action.name());
        control.addBinding(new MouseButtonBinding(mouseButton));
    }

    private float value(CubeAction action) {
        return manager.getControl(action.name()).getValue();
    }

    @Override
    public void update(float time) {
        if (value(EXIT) > 0) {
            DarkstarUpdater.getInstance().serverLogout();
            System.exit(0); //OK, this is just a demo...
        } else if (value(SNAPSHOT) > 0) {
            DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot("img-" + System.currentTimeMillis());
        }

        Vector3f origTranslation = node.getLocalTranslation().clone();
        Quaternion origRotation = node.getLocalRotation().clone();

        Vector3f newLocation = node.getLocalTranslation().clone();
        Quaternion newRotation = node.getLocalRotation().clone();

        float turnAmount = TURN_SPEED * time * (value(LEFT) - value(RIGHT));
        hAngle += turnAmount;
        newRotation.fromAngles(vAngle, hAngle, 0f);
        node.getLocalRotation().set(newRotation);

        // I don't get this next line. What is getRotationColumn? value must be
        // between 0 and 2. I think 0=x, 1=y and 2=z... But I is baffled!
        Vector3f direction = new Vector3f(node.getLocalRotation().getRotationColumn(2));

        float speed = time * FORWARD_SPEED * (value(DOWN) - value(UP));
        speed *= 1 + (2 * value(FAST)); // Double the speed if the fast button is being pressed.
        newLocation.addLocal(direction.mult(speed));

        node.getLocalTranslation().set(newLocation);
        node.updateGeometricState(0, true);
        
        if (node.hasCollision(walls, false)) {
            node.getLocalTranslation().set(origTranslation);
            node.getLocalRotation().set(origRotation);
            hAngle -= turnAmount;
            node.updateGeometricState(0, true);
        }
    }
}