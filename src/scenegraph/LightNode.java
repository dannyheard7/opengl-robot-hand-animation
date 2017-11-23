package scenegraph;

import gmaths.Mat4;
import gmaths.Vec3;
import gmaths.Vec4;
import lights.Light;
import models.Mesh;
import com.jogamp.opengl.*;
import scenegraph.SGNode;

public class LightNode extends SGNode {

    protected Light light;

    public LightNode(String name, Light l) {
        super(name);
        light = l;
    }

    protected void update(Mat4 t) {
        Vec3 pos = Mat4.multiply(t, new Vec3(1, 1, 1));
        light.setPosition(pos);

//        System.out.println(pos);

        super.update(t);
    }

    public void draw(GL3 gl) {
        light.render(gl, worldTransform);

        for (int i=0; i<children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

}