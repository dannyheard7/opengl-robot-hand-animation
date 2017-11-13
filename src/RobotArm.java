import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;

public class RobotArm {

    private Mesh cube;
    private SGNode robot;

    private RobotFinger indexFinger, middleFinger, ringFinger, pinkyFinger;

    public RobotArm() {
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
        TransformNode armTransform = new TransformNode("arm transform", m);
        MeshNode armShape = new MeshNode("Cube(arm)", cube);


        NameNode hand = new NameNode("hand");
        m = Mat4Transform.translate(0, armHeight, 0);
        TransformNode handTranslate = new TransformNode("hand translate", m);
        m = Mat4Transform.scale(handWidth, handHeight, handScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode handTransform = new TransformNode("hand transform", m);
        MeshNode handShape = new MeshNode("Cube(hand)", cube);

        float dist = 2.5f  / 3;


        indexFinger = new RobotFinger();
        middleFinger = new RobotFinger();
        ringFinger = new RobotFinger();
        pinkyFinger = new RobotFinger();

        SGNode indexFingerNode = indexFinger.buildSceneGraph(gl, light, camera,"Index Finger", handHeight, -1.25f, 1f);
        SGNode middleFingerNode = middleFinger.buildSceneGraph(gl, light, camera,"Middle Finger", handHeight, (-1.25f+dist), 1.25f);
        SGNode ringFingerNode = ringFinger.buildSceneGraph(gl, light, camera,"Ring Finger", handHeight, (1.25f-dist), 1f);
        SGNode pinkyFingerNode = pinkyFinger.buildSceneGraph(gl, light, camera,"Pinky Finger", handHeight, 1.25f, 0.75f);


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

        robot.update();

        return robot;
    }



    public void updatePerspectiveMatrices(Mat4 perspective) {
        cube.setPerspective(perspective);

        indexFinger.updatePerspectiveMatrices(perspective);
        middleFinger.updatePerspectiveMatrices(perspective);
        ringFinger.updatePerspectiveMatrices(perspective);
        pinkyFinger.updatePerspectiveMatrices(perspective);
    }

    public void update(double delta) {

    }


    public void disposeMeshes(GL3 gl) {
        cube.dispose(gl);

        indexFinger.disposeMeshes(gl);
        middleFinger.disposeMeshes(gl);
        ringFinger.disposeMeshes(gl);
        pinkyFinger.disposeMeshes(gl);
    }

}
