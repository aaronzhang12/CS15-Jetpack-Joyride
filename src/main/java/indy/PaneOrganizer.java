package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Paneorganizer is the highest level graphical
 * class, managing the labels and gamepane
 */
public class PaneOrganizer {

    private StackPane root;
    private BorderPane layout;
    private Pane gamePane;
    private Game game;
    private Rectangle topBorder;
    private Rectangle bottomBorder;
    private Label scoreLabel;
    private Label queueLabel;
    private Label queueContentsLabel;
    private Label helperLabel;
    private Button pauseButton;

    /**
     * The constructor initializes the panes, labels,
     * buttons, and game border
     */
    public PaneOrganizer() {
        this.root = new StackPane();
        this.layout = new BorderPane();
        this.gamePane = new Pane();
        this.gamePane.setStyle(Constants.GAMEPANE_COLOR);

        this.gamePane.setFocusTraversable(true);
        this.layout.setCenter(this.gamePane);

        VBox hud = new VBox();
        hud.setSpacing(8);
        hud.setPadding(new Insets(10, 12, 10, 12));
        hud.setStyle(Constants.HUD_BACKGROUND);

        this.scoreLabel = new Label("Score: 0");
        this.scoreLabel.setStyle(Constants.SCORELABEL_STYLE);

        this.queueLabel = new Label();
        this.queueLabel.setStyle(Constants.QUEUELABEL_STYLE);

        this.queueContentsLabel = new Label();
        this.queueContentsLabel.setStyle(Constants.QUEUECONTENTSLABEL_STYLE);

        this.helperLabel = new Label("Hold SPACE to fly. Stay off the ceiling & floor!");
        this.helperLabel.setStyle(Constants.QUEUECONTENTSLABEL_STYLE);

        HBox statsRow = new HBox(12, this.scoreLabel, this.queueLabel, this.queueContentsLabel);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        hud.getChildren().addAll(statsRow, this.helperLabel);

        HBox controlBar = this.setUpButtonPane();
        controlBar.setSpacing(10);
        this.setUpPauseButton(controlBar);
        this.setUpRestartButton(controlBar);
        this.setUpQuitButton(controlBar);

        VBox overlay = new VBox();
        overlay.setAlignment(Pos.TOP_CENTER);
        overlay.setPadding(new Insets(14));
        overlay.setMouseTransparent(true);
        overlay.getChildren().add(hud);

        this.layout.setBottom(controlBar);

        this.root.getChildren().addAll(this.layout, overlay);

        this.game = new Game(this.gamePane, this.scoreLabel, this.queueLabel, this.queueContentsLabel);
        this.gamePane.requestFocus();

        this.setUpBorders();
    }

    /**
     * Set up method for the borders of the game,
     * modeled by two rectangles that represent
     * the floor and ceiling
     */
    private void setUpBorders() {
        this.topBorder = new Rectangle(
                0,
                Constants.CEILING_HEIGHT - Constants.BORDER_HEIGHT,
                Constants.APP_WIDTH,
                Constants.BORDER_HEIGHT
        );
        this.bottomBorder = new Rectangle(0, Constants.FLOOR_HEIGHT, Constants.APP_WIDTH, Constants.BORDER_HEIGHT);

        this.topBorder.setFill(Constants.BORDER_COLOR);
        this.bottomBorder.setFill(Constants.BORDER_COLOR);

        this.gamePane.getChildren().addAll(this.topBorder, this.bottomBorder);
    }

    /**
     * Set up method for the button pane
     * at the bottom of the root pane
     * @return
     */
    private HBox setUpButtonPane() {
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(10));
        buttonPane.setStyle(Constants.HUD_BACKGROUND);
        return buttonPane;
    }

    /**
     * Set up method for the quit button
     * which exits the application
     * @param buttonPane
     */
    private void setUpQuitButton(HBox buttonPane) {
        Button quitButton = new Button("Quit");
        this.styleButton(quitButton);
        quitButton.setOnAction((ActionEvent e) -> {
            System.exit(0);
        });
        buttonPane.getChildren().add(quitButton);
    }

    /**
     * Set up method for the restart button
     * @param buttonPane
     */
    private void setUpRestartButton(HBox buttonPane) {
        Button restartButton = new Button("Restart Game");
        this.styleButton(restartButton);
        restartButton.setOnAction((ActionEvent e) -> {
            this.resetGame();
        });
        buttonPane.getChildren().add(restartButton);
    }

    /**
     * Set up method for the pause button
     * @param buttonPane
     */
    private void setUpPauseButton(HBox buttonPane) {
        this.pauseButton = new Button("Pause");
        this.styleButton(this.pauseButton);
        this.pauseButton.setOnAction((ActionEvent e) -> {
            boolean paused = this.game.togglePause();
            this.pauseButton.setText(paused ? "Resume" : "Pause");
        });
        buttonPane.getChildren().add(this.pauseButton);
    }

    /**
     * Applies a consistent style to control buttons and basic hover state.
     */
    private void styleButton(Button button) {
        button.setStyle(Constants.BUTTON_STYLE);
        button.setOnMouseEntered(e -> button.setStyle(Constants.BUTTON_STYLE_HOVER));
        button.setOnMouseExited(e -> button.setStyle(Constants.BUTTON_STYLE));
    }

    /**
     * Helper method for resetting the game by
     * resetting the pane and labels, and passing these
     * updated arguments into a new game
     */
    private void resetGame() {
        this.gamePane.getChildren().clear();
        this.scoreLabel.setText("Score: 0");
        this.queueLabel.setText("");
        this.queueContentsLabel.setText("");
        if (this.pauseButton != null) {
            this.pauseButton.setText("Pause");
        }
        this.setUpBorders();
        this.game.stopTimeLine();
        this.game = new Game(this.gamePane, this.scoreLabel, this.queueLabel, this.queueContentsLabel);
        this.gamePane.requestFocus();
    }

    /**
     * Getter method for the root borderpane
     * @return
     */
    public StackPane getRoot() {
        return this.root;
    }

    /**
     * Exposes the main game pane for focus control.
     */
    public Pane getGamePane() {
        return this.gamePane;
    }
}
