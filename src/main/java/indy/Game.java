package indy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import java.io.FileWriter;
import java.io.FileReader;

/**
 * the game class is the highest level logical class,
 * handling updating obstacles, vehicle interactions with
 * obstacles and powerups, setting up ui elements
 */
public class Game {

    private Timeline timeline;
    private Pane gamePane;
    private Vehicle currentVehicle;
    private Queue<QueuedTransformation> transformationQueue;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerUp> powerUps;
    private WorldGenerator worldGenerator;
    private ParallaxBackground parallaxBackground;
    private double powerUpTimer;
    private boolean isGameOver;
    private double score;
    private Label scoreLabel;
    private Label queueLabel;
    private Label queueContentsLabel;
    private int shieldCount = 0;
    private Label shieldLabel;
    private boolean invincible = false;
    private Timeline zoomTimeline;
    private double oldGameSpeed;
    private boolean zoomActive = false;
    private int highScore = 0;
    private Label highScoreLabel;
    private Timeline slowMoTimeline;
    private boolean slowMoActive = false;
    private double oldGameSpeedForSlowMo;
    private ShieldPowerUp activeShieldPowerUp;
    private ZoomPowerUp activeZoomPowerUp;
    private SlowMoPowerUp activeSlowMoPowerUp;
    private boolean paused;

    /**
     * The constructor sets up the current vehicle, initially an avatar,
     * initializes the obstacle and powerup arrays, loads and displays
     * the high score, sets up the game timeline, assigns event handlers
     * for user input, and initializes ui elements like the score, queue,
     * and shield labels
     * @param gamePane
     * @param scoreLabel
     * @param queueLabel
     * @param queueContentsLabel
     */
    public Game(Pane gamePane, Label scoreLabel, Label queueLabel, Label queueContentsLabel) {
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;
        this.queueLabel = queueLabel;
        this.queueContentsLabel = queueContentsLabel;
        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();

        this.currentVehicle = new Avatar(this.gamePane);

        this.transformationQueue = new LinkedList<>();

        this.obstacles = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.isGameOver = false;
        this.score = 0;

        this.worldGenerator = new WorldGenerator(gamePane, this);
        this.parallaxBackground = new ParallaxBackground(gamePane);
        this.worldGenerator.updateVehicle(this.currentVehicle);

        this.shieldLabel = new Label(Constants.SHIELD_LABEL_TEXT);
        this.shieldLabel.setStyle(Constants.SHIELD_LABEL_TEXT_FONT);

        this.shieldLabel.setTranslateX(Constants.SHIELD_LABEL_STARTX);
        this.shieldLabel.setTranslateY(Constants.SHIELD_LABEL_STARTY);
        this.gamePane.getChildren().add(this.shieldLabel);

        this.loadGameData();
        this.setupHighScoreUI();
        this.highScoreLabel.setText("High Score: " + this.highScore);

        this.setupTimeline();
        this.setupKeyHandlers();
        this.updateQueueLabels();
        this.powerUpTimer = 0;
        this.paused = false;
    }

    /**
     * Updatess the on-screen label that shows hw many transformations
     * are in the queue and which type they are. If empty, it
     * displays none, and shows up to 3
     */
    private void updateQueueLabels() {
        this.queueLabel.setText("Transformations: " + this.transformationQueue.size());

        StringBuilder queueContents = new StringBuilder("Next: ");
        if (this.transformationQueue.isEmpty()) {
            queueContents.append("None");
        } else {
            Iterator<QueuedTransformation> iterator = this.transformationQueue.iterator();
            int count = 0;
            while (iterator.hasNext() && count < 3) {
                if (count > 0) queueContents.append(" → ");
                queueContents.append(iterator.next().getType().getDisplayName());
                count++;
            }
            if (this.transformationQueue.size() > 3) {
                queueContents.append(" ...");
            }
        }
        this.queueContentsLabel.setText(queueContents.toString());
    }

    /**
     * Adds a transformation to the queue. if the current vehicle is an Avatar,
     * it transforms immediately. Otherwise, it stores the transformation for later use.
     * @param transformation
     */
    public void queueTransformation(QueuedTransformation transformation) {
        if (this.currentVehicle.getVehicleType() == VehicleType.AVATAR) {
            this.transformVehicle(transformation.createVehicle(this.gamePane));
        } else {
            this.transformationQueue.offer(transformation);
            this.updateQueueLabels();
        }
    }

