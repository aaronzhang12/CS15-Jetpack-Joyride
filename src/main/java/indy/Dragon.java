package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * The dragon class extends Vehicle, and is modeled
 * by a series of rectangles which undulates when moving.
 * It has reverse gravity effects, where gravity pushes it up
 * and thrust pulls it down.
 */
public class Dragon extends Vehicle {
    private Rectangle[] segments;
    private double undulationTime;

    /**
     * The constructor initializes an array of rectangles
     * which represent the dragon
     * @param pane
     */
    public Dragon(Pane pane) {
        super(pane);

        this.segments = new Rectangle[Constants.DRAGON_SEGMENT_COUNT];
        this.undulationTime = 0;

        for (int i = 0; i < Constants.DRAGON_SEGMENT_COUNT; i++) {
            Rectangle segment = new Rectangle(
                    i * Constants.DRAGON_SEGMENT_WIDTH,
                    0,
                    Constants.DRAGON_SEGMENT_WIDTH,
                    Constants.DRAGON_HEIGHT
            );
            segment.setFill(Constants.DRAGON_COLOR);
            this.segments[i] = segment;
            this.vehicleGroup.getChildren().add(segment);
        }

        this.setPosition(Constants.AVATAR_START_X, Constants.FLOOR_HEIGHT - Constants.DRAGON_HEIGHT);
    }

    /**
     * Movement method which reverses the movement mechanics
     * of the avatar, and prevents the dragon from going
     * beyond the floor/ceiling. It also varies the height
     * of each segment in the array creating the undulation effect
     * @param timeSeconds
     */
    @Override
    public void move(double timeSeconds) {
        this.undulationTime += timeSeconds;

        this.velocity -= Constants.GRAVITY_SPEED * timeSeconds;

        if (this.thrustActive) {
            this.velocity += Constants.THRUST_ACCELERATION * timeSeconds;
        }

        double changeY = this.velocity * timeSeconds;
        this.vehicleGroup.setTranslateY(this.vehicleGroup.getTranslateY() + changeY);

        for (int i = 0; i < Constants.DRAGON_SEGMENT_COUNT; i++) {
            double offset = Constants.DRAGON_UNDULATION_AMPLITUDE *
                    Math.sin(Constants.DRAGON_UNDULATION_FREQUENCY * this.undulationTime + i);
            this.segments[i].setTranslateY(offset);
        }

        if (this.vehicleGroup.getTranslateY() <= Constants.CEILING_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.CEILING_HEIGHT);
            this.velocity = 0;
        }

        if (this.vehicleGroup.getTranslateY() >= Constants.FLOOR_HEIGHT - Constants.DRAGON_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.FLOOR_HEIGHT - Constants.DRAGON_HEIGHT);
            this.velocity = 0;
        }
    }

    /**
     * This method activates thrust when the space
     * key is pressed
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.SPACE) {
            this.thrustActive = true;
        }
    }

    /**
     * This method deactivates thrust when the
     * space key is released
     * @param code
     */
    @Override
    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.SPACE) {
            this.thrustActive = false;
        }
    }

    /**
     * Getter method for the dragon vehicle type
     * @return
     */
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.DRAGON;
    }
}


