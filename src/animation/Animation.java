/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package animation;

import java.util.LinkedList;
import java.util.Map;

/* Animate using linear interpolation */
public class Animation {

    private LinkedList<KeyFrame> keyFrames;
    private int position;

    private float step, time;

    public Animation(float step){
        this.step = step;
        this.time = 0;
        this.position = 1; // Start moving from first to second frame initially

        keyFrames = new LinkedList<>();
    }

    public void addKeyFrame(KeyFrame k) {
        keyFrames.add(k);
    }

    public void update(double elapsedTime) {
        if(keyFrames.size() > position) {
            KeyFrame nextFrame = keyFrames.get(position);
            KeyFrame prevFrame = keyFrames.get(position - 1);
            boolean progressFrame = true;

            Map<String, Position> nextFramePositions = nextFrame.getPositions();

            for (String key : nextFramePositions.keySet()) {
                // Keyframes must have the same positions
                Position nextFramePosition = nextFramePositions.get(key);
                Position prevFramePosition = prevFrame.getPositions().get(key);

                float prevVal = prevFramePosition.getValue();
                float nextVal = nextFramePosition.getValue();
                int direction = Float.compare(nextVal, prevVal);

                if(direction != 0) {
                    float diff = nextVal - prevVal;
                    float curVal = prevVal + (diff * time + step); // Linear interpolation

                    // Stop animation overshooting and continuing
                    if (direction < 0 && curVal < nextVal || direction > 0 && curVal > nextVal) {
                        curVal = nextVal;
                    } else {
                        progressFrame = false;
                    }

                    prevFramePosition.getPositionFunc().accept(curVal);
                }
            }
            time += step;

            if (progressFrame) {
                position += 1;
                time = 0;
            }
        }
    }

    public void reset() {
        this.position = 1;
        this.time = 0;
    }

    /* Skip to keyframe in animation sequence */
    public void skipToKeyFrame(KeyFrame keyFrame) {
        int index = keyFrames.indexOf(keyFrame) + 1;
        this.position = index;
        this.time = 0;
    }

}
