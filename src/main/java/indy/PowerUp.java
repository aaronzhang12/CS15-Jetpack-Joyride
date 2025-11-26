package indy;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 * The powerup super class is modeled by an icon group
 * and oscillates across the screen.
 */
public abstract class PowerUp {

    protected Pane pane;
    protected Group icon;

    /**
     * The constructor creates the icon and adds it graphically
     * @param pane
     */
    public PowerUp(Pane pane) {
        this.pane = pane;
        this.icon = this.createIcon();
        this.pane.getChildren().add(this.icon);
    }

    /**
     * This movement method describe the oscillation
     * of the icon across the screen and removes it graphically
     * once it leaves the left side of the screen
     */
    public void move() {
        double iconHeight = this.icon.getBoundsInParent().getHeight();
        this.icon.setTranslateX(this.icon.getTranslateX() - Constants.POWERUP_MOVEMENT_SPEED);

        double amplitude = (Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT - iconHeight) / 2.0 ;
        double midpoint = Constants.CEILING_HEIGHT + amplitude;
        double period = Constants.POWER_UP_OSCILLATION_PERIOD;

        double time = System.currentTimeMillis() % period;
        double oscillation = amplitude * Math.sin(2 * Math.PI * time / period);

        this.icon.setTranslateY(midpoint + oscillation);

        if (this.icon.getTranslateX() < 0) {
            this.removeFromPane();
        }
    }

    /**
     * This collision method detects if the vehicle group's bounds
     * intersects the icon group's bounds
     * @param vehicle
     * @return
     */
    public boolean collide(Vehicle vehicle) {
        return vehicle.getBounds().intersects(this.icon.getBoundsInParent());
    }

    /**
     * This method removes the icon graphically from the pane
     */
    public void removeFromPane() {
        this.pane.getChildren().remove(this.icon);
    }

    /**
     * This method flags the icon for removal if it has
     * exited the screen on the left
     * @return
     */
    public boolean shouldRemove() {
        return this.icon.getTranslateX() + this.icon.getBoundsInParent().getWidth() < 0;
    }

    /**
     * Abstract method for how the powerup should behave
     * after being collected, to be implemented by subclasses
     * @param game
     */
    public abstract void activatePowerUp(Game game);

    /**
     * Abstract method for what the Icon group should be,
     * to be implemented by subclasses
     * @return
     */
    protected abstract Group createIcon();
}