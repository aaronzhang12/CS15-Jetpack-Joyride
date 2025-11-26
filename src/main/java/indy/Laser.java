package indy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * The laser class extends obstacle and is modeled
 * by two setup blocks that serve as a 3 second warning
 * followed by a laser block that is active for 2 seconds.
 */
public class Laser extends Obstacle{

    private Rectangle leftBlock;
    private Rectangle rightBlock;
    private Rectangle laser;
    private Timeline blockTimeline;
    private Timeline barTimeline;
    private int yPosition;

    /**
     * The constructor calls super on the same parameters
     * and randomizes the starting y position, and then starts
     * the block setup timeline
     * @param vehicle
     * @param pane
     * @param speedMultiplier
     */
    public Laser(Vehicle vehicle, Pane pane, double speedMultiplier) {
        super(vehicle, pane, speedMultiplier);

        int max = Constants.FLOOR_HEIGHT - Constants.LASER_BLOCK_HEIGHT;
        int min = Constants.CEILING_HEIGHT;
        this.yPosition = (int) (Math.random()*(max-min)) + min;

        this.setUpBlockTimeline();
    }

    /**
     * This timeline sets up two blocks which house the laser.
     * It essentially provides a 3 second warning to the player
     * that a laser is about to appear, and when the timeline is finished
     * it calls for the actual laser bar to appear
     */
    private void setUpBlockTimeline() {
        this.leftBlock = new Rectangle(
                Constants.LEFT_BLOCK_X_POSITION,
                this.yPosition,
                Constants.LASER_BLOCK_WIDTH,
                Constants.LASER_BLOCK_HEIGHT
        );
        this.leftBlock.setFill(Constants.LASER_BLOCK_COLOR);

        this.rightBlock = new Rectangle(
                Constants.RIGHT_BLOCK_X_POSITION,
                this.yPosition,
                Constants.LASER_BLOCK_WIDTH,
                Constants.LASER_BLOCK_HEIGHT
        );
        this.rightBlock.setFill(Constants.LASER_BLOCK_COLOR);
        this.pane.getChildren().addAll(this.leftBlock, this.rightBlock);

        this.blockTimeline = new Timeline(new KeyFrame(Duration.millis(Constants.DURATION)));
        this.blockTimeline.setCycleCount(Constants.BLOCK_TIMELINE_DURATION);
        this.blockTimeline.setOnFinished((ActionEvent e) -> {
            this.setUpBarTimeline();
        });
        this.blockTimeline.play();
    }

    /**
     * The setupbar timeline initializes the actual laser
     * between the two blocks. If a collision is detected in this time,
     * the timeline stops, and everything is removed from the pane.
     * The laser lasts for 2 seconds.
     */
    private void setUpBarTimeline() {
        this.laser = new Rectangle(
                Constants.LASER_BAR_X_POSITION,
                this.yPosition-(Constants.LASER_BLOCK_HEIGHT-Constants.LASER_BAR_HEIGHT)/2,
                Constants.LASER_BAR_WIDTH,
                Constants.LASER_BAR_HEIGHT
        );
        this.laser.setFill(Constants.LASER_BAR_COLOR);

        Bloom bloom = new Bloom();
        bloom.setThreshold(1.0);
        this.laser.setEffect(bloom);

        this.pane.getChildren().add(this.laser);

        this.barTimeline = new Timeline(new KeyFrame(Duration.millis(Constants.DURATION), (ActionEvent e) -> {
            if (this.collide()) {
                this.barTimeline.stop();
                this.removeFromPane();
            }
        }));
        this.barTimeline.setCycleCount(Constants.LASER_TIMELINE_DURATION);
        this.barTimeline.setOnFinished((ActionEvent e) -> {
            this.removeFromPane();
            this.isActive = false;
        });
        this.barTimeline.play();
    }

    /**
     * The collision method detects collision against the
     * left and right block, as well as the laser bar, by checking
     * if the vehicle group's bounds intersects with each of the rectangles.
     * @return
     */
    @Override
    public boolean collide() {
        Bounds vehicleBounds = this.vehicle.getBounds();

        Boolean leftBlockCollision = this.leftBlock.getBoundsInParent().intersects(vehicleBounds);
        Boolean rightBlockCollision = this.rightBlock.getBoundsInParent().intersects(vehicleBounds);
        Boolean barCollision = this.laser != null && this.laser.getBoundsInParent().intersects(vehicleBounds);

        return leftBlockCollision || rightBlockCollision || barCollision;
    }

    /**
     * A getter method for the bounds of the laser.
     * @return
     */
    @Override
    public Bounds getBounds() {
        if (!this.isActive) {
            return null;
        }
        Shape unionShape = Shape.union(this.leftBlock, this.rightBlock);
        if (this.laser != null) {
            unionShape = Shape.union(unionShape, this.laser);
        }
        return unionShape.getBoundsInParent();
    }

    /**
     * Movement method inherited from obstacle is not used
     * because lasers are stationary on the screen
     */
    @Override
    public void move() {
    }

    /**
     * This method handles the graphical removal of all the
     * laser's components and stops the laser process at its current
     * point, whether its setting up blocks or the bar.
     */
    @Override
    public void removeFromPane() {
        if (this.blockTimeline != null) {
            this.blockTimeline.stop();
        }
        if (this.barTimeline != null) {
            this.barTimeline.stop();
        }
        this.pane.getChildren().removeAll(this.leftBlock, this.rightBlock, this.laser);
        this.isActive = false;
    }

    /**
     * Getter method for the laser obstacle type
     * @return
     */
    @Override
    public ObstacleType getObstacleType() {
        return ObstacleType.LASER;
    }

    @Override
    public void pause() {
        if (this.blockTimeline != null) {
            this.blockTimeline.pause();
        }
        if (this.barTimeline != null) {
            this.barTimeline.pause();
        }
    }

    @Override
    public void resume() {
        if (this.blockTimeline != null && this.blockTimeline.getStatus() == Timeline.Status.PAUSED) {
            this.blockTimeline.play();
        }
        if (this.barTimeline != null && this.barTimeline.getStatus() == Timeline.Status.PAUSED) {
            this.barTimeline.play();
        }
    }
}
