package models;

import com.jogamp.opengl.GL3;
import core.TextureLibrary;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import lights.Light;
import mesh.Camera;
import mesh.Mesh;
import mesh.TwoTriangles;
import scenegraph.MeshNode;
import scenegraph.NameNode;
import scenegraph.SGNode;
import scenegraph.TransformNode;

import java.util.ArrayList;

// TODO: Restructure this into a general model class
public class Room extends Model {

    private Mesh floorMesh, wallMesh, ceilingMesh;
    private SGNode room;

    public Room(GL3 gl, ArrayList<Light> lights, Camera camera) {
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/marble-floor.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/marble-tiles.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/plaster.jpg");

        floorMesh = new TwoTriangles(gl, textureId0);
        floorMesh.setLights(lights);
        floorMesh.setCamera(camera);

        wallMesh = new TwoTriangles(gl, textureId1);
        wallMesh.setLights(lights);
        wallMesh.setCamera(camera);

        ceilingMesh = new TwoTriangles(gl, textureId2);
        ceilingMesh.setLights(lights);
        ceilingMesh.setCamera(camera);

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
        m = Mat4Transform.scale(roomWidth, roomHeight , roomDepth);
        m = Mat4.multiply(Mat4Transform.rotateAroundZ(90), m);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m); // Get direction of texture correct
        m = Mat4.multiply(Mat4Transform.translate(roomWidth / 2, roomHeight / 2, 0), m);
        TransformNode rightWallTransform = new TransformNode("wall transform", m);
        MeshNode rightWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", wallMesh);

        NameNode leftWall = new NameNode("left wall");
        m = Mat4Transform.scale(roomWidth, roomHeight,roomDepth);
        m = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), m);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
        m = Mat4.multiply(Mat4Transform.translate(- roomWidth / 2, roomHeight /2, 0), m);
        TransformNode leftWallTransform = new TransformNode("wall transform", m);
        MeshNode leftWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", wallMesh);

        NameNode frontWall = new NameNode("front wall");
        m = Mat4Transform.scale(roomWidth, roomHeight,roomDepth);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(-90), m);
        m = Mat4.multiply(Mat4Transform.translate(0, roomHeight / 2, roomDepth /2), m);
        TransformNode frontWallTransform = new TransformNode("wall transform", m);
        MeshNode frontWallShape = new MeshNode("mesh.TwoTriangles(floorMesh)", wallMesh);

        NameNode backWall = new NameNode("back wall");
        m = Mat4Transform.scale(roomWidth, roomHeight,roomDepth);
        m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
        m = Mat4.multiply(Mat4Transform.translate(0, roomHeight / 2, -roomDepth /2), m);
        TransformNode backWallTransform = new TransformNode("wall transform", m);
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
                rightWall.addChild(rightWallTransform);
                    rightWallTransform.addChild(rightWallShape);
            floor.addChild(leftWall);
                leftWall.addChild(leftWallTransform);
                    leftWallTransform.addChild(leftWallShape);
            floor.addChild(frontWall);
                frontWall.addChild(frontWallTransform);
                    frontWallTransform.addChild(frontWallShape);
            floor.addChild(backWall);
                backWall.addChild(backWallTransform);
                    backWallTransform.addChild(backWallShape);
                backWall.addChild(ceiling);
                    ceiling.addChild(ceilingTransform);
                        ceilingTransform.addChild(ceilingShape);

        room.update();
    }


    public void render(GL3 gl) {
        room.draw(gl);
    }

    public void updatePerspectiveMatrices(Mat4 perspective) {
        floorMesh.setPerspective(perspective);
        wallMesh.setPerspective(perspective);
        ceilingMesh.setPerspective(perspective);
    }

    public void disposeMeshes(GL3 gl) {
        floorMesh.dispose(gl);
        wallMesh.dispose(gl);
        ceilingMesh.dispose(gl);
    }

}
