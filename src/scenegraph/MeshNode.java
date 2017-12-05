/* Author Steve Maddock */

package scenegraph;

import com.jogamp.opengl.GL3;
import mesh.Mesh;

public class MeshNode extends SGNode {

  protected Mesh mesh;

  public MeshNode(String name, Mesh m) {
    super(name);
    mesh = m; 
  }

  public void draw(GL3 gl, float elapsedTime) {
    mesh.render(gl, worldTransform, elapsedTime);
    for (int i=0; i<children.size(); i++) {
      children.get(i).draw(gl, elapsedTime);
    }
  }

}