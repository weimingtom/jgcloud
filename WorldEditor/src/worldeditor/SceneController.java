package worldeditor;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class SceneController {
    public static void updateTranslation(Spatial spatial, float xVal, float yVal, float zVal) {
        spatial.setLocalTranslation(xVal, yVal, zVal);
        spatial.updateRenderState();
    }

    public static void updateRotation(Spatial spatial, float xAngle, float yAngle, float zAngle) {
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(FastMath.PI * (xAngle/180f), FastMath.PI * (yAngle/180f), FastMath.PI * (zAngle/180f));
        spatial.setLocalRotation(rotation);
        spatial.updateRenderState();
    }

    public static void updateScale(Spatial spatial, float xVal, float yVal, float zVal) {
        spatial.setLocalScale(new Vector3f(xVal, yVal, zVal));
        spatial.updateRenderState();
    }

    public static float radiansToDegrees(float radians) {
        return radians * (180/FastMath.PI);
    }
}
