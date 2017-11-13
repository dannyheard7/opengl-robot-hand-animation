import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class RobotHand {

    private Mesh cube;
    private SGNode robot;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger, thumb;

    public RobotHand() {
    }

    public SGNode buildSceneGraph(GL3 gl, Light light, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        cube = new Cube(gl, textureId3, textureId4);
        cube.setLight(light);
        cube.setCamera(camera);

        float armHeight = 3f;
        float armScale = 1.25f;

        float handHeight = 1.5f;
        float handWidth = 3f;
        float handScale = 1f;

        robot = new NameNode("root");

        TransformNode robotMoveTranslate = new TransformNode("robot translate", Mat4Transform.translate(0,0,0));

        NameNode arm = new NameNode("arm");
        Mat4 m = Mat4Transform.scale(armScale, armHeight,armScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode armTransform = new TransformNode("arm rotate", m);
        MeshNode armShape = new MeshNode("Cube(arm)", cube);


        NameNode hand = new NameNode("hand");
        m = Mat4Transform.translate(0, armHeight, 0);
        TransformNode handTranslate = new TransformNode("hand translate", m);
        m = Mat4Transform.scale(handWidth, handHeight, handScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode handTransform = new TransformNode("hand rotate", m);
        MeshNode handShape = new MeshNode("Cube(hand)", cube);

        float dist = 2.5f  / 3;

        indexFinger = new RobotFinger(gl, light, camera);
        middleFinger = new RobotFinger(gl, light, camera);
        ringFinger = new RobotFinger(gl, light, camera);
        pinkyFinger = new RobotFinger(gl, light, camera);
        thumb = new RobotFinger(gl, light, camera);

        Vec3 indexPos = new Vec3(-1.25f, handHeight, 0f);
        Vec3 middlePos = new Vec3((-1.25f+dist), handHeight, 0f);
        Vec3 ringPos = new Vec3((1.25f-dist), handHeight, 0f);
        Vec3 pinkyPos = new Vec3(1.25f, handHeight, 0f);
        Vec3 thumbPos = new Vec3(-1.5f, handHeight / 2, 0f);

        SGNode indexFingerNode = indexFinger.buildSceneGraph("Index Finger", indexPos, 1f);
        SGNode middleFingerNode = middleFinger.buildSceneGraph("Middle Finger",  middlePos, 1.25f);
        SGNode ringFingerNode = ringFinger.buildSceneGraph("Ring Finger", ringPos, 1f);
        SGNode pinkyFingerNode = pinkyFinger.buildSceneGraph("Pinky Finger", pinkyPos, 0.75f);
        SGNode thumbNode = thumb.buildSceneGraph("Thumb Finger", thumbPos, 0.75f);

        thumb.applyTransform(Mat4Transform.rotateAroundZ(90));

        robot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(arm);
                arm.addChild(armTransform);
                    armTransform.addChild(armShape);
                arm.addChild(handTranslate);
                    handTranslate.addChild(hand);
                        hand.addChild(handTransform);
                            handTransform.addChild(handShape);
                        hand.addChild(indexFingerNode);
                        hand.addChild(middleFingerNode);
                        hand.addChild(ringFingerNode);
                        hand.addChild(pinkyFingerNode);
                        hand.addChild(thumbNode);

        robot.update();

        return robot;
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


    public void disposeMeshes(GL3 gl) {
        cube.dispose(gl);

        indexFinger.disposeMeshes(gl);
        middleFinger.disposeMeshes(gl);
        ringFinger.disposeMeshes(gl);
        pinkyFinger.disposeMeshes(gl);
        thumb.disposeMeshes(gl);
    }

}