    /**
     * Set uyp method for the game timeline and calls update
     * to advance the game state, and set to run indefinitely
     * until the game is over or user manually stops
     */
    private void setupTimeline() {
        double timeSeconds = Constants.DURATION / 1000.0;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(Constants.DURATION), (ActionEvent e) -> {
            this.update(timeSeconds);
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    /**
     * advances the game state, including
     * moving the vehicle, updating the world generator and spawning new
     * obstacles/powerups, checking for collisions, and updating the score,
     * all of which are delegated to helper methods
     * @param timeSeconds
     */
    private void update(double timeSeconds) {
        if (this.isGameOver) {
            return;
        }

        this.currentVehicle.move(timeSeconds);
        this.worldGenerator.update();
        this.updateObstacles();
        this.updatePowerUps();
        this.checkCollisions();
        this.maybeAddPowerUp(timeSeconds);
        this.updateScore(timeSeconds);
        this.parallaxBackground.update(timeSeconds, this.worldGenerator.getGameSpeed());
    }

    /**
     * increments the score based on the current game speed and elapsed
     * time, updates the score label
     * @param timeSeconds
     */
    private void updateScore(double timeSeconds) {
        double speedMultiplier = this.worldGenerator.getGameSpeed();
        this.score += Constants.APP_WIDTH * timeSeconds * speedMultiplier;
        this.scoreLabel.setText("Score: " + (int) this.score);
    }

    /**
     * This method registers a newly created obstacle, adding it to the obstacle list.
     * @param obstacle
     */
    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
    }

    private void updateObstacles() {
        Iterator<Obstacle> iterator = this.obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.move();

            if (obstacle.shouldRemove()) {
                obstacle.removeFromPane();
                iterator.remove();
                this.worldGenerator.removeObstacle(obstacle);
            }
        }
    }

    /**
     * This method periodically attempts to add a new power-up
     * to the game based on a fixed generation frequency.
     */
    private void maybeAddPowerUp(double timeSeconds) {
        this.powerUpTimer += timeSeconds;
        double interval = Math.max(
                Constants.POWERUP_MIN_INTERVAL_SECONDS,
                Constants.POWERUP_BASE_INTERVAL_SECONDS - (this.worldGenerator.getGameSpeed() * 0.6)
        );

        if (this.powerUpTimer >= interval) {
            this.addPowerUp();
            this.powerUpTimer = 0;
        }
    }

