package indy;

import javafx.scene.layout.Pane;
import javafx.geometry.Bounds;

/**
 * This is the abstract obstacle superclass from
 * which zapper, rocket, and laser inherit. It contains
 * important collision, movement, and other logical methods
 * which the subclasses will implement.
 */
public abstract class Obstacle {
    protected Vehicle vehicle;
    protected Pane pane;
    protected boolean isActive;
    protected double speedMultiplier;

    /**
     * The obstace constructor contains an association
     * with the game's vehicle, pane, and speedMultiplier,
     * of which each subclass requires access to for proper functionality
     * @param vehicle
     * @param pane
     * @param speedMultiplier
     */
    public Obstacle(Vehicle vehicle, Pane pane, double speedMultiplier) {
        this.vehicle = vehicle;
        this.pane = pane;
        this.isActive = true;
        this.speedMultiplier = speedMultiplier;
    }

    /**
     * This method allows the speedmultiplier to be updated
     * so that obstacles can reference the current speedmultiplier
     * as the game goes on and move/generate faster
     * @param newSpeedMultiplier
     */
    public void updateSpeedMultiplier(double newSpeedMultiplier) {
        this.speedMultiplier = newSpeedMultiplier;
    }

    /**
     * Abstract movement method to be implemented by subclasses
     */
    public abstract void move();

    /**
     * Abstract collision detection method to be implemented by subclasses
     * @return
     */
    public abstract boolean collide();

    /**
     * Abstract Bounds getter to be implemented by subclasses
     * @return
     */
    public abstract Bounds getBounds();

    /**
     * Abstract graphical removal method to be implemented by subclasses
     */
    public abstract void removeFromPane();

    /**
     * This method tags an obstacle for removal if it is inactive
     * @return
     */
    public boolean shouldRemove() {
        return !this.isActive;
    }

    /**
     * This method sets the current vehicle, functioning like a
     * vehicle updater method so that obstacles can reference
     * the current vehicle when detecting collision
     * @param vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Hooks for pausing/resuming obstacles with internal animations.
     */
    public void pause() { }
    public void resume() { }

    /**
     * Getter method for the type of obstacle. The implementation is
     * left to each of the subclasses.
     * @return
     */
    public abstract ObstacleType getObstacleType();
}
