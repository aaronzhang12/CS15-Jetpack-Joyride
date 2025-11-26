package indy;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.layout.Pane;
import javafx.scene.Group;

/**
 * The shield powerup extends powerup and
 * provides a one-use protective barrier
 * for the vehicle
 */
public class ShieldPowerUp extends PowerUp {
    private Circle shieldVisual;
    private Pane gamePane;

    /**
     * The constructor calls super on the same parameters
     * and makes association to the same pane instance.
     * @param pane
     */
    public ShieldPowerUp(Pane pane) {
        super(pane);
        this.gamePane = pane;
    }

    /**
     * This method creates the shield powerup's icon,
     * which is a circle with a square and triangle that replicate
     * a shield's crest.
     * @return
     */
    @Override
    protected Group createIcon() {
        Circle base = new Circle(Constants.SHIELDPOWERUP_RADIUS, Constants.SHIELDPOWERUP_COLOR);
        javafx.scene.shape.Rectangle sq = new javafx.scene.shape.Rectangle(-5, -5, 10, 10);
        sq.setFill(Constants.SHIELD_COLOR);

        Polygon tri = new Polygon();
        tri.getPoints().addAll(
                -5.0, 5.0,
                0.0, 15.0,
                5.0, 5.0
        );
        tri.setFill(Constants.SHIELD_COLOR);

        Group g = new Group(base, sq, tri);
        g.setTranslateX(Constants.APP_WIDTH);
        g.setTranslateY(Math.random() * (Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT - 40) +
                (Constants.CEILING_HEIGHT + 20));

        return g;
    }

    /**
     * This method adds a visual cue indicating that
     * a shield is active. It is modeled by a circle that
     * surrounds the vehicle and is bound to the vehicle's movements
     * @param vehicleGroup
     */

    public void addShieldVisual(Group vehicleGroup) {
        if (this.shieldVisual == null) {
            this.shieldVisual = new Circle();
            this.shieldVisual.setRadius(Constants.SHIELDVISUAL_RADIUS);
            this.shieldVisual.setStyle(Constants.SHIELDVISUAL_STYLE);
            this.gamePane.getChildren().add(this.shieldVisual);

            this.bindToVehicle(vehicleGroup);
        }
    }

    /**
     * Rebind the existing shield visual to a new vehicle group
     * (used when transforming while a shield is active).
     * @param vehicleGroup the group to follow
     */
    public void rebindShieldVisual(Group vehicleGroup) {
        if (this.shieldVisual == null) {
            this.addShieldVisual(vehicleGroup);
            return;
        }
        this.shieldVisual.translateXProperty().unbind();
        this.shieldVisual.translateYProperty().unbind();
        this.bindToVehicle(vehicleGroup);
    }

    private void bindToVehicle(Group vehicleGroup) {
        this.shieldVisual.translateXProperty().bind(vehicleGroup.translateXProperty()
                .add(Constants.VEHICLE_POWERUP_WIDTH / 2.0));
        this.shieldVisual.translateYProperty().bind(vehicleGroup.translateYProperty()
                .add(Constants.VEHICLE_POWERUP_WIDTH / 2.0));
    }

    /**
     * This method handles the graphical removal of the
     * shield visual
     */
    public void removeShieldVisual() {
        if (this.shieldVisual != null) {

            this.shieldVisual.translateXProperty().unbind();
            this.shieldVisual.translateYProperty().unbind();


            this.gamePane.getChildren().remove(this.shieldVisual);
            this.shieldVisual = null;
        }
    }

    /**
     * This method tells game to activate the shield powerup
     * when it is collected
     * @param game
     */
    @Override
    public void activatePowerUp(Game game) {
        game.activateShieldPowerUp(this);
    }
}
