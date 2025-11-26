package indy;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * The rocket class extends obstacle and is modeled
 * by a 3 second warning sign on the right part of the
 * screen that tracks the vehicle's position, and then a
 * triangle and rectangle projectile which fires at the vehicle.
 */
public class Rocket extends Obstacle {

    private Rectangle body;
    private Polygon tip;
    private Text warningSign;
    private double xPosition;
    private double yPosition;
    private boolean warningDisplayed;
    private int warningCounter;

    /**
     * The constructor calls super on the same parameters,
     * initializes the warning sign, and marks the rocket
     * as inactive.
     * parameters
     * @param vehicle
     * @param pane
     * @param speedMultiplier
     */
    public Rocket(Vehicle vehicle, Pane pane, double speedMultiplier) {
        super(vehicle, pane, speedMultiplier);
        this.isActive = false;
        this.warningDisplayed = true;
        this.warningCounter = 0;
        this.initializeWarningSign();
    }

    /**
     * This method sets up the warning sign and matches
     * its position to the vehicle's position
     */
    private void initializeWarningSign() {
        this.warningSign = new Text("!");
        this.warningSign.setFill(Constants.WARNING_SIGN_COLOR);
        this.warningSign.setStyle(Constants.WARNING_SIGN_FONT);
        this.xPosition = Constants.APP_WIDTH - 20;
        this.yPosition = this.vehicle.getY();
        this.warningSign.setX(this.xPosition);
        this.warningSign.setY(this.yPosition);
        this.pane.getChildren().add(this.warningSign);
    }

    /**
     * This method sets up the rocket itself,
     * consisting of a rectangular body and triangular tip,
     * and marks the rocket as active
     */
    private void initializeRocket() {
        this.isActive = true;
        this.xPosition = Constants.APP_WIDTH;
        this.yPosition = this.vehicle.getY();

        this.body = new Rectangle(
                this.xPosition,
                this.yPosition,
                Constants.ROCKET_RECTANGLE_WIDTH,
                Constants.ROCKET_RECTANGLE_HEIGHT);
        this.body.setFill(Constants.ROCKET_COLOR);

        this.tip = new Polygon();
        this.tip.getPoints().addAll(
                this.xPosition, this.yPosition,
                this.xPosition - Constants.ROCKET_RECTANGLE_WIDTH / 2, this.yPosition +
                        Constants.ROCKET_RECTANGLE_HEIGHT / 2,
                this.xPosition, this.yPosition + Constants.ROCKET_RECTANGLE_HEIGHT
        );
        this.tip.setFill(Constants.ROCKET_TIP_COLOR);

        this.pane.getChildren().addAll(this.body, this.tip);
    }

    /**
     * This method controls the movement of the warning sign,
     * which follows the y position of the vehicle and also
     * the rocket, which fires at the last y position of the sign, at
     * higher speeds as the game progresses. It removes the rocket once
     * it leaves the screen on the left.
     */
    @Override
    public void move() {
        if (this.warningDisplayed) {
            this.yPosition = this.vehicle.getY();
            this.warningSign.setY(this.yPosition + Constants.ROCKET_RECTANGLE_HEIGHT / 2);

            this.warningCounter++;

            if (this.warningCounter >= Constants.ROCKET_WARNING_DURATION) {
                this.pane.getChildren().remove(this.warningSign);
                this.initializeRocket();
                this.warningDisplayed = false;
                this.isActive = true;
            }
        } else if (this.isActive) {
            this.xPosition -= Constants.ROCKET_MOVEMENT_SPEED*this.speedMultiplier;

            this.body.setX(this.xPosition);
            this.body.setY(this.yPosition);

            this.tip.getPoints().set(0, this.xPosition);
            this.tip.getPoints().set(1, this.yPosition);

            this.tip.getPoints().set(2, this.xPosition - Constants.ROCKET_RECTANGLE_WIDTH / 2);
            this.tip.getPoints().set(3, this.yPosition + Constants.ROCKET_RECTANGLE_HEIGHT / 2);

            this.tip.getPoints().set(4, this.xPosition);
            this.tip.getPoints().set(5, this.yPosition + Constants.ROCKET_RECTANGLE_HEIGHT);

            if (this.xPosition + Constants.ROCKET_RECTANGLE_WIDTH < 0) {
                this.removeFromPane();
            }
        }
    }

    /**
     * The collision method detects collision against
     * the body and tip of the rocket, by checking if
     * the vehicle group's bounds intersects with either component.
     * @return
     */
    @Override
    public boolean collide() {
        if (this.isActive) {
            Bounds vehicleBounds = this.vehicle.getBounds();
            boolean collisionWithBody = this.body.getBoundsInLocal().intersects(vehicleBounds);
            boolean collisionWithTip = this.tip.getBoundsInLocal().intersects(vehicleBounds);
            return collisionWithBody || collisionWithTip;
        }
        return false;
    }

    /**
     * A getter method for the bounds of the rocket.
     * @return
     */
    @Override
    public Bounds getBounds() {
        if (this.isActive) {
            Bounds bodyBounds = this.body.getBoundsInParent();
            Bounds tipBounds = this.tip.getBoundsInParent();
            double minX = Math.min(bodyBounds.getMinX(), tipBounds.getMinX());
            double minY = Math.min(bodyBounds.getMinY(), tipBounds.getMinY());
            double maxX = Math.max(bodyBounds.getMaxX(), tipBounds.getMaxX());
            double maxY = Math.max(bodyBounds.getMaxY(), tipBounds.getMaxY());
            return new javafx.geometry.BoundingBox(minX, minY, maxX - minX, maxY - minY);
        } else if (this.warningDisplayed) {
            return this.warningSign.getBoundsInParent();
        } else {
            return null;
        }
    }

    /**
     * This method handles the graphical removal of all
     * the rocket's components and flags the rocket as inactive.
     */
    @Override
    public void removeFromPane() {
        if (this.warningDisplayed) {
            this.pane.getChildren().remove(this.warningSign);
        }
        if (this.isActive) {
            this.pane.getChildren().removeAll(this.body, this.tip);
        }
        this.isActive = false;
        this.warningDisplayed = false;
    }

    /**
     * This method Tags the rocket for removal if the body as
     * inactive and no warning sign is being displayed.
     * @return
     */
    @Override
    public boolean shouldRemove() {
        return !this.isActive && !this.warningDisplayed;
    }

    /**
     * Getter method for the rocket obstacle type
     * @return
     */
    @Override
    public ObstacleType getObstacleType() {
        return ObstacleType.ROCKET;
    }

}
