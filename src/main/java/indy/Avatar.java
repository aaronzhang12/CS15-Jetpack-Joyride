package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * The avatar class extends vehicle and is the default vehicle
 * initialized in the game. It has a constants gravity
 * pulling it downwards and can accelerate upwards when
 * the space key is pressed.
 */

public class Avatar extends Vehicle {

    private Rectangle body;
    private Rectangle pack;
    private Circle head;
    private Rectangle visor;
    private Polygon thrusterFlame;

    /**
     * The constructor creates a square that models the avatar
     * @param pane
     */

    public Avatar(Pane pane) {
        super(pane);

        this.thrusterFlame = new Polygon(
                -4.0, 30.0,
                0.0, 46.0,
                4.0, 30.0);
        this.thrusterFlame.setFill(Constants.AVATAR_THRUSTER_COLOR);
        this.thrusterFlame.setOpacity(0);

        this.pack = new Rectangle(2, 16, 12, 20);
        this.pack.setArcWidth(8);
        this.pack.setArcHeight(8);
        this.pack.setFill(Constants.AVATAR_BODY_ACCENT);

        this.body = new Rectangle(10, 10, 26, 28);
        this.body.setArcWidth(12);
        this.body.setArcHeight(12);
        this.body.setFill(Constants.AVATAR_COLOR);

        this.head = new Circle(23, 12, 10);
        this.head.setFill(Constants.AVATAR_BODY_ACCENT);

        this.visor = new Rectangle(17, 7, 12, 8);
        this.visor.setArcWidth(8);
        this.visor.setArcHeight(8);
        this.visor.setFill(Constants.AVATAR_VISOR_COLOR);

        this.vehicleGroup.getChildren().addAll(this.thrusterFlame, this.pack, this.body, this.head, this.visor);

        this.setPosition(Constants.AVATAR_START_X, Constants.FLOOR_HEIGHT - Constants.AVATAR_HEIGHT);
    }

    /**
     * This method controls the method avatar movement
     * with gravity and thrust, and prevents the avatar
     * from going beyond the floor/ceiling
     * @param timeSeconds
     */
    @Override
    public void move(double timeSeconds) {
        this.velocity += Constants.GRAVITY_SPEED * timeSeconds;

        if (this.thrustActive) {
            this.velocity -= Constants.THRUST_ACCELERATION * timeSeconds;
        }

        this.velocity *= Constants.VELOCITY_DAMPING;
        this.velocity = Math.max(Constants.MAX_ASCENT_SPEED, Math.min(Constants.MAX_FALL_SPEED, this.velocity));

        double changeY = this.velocity * timeSeconds;
        this.vehicleGroup.setTranslateY(this.vehicleGroup.getTranslateY() + changeY);

        if (this.vehicleGroup.getTranslateY() >= Constants.FLOOR_HEIGHT - Constants.AVATAR_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.FLOOR_HEIGHT - Constants.AVATAR_HEIGHT);
            this.velocity = 0;
        }

        if (this.vehicleGroup.getTranslateY() <= Constants.CEILING_HEIGHT) {
            this.vehicleGroup.setTranslateY(Constants.CEILING_HEIGHT);
            this.velocity = 0;
        }

        this.updateThrusterVisual();
    }

    /**
     * Simple thrust feedback for smoother feel.
     */
    private void updateThrusterVisual() {
        if (this.thrustActive) {
            this.thrusterFlame.setOpacity(0.9);
            this.thrusterFlame.setScaleX(0.9 + Math.random() * 0.3);
            this.thrusterFlame.setScaleY(1.0 + Math.random() * 0.4);
            this.thrusterFlame.setFill(
                    this.thrustActive ? Constants.AVATAR_THRUSTER_COLOR.interpolate(Color.RED, 0.25) : Constants.AVATAR_THRUSTER_COLOR
            );
        } else {
            this.thrusterFlame.setOpacity(0.0);
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
     * this method deactivates thrust when the
     * space bar is released
     * @param code
     */
    @Override
    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.SPACE) {
            this.thrustActive = false;
        }
    }

    /**
     * Getter method for the avatar vehicle type
     * @return
     */
    @Override
    public VehicleType getVehicleType() {
        return VehicleType.AVATAR;
    }
}


