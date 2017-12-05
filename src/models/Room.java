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
import mesh.TwoTriangles;
import mesh.TwoTrianglesNoLighting;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

public class Room extends Model {

    private TwoTriangles floorMesh, wallMesh, windowWallMesh, ceilingMesh;
    private TwoTrianglesNoLighting outsideSceneMesh;
    private PictureFrame backWallPictureFrame, rightWallPictureFrame, frontWallPictureFrame;
    private SGNode room;

    public Room(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/marble-floor.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/marble-tiles.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/marble-tiles-window.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/plaster.jpg");

        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/sky.jpg");

        floorMesh = new TwoTriangles(gl, textureId0);
        floorMesh.setLights(lights);
        floorMesh.setCamera(camera);
        floorMesh.setSpecular(new Vec3(0.2f, 0.2f, 0.2f));

        wallMesh = new TwoTriangles(gl, textureId1);
        wallMesh.setLights(lights);
        wallMesh.setCamera(camera);

        windowWallMesh = new TwoTriangles(gl, textureId2);
        windowWallMesh.setLights(lights);
        windowWallMesh.setCamera(camera);

        ceilingMesh = new TwoTriangles(gl, textureId3);
        ceilingMesh.setLights(lights);
        ceilingMesh.setCamera(camera);

        outsideSceneMesh = new TwoTrianglesNoLighting(gl, textureId4);
        outsideSceneMesh.setLights(lights);
        outsideSceneMesh.setCamera(camera);
        outsideSceneMesh.setMovingTexture(true);

        backWallPictureFrame = new PictureFrame(gl, lights, camera);
        backWallPictureFrame.setPosition(new Vec3(0,0.65f,-2f)); // Any closer gives z-buffer issues
        backWallPictureFrame.setImage(gl, "textures/hand.jpg");

        rightWallPictureFrame = new PictureFrame(gl, lights, camera);
        rightWallPictureFrame.setPosition(new Vec3(0,0.6f,-2f));
        rightWallPictureFrame.setImage(gl, "textures/hand2.jpg");

        frontWallPictureFrame = new PictureFrame(gl, lights, camera);
        frontWallPictureFrame.setPosition(new Vec3(0,0.6f,-2f));
        frontWallPictureFrame.setImage(gl, "textures/hand3.jpg");

        this.setupSceneGraph();
    }

    private void setupSceneGraph() {
        float roomHeight = 16;
        float roomWidth = 16;
        float roomDepth = 16;

        room = new NameNode("room");

        NameNode floor = new NameNode("floor");
        Mat4 m = Mat4Transform.scale(roomWidth, 1, roomDepth);
        TransformNode floorTransform = new TransformNode("floor transform", m);
        MeshNode floorShape = new MeshNode("mesh.TwoTriangles(floorMesh)", floorMesh);

        NameNode rightWall = new NameNode("right wall");
        TransformNode rightWallScale = new TransformNode("right wall scale", Mat4Transform.scale(roomWidth, roomHeight,roomDepth));
        m = Mat4Transform.rotateAroundZ(90);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m); // Get direction of texture correct
        TransformNode righttWallRotate = new TransformNode("right wall rotate", m);
        TransformNode rightWallTranslate = new TransformNode("right wall translate", Mat4Transform.translate(roomWidth / 2, roomHeight /2, 0));
        MeshNode rightWallShape = new MeshNode("mesh.TwoTriangles(wallMesh)", wallMesh);

        NameNode leftWall = new NameNode("left wall");
        TransformNode leftWallScale = new TransformNode("left wall scale", Mat4Transform.scale(roomWidth, roomHeight,roomDepth));
        m = Mat4Transform.rotateAroundZ(-90);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
        TransformNode leftWallRotate = new TransformNode("left wall rotate", m);
        TransformNode leftWallTranslate = new TransformNode("left wall translate", Mat4Transform.translate(-roomWidth / 2, roomHeight /2, 0));
        MeshNode leftWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", windowWallMesh);

        NameNode outsideScene = new NameNode("outside scene");
        TransformNode outsideSceneTranslate = new TransformNode("outside scene translate", Mat4Transform.translate(0,  -0.1f, -1.1f));
        TransformNode outsideSceneScale = new TransformNode("outside scene scale", Mat4Transform.scale(10, 1, 6f) );
        MeshNode outsideSceneShape = new MeshNode("mesh.TwoTriangles(outsideSceneMesh)", outsideSceneMesh);

