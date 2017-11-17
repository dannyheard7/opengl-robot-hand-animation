import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class RobotHand {

    private Mesh cube;
    private SGNode robot;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger, thumb;

    public RobotHand(GL3 gl, Light light, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        cube = new Cube(gl, textureId3, textureId4);
        cube.setLight(light);
        cube.setCamera(camera);

        indexFinger = new RobotFinger(gl, light, camera);
        middleFinger = new RobotFinger(gl, light, camera);
        ringFinger = new RobotFinger(gl, light, camera);
        pinkyFinger = new RobotFinger(gl, light, camera);
        thumb = new RobotFinger(gl, light, camera);
    }

    public SGNode getSceneGraph() {
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

        Vec3 indexPos = new Vec3(1.25f, handHeight, 0f);
        Vec3 middlePos = new Vec3((1.25f-dist), handHeight, 0f);
        Vec3 ringPos = new Vec3((-1.25f+dist), handHeight, 0f);
        Vec3 pinkyPos = new Vec3(-1.25f, handHeight, 0f);
        Vec3 thumbPos = new Vec3(1.5f, handHeight / 2, 0f);

        SGNode indexFingerNode = indexFinger.buildSceneGraph("Index Finger", indexPos, 1f);
        SGNode middleFingerNode = middleFinger.buildSceneGraph("Middle Finger",  middlePos, 1.25f);
        SGNode ringFingerNode = ringFinger.buildSceneGraph("Ring Finger", ringPos, 1f);
        SGNode pinkyFingerNode = pinkyFinger.buildSceneGraph("Pinky Finger", pinkyPos, 0.75f);
        SGNode thumbNode = thumb.buildSceneGraph("Thumb Finger", thumbPos, 0.75f);

        thumb.translateFinger(Mat4Transform.rotateAroundZ(-90));

        robot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(arm);
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

    public void neutralPosition() {

    }

    // TODO: these positions are relative so need to reset before each
    // i.e Get finger positions, apply these transformations, instead of updating just set transformations to each piece
    public void positionD() {
        thumb.curled(50);

        middleFinger.curled(90);
        ringFinger.curled(90);
        pinkyFinger.curled(90);

        Mat4 m = Mat4Transform.rotateAroundZ(5);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(18));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.05f, 0.20f));
        thumb.translateFinger(m);

        // Need to move all fingers towards thumb slightly
        m = Mat4Transform.rotateAroundX(-40);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(15));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.5f, 0));

        middleFinger.translateFinger(m);
        ringFinger.translateFinger(m);
        pinkyFinger.translateFinger(m);

        robot.update();
    }

    public void positionA() {
        // thumb rotated upwards
        Mat4 m = Mat4Transform.rotateAroundZ(90);
        m = Mat4.multiply(m, Mat4Transform.translate(0.25f, 0, 0));

        thumb.translateFinger(m);

        // Index, Middle, Ring & pinky folded
        indexFinger.curled(90);
        middleFinger.curled(90);
        ringFinger.curled(90);
        pinkyFinger.curled(90);

        robot.update();

    }

    public void positionY() {
        // Pinky Finger rotated away & thumb towards other fingers
        Mat4 m = Mat4Transform.rotateAroundZ(20);

        pinkyFinger.translateFinger(m);
        thumb.translateFinger(m);

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
