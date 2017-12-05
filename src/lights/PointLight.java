/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package lights;

import com.jogamp.opengl.GL3;
import gmaths.Vec3;

public class PointLight extends Light {

    private float constant, linear, quadratic;

    public PointLight(GL3 gl, Vec3 color, float constant, float linear, float quadratic) {
        super(gl, color);

        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    public float getConstant() {
        return constant;
    }

    public float getLinear() {
        return linear;
    }

    public float getQuadratic() {
        return quadratic;
    }
}
