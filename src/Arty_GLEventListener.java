/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.DirectionalLight;
import lights.Light;
import core.Camera;
import models.Lamp;
import models.RobotHand;
import models.Room;

import java.util.ArrayList;
import java.util.Arrays;

public class Arty_GLEventListener implements GLEventListener {

    private float aspect;

    public Arty_GLEventListener(Camera camera) {
        this.camera = camera;
    }

    /* Initialisation */
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW


        gl.glEnable(GL.GL_BLEND); // Enable alpha texture for window
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        initialise(gl);
        startTime = getSeconds();
        updatePerspectiveMatrices();
    }

    /* Called to indicate the drawing surface has been moved and/or resized  */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        aspect = (float) width / (float) height;
        updatePerspectiveMatrices();
    }

    /* Draw */
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    /* Clean up memory, if necessary */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        disposeMeshes(gl);
    }


    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    private boolean animation = false;

    public void startAnimation() {
        animation = true;
    }

    public void stopAnimation() {
        animation = false;
    }

    public void resetHand() {
        robotHand.neutralPosition();
        animation = false;
    }

    public void letterA() {
        robotHand.positionA();
        animation = false;
    }

    public void letterY() {
        robotHand.positionY();
        animation = false;
    }

    public void letterH() {
        robotHand.positionH();
        animation = false;
    }

    public void peaceGesture() {
        robotHand.peaceGesture();
        animation = false;
    }

    public void toggleWordLight() {
        worldLight.toggle();
    }

    public void toggleLamp1() {
       lamp.getLight().toggle();
    }

    public void toggleLamp2() {
        lamp2.getLight().toggle();
    }

    public void toggleRingSpotlight() {
        robotHand.getRing().getLight().toggle();
    }


    private Camera camera;
    private Mat4 perspective;

    private DirectionalLight worldLight;

    private RobotHand robotHand;
    private Lamp lamp, lamp2;
    private Room room;


    private void initialise(GL3 gl) {
        Vec3 lightColor = new Vec3(0.5f, 0.5f, 0.5f);
        worldLight = new DirectionalLight(gl, lightColor, new Vec3(-0.2f, -1.0f, -0.3f));
        worldLight.setCamera(camera);

        ArrayList<Light> lights = new ArrayList<>(Arrays.asList(worldLight));

        lamp = new Lamp(gl, lights, camera);
        lamp.setPosition(new Vec3(-4, 0, 6));

        lamp2 = new Lamp(gl, lights, camera);
        lamp2.setPosition(new Vec3(4, 0, -6));

        room = new Room(gl, lights, camera);
        robotHand = new RobotHand(gl, lights, camera);

        room.addModel(lamp);
        room.addModel(lamp2);
        room.addModel(robotHand);
    }

    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        float elapsedTime = (float)(getSeconds() - startTime);
        if (animation) robotHand.update(elapsedTime);

        worldLight.render(gl);

        room.render(gl, elapsedTime);

    }

    private void updatePerspectiveMatrices() {
        perspective = Mat4Transform.perspective(45, aspect);
        worldLight.setPerspective(perspective);

        room.updatePerspectiveMatrices(perspective);
        robotHand.updatePerspectiveMatrices(perspective);
        lamp.updatePerspectiveMatrices(perspective);
        lamp2.updatePerspectiveMatrices(perspective);
    }

    private void disposeMeshes(GL3 gl) {
        worldLight.dispose(gl);

        room.disposeMeshes(gl);
        robotHand.disposeMeshes(gl);
        lamp.disposeMeshes(gl);
        lamp2.disposeMeshes(gl);
    }

}