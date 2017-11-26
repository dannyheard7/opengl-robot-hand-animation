package models;

import gmaths.Vec3;
import core.TextureLibrary;
import mesh.Camera;
import lights.Light;
import mesh.Mesh;
import mesh.Sphere;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class RobotFinger extends Model {

    private Mesh sphere;
    private NameNode finger;
    private TransformNode fingerBottomJoint, fingerMiddleJoint, fingerTopJoint;
    private Mat4 fingerBottomJointPosition, fingerMiddleJointPosition, fingerTopJointPosition;

    private float sectionHeight, fingerWidth, fingerDepth;

    public RobotFinger(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);
    }

    public SGNode buildSceneGraph(String name, Vec3 position, Mat4 rotation, float sectionHeight) {
        finger = new NameNode(name);

        this.fingerWidth = 0.75f;
        this.fingerDepth = 1f;
        this.sectionHeight = sectionHeight;

        TransformNode fingerPosition = new TransformNode("finger position", Mat4Transform.translate(position));

        fingerBottomJointPosition = rotation;
        fingerBottomJoint = new TransformNode("finger bottom joint", fingerBottomJointPosition);

        NameNode fingerBottom = new NameNode("finger bottom");
        Mat4 m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerBottomTransform = new TransformNode("finger bottom transform", m);
        MeshNode fingerBottomShape = new MeshNode("mesh.Sphere(finger bottom)", sphere);

        fingerMiddleJointPosition = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerMiddleJoint = new TransformNode("middle joint", fingerMiddleJointPosition);

        NameNode fingerMiddle = new NameNode("finger middle");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerMiddleTransform = new TransformNode("finger middle transform", m);
        MeshNode fingerMiddleShape = new MeshNode("mesh.Sphere(finger middle)", sphere);

        fingerTopJointPosition = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerTopJoint = new TransformNode("finger top joint", fingerTopJointPosition);

        NameNode fingerTop = new NameNode("finger top");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerTopTransform = new TransformNode("finger top transform", m);
        MeshNode fingerTopShape = new MeshNode("mesh.Sphere(finger top)", sphere);

        finger.addChild(fingerPosition);
            fingerPosition.addChild(fingerBottomJoint);
                fingerBottomJoint.addChild(fingerBottom);
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

    public void transformFinger(Mat4 m) {
        m = Mat4.multiply(fingerBottomJointPosition, m);
        fingerBottomJoint.setTransform(m);

        finger.update();
    }

    public void reset() {
        fingerBottomJoint.setTransform(fingerBottomJointPosition);
        fingerMiddleJoint.setTransform(fingerMiddleJointPosition);
        fingerTopJoint.setTransform(fingerTopJointPosition);

        finger.update();
    }

    public void curl(float angle) {
        // If angle is less than 90, then spheres collide
        double val = Math.abs(Math.sin(Math.toRadians(angle)));
        float diff = (this.fingerWidth / 2) / (float)Math.exp(1-val);

        Mat4 m = Mat4.multiply(fingerBottomJointPosition, Mat4Transform.translate(0, diff, 0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(angle));

        fingerBottomJoint.setTransform(m);

        m = Mat4.multiply(fingerMiddleJointPosition, Mat4Transform.translate(0, diff, 0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(angle));

        fingerMiddleJoint.setTransform(m);

        m = Mat4.multiply(fingerTopJointPosition, Mat4Transform.translate(0, diff, 0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(angle));

        fingerTopJoint.setTransform(m);

        finger.update();
    }

    public void addRing(Ring ring) {
        fingerBottomJoint.addChild(ring.getSceneGraph());
    }

    public float getFingerWidth() {
        return this.fingerWidth;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
    }
}
