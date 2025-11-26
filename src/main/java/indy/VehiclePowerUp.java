package indy;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 * The vehicle powerup extends powerup and
 * transforms the vehicle into one of 4 special
 * vehicles
 */
public class VehiclePowerUp extends PowerUp {

    /**
     * The constructor calls super on the same parameters
     * @param pane
     */
    public VehiclePowerUp(Pane pane) {
        super(pane);
    }

    /**
     * This method creates the vehicle powerup's icon,
     * which is a square with iridescent shading
     * @return
     */
    @Override
    protected Group createIcon() {
        Rectangle square = new Rectangle(0, 0, Constants.VEHICLE_POWERUP_WIDTH, Constants.VEHICLE_POWERUP_WIDTH);
        square.setFill(this.createIridescentColor());

        Group g = new Group(square);

        g.setTranslateX(Constants.APP_WIDTH + 50);
        g.setTranslateY(Math.random() * (Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT -
                Constants.VEHICLE_POWERUP_WIDTH) + Constants.CEILING_HEIGHT);
        return g;
    }

    /**
     * Helper method for creating the iridescent color gradient
     * @return
     */
    private LinearGradient createIridescentColor() {
        return new LinearGradient(
                0, 0, 1, 1,
                true, CycleMethod.REPEAT,
                new Stop(0, Color.BLUE),
                new Stop(0.5, Color.CYAN),
                new Stop(1, Color.PURPLE)
        );
    }

    /**
     * This method randomly selects a vehicle transformationtype and
     * creates a queuedtransformation of this type, and passes it
     * into game to handle the queueing of the transformation
     * @param game
     */
    @Override
    public void activatePowerUp(Game game) {
        TransformationType type = TransformationType.values()[(int)(Math.random() *
                TransformationType.values().length)];
        game.queueTransformation(new QueuedTransformation(type));
    }
}
