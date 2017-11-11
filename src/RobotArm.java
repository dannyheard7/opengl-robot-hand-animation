import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;

public class RobotArm {

    private Mesh sphere, cube, cube2;
    private SGNode arm;
    private TransformNode armRotate;

    public RobotArm() {
    }

    public SGNode buildSceneGraph(GL3 gl, Light light, Camera camera) {
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");

        sphere = new Sphere(gl, textureId1, textureId2);
        cube = new Cube(gl, textureId3, textureId4);
        cube2 = new Cube(gl, textureId5, textureId6);

        sphere.setLight(light);
        sphere.setCamera(camera);
        cube.setLight(light);
        cube.setCamera(camera);
        cube2.setLight(light);
        cube2.setCamera(camera);

        float armLength = 2f;
        float armScale = 2f;

        arm = new NameNode("root");

        TransformNode armTranslate = new TransformNode("arm translate",
                Mat4Transform.translate((armLength*0.5f)+(armScale*0.5f),1.0f,0));
        armRotate = new TransformNode("arm rotate",Mat4Transform.rotateAroundX(180));
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(armScale, armLength,armScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode armScaleTransform = new TransformNode("arm scale", m);
        MeshNode armShape = new MeshNode("Cube(arm)", cube2);


        arm.addChild(armTranslate);
        armTranslate.addChild(armRotate);
        armRotate.addChild(armScaleTransform);
        armScaleTransform.addChild(armShape);

        arm.update();

        return arm;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
        cube.setPerspective(perspective);
        cube2.setPerspective(perspective);
    }

    public void update(double delta) {
        this.updateLeftArm(delta);
    }

    private void updateLeftArm(double delta) {
        float rotateAngle = 180f+90f*(float)Math.sin(delta);
        armRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
        armRotate.update();
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
        cube.dispose(gl);
        cube2.dispose(gl);
    }

    public void loweredArms() {
        armRotate.setTransform(Mat4Transform.rotateAroundX(180));
        armRotate.update();
    }

    public void raisedArms() {
        armRotate.setTransform(Mat4Transform.rotateAroundX(0));
        armRotate.update();
    }
}