        NameNode frontWall = new NameNode("front wall");
        TransformNode frontWallScale = new TransformNode("front wall scale", Mat4Transform.scale(roomWidth, roomHeight,roomDepth));
        m = Mat4Transform.rotateAroundX(-90);
        m = Mat4.multiply(Mat4Transform.rotateAroundZ(180), m); // rotate so it's not 'upside down'
        TransformNode frontWallRotate = new TransformNode("front wall rotate", m);
        TransformNode frontWallTranslate = new TransformNode("front wall translate", Mat4Transform.translate(0, roomHeight / 2, roomDepth /2));
        MeshNode frontWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", wallMesh);

        NameNode backWall = new NameNode("back wall");
        TransformNode backWallTranslate = new TransformNode("back wall translate", Mat4Transform.translate(0, roomHeight / 2, -roomDepth /2));
        TransformNode backWallRotate = new TransformNode("back wall rotate", Mat4Transform.rotateAroundX(90));
        TransformNode backWallScale = new TransformNode("back wall scale", Mat4Transform.scale(roomWidth, roomHeight,roomDepth));
        MeshNode backWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", wallMesh);

        NameNode ceiling = new NameNode("ceiling");
        m = Mat4Transform.scale(roomWidth, 1, roomDepth);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(180), m);
        m = Mat4.multiply(Mat4Transform.translate(0, roomHeight, 0), m);
        TransformNode ceilingTransform = new TransformNode("ceiling transform", m);
        MeshNode ceilingShape = new MeshNode("mesh.TwoTriangles(floorMesh)", ceilingMesh);


        room.addChild(floor);
            floor.addChild(floorTransform);
                floorTransform.addChild(floorShape);
            floor.addChild(rightWall);
                rightWall.addChild(rightWallTranslate);
                    rightWallTranslate.addChild(righttWallRotate);
                        righttWallRotate.addChild(rightWallScale);
                            rightWallScale.addChild(rightWallShape);
            floor.addChild(leftWall);
                leftWall.addChild(leftWallTranslate);
                    leftWallTranslate.addChild(leftWallRotate);
                        leftWallRotate.addChild(outsideScene); // Have to render outside scene first to stop culling
                            outsideScene.addChild(outsideSceneTranslate);
                                outsideSceneTranslate.addChild(outsideSceneScale);
                                    outsideSceneScale.addChild(outsideSceneShape);
                        leftWallRotate.addChild(leftWallScale);
                            leftWallScale.addChild(leftWallShape);
            floor.addChild(frontWall);
                frontWall.addChild(frontWallTranslate);
                    frontWallTranslate.addChild(frontWallRotate);
                        frontWallRotate.addChild(frontWallScale);
                            frontWallScale.addChild(frontWallShape);
            floor.addChild(backWall);
                backWall.addChild(backWallTranslate);
                    backWallTranslate.addChild(backWallRotate);
                        backWallRotate.addChild(backWallScale);
                            backWallScale.addChild(backWallShape);
                backWall.addChild(ceiling);
                    ceiling.addChild(ceilingTransform);
                        ceilingTransform.addChild(ceilingShape);


            backWallRotate.addChild(backWallPictureFrame.getSceneGraphRootNode());
            frontWallRotate.addChild(frontWallPictureFrame.getSceneGraphRootNode());
            righttWallRotate.addChild(rightWallPictureFrame.getSceneGraphRootNode());

        room.update();
    }

    public void render(GL3 gl, float elapsedTime) {
        room.draw(gl, elapsedTime);
    }

    public void addModel(Model model) {
        room.addChild(model.getSceneGraphRootNode());
    }

    @Override
    public SGNode getSceneGraphRootNode() {
        return room;
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        floorMesh.setPerspective(perspective);
        wallMesh.setPerspective(perspective);
        windowWallMesh.setPerspective(perspective);
        outsideSceneMesh.setPerspective(perspective);
        ceilingMesh.setPerspective(perspective);

        backWallPictureFrame.updatePerspectiveMatrices(perspective);
        rightWallPictureFrame.updatePerspectiveMatrices(perspective);
        frontWallPictureFrame.updatePerspectiveMatrices(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        floorMesh.dispose(gl);
        wallMesh.dispose(gl);
        windowWallMesh.dispose(gl);
        outsideSceneMesh.dispose(gl);
        ceilingMesh.dispose(gl);

        backWallPictureFrame.disposeMeshes(gl);
        rightWallPictureFrame.disposeMeshes(gl);
        frontWallPictureFrame.disposeMeshes(gl);
    }

}
