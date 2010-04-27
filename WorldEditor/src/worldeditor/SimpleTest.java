package worldeditor;

import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.MouseInput;
import com.jme.scene.Node;
import com.jme.scene.shape.*;

public class SimpleTest extends SimpleGame implements worldeditor.InspectableGame {

    public static void main(String[] args) {
        SimpleTest st = new SimpleTest();
        st.setConfigShowMode(ConfigShowMode.AlwaysShow);
        WorldEditorFrame.runWorldEditorFrame(st);
        st.start();
    }

    @Override
    protected void simpleInitGame() {
        // Windowed mode is extremely irritating without these two settings.
        MouseInput.get().setCursorVisible(true);
        ((FirstPersonHandler) input).setButtonPressRequired(true);

        Teapot t = new Teapot("teapot-1");
        Sphere s = new Sphere("sphere-1", 32, 32, 1.0f);

        rootNode.attachChild(t);
        rootNode.attachChild(s);

        rootNode.updateRenderState();
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }
}
