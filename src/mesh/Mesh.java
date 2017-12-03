package mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import core.Shader;
import gmaths.Mat4;
import lights.DirectionalLight;
import lights.Light;
import lights.PointLight;
import lights.SpotLight;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public abstract class Mesh {
  
  protected float[] vertices;
  protected int[] indices;
  private int vertexStride = 8;
  private int vertexXYZFloats = 3;
  private int vertexNormalFloats = 3;
  private int vertexTexFloats = 2;
  private int[] vertexBufferId = new int[1];
  protected int[] vertexArrayId = new int[1];
  private int[] elementBufferId = new int[1];
  
  protected Material material;
  protected Shader shader;
  protected Mat4 model;
  
  protected mesh.Camera camera;
  protected Mat4 perspective;
  protected ArrayList<Light> lights;
  
  public Mesh(GL3 gl) {
    material = new Material();
    model = new Mat4(1);
  }
  
  public void setModelMatrix(Mat4 m) {
    model = m;
  }
  
  public void setCamera(mesh.Camera camera) {
    this.camera = camera;
  }
  
  public void setPerspective(Mat4 perspective) {
    this.perspective = perspective;
  }
  
  public void setLights(ArrayList<Light> lights) {
    this.lights = lights;
  }
  
  public void dispose(GL3 gl) {
    gl.glDeleteBuffers(1, vertexBufferId, 0);
    gl.glDeleteVertexArrays(1, vertexArrayId, 0);
    gl.glDeleteBuffers(1, elementBufferId, 0);
  }
    
  protected void fillBuffers(GL3 gl) {
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    
    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);
    
    int stride = vertexStride;
    int numXYZFloats = vertexXYZFloats;
    int offset = 0;
    gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);
  
    int numNormalFloats = vertexNormalFloats; // x,y,z for each vertex 
    offset = numXYZFloats*Float.BYTES;  // the normal values are three floats after the three x,y,z values
                                    // so change the offset value
    gl.glVertexAttribPointer(1, numNormalFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
                                    // the vertex shader uses location 1 (sometimes called index 1)
                                    // for the normal information
                                    // location, size, type, normalize, stride, offset
                                    // offset is relative to the start of the array of data
    gl.glEnableVertexAttribArray(1);// Enable the vertex attribute array at location 1

    // now do the texture coordinates  in vertex attribute 2
    int numTexFloats = vertexTexFloats;
    offset = (numXYZFloats+numNormalFloats)*Float.BYTES;
    gl.glVertexAttribPointer(2, numTexFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(2);
    
    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
    gl.glBindVertexArray(0);
  }
  
  // public abstract void display(int indent);
  
  public abstract void render(GL3 gl, Mat4 model, float elapsedTime);
  
  public void render(GL3 gl, float elapsedTime) {
    render(gl, model, elapsedTime);
  }

  protected void renderLights(GL3 gl) {
    int numPointLights = 0;

    for (Light light : lights) {
      if (light instanceof DirectionalLight) {
        DirectionalLight dirLight = (DirectionalLight) light;

        shader.setVec3(gl, "dirLight.direction", dirLight.getDirection());
        shader.setVec3(gl, "dirLight.ambient", light.getMaterial().getAmbient());
        shader.setVec3(gl, "dirLight.diffuse", light.getMaterial().getDiffuse());
        shader.setVec3(gl, "dirLight.specular", light.getMaterial().getSpecular());
      } else if (light instanceof PointLight) {
        // Either do this or have one big light class? Might make more sense even though some variables are unused
        PointLight pointLight = (PointLight) light;

        String name = "pointLights[" + numPointLights + "]";

        shader.setVec3(gl, name + ".position", pointLight.getPosition());
        shader.setVec3(gl, name + ".ambient", pointLight.getMaterial().getAmbient());
        shader.setVec3(gl, name + ".diffuse", pointLight.getMaterial().getDiffuse());
        shader.setVec3(gl, name + ".specular", pointLight.getMaterial().getSpecular());

        shader.setFloat(gl, name + ".constant", pointLight.getConstant());
        shader.setFloat(gl, name + ".linear", pointLight.getLinear());
        shader.setFloat(gl, name + ".quadratic", pointLight.getQuadratic());

        numPointLights += 1;
      } else if (light instanceof SpotLight) {
        SpotLight spotLight = (SpotLight) light;

        shader.setVec3(gl, "spotLight.position", spotLight.getPosition());
        shader.setVec3(gl, "spotLight.ambient", spotLight.getMaterial().getAmbient());
        shader.setVec3(gl, "spotLight.diffuse", spotLight.getMaterial().getDiffuse());
        shader.setVec3(gl, "spotLight.specular", spotLight.getMaterial().getSpecular());

        shader.setFloat(gl, "spotLight.constant", spotLight.getConstant());
        shader.setFloat(gl, "spotLight.linear", spotLight.getLinear());
        shader.setFloat(gl, "spotLight.quadratic", spotLight.getQuadratic());

        shader.setVec3(gl, "spotLight.direction", spotLight.getDirection());
        shader.setFloat(gl, "spotLight.cutOff", spotLight.getCutOff());
        shader.setFloat(gl, "spotLight.outerCutOff", spotLight.getOuterCutOff());
      }
    }
  }
  
  //public abstract void render(GL3 gl, lights.Light light, Vec3 viewPosition, Mat4 perspective, Mat4 view);
  /*public void render(GL3 gl, lights.Light light, Vec3 viewPosition, Mat4 perspective, Mat4 view) {
    setViewPosition(viewPosition);
    setView(view);
    setPerspective(perspective);
    setLight(light);
    render(gl, this.model);
  }
  */
  
  
}