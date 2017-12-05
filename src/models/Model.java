/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package models;

import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import scenegraph.SGNode;

public abstract class Model {

    public Model() {}

    public abstract SGNode getSceneGraphRootNode();

    public abstract void updatePerspectiveMatrices(Mat4 perspective);

    public abstract void disposeMeshes(GL3 gl);
}
