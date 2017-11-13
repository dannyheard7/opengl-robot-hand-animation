import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;

public class RobotFinger {

    private Mesh cube;
    private TransformNode fingerMiddleTranslate, fingerTopTranslate, fingerBottomTransform, fingerMiddleTransform, fingerTopTransform;


    // TODO: Stop passing gl, camera and light around so much?
    // TODO: xTranslate needs to be a matrix so we can do the thumb this way too
    public SGNode buildSceneGraph(GL3 gl, Light light, Camera camera, String name, float handHeight, float xTranslate, float sectionHeight) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        cube = new Cube(gl, textureId3, textureId4);
        cube.setLight(light);
        cube.setCamera(camera);

        NameNode finger = new NameNode(name);

        float fingerWidth = 0.25f;
        float fingerScale = 1f;

        Mat4 m = Mat4Transform.translate(xTranslate,  handHeight, 0);
        TransformNode fingerTranslate = new TransformNode("finger translate", m);

        NameNode fingerBottom = new NameNode("finger bottom");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        fingerBottomTransform = new TransformNode("finger bottom transform", m);
        MeshNode fingerBottomShape = new MeshNode("Cube(finger bottom)", cube);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerMiddleTranslate = new TransformNode("finger middle translate", m);
        NameNode fingerMiddle = new NameNode("finger middle");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        fingerMiddleTransform = new TransformNode("finger middle transform", m);
        MeshNode fingerMiddleShape = new MeshNode("Cube(finger middle)", cube);

        m = Mat4Transform.translate(0,  sectionHeight, 0);
        fingerTopTranslate = new TransformNode("finger top translate", m);
        NameNode fingerTop = new NameNode("finger top");
        m = Mat4Transform.scale(fingerWidth, sectionHeight, fingerScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        fingerTopTransform = new TransformNode("finger top transform", m);
        MeshNode fingerTopShape = new MeshNode("Cube(finger top)", cube);

        finger.addChild(fingerTranslate);
            fingerTranslate.addChild(fingerBottom);
                fingerBottom.addChild(fingerBottomTransform);
                    fingerBottomTransform.addChild(fingerBottomShape);
            fingerTranslate.addChild(fingerMiddleTranslate);
                fingerMiddleTranslate.addChild(fingerMiddle);
                    fingerMiddle.addChild(fingerMiddleTransform);
                        fingerMiddleTransform.addChild(fingerMiddleShape);
                fingerMiddleTranslate.addChild(fingerTopTranslate);
                    fingerTopTranslate.addChild(fingerTop);
                        fingerTop.addChild(fingerTopTransform);
                            fingerTopTransform.addChild(fingerTopShape);


        return finger;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        cube.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        cube.dispose(gl);
    }
}
