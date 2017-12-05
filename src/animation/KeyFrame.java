/* I declare that this code is my own work */
/* Author Danny Heard dheard2@sheffield.ac.uk */

package animation;

import java.util.LinkedHashMap;
import java.util.Map;


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


    public void display() {
        // Set all positions to their values
        for (Position position : this.positions.values()) {
            position.getPositionFunc().accept(position.getValue());
        }
    }
}
