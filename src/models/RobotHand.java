/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */


package models;

import animation.Animation;
import animation.KeyFrame;
import animation.Position;
import com.jogamp.opengl.GL3;
import core.Camera;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.Light;
import mesh.Cube;
import mesh.Mesh;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RobotHand extends Model {

    private Mesh handCube, armCube;
    private SGNode robotArm;
    private TransformNode handTransform;
    private Mat4 handPosition;

    private float handHeight, handWidth;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger, thumb;
    private Ring ring;

    private Animation handAnim;
    private KeyFrame keyFrameNeutral, keyFrameA, keyFrameY, keyFrameH, keyFramePeace;

    public RobotHand(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");

        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/dark-marble.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/dark-marble_specular.jpg");

        handCube = new Cube(gl, textureId0, textureId1);
        handCube.setLights(lights);
        handCube.setCamera(camera);

        armCube = new Cube(gl, textureId2, textureId3);
        armCube.setLights(lights);
        armCube.setCamera(camera);

        this.handHeight = 1.5f;
        this.handWidth = 3f;

        float dist = (handWidth - 0.5f) / 3;
        Vec3 indexPos = new Vec3(1.25f, handHeight, 0f);
        Vec3 middlePos = new Vec3((1.25f-dist), handHeight, 0f);
        Vec3 ringPos = new Vec3((-1.25f+dist), handHeight, 0f);
        Vec3 pinkyPos = new Vec3(-1.25f, handHeight, 0f);
        Vec3 thumbPos = new Vec3(1.5f, handHeight / 2, 0f);

        indexFinger = new RobotFinger(gl, lights, camera, "Index Finger", indexPos, 1f);
        middleFinger = new RobotFinger(gl, lights, camera, "Middle Finger",  middlePos, 1.25f);
        ringFinger = new RobotFinger(gl, lights, camera, "Ring Finger", ringPos,1f);
        pinkyFinger = new RobotFinger(gl, lights, camera, "Pinky Finger", pinkyPos,0.75f);
        thumb = new RobotFinger(gl, lights, camera, "Thumb", thumbPos, 0.75f);

        ring = new Ring(gl, lights, camera);

        this.setupSceneGraph();
        this.setupAnimation();
    }

    private void setupAnimation() {
        Position indexFingerStraight = new Position(0, indexFinger::curl);
        Position middleFingerStraight = new Position(0, middleFinger::curl);
        Position ringFingerStraight = new Position(0, ringFinger::curl);
        Position pinkyFingerStraight = new Position(0, pinkyFinger::curl);
        Position thumbStraight = new Position(0, thumb::curl);

        Position indexFingerZRotation = new Position(0, indexFinger::rotateAroundZ);
        Position middleFingerZRotation = new Position(0, middleFinger::rotateAroundZ);
        Position pinkyFingerZRotation = new Position(0, pinkyFinger::rotateAroundZ);
        Position thumbZRotation = new Position(-90, thumb::rotateAroundZ);

        Position handZRotation = new Position(0, this::rotateAroundZ);

        Map<String, Position> neutralPositions = new LinkedHashMap<>();
        neutralPositions.put("Index Finger", indexFingerStraight);
        neutralPositions.put("Middle Finger", middleFingerStraight);
        neutralPositions.put("Ring Finger", ringFingerStraight);
        neutralPositions.put("Pinky Finger", pinkyFingerStraight);
        neutralPositions.put("Thumb", thumbStraight);
        neutralPositions.put("Index Finger Z Rotation", indexFingerZRotation);
        neutralPositions.put("Middle Finger Z Rotation", middleFingerZRotation);
        neutralPositions.put("Pinky Finger Z Rotation", pinkyFingerZRotation);
        neutralPositions.put("Thumb Z Rotation", thumbZRotation);
        neutralPositions.put("Hand Z Rotation", handZRotation);

        Position indexFingerCurled = new Position(90, indexFinger::curl);
        Position middleFingerCurled = new Position(90, middleFinger::curl);
        Position ringFingerCurled = new Position(90, ringFinger::curl);
        Position pinkyFingerCurled = new Position(90, pinkyFinger::curl);

        Map<String, Position> curledFingers = new LinkedHashMap<>(neutralPositions);
        curledFingers.put("Index Finger", indexFingerCurled);
        curledFingers.put("Middle Finger", middleFingerCurled);
        curledFingers.put("Ring Finger", ringFingerCurled);
        curledFingers.put("Pinky Finger", pinkyFingerCurled);

        // Neutral

        keyFrameNeutral = new KeyFrame(neutralPositions);

        // Letter A

        thumbZRotation = new Position(0, thumb::rotateAroundZ);

        keyFrameA = new KeyFrame(curledFingers);
        keyFrameA.addPosition("Thumb Z Rotation", thumbZRotation);

        //Letter Y

        pinkyFingerZRotation = new Position(20, pinkyFinger::rotateAroundZ);
        thumbZRotation = new Position(-70, thumb::rotateAroundZ);

        keyFrameY = new KeyFrame(curledFingers);
        keyFrameY.addPosition("Pinky Finger", pinkyFingerStraight);
        keyFrameY.addPosition("Pinky Finger Z Rotation", pinkyFingerZRotation);
        keyFrameY.addPosition("Thumb Z Rotation", thumbZRotation);

        // Letter H
        thumbStraight = new Position(90, thumb::curl);
        handZRotation = new Position(70, this::rotateAroundZ);

        keyFrameH = new KeyFrame(curledFingers);
        keyFrameH.addPosition("Index Finger", indexFingerStraight);
        keyFrameH.addPosition("Middle Finger", middleFingerStraight);
        keyFrameH.addPosition("Thumb", thumbStraight);
        keyFrameH.addPosition("Hand Z Rotation", handZRotation);

        // Peace sign

        indexFingerZRotation = new Position(-15, indexFinger::rotateAroundZ);
        middleFingerZRotation = new Position(15, middleFinger::rotateAroundZ);
        thumbStraight = new Position(90, thumb::curl);

        keyFramePeace = new KeyFrame(curledFingers);
        keyFramePeace.addPosition("Index Finger", indexFingerStraight);
        keyFramePeace.addPosition("Middle Finger", middleFingerStraight);
        keyFramePeace.addPosition("Index Finger Z Rotation", indexFingerZRotation);
        keyFramePeace.addPosition("Middle Finger Z Rotation", middleFingerZRotation);
        keyFramePeace.addPosition("Thumb", thumbStraight);

        handAnim = new Animation(0.01f);
        handAnim.addKeyFrame(keyFrameNeutral);
        handAnim.addKeyFrame(keyFrameA);
        handAnim.addKeyFrame(keyFrameNeutral);
        handAnim.addKeyFrame(keyFrameY);
        handAnim.addKeyFrame(keyFrameNeutral);
        handAnim.addKeyFrame(keyFrameH);
        handAnim.addKeyFrame(keyFrameNeutral);
        handAnim.addKeyFrame(keyFramePeace);
        handAnim.addKeyFrame(keyFrameNeutral);
    }

    private void setupSceneGraph() {
        float armHeight = 3f;
        float armScale = 0.5f;

        float handScale = 1f;

        robotArm = new NameNode("robotArm arm");

        TransformNode robotArmTranslate = new TransformNode("arm translate", Mat4Transform.translate(0,0,0));

        NameNode arm = new NameNode("arm");
        Mat4 m = Mat4Transform.scale(armScale, armHeight,armScale);
        m = Mat4.multiply(Mat4Transform.translate(0,armHeight / 2,0), m);
        TransformNode armTransform = new TransformNode("arm transform", m);
        MeshNode armShape = new MeshNode("mesh.Cube(arm)", armCube);

        NameNode hand = new NameNode("hand");
        handPosition = Mat4Transform.translate(0, armHeight, 0);
        handTransform = new TransformNode("hand translate", handPosition);

        m = Mat4Transform.scale(handWidth, handHeight, handScale);
        m = Mat4.multiply(Mat4Transform.translate(0,handHeight / 2,0), m);
        TransformNode handTransform = new TransformNode("hand transform", m);
        MeshNode handShape = new MeshNode("mesh.Cube(hand)", handCube);

        thumb.rotateAroundZ(-90);

        ringFinger.addRing(ring);

        robotArm.addChild(robotArmTranslate);
            robotArmTranslate.addChild(arm);
                arm.addChild(armTransform);
                    armTransform.addChild(armShape);
                arm.addChild(this.handTransform);
                    this.handTransform.addChild(hand);
                        hand.addChild(handTransform);
                            handTransform.addChild(handShape);
                        hand.addChild(thumb.getSceneGraphRootNode());
                        hand.addChild(indexFinger.getSceneGraphRootNode());
                        hand.addChild(middleFinger.getSceneGraphRootNode());
                        hand.addChild(ringFinger.getSceneGraphRootNode());
                        hand.addChild(pinkyFinger.getSceneGraphRootNode());

        robotArm.update();
    }

    @Override
    public SGNode getSceneGraphRootNode() {
        return robotArm;
    }

    @Override
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
        handAnim.update(delta);
    }

    public void neutralPosition() {
        keyFrameNeutral.display();
        handAnim.reset();
    }

    public void positionA() {
        keyFrameA.display();
        handAnim.skipToKeyFrame(keyFrameA);
    }

    public void positionY() {
        keyFrameY.display();
        handAnim.skipToKeyFrame(keyFrameY);
    }

    public void positionH() {
        keyFrameH.display();
        handAnim.skipToKeyFrame(keyFrameH);
    }

    public void peaceGesture() {
        keyFramePeace.display();
        handAnim.skipToKeyFrame(keyFramePeace);
    }

    public Ring getRing() {
        return ring;
    }

    public void rotateAroundZ(float angle) {
        Mat4 m = Mat4.multiply(handPosition, Mat4Transform.rotateAroundZ(angle));
        handTransform.setTransform(m);

        robotArm.update();
    }

    @Override
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
