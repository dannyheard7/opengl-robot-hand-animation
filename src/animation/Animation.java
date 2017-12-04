package animation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Animation {

    private LinkedList<KeyFrame> keyFrames;
    private Map<Integer, Float> pauses;
    private int pos;

    private float step, time;

    public Animation(float step){
        this.step = step;
        this.time = 0;
        this.pos = 1; // Start moving from first to second frame initially

        keyFrames = new LinkedList<>();
        pauses = new LinkedHashMap<>();
    }

    public void addKeyFrame(KeyFrame k) {
        keyFrames.add(k);
    }

    public void update(double delta) {
        if(keyFrames.size() > pos) {
            KeyFrame nextFrame = keyFrames.get(pos);
            KeyFrame prevFrame = keyFrames.get(pos - 1);
            boolean progressFrame = true;

            Map<String, Position> nextFramePositions = nextFrame.getPositions();

            for (String key : nextFramePositions.keySet()) {
                // Keyframes must have the same positions!
                Position nextFramePosition = nextFramePositions.get(key);
                Position prevFramePosition = prevFrame.getPositions().get(key);

                float prevVal = prevFramePosition.getValue();
                float nextVal = nextFramePosition.getValue();
                int direction = Float.compare(nextVal, prevVal);

                if(direction != 0) {
                    float diff = nextVal - prevVal;
                    float curVal = prevVal + (diff * time + step);

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
                pos += 1;
                time = 0;
            }
        }
    }

    public void reset() {
        this.pos = 1;
        this.time = 0;
    }

    public void skipToKeyFrame(KeyFrame keyFrame) {
        int index = keyFrames.indexOf(keyFrame) + 1;
        this.pos = index;
        this.time = 0;
    }

}
