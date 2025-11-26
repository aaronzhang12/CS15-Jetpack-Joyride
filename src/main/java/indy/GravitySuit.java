package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

/**
 * The GravitySuit class extends vehicle and is modeled
 * by a square and triangle. It can switch between positive
 * and negative body in midair
 */
public class GravitySuit extends Vehicle {

    private Polygon hair;
    private Rectangle body;
    private boolean gravityReversed;
    private double lastGravityToggleTime = 0;

    /**
     * The constructor initializes the rectangle and triangle
     * which represent the gravitysuit. It also starts with
     * downward gravity
     * @param pane
     */
    public GravitySuit(Pane pane) {
        super(pane);

        this.body = new Rectangle(0, 0, Constants.GRAVITYSUIT_WIDTH, Constants.GRAVITYSUIT_HEIGHT);
        this.body.setFill(Constants.GRAVITYSUIT_COLOR);

        this.hair = new Polygon();
        this.hair.getPoints().addAll(
                0.0, 0.0,
                Constants.GRAVITYSUIT_WIDTH / 2.0, -20.0,
                Constants.GRAVITYSUIT_WIDTH, 0.0
        );
        this.hair.setFill(Constants.GRAVITYSUIT_HAIR_COLOR);

        this.vehicleGroup.getChildren().addAll(this.hair, this.body);

        this.setPosition(Constants.AVATAR_START_X, Constants.FLOOR_HEIGHT - Constants.GRAVITYSUIT_HEIGHT);
        this.gravityReversed = false;
    }

    /**
     * This method controls the movement of the gravitysuit,
     * moving downwards if regular gravity and upwards
     * if positive gravity. It also prevents the gravitysuit
     * from moving beyond the floor/ceiling.
     * @param timeSeconds
     */
    @Override
    public void move(double timeSeconds) {
        double speed = Constants.GRAVITYSUIT_SPEED;

        this.velocity = this.gravityReversed ? -speed : speed;

        double changeY = this.velocity * timeSeconds;
        this.vehicleGroup.setTranslateY(this.vehicleGroup.getTranslateY() + changeY);

        if (!this.gravityReversed) {
            if (this.vehicleGroup.getTranslateY() >= Constants.FLOOR_HEIGHT - Constants.GRAVITYSUIT_HEIGHT) {
                this.vehicleGroup.setTranslateY(Constants.FLOOR_HEIGHT - Constants.GRAVITYSUIT_HEIGHT);
                this.velocity = 0;
            }
        } else {
            if (this.vehicleGroup.getTranslateY() <= Constants.CEILING_HEIGHT) {
                this.vehicleGroup.setTranslateY(Constants.CEILING_HEIGHT);
                this.velocity = 0;
            }
        }
    }

    /**
     * This method toggles positive/negative gravity
     * when the space bar is clicked, with a cooldown.
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.SPACE) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastGravityToggleTime >= Constants.GRAVITY_TOGGLE_COOLDOWN) {
                this.gravityReversed = !this.gravityReversed;
                this.flipGravity();
                this.lastGravityToggleTime = currentTime;
            }
        }
    }

    /**
     * Method inherited from vehicle not used
     * @param code
     */
    @Override
    public void onKeyReleased(KeyCode code) {
    }

    /**
     * This method imitates the changing of gravity
     * flipping the gravitysuit upside-down and right-side-up
     * by rotating it 180 degrees.
     */
    private void flipGravity() {
        RotateTransition rotate = new RotateTransition(Duration.millis(200), this.vehicleGroup);
        if (this.gravityReversed) {
            rotate.setByAngle(180);
        } else {
            rotate.setByAngle(-180);
        }
        rotate.play();
    }

    /**
     * Getter method for the gravitysuit vehicle type
     * @return
     */
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.GRAVITY_SUIT;
    }
}