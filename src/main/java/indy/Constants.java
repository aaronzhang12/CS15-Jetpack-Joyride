package indy;

import javafx.scene.paint.Color;

/**
 * The constants class contains all relevant jetpackjoyride constants
 */
public class Constants {

    //app bounds
    public static final int APP_WIDTH = 1000;
    public static final int APP_HEIGHT = 800;
    public static final String GAMEPANE_COLOR = "-fx-background-color: linear-gradient(to bottom, #0b1023 0%, #0e1a3a 40%, #0a1c2b 100%);";
    public static final Color BORDER_COLOR = Color.web("#0f2f4a");
    public static final String HUD_BACKGROUND = "-fx-background-color: rgba(15, 25, 45, 0.7); -fx-background-radius: 12;";
    public static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to right, #ff9a44, #ff4b2b);"
            + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 14;";
    public static final String BUTTON_STYLE_HOVER = "-fx-background-color: linear-gradient(to right, #ffc371, #ff5f6d);"
            + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 14;";
    public static final int BORDER_HEIGHT = 150;

    //label constants
    public static final String SCORELABEL_STYLE = "-fx-font-size: 16px; -fx-text-fill: white;";
    public static final String QUEUELABEL_STYLE = "-fx-text-fill: white;";
    public static final String QUEUECONTENTSLABEL_STYLE = "-fx-text-fill: white;";
    public static final String HIGHSCORELABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 16px;";
    public static final int HIGHSCORELABEL_STARTX = 10;
    public static final int HIGHSCORELABEL_STARTY = 50;

    //game bounds
    public static final int FLOOR_HEIGHT = 650;
    public static final int CEILING_HEIGHT = 150;

    //gravity/movement parameters
    public static final double GRAVITY_SPEED = 500;
    public static final double DURATION = 16.666;
    public static final double THRUST_ACCELERATION = 1200;

    //avatar constants
    public static final int AVATAR_WIDTH = 42;
    public static final int AVATAR_HEIGHT = 42;
    public static final double AVATAR_START_X = 0.4*Constants.APP_WIDTH;
    public static final Color AVATAR_COLOR = Color.web("#4cb8c4");
    public static final Color AVATAR_BODY_ACCENT = Color.web("#1f2b44");
    public static final Color AVATAR_VISOR_COLOR = Color.web("#e0f7ff");
    public static final Color AVATAR_THRUSTER_COLOR = Color.web("#ffdd55");
    public static final double MAX_FALL_SPEED = 900;
    public static final double MAX_ASCENT_SPEED = -800;
    public static final double VELOCITY_DAMPING = 0.985;

    //zapper constants
    public static final Color ZAPPER_COLOR = Color.YELLOW;
    public static final double ZAPPER_WIDTH = 40;
    public static final double ZAPPER_MOVEMENT_SPEED = 4;

    //rocket constants
    public static final double ROCKET_MOVEMENT_SPEED = 8;
    public static final Color WARNING_SIGN_COLOR = Color.RED;
    public static final String WARNING_SIGN_FONT = "-fx-font-size: 36px; -fx-font-weight: bold";
    public static final int ROCKET_WARNING_DURATION = 180;
    public static final int ROCKET_RECTANGLE_WIDTH = 20;
    public static final double ROCKET_RECTANGLE_HEIGHT = 10;
    public static final Color ROCKET_COLOR = Color.GREY;
    public static final Color ROCKET_TIP_COLOR = Color.RED;

    //laser constants
    public static final int LASER_BLOCK_WIDTH = 20;
    public static final int LASER_BLOCK_HEIGHT = 10;
    public static final int LASER_BAR_WIDTH = 800;
    public static final int LASER_BAR_HEIGHT = 10;

    public static final int LEFT_BLOCK_X_POSITION = 90;
    public static final int RIGHT_BLOCK_X_POSITION = 900;
    public static final int LASER_BAR_X_POSITION = 100;

    public static final int BLOCK_TIMELINE_DURATION = 180;
    public static final int LASER_TIMELINE_DURATION = 120;

    public static final Color LASER_BLOCK_COLOR = Color.GREY;
    public static final Color LASER_BAR_COLOR = Color.RED;

    //powerup constants
    public static final int POWERUP_MOVEMENT_SPEED = 2;
    public static final int VEHICLE_POWERUP_WIDTH = 50;
    public static final int POWER_UP_OSCILLATION_PERIOD = 6000;
    public static final double POWERUP_BASE_INTERVAL_SECONDS = 8.0;
    public static final double POWERUP_MIN_INTERVAL_SECONDS = 4.5;

