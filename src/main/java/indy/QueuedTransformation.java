package indy;

import javafx.scene.layout.Pane;

/**
 * This class represents a queued vehicle transformation in the game
 * so that game can store it in the queue and perform the appropriate
 * transformation to the correct vehicle type
 */
public class QueuedTransformation {
    private final TransformationType type;

    /**
     * The constructor creates a new QueuedTransformation with
     * the specified type
     * @param type
     */
    public QueuedTransformation(TransformationType type) {
        this.type = type;
    }

    /**
     * Getter method for the type of queued transformation
     * @return
     */
    public TransformationType getType() {
        return this.type;
    }

    /**
     * Creates a vehicle instance corresponding to the
     * queued transformation type
     * @param pane
     * @return
     */
    public Vehicle createVehicle(Pane pane) {
        return this.type.createVehicle(pane);
    }
}
