/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package lights;

import com.jogamp.opengl.GL3;
import gmaths.Vec3;

public class SpotLight extends Light {

    private float constant, linear, quadratic, cutOff, outerCutOff;
    private Vec3 initDirection, curDirection;

    public SpotLight(GL3 gl, Vec3 color, float constant, float linear, float quadratic, Vec3 direction, float cutOff, float outerCutOff) {
        super(gl, color);

        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;

        this.curDirection = direction;
        this.initDirection = direction;
        this.cutOff = cutOff;
        this.outerCutOff = outerCutOff;
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

    public Vec3 getDirection() {
        return curDirection;
    }

    public Vec3 getInitDirection() {
        return initDirection;
    }

    public float getCutOff() {
        return cutOff;
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setDirection(Vec3 direction) {
        this.curDirection = direction;
    }
}
