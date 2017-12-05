/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

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

        this.setPosition(new Vec3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)); // Directional lights are set infinitly far away

        this.direction = direction;
    }


    public void disable() {
        this.currentColor = new Vec3(0.2f, 0.2f, 0.2f); // Leave some ambient light so scene is not in total darkness
        material.setAmbient(currentColor);
        material.setDiffuse(0, 0, 0);
        material.setSpecular(0, 0, 0);

        enabled = false;
    }


    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
