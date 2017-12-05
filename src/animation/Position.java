/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package animation;

import java.util.function.Consumer;

/* A position consists of a value and the function to accept the value, function must take a float and return nothing */
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