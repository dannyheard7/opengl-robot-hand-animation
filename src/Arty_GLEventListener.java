import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
  
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

  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
   
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

  
  public void loweredArms() {
    stopAnimation();

  }
   
  public void raisedArms() {
    stopAnimation();

  }
  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Mesh floor;
  private Light light;
  private SGNode robotSceneGraph;
  private RobotHand robotHand;

  
  private void initialise(GL3 gl) {

    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/marble.jpg");

    floor = new TwoTriangles(gl, textureId0);
    floor.setModelMatrix(Mat4Transform.scale(16,1,16));   

    light = new Light(gl);
    light.setCamera(camera);
    
    floor.setLight(light);
    floor.setCamera(camera);

    robotHand = new RobotHand();
    robotSceneGraph = robotHand.buildSceneGraph(gl, light, camera);
    //robotSceneGraph.print(0, false);
    //System.exit(0);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    updatePerspectiveMatrices();
    
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);

    floor.render(gl);

    double elapsedTime = getSeconds()-startTime;
    
    if (animation) robotHand.update(elapsedTime);
    robotSceneGraph.draw(gl);
  }
    
  private void updatePerspectiveMatrices() {
    // needs to be changed if user resizes the window
    perspective = Mat4Transform.perspective(45, aspect);
    light.setPerspective(perspective);
    floor.setPerspective(perspective);

    robotHand.updatePerspectiveMatrices(perspective);
  }
  
  private void disposeMeshes(GL3 gl) {
    light.dispose(gl);
    floor.dispose(gl);
    robotHand.disposeMeshes(gl);
  }

  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 2.7f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }
  
}