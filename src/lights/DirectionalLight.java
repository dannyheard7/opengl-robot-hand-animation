package lights;

import com.jogamp.opengl.GL3;
import gmaths.Vec3;

public class DirectionalLight extends Light {

    public DirectionalLight(GL3 gl) {
        super(gl);

        material.setAmbient(0.5f, 0.5f, 0.5f);
        material.setDiffuse(0.5f, 0.5f, 0.5f);
        material.setSpecular(0, 0, 0);

        this.setPosition(new Vec3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));
    }


    public Vec3 getDirection() {
        return new Vec3(-0.2f, -1.0f, -0.3f);
    }

}
