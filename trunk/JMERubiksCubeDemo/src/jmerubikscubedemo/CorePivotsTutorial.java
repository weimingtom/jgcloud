package jmerubikscubedemo;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Sphere;

/**
 * This tutorial will show just the inner "pivots" of the Rubiks cube. The cubes
 * themselves will be attached to these in a relative manner, so that when the
 * pivot turns, all connecting cubes will turn as well.
 *
 * This demo will use sphere's so that we can see the pivots. We'll just use a
 * simple node as the pivot later, as we don't need to see the inner mechanics.
 *
 * @author Richard Hawkes (2009)
 */
public class CorePivotsTutorial extends SimpleGame {

    private Sphere centrePivot,  frontPivot,  rearPivot,  topPivot,  bottomPivot,  leftPivot,  rightPivot;
    private static float OBJECT_SIZE = 50.0F;
    private static int DEFAULT_SAMPLES = 10;

    public static void main(String[] args) {
        CorePivotsTutorial tutorial1 = new CorePivotsTutorial();
        tutorial1.setConfigShowMode(ConfigShowMode.AlwaysShow);
        tutorial1.start();
    }

    @Override
    protected void simpleInitGame() {
        createPivots();
        createCylinders();
    }

    /**
     * Create a graphical display of the pivot points within a Rubiks cube. Each
     * pivot point will have cubes attached to it. The cubes attached to a pivot
     * will change when another pivot performs a turn. Such is the nature of the
     * Rubiks cube!
     */
    private void createPivots() {
        Node pivotNodes = new Node("pivots");

        centrePivot = new Sphere("centre", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        pivotNodes.attachChild(centrePivot);

        frontPivot = new Sphere("front", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        frontPivot.setLocalTranslation(0, 0, 0 - OBJECT_SIZE);
        pivotNodes.attachChild(frontPivot);

        rearPivot = new Sphere("rear", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        rearPivot.setLocalTranslation(0, 0, OBJECT_SIZE);
        pivotNodes.attachChild(rearPivot);

        leftPivot = new Sphere("left", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        leftPivot.setLocalTranslation(0 - OBJECT_SIZE, 0, 0);
        pivotNodes.attachChild(leftPivot);

        rightPivot = new Sphere("right", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        rightPivot.setLocalTranslation(OBJECT_SIZE, 0, 0);
        pivotNodes.attachChild(rightPivot);

        topPivot = new Sphere("top", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        topPivot.setLocalTranslation(0, OBJECT_SIZE, 0);
        pivotNodes.attachChild(topPivot);

        bottomPivot = new Sphere("bottom", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 5);
        bottomPivot.setLocalTranslation(0, 0 - OBJECT_SIZE, 0);
        pivotNodes.attachChild(bottomPivot);

        rootNode.attachChild(pivotNodes);
    }

    private void createCylinders() {
        Node cylinderNodes = new Node("Cylinders");

        Cylinder c1 = new Cylinder("C1", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        Quaternion q1 = new Quaternion();
        q1.fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
        c1.setLocalRotation(q1);
        c1.setLocalTranslation(0, OBJECT_SIZE/2, 0);
        cylinderNodes.attachChild(c1);

        Cylinder c2 = new Cylinder("C2", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        c2.setLocalRotation(q1);
        c2.setLocalTranslation(0, 0-(OBJECT_SIZE/2), 0);
        cylinderNodes.attachChild(c2);

        Cylinder c3 = new Cylinder("C3", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        c3.setLocalTranslation(0, 0, 0-(OBJECT_SIZE/2));
        cylinderNodes.attachChild(c3);

        Cylinder c4 = new Cylinder("C4", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        c4.setLocalTranslation(0, 0, OBJECT_SIZE/2);
        cylinderNodes.attachChild(c4);

        Cylinder c5 = new Cylinder("C5", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        Quaternion q2 = new Quaternion();
        q2.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
        c5.setLocalRotation(q2);
        c5.setLocalTranslation(0-(OBJECT_SIZE/2), 0, 0);
        cylinderNodes.attachChild(c5);

        Cylinder c6 = new Cylinder("C6", DEFAULT_SAMPLES, DEFAULT_SAMPLES, 1, OBJECT_SIZE);
        c6.setLocalRotation(q2);
        c6.setLocalTranslation(OBJECT_SIZE/2, 0, 0);
        cylinderNodes.attachChild(c6);

        rootNode.attachChild(cylinderNodes);
    }
}
