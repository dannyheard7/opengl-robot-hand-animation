package lights;

import com.jogamp.opengl.GL3;
import gmaths.Vec3;

public class DirectionalLight extends Light {

    private Vec3 direction;

    public DirectionalLight(GL3 gl, Vec3 color, Vec3 direction) {
        super(gl, color);

        material.setAmbient(0.5f, 0.5f, 0.5f);
        material.setDiffuse(0.5f, 0.5f, 0.5f);
        material.setSpecular(0, 0, 0);

        this.setPosition(new Vec3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));

        this.direction = direction;
    }


    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
