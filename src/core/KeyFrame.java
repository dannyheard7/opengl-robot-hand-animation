package core;

import java.util.function.Consumer;

public class KeyFrame {

    private float startValue, currentValue, endValue, step;
    private Consumer<Float> func;

    public KeyFrame(float startValue, float endValue, float step, Consumer<Float> func) {
        this.startValue = startValue;
        this.currentValue = startValue;
        this.endValue = endValue;
        this.step = step;

        this.func = func;
    }

    public void update(double delta) {
        if (this.currentValue < this.endValue){
            this.currentValue += step;

            if (this.currentValue > this.endValue)
                this.currentValue = this.endValue;

            func.accept(this.currentValue);
        }
    }

    public void reset() {
        this.currentValue = startValue;
    }
}
