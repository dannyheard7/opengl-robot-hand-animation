import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import gmaths.Vec3;
import mesh.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Arty extends JFrame implements ActionListener {
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private Arty_GLEventListener glEventListener;
  private final FPSAnimator animator; 
  private Camera camera;

  public static void main(String[] args) {
    Arty b1 = new Arty("Assignment");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
  }

  public Arty(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    camera = new Camera(new Vec3(4f,12f,18f), Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new Arty_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);

    JPanel p = new JPanel();
      JButton b = new JButton("Toggle World Light");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Toggle Lamp 1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Toggle Lamp 2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("start");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("stop");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("reset");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("A");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Y");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("H");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Peace");
      b.addActionListener(this);
      p.add(b);
    this.add(p, BorderLayout.SOUTH);
    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
  
  public void actionPerformed(ActionEvent e) {

      if (e.getActionCommand().equalsIgnoreCase("Toggle World Light")) {
          glEventListener.toggleWordLight();
      }
      else if (e.getActionCommand().equalsIgnoreCase("Toggle Lamp 1")) {
          glEventListener.toggleLamp1();
      }
      else if (e.getActionCommand().equalsIgnoreCase("Toggle Lamp 2")) {
          glEventListener.toggleLamp2();
      }
    else if (e.getActionCommand().equalsIgnoreCase("start")) {
      glEventListener.startAnimation();
    }
    else if (e.getActionCommand().equalsIgnoreCase("stop")) {
      glEventListener.stopAnimation();
    }
    else if (e.getActionCommand().equalsIgnoreCase("reset")) {
      glEventListener.resetHand();
    }
    else if (e.getActionCommand().equalsIgnoreCase("A")) {
      glEventListener.letterA();
    }
    else if (e.getActionCommand().equalsIgnoreCase("Y")) {
      glEventListener.letterY();
    }
    else if (e.getActionCommand().equalsIgnoreCase("H")) {
      glEventListener.letterH();
    }
    else if (e.getActionCommand().equalsIgnoreCase("Peace")) {
      glEventListener.peaceGesture();
    }
  }
  
}
 
class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }
}