    //zoompowerup constants
    public static final int ZOOMPOWERUP_RADIUS = 20;
    public static final Color ZOOMPOWERUP_COLOR = Color.BLUE;
    public static final String ZOOMPOWERUP_TEXT = ">>";
    public static final Color ZOOMPOWERUP_TEXT_COLOR = Color.WHITE;
    public static final String ZOOMPOWERUP_TEXT_FONT = "-fx-font-weight: bold; -fx-font-size: 18px;";
    public static final int ZOOMPOWERUP_SPEEDFACTOR = 5;
    public static final int ZOOMPOWERUP_TIME = 5;

    //slowmopowerup constants
    public static final int SLOWMOPOWERUP_RADIUS = 20;
    public static final Color SLOWMOPOWERUP_COLOR = Color.YELLOWGREEN;
    public static final String SLOWMOPOWERUP_TEXT = "S";
    public static final String SLOWMOPOWERUP_STYLE = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: black;";
    public static final int SLOWMOPOWERUP_VISUAL_RADIUS = 50;
    public static final Color SLOWMOPOWERUP_VISUAL_COLOR = javafx.scene.paint.Color.rgb(
            128, 0, 128, 0.3);

    //shieldpowerup constants
    public static final int SHIELDPOWERUP_RADIUS = 20;
    public static final Color SHIELDPOWERUP_COLOR = Color.LIGHTGREEN;
    public static final Color SHIELD_COLOR = Color.DARKGREEN;
    public static final int SHIELDVISUAL_RADIUS = 50;
    public static final String SHIELDVISUAL_STYLE = "-fx-fill: rgba(0,0,255,0.3);";

    //shield label constants
    public static final String SHIELD_LABEL_TEXT = "Shields: 0";
    public static final String SHIELD_LABEL_TEXT_FONT = "-fx-text-fill: white; -fx-font-size: 16px;";
    public static final int SHIELD_LABEL_STARTX = 10;
    public static final int SHIELD_LABEL_STARTY = 110;

    //stomper constants
    public static final Color STOMPER_COLOR = Color.RED;
    public static final int STOMPER_GRAVITY = 1000;
    public static final double STOMPER_HEIGHT = 50;
    public static final int STOMPER_WIDTH = 50;
    public static final double STOMPER_THRUST = 800;
    public static final double STOMPER_JUMP_VELOCITY = -600;

    //motorcycle constants
    public static final Color MOTORCYCLE_COLOR = Color.PINK;
    public static final int MOTORCYCLE_WIDTH = 60;
    public static final int MOTORCYCLE_HEIGHT = 30;
    public static final double SHORT_JUMP_THRESHOLD = 200;
    public static final double SHORT_JUMP_VELOCITY = -400;
    public static final double LONG_JUMP_VELOCITY = -600;

    //gravitysuit constants
    public static final Color GRAVITYSUIT_COLOR = Color.WHITE;
    public static final Color GRAVITYSUIT_HAIR_COLOR = Color.CYAN;
    public static final double GRAVITYSUIT_HEIGHT = 30;
    public static final double GRAVITYSUIT_WIDTH = 20;
    public static final double GRAVITYSUIT_SPEED = 300;
    public static final double GRAVITY_TOGGLE_COOLDOWN = 200;

    //dragon constants
    public static final Color DRAGON_COLOR = Color.GREEN;
    public static final int DRAGON_WIDTH = 80;
    public static final int DRAGON_HEIGHT = 40;
    public static final int DRAGON_SEGMENT_COUNT = 5;
    public static final double DRAGON_SEGMENT_WIDTH = Constants.DRAGON_WIDTH / Constants.DRAGON_SEGMENT_COUNT;
    public static final double DRAGON_UNDULATION_AMPLITUDE = 10;
    public static final double DRAGON_UNDULATION_FREQUENCY = 2;

    //game over constants
    public static final Color GAME_OVER_TEXT_COLOR = Color.RED;
    public static final String GAME_OVER_TEXT_FONT = "Arial";
    public static final double GAME_OVER_TEXT_SIZE = 48;
    public static final String GAME_OVER_OVERLAY_COLOR = "-fx-background-color: rgba(0, 0, 0, 0.5);";

    //worldGenerator constants
    public static final double MAX_GAME_SPEED = 3.0;
    public static final double SPEED_INCREMENT = 0.00055;
    public static final int MAX_SIMULTANEOUS_OBSTACLES = 7;
    public static final double MAX_Y_AXIS_COVERAGE = 0.6;
    public static final int VEHICLE_TRANSFORM_COOLDOWN = 300;
    public static final double NON_AVATAR_DIFFICULTY_MULTIPLIER = 2.0;
    public static final double LASER_PROBABILITY = 0.06;
    public static final double MULTI_LASER_THRESHOLD = 2.0;
    public static final int MIN_OBSTACLE_SPACING = 180;
    public static final double MOTORCYCLE_ROCKET_PROBABILITY = 0.0;
    public static final double MOTORCYCLE_LOW_ZAPPER_PROBABILITY = 0.7;
}
