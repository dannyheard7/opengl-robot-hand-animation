package scenegraph;

import gmaths.Mat4;
import gmaths.Vec3;
import lights.Light;
import com.jogamp.opengl.*;
import lights.SpotLight;

public class LightNode extends SGNode {

    protected Light light;

    public LightNode(String name, Light l) {
        super(name);
        light = l;
    }

    protected void update(Mat4 t) {
        Vec3 pos = Mat4.multiply(t, new Vec3()); // TODO: Should be able to get light position and update
        light.setPosition(pos);

        if (light instanceof SpotLight) {
            SpotLight spotLight = (SpotLight) light;

            Vec3 dir = Mat4.multiply(t, new Vec3(0, 0, 1));

            spotLight.setDirection(dir);
//            System.out.println(dir);
        }

        super.update(t);
    }

    public void draw(GL3 gl) {
        light.render(gl, worldTransform);

        for (int i=0; i<children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

}