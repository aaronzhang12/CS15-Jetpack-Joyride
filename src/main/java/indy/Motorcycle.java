package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * The motorcycle class extends vehicle and is modeled by
 * a rectangle and two circular wheels. It can do both a short
 * and high jump depending on the press time.
 */

public class Motorcycle extends Vehicle {

    private Rectangle body;
    private Circle frontWheel;
    private Circle rearWheel;
    private long keyPressTime;

    /**
     * The constructor initializes both circles and the rectangle
     * @param pane
     */
    public Motorcycle(Pane pane) {
        super(pane);

        this.body = new Rectangle(0, 0, Constants.MOTORCYCLE_WIDTH, Constants.MOTORCYCLE_HEIGHT);
        this.body.setFill(Constants.MOTORCYCLE_COLOR);

        double frontWheelCenterX = Constants.MOTORCYCLE_WIDTH;
        double frontWheelCenterY = Constants.MOTORCYCLE_HEIGHT + (Constants.MOTORCYCLE_HEIGHT / 2);
        this.frontWheel = new Circle(0, 0, Constants.MOTORCYCLE_HEIGHT / 2);
        this.frontWheel.setTranslateX(frontWheelCenterX);
        this.frontWheel.setTranslateY(frontWheelCenterY);
        this.frontWheel.setFill(Constants.MOTORCYCLE_COLOR);

        double rearWheelCenterX = 0;
        double rearWheelCenterY = Constants.MOTORCYCLE_HEIGHT + (Constants.MOTORCYCLE_HEIGHT / 2);
        this.rearWheel = new Circle(0, 0, Constants.MOTORCYCLE_HEIGHT / 2);
        this.rearWheel.setTranslateX(rearWheelCenterX);
        this.rearWheel.setTranslateY(rearWheelCenterY);
        this.rearWheel.setFill(Constants.MOTORCYCLE_COLOR);

        this.vehicleGroup.getChildren().addAll(this.body, this.frontWheel, this.rearWheel);

        double totalHeight = Constants.MOTORCYCLE_HEIGHT + (Constants.MOTORCYCLE_HEIGHT / 2) + 10;
        this.setPosition(Constants.AVATAR_START_X, Constants.FLOOR_HEIGHT - totalHeight);
    }

    /**
     * This method controls the movement of the motorcycle
     * and prevents it from going beyond the floor/ceilign
     * @param timeSeconds
     */
    @Override
    public void move(double timeSeconds) {
        this.velocity += Constants.GRAVITY_SPEED * timeSeconds;

        double changeY = this.velocity * timeSeconds;
        this.vehicleGroup.setTranslateY(this.vehicleGroup.getTranslateY() + changeY);

        double currentY = this.vehicleGroup.getTranslateY();
        double groundY = Constants.FLOOR_HEIGHT - (2 * Constants.MOTORCYCLE_HEIGHT);
        if (currentY >= groundY) {
            this.vehicleGroup.setTranslateY(groundY);
            this.velocity = 0;
        }

        if (currentY <= Constants.CEILING_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.CEILING_HEIGHT);
            this.velocity = 0;
        }
    }

    /**
     * This method starts the clock from pressing the space key
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.SPACE) {
            this.keyPressTime = System.currentTimeMillis();
        }
    }

    /**
     * This method calculates the total duration between pressing
     * and releasing the space key. If it is under a certain threshold,
     * the motorcycle performs a short jump, and if over, a long jump.
     * @param code
     */
    @Override
    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.SPACE) {
            long keyReleaseTime = System.currentTimeMillis();
            long duration = keyReleaseTime - this.keyPressTime;

            if (duration < Constants.SHORT_JUMP_THRESHOLD) {
                this.performJump(Constants.SHORT_JUMP_VELOCITY);
            } else {
                this.performJump(Constants.LONG_JUMP_VELOCITY);
            }
        }
    }

    /**
     * This method models the motorcycles jump when the
     * motorcycle is on the ground.
     * @param jumpVelocity
     */
    private void performJump(double jumpVelocity) {
        double groundY = Constants.FLOOR_HEIGHT - (2* Constants.MOTORCYCLE_HEIGHT);
        if (this.vehicleGroup.getTranslateY() >= groundY) {
            this.velocity = jumpVelocity;
        }
    }

    /**
     * Getter method for the motorcycle vehicle type
     * @return
     */
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.MOTORCYCLE;
    }
}

