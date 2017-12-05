/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package models;

import com.jogamp.opengl.GL3;
import core.Camera;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.Light;
import lights.SpotLight;
import mesh.Mesh;
import mesh.Sphere;
import scenegraph.*;

import java.util.ArrayList;

public class Ring extends Model {

    private Mesh sphere;
    private SGNode ring;

    private SpotLight light;

    private TransformNode ringTranslate;

    public Ring(GL3 gl, ArrayList<Light> lights, Camera camera) {
        Vec3 lightDirection = new Vec3(0, 0, -1);
        float cutOff = (float)Math.cos(Math.toRadians(5.5f));
        float outerCutOff = (float)Math.cos(Math.toRadians(8.5f));

        Vec3 lightColor = new Vec3(0.94f, 0.25f, 0.22f);
        light = new SpotLight(gl, lightColor,1.0f, 0.09f,0.032f, lightDirection, cutOff, outerCutOff);
        light.setCamera(camera);
        lights.add(light);

        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/gold.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/gold_specular.jpg");

        sphere = new Sphere(gl, textureId3, textureId4);
        sphere.setLights(lights);
        sphere.setCamera(camera);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        ring = new NameNode("ring");

        ringTranslate = new TransformNode("ring translate", Mat4Transform.translate(0,0.5f,0));

        NameNode band = new NameNode("band");
        Mat4 m = Mat4Transform.scale(1f, 0.2f, 1.2f);
        m = Mat4.multiply(Mat4Transform.translate(0,0f,0), m);
        TransformNode bandTransform = new TransformNode("band rotate", m);
        MeshNode bandShape = new MeshNode("mesh.Sphere(band)", sphere);

        NameNode lightNameNode = new NameNode("light");
        m =  Mat4Transform.scale(0.2f, 0.2f, 0.2f);
        m = Mat4.multiply(Mat4Transform.translate(0, 0, -0.6f), m);
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

    public SpotLight getLight() {
        return light;
    }

    public void setPosition(Vec3 position) {
        ringTranslate.setTransform(Mat4Transform.translate(position));
    }

    @Override
    public SGNode getSceneGraphRootNode() {
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
