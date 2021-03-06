/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package scenegraph;

import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Vec3;
import gmaths.Vec4;
import lights.Light;
import lights.SpotLight;


public class LightNode extends SGNode {

    protected Light light;

    public LightNode(String name, Light l) {
        super(name);
        light = l;
    }

    protected void update(Mat4 t) {
        Vec3 pos = Mat4.multiply(t, new Vec4()).toVec3();
        light.setPosition(pos); // Position of light needs to be set correctly to render

        if (light instanceof SpotLight) {
            SpotLight spotLight = (SpotLight) light;
            Vec3 spotlightDir = spotLight.getInitDirection(); //Spotlights need a direction

            // Calculate direction by multiplying world transform by original spotlight direction
            // Because it's direction and not position, set homogeneous to 0
            Vec3 dir = Mat4.multiply(t, new Vec4(spotlightDir, 0)).toVec3();

            spotLight.setDirection(dir);
        }

        super.update(t);
    }

    public void draw(GL3 gl, float elapsedTime) {
        light.render(gl, worldTransform);

        for (int i=0; i<children.size(); i++) {
            children.get(i).draw(gl, elapsedTime);
        }
    }

}