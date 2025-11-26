package indy;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * The slowmo powerup extends powerup
 * and slows down the gamespeed making it
 * easier for players to dodge obstacles
 */
public class SlowMoPowerUp extends PowerUp {
    private Circle slowMoVisual;
    private Pane gamePane;

    /**
     * The constructor calls super on the same parameters
     * and makes association to the same pane instance.
     * @param pane
     */
    public SlowMoPowerUp(Pane pane) {
        super(pane);
        this.gamePane = pane;
    }

    /**
     * This method creates the slowmo poweurp's icon,
     * which is a circle with a "S" text indicating "slowmo"
     * @return
     */
    @Override
    protected Group createIcon() {
        Circle c = new Circle(0, 0, Constants.SLOWMOPOWERUP_RADIUS, Constants.SLOWMOPOWERUP_COLOR);

        Text slowText = new Text(Constants.SLOWMOPOWERUP_TEXT);
        slowText.setStyle(Constants.SLOWMOPOWERUP_STYLE);
        slowText.setX(-5);
        slowText.setY(6);

        Group g = new Group(c, slowText);

        g.setTranslateX(Constants.APP_WIDTH + 50);
        double startY = Math.random() * (Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT - 40)
                + (Constants.CEILING_HEIGHT + 20);
        g.setTranslateY(startY);
        return g;
    }

    /**
     * This method adds a visual cue that
     * the gamespeed is being slowed down. It is modeled
     * by a circle that surrounds the vehicle and is bound
     * t the vehicle's movements
     * @param vehicleGroup
     */
    public void addSlowMoVisual(Group vehicleGroup) {
        if (this.slowMoVisual == null) {
            this.slowMoVisual = new Circle();
            this.slowMoVisual.setRadius(Constants.SLOWMOPOWERUP_VISUAL_RADIUS);
            this.slowMoVisual.setFill(Constants.SLOWMOPOWERUP_VISUAL_COLOR);
            this.gamePane.getChildren().add(this.slowMoVisual);

            this.bindToVehicle(vehicleGroup);
        }
    }

    /**
     * Rebind the slow-mo visual to a new vehicle group if the player transforms.
     */
    public void rebindSlowMoVisual(Group vehicleGroup) {
        if (this.slowMoVisual == null) {
            this.addSlowMoVisual(vehicleGroup);
            return;
        }
        this.slowMoVisual.translateXProperty().unbind();
        this.slowMoVisual.translateYProperty().unbind();
        this.bindToVehicle(vehicleGroup);
    }

    private void bindToVehicle(Group vehicleGroup) {
        this.slowMoVisual.translateXProperty().bind(vehicleGroup.translateXProperty()
                .add(Constants.VEHICLE_POWERUP_WIDTH/2.0));
        this.slowMoVisual.translateYProperty().bind(vehicleGroup.translateYProperty()
                .add(Constants.VEHICLE_POWERUP_WIDTH/2.0));
    }

    /**
     * This method handles the graphical removal of
     * the slowmo visual
     */
    public void removeSlowMoVisual() {
        if (this.slowMoVisual != null) {

            this.slowMoVisual.translateXProperty().unbind();
            this.slowMoVisual.translateYProperty().unbind();

            this.gamePane.getChildren().remove(this.slowMoVisual);
            this.slowMoVisual = null;
        }
    }

    /**
     * This method tells game to activate the slowmo powerup
     * when it is collected
     * @param game
     */
    @Override
    public void activatePowerUp(Game game) {
        game.activateSlowMoPowerUp();
    }
}
