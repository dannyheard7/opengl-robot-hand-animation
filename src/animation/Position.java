package animation;

import java.util.function.Consumer;

public class Position {
    private float value;
    private Consumer<Float> positionFunc;

    public Position(float value, Consumer<Float> positionFunc) {
        this.value = value;
        this.positionFunc = positionFunc;
    }

    public Consumer<Float> getPositionFunc() {
        return positionFunc;
    }

    public float getValue() {
        return value;
    }
}