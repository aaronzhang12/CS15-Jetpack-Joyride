package indy;

import javafx.scene.layout.Pane;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

/**
 * The zapper class extends obstacle and is modeled
 * by a rectangular bar, whose orientation is randomized
 * between horizontal, vertical, or rotating.
 */
public class Zapper extends Obstacle {
    private Rectangle rectangle;
    private String orientation;
    private RotateTransition rotateTransition;
    private int zapperLength;
    private double posX;
    private double posY;

    /**
     * The constructor calls super on the same parameters
     * and initializes the rectangle which represents the zapper.
     * @param vehicle
     * @param pane
     * @param speedMultiplier
     */
    public Zapper(Vehicle vehicle, Pane pane, double speedMultiplier) {
        super(vehicle, pane, speedMultiplier);
        this.rectangle = this.generateZapper();
        pane.getChildren().add(this.rectangle);
    }

    /**
     * A zapper generating method that randomizes the length of the
     * zapper, and creates a rectangle, randomized
     * between horizontal, vertical, or rotating orientation, at a
     * random y location.
     *
     * @return
     */
    private Rectangle generateZapper() {
        this.orientation = this.generateOrientation();
        this.zapperLength = (int) (Math.random() * 50) + 100;

        Rectangle zapper;
        double xLocation = Constants.APP_WIDTH + 100;
        double yLocation;

        switch (this.orientation) {
            case "Horizontal":
                yLocation = this.randomYPositionHorizontal();
                zapper = new Rectangle(0, 0, zapperLength, Constants.ZAPPER_WIDTH);
                zapper.setFill(Constants.ZAPPER_COLOR);
                break;
            case "Vertical":
                yLocation = this.randomYPositionVertical();
                zapper = new Rectangle(0, 0, Constants.ZAPPER_WIDTH, zapperLength);
                zapper.setFill(Constants.ZAPPER_COLOR);
                break;
            case "Rotating":
                yLocation = this.randomYPositionHorizontal();
                // rotating zapper is treated like horizontal for positioning
                zapper = new Rectangle(0, 0, zapperLength, Constants.ZAPPER_WIDTH);
                zapper.setFill(Constants.ZAPPER_COLOR);
                this.setupRotate(zapper);
                break;
            default:
                yLocation = this.randomYPositionHorizontal();
                zapper = new Rectangle(0, 0, zapperLength, Constants.ZAPPER_WIDTH);
                zapper.setFill(Constants.ZAPPER_COLOR);
                break;
        }

        this.posX = xLocation;
        this.posY = yLocation;
        zapper.setTranslateX(this.posX);
        zapper.setTranslateY(this.posY);

        return zapper;
    }

    /**
     * This method randomizes the y position of horizontal zappers
     * @return
     */
    private double randomYPositionHorizontal() {
        double minY = Constants.CEILING_HEIGHT + Constants.ZAPPER_WIDTH;
        double maxY = Constants.FLOOR_HEIGHT - 2 * Constants.ZAPPER_WIDTH;
        return Math.random() * (maxY - minY) + minY;
    }

    /**
     * This method randomizes the y position of vertical zappers
     * @return
     */
    private double randomYPositionVertical() {
        double minY = Constants.CEILING_HEIGHT + Constants.ZAPPER_WIDTH;
        double maxY = Constants.FLOOR_HEIGHT - this.zapperLength - Constants.ZAPPER_WIDTH;
        return Math.random() * (maxY - minY) + minY;
    }

    /**
     * This method sets up the rotation animation of rotating
     * zappers at a random angular speed
     * @param zapper
     */
    private void setupRotate(Rectangle zapper) {
        this.rotateTransition = new RotateTransition(Duration.seconds(3 + Math.random() * 2), zapper);
        this.rotateTransition.setByAngle(360);
        this.rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        this.rotateTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        this.rotateTransition.play();
    }

    /**
     * This helper method randomizes the orientation of the zapper
     * @return
     */
    private String generateOrientation() {
        int orientation = (int) (Math.random() * 3);
        switch (orientation) {
            case 0:
                return "Horizontal";
            case 1:
                return "Vertical";
            case 2:
                return "Rotating";
            default:
                return "Horizontal";
        }
    }

    /**
     * This method controls the movement logic of the zapper
     * and increases the speed as the game progresses
     */
    @Override
    public void move() {
        this.posX -= Constants.ZAPPER_MOVEMENT_SPEED*this.speedMultiplier;
        this.rectangle.setTranslateX(this.posX);
        // posY stays the same
    }

    /**
     * This collision method detects collision by checking
     * if the vehicle group's bounds intersects with the
     * zapper's rectangle.
     * @return
     */
    @Override
    public boolean collide() {
        return this.rectangle.getBoundsInParent().intersects(this.vehicle.getBounds());
    }

    /**
     * A getter method for the bounds of the zapper
     * @return
     */
    @Override
    public Bounds getBounds() {
        return this.rectangle.getBoundsInParent();
    }

    /**
     * This method handles the graphical removal of zapper
     * and Stops the zapper rotating animation and removes
     * the zapper's rectangle from the pane.
     */
    @Override
    public void removeFromPane() {
        if (this.rotateTransition != null) {
            this.rotateTransition.stop();
        }
        this.pane.getChildren().remove(this.rectangle);
    }

    /**
     * A getter method for the length of the zapper
     * @return
     */
    public int getZapperLength() {
        return this.zapperLength;
    }

    /**
     * Getter method for the zapper obstacle type
     * @return
     */
    @Override
    public ObstacleType getObstacleType() {
        return ObstacleType.ZAPPER;
    }
}
