import models.Camera;
import lights.Light;
import models.Mesh;
import models.Sphere;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class RobotFinger {

    private Mesh sphere;
    private TransformNode fingerJoint, fingerMiddleJoint, fingerTopJoint;

    private float sectionHeight, fingerWidth, fingerDepth;


    public RobotFinger(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);
    }

    public float getSectionHeight() {
        return this.sectionHeight;
    }


    // TODO: Stop passing gl, camera and light around so much?
    public SGNode buildSceneGraph(String name, Vec3 pos, float sectionHeight) {
        NameNode finger = new NameNode(name);

        this.fingerWidth = 0.75f;
        this.fingerDepth = 1f;
        this.sectionHeight = sectionHeight;

        Mat4 m = Mat4Transform.translate(pos);
        fingerJoint = new TransformNode("finger joint", m);

        NameNode fingerBottom = new NameNode("finger bottom");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerBottomTransform = new TransformNode("finger bottom transform", m);
        MeshNode fingerBottomShape = new MeshNode("models.Sphere(finger bottom)", sphere);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerMiddleJoint = new TransformNode("middle joint", m);

        NameNode fingerMiddle = new NameNode("finger middle");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerMiddleTransform = new TransformNode("finger middle transform", m);
        MeshNode fingerMiddleShape = new MeshNode("models.Sphere(finger middle)", sphere);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerTopJoint = new TransformNode("finger top joint", m);

        NameNode fingerTop = new NameNode("finger top");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerTopTransform = new TransformNode("finger top transform", m);
        MeshNode fingerTopShape = new MeshNode("models.Sphere(finger top)", sphere);

        finger.addChild(fingerJoint);
            fingerJoint.addChild(fingerBottom);
                fingerBottom.addChild(fingerBottomTransform);
                    fingerBottomTransform.addChild(fingerBottomShape);
                fingerBottom.addChild(fingerMiddleJoint);
                    fingerMiddleJoint.addChild(fingerMiddle);
                        fingerMiddle.addChild(fingerMiddleTransform);
                            fingerMiddleTransform.addChild(fingerMiddleShape);
                        fingerMiddle.addChild(fingerTopJoint);
                            fingerTopJoint.addChild(fingerTop);
                                fingerTop.addChild(fingerTopTransform);
                                    fingerTopTransform.addChild(fingerTopShape);


        return finger;
    }

    public void translateFinger(Mat4 m) {
        fingerJoint.updateTransform(m);
    }

    public void curled(float angle) {
        // If angle is less than 90, then spheres will not be touching
        double val = Math.abs(Math.sin(Math.toRadians(angle)));

        float diff = 0;
        if (val > 0 && val < 1) {
            diff = (this.sectionHeight / 4) / (float)val;
        }

        Mat4 m = Mat4Transform.translate(0, 0.5f, 0);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(angle));

        fingerJoint.updateTransform(m);

        m = Mat4.multiply(m, Mat4Transform.translate(0, -diff, 0.25f));

        fingerMiddleJoint.updateTransform(m);

        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.25f, 0));

        fingerTopJoint.updateTransform(m);
    }


    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
    }
}
