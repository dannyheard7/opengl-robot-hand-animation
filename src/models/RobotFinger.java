/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package models;

import com.jogamp.opengl.GL3;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.Light;
import core.Camera;
import mesh.Mesh;
import mesh.Sphere;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class RobotFinger extends Model {

    private Mesh sphere;
    private NameNode finger;
    private TransformNode fingerTransform, fingerBottomJoint, fingerMiddleJoint, fingerTopJoint;
    private Mat4 fingerPosition, fingerBottomJointPosition, fingerMiddleJointPosition, fingerTopJointPosition;

    private float sectionHeight, fingerWidth, fingerDepth;

    public RobotFinger(GL3 gl, ArrayList<Light> lights, Camera camera, String name, Vec3 position, float sectionHeight) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);

        this.buildSceneGraph(name, position, sectionHeight);
    }

    private void buildSceneGraph(String name, Vec3 position, float sectionHeight) {
        finger = new NameNode(name);

        this.fingerWidth = 0.75f;
        this.fingerDepth = 1f;
        this.sectionHeight = sectionHeight;
        this.fingerPosition = Mat4Transform.translate(position);
        float spacing = fingerWidth / 4;

        fingerTransform = new TransformNode("finger position", fingerPosition);

        fingerBottomJointPosition = Mat4Transform.translate(0, spacing, 0);
        fingerBottomJoint = new TransformNode("finger bottom joint", fingerBottomJointPosition);

        NameNode fingerBottom = new NameNode("finger bottom");
        Mat4 m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(Mat4Transform.translate(0,sectionHeight / 2,0), m);
        TransformNode fingerBottomTransform = new TransformNode("finger bottom transform", m);
        MeshNode fingerBottomShape = new MeshNode("mesh.Sphere(finger bottom)", sphere);

        fingerMiddleJointPosition = Mat4Transform.translate(0,  sectionHeight + spacing, 0);
        fingerMiddleJoint = new TransformNode("middle joint", fingerMiddleJointPosition);

        NameNode fingerMiddle = new NameNode("finger middle");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(Mat4Transform.translate(0,sectionHeight / 2,0), m);
        TransformNode fingerMiddleTransform = new TransformNode("finger middle transform", m);
        MeshNode fingerMiddleShape = new MeshNode("mesh.Sphere(finger middle)", sphere);

        fingerTopJointPosition = Mat4Transform.translate(0,  sectionHeight + spacing, 0);
        fingerTopJoint = new TransformNode("finger top joint", fingerTopJointPosition);

        NameNode fingerTop = new NameNode("finger top");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(Mat4Transform.translate(0,sectionHeight/2,0),  m);
        TransformNode fingerTopTransform = new TransformNode("finger top transform", m);
        MeshNode fingerTopShape = new MeshNode("mesh.Sphere(finger top)", sphere);

        finger.addChild(fingerTransform);
            fingerTransform.addChild(fingerBottomJoint);
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
    }

    public void reset() {
        fingerBottomJoint.setTransform(fingerBottomJointPosition);
        fingerMiddleJoint.setTransform(fingerMiddleJointPosition);
        fingerTopJoint.setTransform(fingerTopJointPosition);

        finger.update();
    }

    public void curl(float angle) {
        Mat4 m = Mat4.multiply(fingerBottomJointPosition, Mat4Transform.rotateAroundX(angle));
        fingerBottomJoint.setTransform(m);

        m = Mat4.multiply(fingerMiddleJointPosition, Mat4Transform.rotateAroundX(angle));
        fingerMiddleJoint.setTransform(m);

        m = Mat4.multiply(fingerTopJointPosition, Mat4Transform.rotateAroundX(angle));
        fingerTopJoint.setTransform(m);

        finger.update();
    }

    public void rotateAroundZ(float angle) {
        Mat4 m = Mat4.multiply(fingerPosition, Mat4Transform.rotateAroundZ(angle));
        fingerTransform.setTransform(m);

        finger.update();
    }

    public void rotateAroundY(float angle) {
        Mat4 m = Mat4.multiply(fingerPosition, Mat4Transform.rotateAroundZ(angle));
        fingerTransform.setTransform(m);

        finger.update();
    }

    public void addRing(Ring ring) {
        fingerBottomJoint.addChild(ring.getSceneGraphRootNode());
    }

    @Override
    public SGNode getSceneGraphRootNode() {
        return finger;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
    }
}
