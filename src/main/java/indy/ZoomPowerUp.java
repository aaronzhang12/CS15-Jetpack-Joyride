package indy;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

/**
 * The zoom powerup extends powerup and
 * provides a massive speed boost to the vehicle
 * and makes it invulnerable to obstacle collision
 */
public class ZoomPowerUp extends PowerUp {
    private Polygon zoomTriangle;

    /**
     * The constructor calls super on the same parameters
     * @param pane
     */
    public ZoomPowerUp(Pane pane) {
        super(pane);
    }

    /**
     * this method creates the zoom powerup's icon
     * which is a circle with ">>" text indicating
     * forward zoom
     * @return
     */
    @Override
    protected Group createIcon() {
        Circle c = new Circle(0, 0, Constants.ZOOMPOWERUP_RADIUS, Constants.ZOOMPOWERUP_COLOR);

        Text arrowText = new Text(Constants.ZOOMPOWERUP_TEXT);
        arrowText.setFill(Constants.ZOOMPOWERUP_TEXT_COLOR);
        arrowText.setStyle(Constants.ZOOMPOWERUP_TEXT_FONT);
        arrowText.setX(-10);
        arrowText.setY(5);

        javafx.scene.Group g = new javafx.scene.Group(c, arrowText);

        g.setTranslateX(Constants.APP_WIDTH + 50);
        double startY = Math.random() * (Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT - 40)
                + (Constants.CEILING_HEIGHT + 20);
        g.setTranslateY(startY);
        return g;
    }

    /**
     * This method adds a visual cue indicating
     * the zoom is active. It is modeled by a triangle
     * behind the vehicle and is bound to the vehicle's movements
     * @param vehicleGroup
     */
    public void addZoomVisual(Group vehicleGroup) {
        this.attachZoomVisual(vehicleGroup);
    }

    /**
     * Ensures the zoom visual is attached to the provided vehicle group,
     * reusing the same triangle if it already exists.
     * @param vehicleGroup
     */
    public void attachZoomVisual(Group vehicleGroup) {
        if (this.zoomTriangle == null) {
            this.zoomTriangle = new Polygon();
            this.zoomTriangle.getPoints().addAll(
                    -20.0, 20.0,
                    -40.0, 40.0,
                    -40.0, 0.0
            );
            this.zoomTriangle.setFill(javafx.scene.paint.Color.BLUE);
        }
        if (this.zoomTriangle.getParent() != null) {
            ((Group) this.zoomTriangle.getParent()).getChildren().remove(this.zoomTriangle);
        }
        vehicleGroup.getChildren().add(this.zoomTriangle);
    }

    /**
     * This method handles the graphical removal of
     * the zoom visual
     * @param vehicleGroup
     */
    public void removeZoomVisual(Group vehicleGroup) {
        if (this.zoomTriangle != null) {
            vehicleGroup.getChildren().remove(this.zoomTriangle);
            this.zoomTriangle = null;
        }
    }

    /**
     * This method tells game to activate the zoom
     * powerup when it is collected
     * @param game
     */
    @Override
    public void activatePowerUp(Game game) {
        game.activateZoomPowerUp();
    }
}
