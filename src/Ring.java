import gmaths.Vec3;
import lights.SpotLight;
import models.Camera;
import lights.Light;
import models.Mesh;
import models.Sphere;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class Ring {

    private Mesh sphere;
    private SGNode ring;

    private SpotLight light;

    public Ring(GL3 gl, ArrayList<Light> lights, Camera camera) {
        Vec3 lightDirection = new Vec3(0, 0, 0);

        float cutOff = (float)Math.cos(Math.toRadians(12.5f));
        float outerCutOff = (float)Math.cos(Math.toRadians(17.5f));
        light = new SpotLight(gl, 1.0f, 0.14f,0.07f, lightDirection, cutOff, outerCutOff);
        light.setCamera(camera);
        lights.add(light); // Shallow copying, might this cause problems?

        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        ring = new NameNode("robot arm");

        TransformNode ringTranslate = new TransformNode("ring translate", Mat4Transform.translate(0,0,0));

        NameNode band = new NameNode("band");
        Mat4 m = Mat4Transform.scale(1f, 0.2f, 1f);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode bandTransform = new TransformNode("band rotate", m);
        MeshNode bandShape = new MeshNode("models.Sphere(band)", sphere);

        ring.addChild(ringTranslate);
            ringTranslate.addChild(band);
                band.addChild(bandTransform);
                    bandTransform.addChild(bandShape);

        ring.update();
    }

    public void render(GL3 gl) {
        light.render(gl);

        ring.draw(gl);
    }


    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
    }
}
