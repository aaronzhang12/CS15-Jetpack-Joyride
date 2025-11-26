package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.geometry.Bounds;
import javafx.scene.Group;

/**
 * This is the abstract vehicle superclass of which
 * avatar, stomper, motorcycle, dragon, and
 * gravitysuit extend. It contains visual, movement, and
 * logical variables/methods which the sub classes will
 * implement.
 */
public abstract class Vehicle {
    protected Pane pane;
    protected double velocity;
    protected boolean thrustActive;
    protected Group vehicleGroup;

    /**
     * The vehicle constructor contains an association
     * to the gamePane in game, and creates a vehicleGroup
     * which is a collection of polygons modelling each vehicle
     * @param pane
     */
    public Vehicle(Pane pane) {
        this.pane = pane;
        this.velocity = 0;
        this.thrustActive = false;
        this.vehicleGroup = new Group();
        this.pane.getChildren().add(this.vehicleGroup);
    }

    /**
     * Abstract movement method to be implemented by subclasses
     * @param timeSeconds
     */
    public abstract void move(double timeSeconds);

    /**
     * Abstract keypress method to be implemented by vehicle subclasses
     * @param code
     */
    public abstract void onKeyPressed(KeyCode code);

    /**
     * Abstract keyrelease method to be implemented by vehicle subclasses
     * @param code
     */
    public abstract void onKeyReleased(KeyCode code);

    /**
     * Getter method for the bounds of the entire vehicle for collision detection.
     */
    public Bounds getBounds() {
        return this.vehicleGroup.getBoundsInParent();
    }

    /**
     * Removes the vehicle's Group from the Pane.
     */
    public void removeFromPane() {
        this.pane.getChildren().remove(this.vehicleGroup);
    }

    /**
     * Getter method for the X position of the vehicle.
     */
    public double getX() {
        return this.vehicleGroup.getTranslateX();
    }

    /**
     * Getter method for the Y position of the vehicle.
     */
    public double getY() {
        return this.vehicleGroup.getTranslateY();
    }

    /**
     * Position setter method for the vehicle.
     *
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        this.vehicleGroup.setTranslateX(x);
        this.vehicleGroup.setTranslateY(y);
    }

    /**
     * Getter method for the type of vehicle. The implementation is
     * left to the subclasses.
     * @return
     */
    public abstract VehicleType getVehicleType();
}
