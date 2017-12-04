package models;

import com.jogamp.opengl.GL3;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.Light;
import mesh.Camera;
import mesh.TwoTriangles;
import scenegraph.*;

import java.util.ArrayList;

public class PictureFrame extends Model {
    private TwoTriangles frameMesh, pictureMesh;
    private SGNode picture;
    private TransformNode pictureTranslate;

    public PictureFrame(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/picture-frame.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");

        frameMesh = new TwoTriangles(gl, textureId0);
        frameMesh.setLights(lights);
        frameMesh.setCamera(camera);

        pictureMesh = new TwoTriangles(gl, textureId1);
        pictureMesh.setLights(lights);
        pictureMesh.setCamera(camera);

        setupSceneGraph();
    }

    private void setupSceneGraph() {
        picture = new NameNode("picture");

        pictureTranslate = new TransformNode("picture translate", new Mat4(1));

        NameNode pictureFrame = new NameNode("picture frame");
        Mat4 m = Mat4Transform.scale(4f, 1f, 5f);
        TransformNode pictureFrameTransform = new TransformNode("picture frame transform", m);
        MeshNode pictureFrameShape = new MeshNode("mesh.TwoTriangles(backWallPictureFrame)", frameMesh);

        NameNode canvas = new NameNode("canvas");
        m = Mat4Transform.scale(3.5f, 1f, 4.25f);
        m = Mat4.multiply(Mat4Transform.translate(0,-0.1f,0), m);
        TransformNode canvasTransform = new TransformNode("canvas transform", m);
        MeshNode canvasShape = new MeshNode("mesh.TwoTriangles(canvas)", pictureMesh);

        picture.addChild(pictureTranslate);
            pictureTranslate.addChild(pictureFrame);
                pictureFrame.addChild(canvas);
                    canvas.addChild(canvasTransform);
                        canvasTransform.addChild(canvasShape);
                pictureFrame.addChild(pictureFrameTransform);
                    pictureFrameTransform.addChild(pictureFrameShape);

        picture.update();
    }

    public void setImage(GL3 gl, String imagePath) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, imagePath);

        pictureMesh.setTextureId(textureId0);
    }

    public void setPosition(Vec3 position) {
        Mat4 m = Mat4Transform.translate(position);
        pictureTranslate.setTransform(m);

        picture.update();
    }

    @Override
    public void updatePerspectiveMatrices(Mat4 perspective) {
        frameMesh.setPerspective(perspective);
        pictureMesh.setPerspective(perspective);
    }

    @Override
    public void disposeMeshes(GL3 gl) {
        frameMesh.dispose(gl);
        pictureMesh.dispose(gl);
    }

    public SGNode getSceneGraphRoot() {
        return picture;
    }
}
