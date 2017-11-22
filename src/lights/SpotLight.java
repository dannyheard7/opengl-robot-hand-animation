package lights;

import com.jogamp.opengl.GL3;
import gmaths.Vec3;

public class SpotLight extends Light {

    private float constant, linear, quadratic, cutOff, outerCutOff;
    private Vec3 direction;

    public SpotLight(GL3 gl, float constant, float linear, float quadratic, Vec3 direction, float cutOff, float outerCutOff) {
        super(gl);

        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;

        this.direction = direction;
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
        return direction;
    }

    public float getCutOff() {
        return cutOff;
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
