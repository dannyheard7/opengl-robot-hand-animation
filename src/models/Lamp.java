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
import lights.PointLight;
import mesh.Cube;
import scenegraph.*;

import java.util.ArrayList;

public class Lamp extends Model {

    private Cube cube;
    private SGNode lamp;

    private TransformNode lampTranslate;
    private PointLight light;

    public Lamp(GL3 gl, ArrayList<Light> lights, Camera camera) {
        Vec3 lightColor = new Vec3(1f, 1f, 1f);
        light = new PointLight(gl, lightColor, 1.0f, 0.09f,0.032f);
        light.setCamera(camera);
        lights.add(light);

        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/dark-marble.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/dark-marble_specular.jpg");

        cube = new Cube(gl, textureId3, textureId4);
        cube.setLights(lights);
        cube.setCamera(camera);

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        lamp = new NameNode("lamp");

        lampTranslate = new TransformNode("lamp translate", Mat4Transform.translate(0, 0,0));

        NameNode base = new NameNode("base");
        Mat4 m = Mat4Transform.scale(1f, 0.2f,1f);
        m = Mat4.multiply(Mat4Transform.translate(0,0, 0), m);
        TransformNode baseTransform = new TransformNode("base transform", m);
        MeshNode baseShape = new MeshNode("mesh.Sphere(base)", cube);

        NameNode tube = new NameNode("tube");
        m = Mat4Transform.scale(0.5f, 5f,0.5f);
        m = Mat4.multiply(Mat4Transform.translate(0, 2.5f,0), m);
        TransformNode tubeTransform = new TransformNode("tube transform", m);
        MeshNode tubeShape = new MeshNode("mesh.Sphere(tube)", cube);

        NameNode lightNameNode = new NameNode("light");
        m = Mat4Transform.scale(0.5f, 0.5f, 0.5f);
        m = Mat4.multiply(Mat4Transform.translate(0, 5.25f, 0f), m);
        TransformNode lightTransform = new TransformNode("light transform", m);
        LightNode lightShape = new LightNode("light.PointLight(lamp)", light); // Lights are part of scene graph

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

    public PointLight getLight() {
        return light;
    }

    @Override
    public SGNode getSceneGraphRootNode() {
        return lamp;
    }

    @Override
    public void updatePerspectiveMatrices(Mat4 perspective) {
        light.setPerspective(perspective);
        cube.setPerspective(perspective);
    }

    @Override
    public void disposeMeshes(GL3 gl) {
        light.dispose(gl);
        cube.dispose(gl);
    }

}
