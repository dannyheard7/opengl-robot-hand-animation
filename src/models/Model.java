package models;

import com.jogamp.opengl.GL3;
import gmaths.Mat4;

public abstract class Model {

    public Model() {}

    public abstract void updatePerspectiveMatrices(Mat4 perspective);

    public abstract void disposeMeshes(GL3 gl);
}
