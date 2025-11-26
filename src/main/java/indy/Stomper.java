package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * The stomper class extends vehicle and is
 * modelled by a rectangle. It can jump and
 * provide thrust to slow its descent.
 */
public class Stomper extends Vehicle {
    private Polygon thrustEffect;
    private Rectangle body;

    /**
     * The constructor initializes the rectangle
     * which represents the stomper.
     * @param pane
     */
    public Stomper(Pane pane) {
        super(pane);
        this.body = new Rectangle(0, 0, Constants.STOMPER_WIDTH, Constants.STOMPER_HEIGHT);
        this.body.setFill(Constants.STOMPER_COLOR);
        this.vehicleGroup.getChildren().add(this.body);
        this.setPosition(Constants.AVATAR_START_X, Constants.FLOOR_HEIGHT - Constants.STOMPER_HEIGHT);
    }

    /**
     * This method controls the stomper movement and adds
     * a thrust effect if thrust is being applied. It prevents
     * the stomper from going beyond the floor/ceiling.
     * @param timeSeconds
     */
    @Override
    public void move(double timeSeconds) {
        this.velocity += Constants.STOMPER_GRAVITY * timeSeconds;

        if (this.thrustActive) {
            this.velocity -= Constants.STOMPER_THRUST * timeSeconds;
            this.showThrustEffect();
        } else {
            this.hideThrustEffect();
        }

        double changeY = this.velocity * timeSeconds;
        this.vehicleGroup.setTranslateY(this.vehicleGroup.getTranslateY() + changeY);

        if (this.vehicleGroup.getTranslateY() >= Constants.FLOOR_HEIGHT - Constants.STOMPER_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.FLOOR_HEIGHT - Constants.STOMPER_HEIGHT);
            this.velocity = 0;
            this.thrustActive = false;
            this.hideThrustEffect();
        }

        if (this.vehicleGroup.getTranslateY() <= Constants.CEILING_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.CEILING_HEIGHT);
            this.velocity = 0;
        }
    }

    /**
     * This method makes the stomper, on a space key press
     * jump if it is on the ground, and provide thrust
     * if it is in the air.
     *
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.SPACE) {
            if (this.vehicleGroup.getTranslateY() >= Constants.FLOOR_HEIGHT - Constants.STOMPER_HEIGHT) {
                this.velocity = Constants.STOMPER_JUMP_VELOCITY;
                this.thrustActive = false;
            } else {
                if (!this.thrustActive) {
                    this.thrustActive = true;
                }
            }
        }
    }

    /**
     * This method deactivates the thrust when
     * the space bar is released
     * @param code
     */
    @Override
    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.SPACE) {
            if (this.thrustActive) {
                this.thrustActive = false;
            }
        }
    }

    /**
     * This method adds a small triangle underneath
     * the stomper to show that thrust is being applied
     */
    private void showThrustEffect() {
        if (this.thrustEffect == null) {
            this.thrustEffect = new Polygon();
            this.thrustEffect.getPoints().addAll(
                    Constants.STOMPER_WIDTH / 2.0, Constants.STOMPER_HEIGHT,
                    Constants.STOMPER_WIDTH / 2.0 - 10, Constants.STOMPER_HEIGHT + 20,
                    Constants.STOMPER_WIDTH / 2.0 + 10, Constants.STOMPER_HEIGHT + 20
            );
            this.thrustEffect.setFill(Color.ORANGE);
            this.vehicleGroup.getChildren().add(this.thrustEffect);
        }
    }

    /**
     * this method removes the thrust effect
     * if thrust is not being applied
     */
    private void hideThrustEffect() {
        if (this.thrustEffect != null) {
            this.vehicleGroup.getChildren().remove(this.thrustEffect);
            this.thrustEffect = null;
        }
    }

    /**
     * Getter method for the stomper vehicle type.
     * @return
     */
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.STOMPER;
    }
}


