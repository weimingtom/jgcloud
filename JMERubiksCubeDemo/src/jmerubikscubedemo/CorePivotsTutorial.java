package jmerubikscubedemo;

import com.jme.app.SimpleGame;
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
    private Sphere centrePivot, frontPivot, rearPivot, topPivot, bottomPivot, leftPivot, rightPivot;

    public static void main(String[] args) {
        CorePivotsTutorial tutorial1 = new CorePivotsTutorial();
        tutorial1.setConfigShowMode(ConfigShowMode.AlwaysShow);
        tutorial1.start();
    }

    @Override
    protected void simpleInitGame() {
        createPivots();
    }

    /**
     * Create a graphical display of the pivot points within a Rubiks cube. Each
     * pivot point will have cubes attached to it. The cubes attached to a pivot
     * will change when another pivot performs a turn. Such is the nature of the
     * Rubiks cube!
     */
    private void createPivots() {
        centrePivot = new Sphere("Centre", 20, 20, 5);
        centrePivot.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(centrePivot);

        frontPivot = new Sphere("Front", 20, 20, 5);
        centrePivot.setLocalTranslation(0, 0, -50); // Z is negative because z decreases as you go in to the distance!!
        rootNode.attachChild(frontPivot);

        rearPivot = new Sphere("Rear", 20, 20, 5);
        rearPivot.setLocalTranslation(0, 0, 50);
        rootNode.attachChild(rearPivot);

        topPivot = new Sphere("Top", 20, 20, 5);
        topPivot.setLocalTranslation(0, 50, 0);
        rootNode.attachChild(topPivot);

        bottomPivot = new Sphere("Bottom", 20, 20, 5);
        bottomPivot.setLocalTranslation(0, -50, 0);
        rootNode.attachChild(bottomPivot);

        leftPivot = new Sphere("Left", 20, 20, 5);
        leftPivot.setLocalTranslation(-50, 0, 0);
        rootNode.attachChild(leftPivot);

        rightPivot = new Sphere("Right", 20, 20, 5);
        rightPivot.setLocalTranslation(50, 0, 0);
        rootNode.attachChild(rightPivot);
    }
}
