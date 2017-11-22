import models.Camera;
import models.Cube;
import lights.Light;
import models.Mesh;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class RobotHand {

    private Mesh cube;
    private SGNode robot;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger, thumb;

    public RobotHand(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        cube = new Cube(gl, textureId3, textureId4);
        cube.setLights(lights);
        cube.setCamera(camera);

        indexFinger = new RobotFinger(gl, lights, camera);
        middleFinger = new RobotFinger(gl, lights, camera);
        ringFinger = new RobotFinger(gl, lights, camera);
        pinkyFinger = new RobotFinger(gl, lights, camera);
        thumb = new RobotFinger(gl, lights, camera);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        float armHeight = 3f;
        float armScale = 1.25f;

        float handHeight = 1.5f;
        float handWidth = 3f;
        float handScale = 1f;

        robot = new NameNode("robot arm");

        TransformNode robotArmTranslate = new TransformNode("robot translate", Mat4Transform.translate(0,0,0));

        NameNode arm = new NameNode("arm");
        Mat4 m = Mat4Transform.scale(armScale, armHeight,armScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode armTransform = new TransformNode("arm rotate", m);
        MeshNode armShape = new MeshNode("models.Cube(arm)", cube);

        NameNode hand = new NameNode("hand");
        m = Mat4Transform.translate(0, armHeight, 0);
        TransformNode handTranslate = new TransformNode("hand translate", m);

        m = Mat4Transform.scale(handWidth, handHeight, handScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode handTransform = new TransformNode("hand transform", m);
        MeshNode handShape = new MeshNode("models.Cube(hand)", cube);

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
        SGNode ringFingerNode = ringFinger.buildSceneGraph("Ring Finger", ringPos, fingerRoate,1f);
        SGNode pinkyFingerNode = pinkyFinger.buildSceneGraph("Pinky Finger", pinkyPos, fingerRoate,0.75f);
        SGNode thumbNode = thumb.buildSceneGraph("Thumb", thumbPos, thumbRotate, 0.75f);

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
        cube.setPerspective(perspective);

        indexFinger.updatePerspectiveMatrices(perspective);
        middleFinger.updatePerspectiveMatrices(perspective);
        ringFinger.updatePerspectiveMatrices(perspective);
        pinkyFinger.updatePerspectiveMatrices(perspective);
        thumb.updatePerspectiveMatrices(perspective);
    }

    public void update(double delta) {

    }

    public void neutralPosition() {
        indexFinger.reset();
        middleFinger.reset();
        ringFinger.reset();
        pinkyFinger.reset();
        thumb.reset();

        robot.update(); // Should this be here? or should it be in robotfinger?
    }

    public void positionD() {
        thumb.curled(70);

        indexFinger.reset();

        // Need a way to be able to curl and transform a finger without one resetting the other
        middleFinger.curled(90);
        ringFinger.curled(90);
        pinkyFinger.curled(90);

        Mat4 m = Mat4Transform.rotateAroundZ(-10);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(28));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.05f, 0.20f));
        thumb.transformFinger(m);

        // Need to move all fingers towards thumb slightly
        m = Mat4Transform.rotateAroundX(30);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(15));

        middleFinger.transformFinger(m);
        ringFinger.transformFinger(m);
        pinkyFinger.transformFinger(m);

        robot.update();
    }

    public void positionA() {
        this.neutralPosition();

        // thumb rotated upwards
        Mat4 m = Mat4Transform.rotateAroundZ(90);
        m = Mat4.multiply(m, Mat4Transform.translate(0.75f / 2, 0, 0));

        thumb.transformFinger(m);

        // Index, Middle, Ring & pinky folded
        indexFinger.curled(90);
        middleFinger.curled(90);
        ringFinger.curled(90);
        pinkyFinger.curled(90);

        robot.update();

    }

    public void positionY() {
        this.neutralPosition();
        
        // Pinky Finger rotated away & thumb towards other fingers
        Mat4 m = Mat4Transform.rotateAroundZ(20);

        pinkyFinger.transformFinger(m);
        thumb.transformFinger(m);

        // Ring, Middle & index folded
        indexFinger.curled(90);
        middleFinger.curled(90);
        ringFinger.curled(90);

        robot.update();
    }


    public void disposeMeshes(GL3 gl) {
        cube.dispose(gl);

        indexFinger.disposeMeshes(gl);
        middleFinger.disposeMeshes(gl);
        ringFinger.disposeMeshes(gl);
        pinkyFinger.disposeMeshes(gl);
        thumb.disposeMeshes(gl);
    }

}
