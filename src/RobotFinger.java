import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class RobotFinger {

    private Mesh sphere;
    private TransformNode fingerTransform, fingerMiddleTranslate, fingerTopTranslate;

    float sectionHeight, fingerWidth, fingerDepth;


    public RobotFinger(GL3 gl, Light light, Camera camera) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLight(light);
        sphere.setCamera(camera);
    }

    public float getSectionHeight() {
        return this.sectionHeight;
    }


    // TODO: Stop passing gl, camera and light around so much?
    // TODO: Naming needs cleaning here
    public SGNode buildSceneGraph(String name, Vec3 pos, float sectionHeight) {
        NameNode finger = new NameNode(name);

        this.fingerWidth = 0.75f;
        this.fingerDepth = 1f;
        this.sectionHeight = sectionHeight;

        Mat4 m = Mat4Transform.translate(pos);
        fingerTransform = new TransformNode("finger translate", m);

        NameNode fingerBottom = new NameNode("finger bottom");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerBottomTransform = new TransformNode("finger bottom rotate", m);
        MeshNode fingerBottomShape = new MeshNode("Cube(finger bottom)", sphere);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerMiddleTranslate = new TransformNode("finger middle translate", m);
        NameNode fingerMiddle = new NameNode("finger middle");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerMiddleTransform = new TransformNode("finger middle rotate", m);
        MeshNode fingerMiddleShape = new MeshNode("Cube(finger middle)", sphere);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerTopTranslate = new TransformNode("finger top translate", m);
        NameNode fingerTop = new NameNode("finger top");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode fingerTopTransform = new TransformNode("finger top rotate", m);
        MeshNode fingerTopShape = new MeshNode("Cube(finger top)", sphere);

        finger.addChild(fingerTransform);
            fingerTransform.addChild(fingerBottom);
                fingerBottom.addChild(fingerBottomTransform);
                    fingerBottomTransform.addChild(fingerBottomShape);
            fingerTransform.addChild(fingerMiddleTranslate);
                fingerMiddleTranslate.addChild(fingerMiddle);
                    fingerMiddle.addChild(fingerMiddleTransform);
                        fingerMiddleTransform.addChild(fingerMiddleShape);
                fingerMiddleTranslate.addChild(fingerTopTranslate);
                    fingerTopTranslate.addChild(fingerTop);
                        fingerTop.addChild(fingerTopTransform);
                            fingerTopTransform.addChild(fingerTopShape);


        return finger;
    }

    public void translateFinger(Mat4 m) {
        fingerTransform.updateTransform(m);
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

        fingerTransform.updateTransform(m);

        m = Mat4.multiply(m, Mat4Transform.translate(0, -diff, 0.25f));

        fingerMiddleTranslate.updateTransform(m);

        m = Mat4.multiply(m, Mat4Transform.translate(0, -0.25f, 0));

        fingerTopTranslate.updateTransform(m);
    }


    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
    }
}
