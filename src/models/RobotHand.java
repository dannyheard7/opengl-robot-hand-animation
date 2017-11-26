package models;

import core.KeyFrame;
import core.TextureLibrary;
import mesh.Camera;
import mesh.Cube;
import lights.Light;
import mesh.Mesh;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class RobotHand extends Model {

    private Mesh handCube, armCube;
    private SGNode robot;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger, thumb;
    private Ring ring;

    private KeyFrame indexFingerAnim, middleFingerAnim, ringFingerAnim, pinkyFingerAnim;

    public RobotHand(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");

        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/container.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container_specular.jpg");

        handCube = new Cube(gl, textureId0, textureId1);
        handCube.setLights(lights);
        handCube.setCamera(camera);

        armCube = new Cube(gl, textureId2, textureId3);
        armCube.setLights(lights);
        armCube.setCamera(camera);

        indexFinger = new RobotFinger(gl, lights, camera);
        middleFinger = new RobotFinger(gl, lights, camera);
        ringFinger = new RobotFinger(gl, lights, camera);
        pinkyFinger = new RobotFinger(gl, lights, camera);
        thumb = new RobotFinger(gl, lights, camera);

        ring = new Ring(gl, lights, camera);

        indexFingerAnim = new KeyFrame(0, 90, 1, indexFinger::curl);
        middleFingerAnim = new KeyFrame(0, 90, 1, middleFinger::curl);
        ringFingerAnim =  new KeyFrame(0, 90, 1, ringFinger::curl);
        pinkyFingerAnim =  new KeyFrame(0, 90, 1, pinkyFinger::curl);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        float armHeight = 3f;
        float armScale = 1.25f;

        float handHeight = 1.5f;
        float handWidth = 3f;
        float handScale = 1f;

        robot = new NameNode("robot armCube");

        TransformNode robotArmTranslate = new TransformNode("armCube translate", Mat4Transform.translate(0,0,0));

        NameNode arm = new NameNode("armCube");
        Mat4 m = Mat4Transform.scale(armScale, armHeight,armScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode armTransform = new TransformNode("armCube transform", m);
        MeshNode armShape = new MeshNode("mesh.Cube(armCube)", armCube);

        NameNode hand = new NameNode("handCube");
        m = Mat4Transform.translate(0, armHeight, 0);
        TransformNode handTranslate = new TransformNode("handCube translate", m);

        m = Mat4Transform.scale(handWidth, handHeight, handScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode handTransform = new TransformNode("handCube transform", m);
        MeshNode handShape = new MeshNode("mesh.Cube(handCube)", handCube);

        float dist = 2.5f  / 3;

        Vec3 indexPos = new Vec3(1.25f, handHeight, 0f);
        Vec3 middlePos = new Vec3((1.25f-dist), handHeight, 0f);
        Vec3 ringPos = new Vec3((-1.25f+dist), handHeight, 0f);
        Vec3 pinkyPos = new Vec3(-1.25f, handHeight, 0f);
        Vec3 thumbPos = new Vec3(1.5f, handHeight / 2, 0f);
        Mat4 fingerRoate = Mat4Transform.rotateAroundX(0);
        Mat4 thumbRotate = Mat4Transform.rotateAroundZ(-90);

        SGNode indexFingerNode = indexFinger.buildSceneGraph("Index Finger", indexPos, fingerRoate, 1f);
        SGNode middleFingerNode = middleFinger.buildSceneGraph("Middle Finger",  middlePos, fingerRoate, 1.25f);
        SGNode ringFingerNode = ringFinger.buildSceneGraph("models.Ring Finger", ringPos, fingerRoate,1f);
        SGNode pinkyFingerNode = pinkyFinger.buildSceneGraph("Pinky Finger", pinkyPos, fingerRoate,0.75f);
        SGNode thumbNode = thumb.buildSceneGraph("Thumb", thumbPos, thumbRotate, 0.75f);

        ringFinger.addRing(ring);

        robot.addChild(robotArmTranslate);
        robotArmTranslate.addChild(arm);
        arm.addChild(armTransform);
        armTransform.addChild(armShape);
        arm.addChild(handTranslate);
        handTranslate.addChild(hand);
        hand.addChild(handTransform);
        handTransform.addChild(handShape);
        hand.addChild(thumbNode);
        hand.addChild(indexFingerNode);
        hand.addChild(middleFingerNode);
        hand.addChild(ringFingerNode);
        hand.addChild(pinkyFingerNode);

        robot.update();
    }

    public void render(GL3 gl) {
        robot.draw(gl);
    }


    public void updatePerspectiveMatrices(Mat4 perspective) {
        handCube.setPerspective(perspective);
        armCube.setPerspective(perspective);

        indexFinger.updatePerspectiveMatrices(perspective);
        middleFinger.updatePerspectiveMatrices(perspective);
        ringFinger.updatePerspectiveMatrices(perspective);
        pinkyFinger.updatePerspectiveMatrices(perspective);
        thumb.updatePerspectiveMatrices(perspective);

        ring.updatePerspectiveMatrices(perspective);
    }

    public void update(double delta) {
        indexFingerAnim.update(delta);
        pinkyFingerAnim.update(delta);
        middleFingerAnim.update(delta);
        ringFingerAnim.update(delta);
    }

    public void neutralPosition() {
        indexFinger.reset();
        middleFinger.reset();
        ringFinger.reset();
        pinkyFinger.reset();
        thumb.reset();

        indexFingerAnim.reset();
        middleFingerAnim.reset();
        ringFingerAnim.reset();
        pinkyFingerAnim.reset();
    }

    // TODO: Maybe a different letter
    public void positionD() {
        this.neutralPosition();

        thumb.curl(60);

        // Need a way to be able to curl and transform a finger without one resetting the other
        middleFinger.curl(90);
        ringFinger.curl(90);
        pinkyFinger.curl(90);

        Mat4 m = Mat4Transform.rotateAroundZ(-10);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(55));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.05f, -0.30f));
        thumb.transformFinger(m);

        // Need to move all fingers towards thumb slightly
        m = Mat4Transform.rotateAroundX(40);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(15));

        middleFinger.transformFinger(m);
        ringFinger.transformFinger(m);
        pinkyFinger.transformFinger(m);
    }

    public void positionA() {
        this.neutralPosition();

        // thumb rotated upwards
        //Mat4 m = Mat4Transform.rotateAroundZ(90);
       // m = Mat4.multiply(m, Mat4Transform.translate(thumb.getFingerWidth() / 2, 0, 0));

        //thumb.transformFinger(m);

        //thumb.rotateZ(90);

        // Index, Middle, models.Ring & pinky folded
        indexFinger.curl(90);
        middleFinger.curl(90);
        ringFinger.curl(90);
        pinkyFinger.curl(90);
    }

    public void positionY() {
        this.neutralPosition();

        // Pinky Finger rotated away & thumb towards other fingers
        Mat4 m = Mat4Transform.rotateAroundZ(20);

        pinkyFinger.transformFinger(m);
        thumb.transformFinger(m);

        // models.Ring, Middle & index folded
        indexFinger.curl(90);
        middleFinger.curl(90);
        ringFinger.curl(90);
    }


    public void disposeMeshes(GL3 gl) {
        handCube.dispose(gl);
        armCube.dispose(gl);

        indexFinger.disposeMeshes(gl);
        middleFinger.disposeMeshes(gl);
        ringFinger.disposeMeshes(gl);
        pinkyFinger.disposeMeshes(gl);
        thumb.disposeMeshes(gl);

        ring.disposeMeshes(gl);
    }

}
