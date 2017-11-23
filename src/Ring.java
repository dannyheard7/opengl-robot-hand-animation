import gmaths.Vec3;
import lights.SpotLight;
import models.Camera;
import lights.Light;
import models.Mesh;
import models.Sphere;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import scenegraph.*;

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
        ring = new NameNode("ring");

        TransformNode ringTranslate = new TransformNode("ring translate", Mat4Transform.translate(0,0.5f,0));

        NameNode band = new NameNode("band");
        Mat4 m = Mat4Transform.scale(1f, 0.2f, 1.2f);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode bandTransform = new TransformNode("band rotate", m);
        MeshNode bandShape = new MeshNode("models.Sphere(band)", sphere);

        NameNode lightNameNode = new NameNode("light");
        m = Mat4Transform.translate(0, 0.1f, -0.6f);
        m = Mat4.multiply(m, Mat4Transform.scale(0.2f, 0.2f, 0.2f));
        TransformNode lightTransform = new TransformNode("light transform", m);
        LightNode lightShape = new LightNode("light.SpotLight(lamp)", light);

        ring.addChild(ringTranslate);
            ringTranslate.addChild(band);
                band.addChild(bandTransform);
                    bandTransform.addChild(bandShape);
                band.addChild(lightNameNode);
                    lightNameNode.addChild(lightTransform);
                        lightTransform.addChild(lightShape);

        ring.update();
    }

    public SGNode getSceneGraph() {
        return ring;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        sphere.setPerspective(perspective);
        light.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        sphere.dispose(gl);
        light.dispose(gl);
    }
}
