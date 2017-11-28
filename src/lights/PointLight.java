package lights;

import com.jogamp.opengl.GL3;

public class PointLight extends Light {

    private float constant, linear, quadratic;

    public PointLight(GL3 gl, float constant, float linear, float quadratic) {
        super(gl);

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
