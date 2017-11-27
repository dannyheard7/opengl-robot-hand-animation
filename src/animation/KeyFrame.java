package animation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyFrame {

    private float value;
    private Consumer<Float> func;

    private Map<String, Position> positions;

    public KeyFrame() {
        this.positions = new LinkedHashMap<>();
    }

    public KeyFrame(Map<String, Position> positions) {
        this.positions = positions;
    }

    public void addPosition(String objectName, Position position) {
        positions.put(objectName, position);
    }

    public Map<String, Position> getPositions() {
        return  this.positions;
    }
}
