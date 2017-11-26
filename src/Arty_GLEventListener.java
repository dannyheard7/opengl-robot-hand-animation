import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import lights.DirectionalLight;
import lights.Light;
import mesh.Camera;
import models.Lamp;
import models.RobotHand;
import models.Room;

import java.util.ArrayList;
import java.util.Arrays;

public class Arty_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private float aspect;

  public Arty_GLEventListener(Camera camera) {
    this.camera = camera;
  }

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    aspect = (float)width/(float)height;
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

  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }

  public void resetHand() {
    robotHand.neutralPosition();
    animation = false;
  }

  public void letterD() {
    robotHand.positionD();

  }
  
  public void letterA() {
    robotHand.positionA();

  }
   
  public void letterY() {
    robotHand.positionY();
  }


  private Camera camera;
  private Mat4 perspective;

  private DirectionalLight dirLight;

  private RobotHand robotHand;
  private Lamp lamp, lamp2;
  private Room room;

  
  private void initialise(GL3 gl) {
    dirLight = new DirectionalLight(gl,  new Vec3(-0.2f, -1.0f, -0.3f));
    dirLight.setCamera(camera);

    ArrayList<Light> lights = new ArrayList<>(Arrays.asList(dirLight));

    lamp = new Lamp(gl, lights, camera);
    lamp.setPosition(new Vec3(-4, 0, 6));

    lamp2 = new Lamp(gl, lights, camera);
    lamp2.setPosition(new Vec3(4, 0, -6));

    room = new Room(gl, lights, camera);
    robotHand = new RobotHand(gl, lights, camera);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    updatePerspectiveMatrices();

    double elapsedTime = getSeconds()-startTime;

    dirLight.render(gl);

    lamp.render(gl);
    lamp2.render(gl);

    room.render(gl);

    if (animation) robotHand.update(elapsedTime);
    robotHand.render(gl);
  }
    
  private void updatePerspectiveMatrices() {
    // needs to be changed if user resizes the window
    perspective = Mat4Transform.perspective(45, aspect);
    dirLight.setPerspective(perspective);

    room.updatePerspectiveMatrices(perspective);
    robotHand.updatePerspectiveMatrices(perspective);
    lamp.updatePerspectiveMatrices(perspective);
    lamp2.updatePerspectiveMatrices(perspective);
  }
  
  private void disposeMeshes(GL3 gl) {
    dirLight.dispose(gl);

    room.disposeMeshes(gl);
    robotHand.disposeMeshes(gl);
    lamp.disposeMeshes(gl);
    lamp2.disposeMeshes(gl);
  }

}