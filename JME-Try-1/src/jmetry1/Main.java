package jmetry1;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Cone;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

public class Main extends SimpleGame {

    public static void main(String[] args) {
        Main app = new Main();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {
        Sphere s = new Sphere("Sphere", 20, 20, 10);
        s.setLocalTranslation(new Vector3f(10, 0, -40));
        s.setModelBound(new BoundingBox());
        s.updateModelBound();

        Cone c = new Cone("Cone", 20, 20, 20,10);
        c.setLocalTranslation(new Vector3f(0,0,-20));
        c.setModelBound(new BoundingBox());
        c.updateModelBound();
//        c.rotatePoints(new Quaternion(90,90,90,90));

        Texture texture = TextureManager.loadTexture(Main.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(texture);
        s.setRenderState(ts);

        rootNode.attachChild(s);
        rootNode.attachChild(c);
    }
}
