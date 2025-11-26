package indy;

import javafx.scene.layout.Pane;

/**
 * Enum representing different types of vehicle
 * transformations available in the game
 */
public enum TransformationType {
    /**
     * Transformation type and display name for "Stomper"
     */
    STOMPER("Stomper"),
    /**
     * Transformation type and display name for "Motorcycle"
     */
    MOTORCYCLE("Motorcycle"),
    /**
     * Transformation type and display name for "Gravity suit"
     */
    GRAVITY_SUIT("Gravity Suit"),
    /**
     * Transformation type and display name for "Dragon"
     */
    DRAGON("DRAGON");

    private final String displayName;

    /**
     * Creates a new Transformation type with the specified display name.
     * @param displayName
     */
    TransformationType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter method for the display name of the transformation type
     * @return
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Creates a new vehicle instance corresponding to the transformation type
     * @param pane
     * @return
     */
    public Vehicle createVehicle(Pane pane) {
        switch (this) {
            case STOMPER:
                return new Stomper(pane);
            case MOTORCYCLE:
                return new Motorcycle(pane);
            case GRAVITY_SUIT:
                return new GravitySuit(pane);
            case DRAGON:
                return new Dragon(pane);
            default:
                return new Stomper(pane);
        }
    }
}