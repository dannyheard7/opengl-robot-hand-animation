package animation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyFrame {

    private Map<String, Position> positions;

    public KeyFrame() {
        this.positions = new LinkedHashMap<>();
    }

    public KeyFrame(Map<String, Position> positions) {
        this.positions = new LinkedHashMap<>(positions);
    }

    public void addPosition(String positionName, Position position) {
        positions.put(positionName, position);
    }

    public void addPositions(Map<String, Position> positions) {
        this.positions.putAll(positions);
    }

    public Map<String, Position> getPositions() {
        return  this.positions;
    }

    public void show() {
        for (Position position : this.positions.values()) {
            position.getPositionFunc().accept(position.getValue());
        }
    }
}