    /**
     * This method randomizes the powerup type with weighted probability
     * (between vehicle, shield, zoom, slowmo) and adds it to the
     * powerups list
     */
    private void addPowerUp() {
        int rand = (int) (Math.random()*10);
        PowerUp p;
        switch (rand) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                p = new VehiclePowerUp(this.gamePane);
                break;
            case 5:
            case 6:
                p = new ShieldPowerUp(this.gamePane);
                break;
            case 7:
            case 8:
                p = new ZoomPowerUp(this.gamePane);
                break;
            case 9:
                p = new SlowMoPowerUp(this.gamePane);
                break;
            default:
                p = new VehiclePowerUp(this.gamePane);
                break;
        }
        this.powerUps.add(p);
    }

    /**
     * This method moves existing powerups across the screen and if
     * they are flagged for removal, are removed from the pane
     */
    private void updatePowerUps() {
        Iterator<PowerUp> iterator = this.powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.move();

            if (powerUp.shouldRemove()) {
                powerUp.removeFromPane();
                iterator.remove();
            }
        }
    }

    /**
     * This method checks collision between the vehicle and obstacles/powerups on screen.
     * If collision with obstacle, with either consume a shield or cause vehicle death.
     * If collision with powerup, removes powerup from pane and activates its ability.
     */
    private void checkCollisions() {
        for (Obstacle obstacle : this.obstacles) {
            if (!this.invincible) {
                if (obstacle.collide()) {
                    if (this.shieldCount > 0) {
                        obstacle.removeFromPane();
                        this.obstacles.remove(obstacle);
                        this.shieldCount--;
                        this.updateShieldDisplay();
                        if (this.shieldCount == 0) {
                            this.removeShieldVisual();
                        }
                        if (!this.slowMoActive) {
                            this.worldGenerator.applyTemporarySlowdown(0.55, 2.5);
                        }
                    } else {
                        this.handleVehicleDeath();
                    }
                    break;
                }
            }
        }

        Iterator<PowerUp> iterator = this.powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp.collide(this.currentVehicle)) {
                boolean isSlowMoPickup = powerUp instanceof SlowMoPowerUp;
                powerUp.activatePowerUp(this);
                powerUp.removeFromPane();
                iterator.remove();
                if (this.currentVehicle.getVehicleType() != VehicleType.AVATAR && !this.slowMoActive && !isSlowMoPickup) {
                    this.worldGenerator.applyTemporarySlowdown(0.85, 1.5);
                }
            }
        }
    }

    /**
     * This method transforms the current vehicle into a new one, preserving position.
     * It also clears all obstacles graphically and logically, and rests
     * the world generator with a reference to the updated vehicle
     *
     * @param newVehicle
     */
    private void transformVehicle(Vehicle newVehicle) {
        boolean isPowerUpTransform = this.currentVehicle.getVehicleType() == VehicleType.AVATAR ||
                (!(this.currentVehicle.getVehicleType() == VehicleType.AVATAR) &&
                        !(newVehicle.getVehicleType() == VehicleType.AVATAR));

        Vehicle oldVehicle = this.currentVehicle;
        this.detachActiveVisuals(oldVehicle);

        this.currentVehicle.removeFromPane();
        double x = this.currentVehicle.getX();
        double y = this.currentVehicle.getY();
        this.currentVehicle = newVehicle;
        this.currentVehicle.setPosition(x, y);

        for (Obstacle obstacle: this.obstacles) {
            obstacle.removeFromPane();
        }
        this.obstacles.clear();

        this.worldGenerator.updateVehicle(this.currentVehicle);
        this.worldGenerator.onVehicleTransform(isPowerUpTransform);

        this.setupKeyHandlers();
        this.updateQueueLabels();
        this.reattachActiveVisuals();
    }

    /**
     * This method handles the logic when a current vehicle dies
     * or hits an obstacle while unshielded. If the current vehicle is an
     * avatar, it triggers game over, and if not the avatar,
     * it transforms into the next queued vehicle in the queue and if the queue is
     * empty, back into the avatar.
     */
    private void handleVehicleDeath() {
        if (this.currentVehicle.getVehicleType() == VehicleType.AVATAR) {
            this.stopGame();
        } else {
            for (Obstacle obstacle: this.obstacles) {
                obstacle.removeFromPane();
            }
            this.obstacles.clear();

            double x = this.currentVehicle.getX();
            double y = this.currentVehicle.getY();
            Vehicle oldVehicle = this.currentVehicle;
            this.currentVehicle.removeFromPane();

            if (!this.transformationQueue.isEmpty()) {
                QueuedTransformation next = this.transformationQueue.poll();
                this.currentVehicle = next.createVehicle(this.gamePane);
            } else {
                this.currentVehicle = new Avatar(this.gamePane);
            }

            this.currentVehicle.setPosition(x,y);
            this.worldGenerator.updateVehicle(this.currentVehicle);
            this.worldGenerator.onVehicleTransform(false);

            this.setupKeyHandlers();
            this.updateQueueLabels();
            this.detachActiveVisuals(oldVehicle);
            this.reattachActiveVisuals();
        }
    }

    private void detachActiveVisuals(Vehicle oldVehicle) {
        if (oldVehicle == null) {
            return;
        }
        if (this.activeZoomPowerUp != null) {
            this.activeZoomPowerUp.removeZoomVisual(oldVehicle.vehicleGroup);
        }
        if (this.activeSlowMoPowerUp != null) {
            this.activeSlowMoPowerUp.removeSlowMoVisual();
        }
        if (this.activeShieldPowerUp != null) {
            this.activeShieldPowerUp.removeShieldVisual();
        }
    }

    private void reattachActiveVisuals() {
        if (this.activeShieldPowerUp != null) {
            this.activeShieldPowerUp.rebindShieldVisual(this.currentVehicle.vehicleGroup);
        }
        if (this.zoomActive && this.activeZoomPowerUp != null) {
            this.activeZoomPowerUp.attachZoomVisual(this.currentVehicle.vehicleGroup);
        }
        if (this.slowMoActive && this.activeSlowMoPowerUp != null) {
            this.activeSlowMoPowerUp.rebindSlowMoVisual(this.currentVehicle.vehicleGroup);
        }
    }

    /**
     * Set up method for the key handlers to control the current vehicle.
     * They allow the player to control (jump, thrust, etc) the vehicle
     */
    private void setupKeyHandlers() {
        this.gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.P) {
                this.togglePause();
                event.consume();
                return;
            }
            if (!this.isGameOver && !this.paused) {
                this.currentVehicle.onKeyPressed(event.getCode());
            }
        });
        this.gamePane.setOnKeyReleased(event -> {
            if (!this.isGameOver && !this.paused) {
                this.currentVehicle.onKeyReleased(event.getCode());
            }
        });
        this.gamePane.requestFocus();
    }

    /**
     * This method stops the main game's timeline
     */
    public void stopTimeLine() {
        this.timeline.stop();
    }

    /**
     * Toggles game pause state and pauses/resumes timelines/obstacles.
     * @return true if now paused
     */
    public boolean togglePause() {
        if (this.paused) {
            this.timeline.play();
            this.resumeActiveTimers();
            this.resumeObstacles();
        } else {
            this.timeline.pause();
            this.pauseActiveTimers();
            this.pauseObstacles();
        }
        this.paused = !this.paused;
        if (!this.paused) {
            this.gamePane.requestFocus();
        }
        return this.paused;
    }

    private void pauseActiveTimers() {
        if (this.zoomTimeline != null) {
            this.zoomTimeline.pause();
        }
        if (this.slowMoTimeline != null) {
            this.slowMoTimeline.pause();
        }
    }

    private void resumeActiveTimers() {
        if (this.zoomActive && this.zoomTimeline != null) {
            this.zoomTimeline.play();
        }
        if (this.slowMoActive && this.slowMoTimeline != null) {
            this.slowMoTimeline.play();
        }
    }

    private void pauseObstacles() {
        for (Obstacle obstacle : this.obstacles) {
            obstacle.pause();
        }
    }

    private void resumeObstacles() {
        for (Obstacle obstacle : this.obstacles) {
            obstacle.resume();
        }
    }

    /**
     * This method ends the current game, stops all timelines,
     * saves the high score if a new one is achieved, clears all
     * obstacles and powerups, and displays a game over overlay
     */
    private void stopGame() {
        if (this.timeline != null) {
            this.stopTimeLine();
        }
        this.isGameOver = true;

        if ((int) this.score > this.highScore) {
            this.highScore = (int)this.score;
            this.highScoreLabel.setText("High Score: " + this.highScore);
            this.saveGameData();
        }

        for (Obstacle obstacle : this.obstacles) {
            obstacle.removeFromPane();
        }
        this.obstacles.clear();

        for (PowerUp powerUp : this.powerUps) {
            powerUp.removeFromPane();
        }
        this.powerUps.clear();

        this.displayGameOver();
    }

    /**
     * Displays a game over message and final score that
     * covers the entire game pane
     */
    private void displayGameOver() {
        Label gameOverLabel = new Label("Game Over! Final score: " + (int) this.score);
        gameOverLabel.setTextFill(Constants.GAME_OVER_TEXT_COLOR);
        gameOverLabel.setFont(new Font(Constants.GAME_OVER_TEXT_FONT, Constants.GAME_OVER_TEXT_SIZE));

        StackPane overlay = new StackPane(gameOverLabel);
        overlay.setPrefSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        overlay.setStyle(Constants.GAME_OVER_OVERLAY_COLOR);

        this.gamePane.getChildren().add(overlay);
    }

    /**
     * This method activates a shield powerup by incrementing the shield
     * count and adding a visual shield around the current vehicle
     * @param powerUp
     */
    public void activateShieldPowerUp(ShieldPowerUp powerUp) {
        this.shieldCount++;
        this.updateShieldDisplay();

        if (this.shieldCount == 1) {
            this.activeShieldPowerUp = powerUp;
            this.activeShieldPowerUp.addShieldVisual(this.currentVehicle.vehicleGroup);
        }
    }

    /**
     * This method removes the shield visual if it exists
     */
    private void removeShieldVisual() {
        if (this.activeShieldPowerUp != null) {
            this.activeShieldPowerUp.removeShieldVisual();
            this.activeShieldPowerUp = null;
        }
    }

    /**
     * This method updates the shield label to show
     * the current number of shields
     */
    private void updateShieldDisplay() {
        this.shieldLabel.setText("Shields: " + this.shieldCount);
    }

    /**
     * This method activates the zoom powerup, greatly increasing
     * the game speed and providing invincibility during this period.
     * Also adds the zoom visual behind the vehicle.
     */
    public void activateZoomPowerUp() {
        if (this.zoomActive) {
            return;
        }

        this.zoomActive = true;
        this.invincible = true;
        this.oldGameSpeed = this.worldGenerator.getGameSpeed();
        this.worldGenerator.lockSpeed();
        this.worldGenerator.setGameSpeed(this.oldGameSpeed * Constants.ZOOMPOWERUP_SPEEDFACTOR);

        this.activeZoomPowerUp = new ZoomPowerUp(this.gamePane);
        this.activeZoomPowerUp.addZoomVisual(this.currentVehicle.vehicleGroup);

        this.zoomTimeline = new Timeline(new KeyFrame(Duration.seconds(Constants.ZOOMPOWERUP_TIME), e -> {
            this.worldGenerator.setGameSpeed(this.oldGameSpeed);
            this.worldGenerator.unlockSpeed();
            if (this.activeZoomPowerUp!= null) {
                this.activeZoomPowerUp.removeZoomVisual(this.currentVehicle.vehicleGroup);
            }
            this.invincible = false;
            this.zoomActive = false;
        }));
        this.zoomTimeline.play();
    }

    /**
     * This method activates the slow-motion pwoerup, reducing the game speed.
     * It also adds a slow mo visual indicating the vehicle is in slowmo.
     */
    public void activateSlowMoPowerUp() {
        if (this.slowMoActive) {
            return;
        }

        this.slowMoActive = true;
        this.oldGameSpeedForSlowMo = this.worldGenerator.getGameSpeed();

        this.worldGenerator.lockSpeed();
        this.worldGenerator.setGameSpeed(this.oldGameSpeedForSlowMo * 0.5);

        this.activeSlowMoPowerUp = new SlowMoPowerUp(this.gamePane);
        this.activeSlowMoPowerUp.addSlowMoVisual(this.currentVehicle.vehicleGroup);

        this.slowMoTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            this.worldGenerator.setGameSpeed(this.oldGameSpeedForSlowMo);
            this.worldGenerator.unlockSpeed();
            if (this.activeSlowMoPowerUp != null) {
                this.activeSlowMoPowerUp.removeSlowMoVisual();
                this.activeSlowMoPowerUp = null;
            }
            this.slowMoActive = false;
        }));
        this.slowMoTimeline.play();
    }

    /**
     *This methods sets up the high score display label.
     * It is called during game initialization and whenever
     * a high score is achieved.
     */
    private void setupHighScoreUI() {
        this.highScoreLabel = new Label("High Score: " + this.highScore);
        this.highScoreLabel.setStyle(Constants.HIGHSCORELABEL_STYLE);
        this.highScoreLabel.setLayoutX(Constants.HIGHSCORELABEL_STARTX);
        this.highScoreLabel.setLayoutY(Constants.HIGHSCORELABEL_STARTY);
        this.gamePane.getChildren().add(this.highScoreLabel);
    }

    /**
     * This method writes the current high score to a txt file.
     * It is called when game ends if a new high score is achieved.
     */
    private void saveGameData() {
        String filename = "jetpackjoyride.txt";
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(String.valueOf(this.highScore));
        }
        catch (Exception e) {
            System.err.println("Error saving game data:" + e.getMessage());
        }
    }

    /**
     * This method reads the high score from a file and initializes it
     * during the game construction. If no file exists, it sets the
     * default score to 0.
     */
    private void loadGameData() {
        String filename = "jetpackjoyride.txt";
        File file = new File(filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
                this.saveGameData();
            } catch (Exception e) {
                System.err.println("Error creating game data file: " + e.getMessage());
            }
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                sb.append((char) character);
            }
            String content = sb.toString().trim();
            if (!content.isEmpty()) {
                this.highScore = Integer.parseInt(content);
            }
        } catch (Exception e) {
            System.err.println("Error loading game data: " + e.getMessage());
        }
    }
}
