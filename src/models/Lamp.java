package models;

import com.jogamp.opengl.GL3;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.Light;
import lights.PointLight;
import mesh.Camera;
import mesh.Mesh;
import mesh.Sphere;
import scenegraph.*;

import java.util.ArrayList;

public class Lamp extends Model {

    private Mesh sphere;
    private SGNode lamp;

    private TransformNode lampTranslate;
    private PointLight light;

    public Lamp(GL3 gl, ArrayList<Light> lights, Camera camera) {
        Vec3 lightColor = new Vec3(1f, 1f, 1f);
        light = new PointLight(gl, lightColor, 1.0f, 0.09f,0.032f);
        light.setCamera(camera);
        lights.add(light); // Shallow copying, might this cause problems?

        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        lamp = new NameNode("lamp");

        lampTranslate = new TransformNode("lamp translate", Mat4Transform.translate(0, 0,0));

        NameNode base = new NameNode("base");
        Mat4 m = Mat4Transform.scale(1f, 0.2f,1f);
        m = Mat4.multiply(Mat4Transform.translate(0,0, 0), m);
        TransformNode baseTransform = new TransformNode("base transform", m);
        MeshNode baseShape = new MeshNode("mesh.Sphere(base)", sphere);

        NameNode tube = new NameNode("tube");
        m = Mat4Transform.scale(0.5f, 5f,0.5f);
        m = Mat4.multiply(Mat4Transform.translate(0, 2.5f,0), m);
        TransformNode tubeTransform = new TransformNode("tube transform", m);
        MeshNode tubeShape = new MeshNode("mesh.Sphere(tube)", sphere);

        NameNode lightNameNode = new NameNode("light");
        m = Mat4Transform.scale(0.5f, 0.5f, 0.5f);
        m = Mat4.multiply(Mat4Transform.translate(0, 5.25f, 0f), m);
        TransformNode lightTransform = new TransformNode("light transform", m);
        LightNode lightShape = new LightNode("light.PointLight(lamp)", light);

        lamp.addChild(lampTranslate);
            lampTranslate.addChild(base);
                base.addChild(baseTransform);
                    baseTransform.addChild(baseShape);
                base.addChild(tube);
                    tube.addChild(tubeTransform);
                        tubeTransform.addChild(tubeShape);
                    tube.addChild(lightNameNode);
                        lightNameNode.addChild(lightTransform);
                            lightTransform.addChild(lightShape);

        lamp.update();
    }

    public void setPosition(Vec3 position) {
        lampTranslate.setTransform(Mat4Transform.translate(position));
        lamp.update();
    }

    public void render(GL3 gl) {
        lamp.draw(gl);
    }

    public PointLight getLight() {
        return light;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        light.setPerspective(perspective);
        sphere.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        light.dispose(gl);
        sphere.dispose(gl);
    }

}
