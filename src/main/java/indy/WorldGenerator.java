package indy;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.geometry.Bounds;

/**
 * The world generator class is the main algorithm
 * of the jetpack joyride game. It is responsible for
 * obstacle generation, adjusting game speed, and managing the
 * difficulty of the game
 */
public class WorldGenerator {
    private Pane gamePane;
    private Vehicle currentVehicle;
    private double gameSpeed;
    private int framesSinceLastObstacle;
    private int transformCooldown;
    private ArrayList<Obstacle> activeObstacles;
    private double nonAvatarTimer;
    private Game game;
    private boolean laserActive;
    private int activeLaserCount;
    private boolean speedLocked;
    private boolean recoveringFromHit;
    private double targetSpeed;
    private double recoveryRatePerSecond;

    /**
     * The constructor contains an association to
     * a game and the pane, start the obstacle frame count,
     * tracks obstacles, manages obstacle flags, and unlocks
     * the game speed.
     * @param gamePane
     * @param game
     */
    public WorldGenerator(Pane gamePane, Game game) {
        this.gamePane = gamePane;
        this.gameSpeed = 1.0;
        this.framesSinceLastObstacle = 0;
        this.transformCooldown = 0;
        this.activeObstacles = new ArrayList<>();
        this.nonAvatarTimer = 0;
        this.game = game;
        this.laserActive = false;
        this.activeLaserCount = 0;
        this.speedLocked = false;
        this.recoveringFromHit = false;
        this.targetSpeed = this.gameSpeed;
        this.recoveryRatePerSecond = 0;
    }

    /**
     * This method updates the current vehicle reference in generator,
     * allowing obstacles and generation logic to adapt to the
     * new vehicle type and its capabilities.
     * @param newVehicle
     */
    public void updateVehicle(Vehicle newVehicle) {
        this.currentVehicle = newVehicle;

        for (Obstacle obstacle : this.activeObstacles) {
            obstacle.setVehicle(newVehicle);
        }
    }

    /**
     * This method updates the world state by adjusting the game
     * speed if the player is in a special vehicle, updating existing
     * obstacles to match the new game speed, and checking if new obstacles
     * should be spawned.
     */
    public void update() {
        if (this.transformCooldown > 0) {
            this.transformCooldown--;
            if (this.transformCooldown == 0) {
                this.speedLocked = false;
            }
        }

        if (!this.speedLocked) {
            this.updateGameSpeed();
        }
        this.updateObstacles();
        this.updateObstacleGeneration();
    }

    /**
     * This method adjusts the avatar game speed over time based
     * on whether the player is in an avatar or non-avatar vehicle. Non-avatar
     * vehicles increase in difficulty/speed faster.
     */
    private void updateGameSpeed() {
        if (this.recoveringFromHit) {
            double delta = this.recoveryRatePerSecond * (Constants.DURATION / 1000.0);
            this.gameSpeed = Math.min(this.targetSpeed, this.gameSpeed + delta);
            if (this.gameSpeed >= this.targetSpeed - 0.01) {
                this.gameSpeed = this.targetSpeed;
                this.recoveringFromHit = false;
            }
            return;
        }

        if (!(this.currentVehicle.getVehicleType() == VehicleType.AVATAR)) {
            this.nonAvatarTimer += Constants.SPEED_INCREMENT * Constants.NON_AVATAR_DIFFICULTY_MULTIPLIER;
            this.gameSpeed = Math.min(Constants.MAX_GAME_SPEED, 1.0 + this.nonAvatarTimer);
        } else {
            this.gameSpeed = Math.min(Constants.MAX_GAME_SPEED, this.gameSpeed + Constants.SPEED_INCREMENT);
            this.nonAvatarTimer = 0;
        }
    }

    /**
     * Updates the speed multipliers of existing obstacles
     * and removes obstacles which have left the left side of the screen.
     * It keeps tracks of lasers activity to know if it is possible to start
     * generating other obstacles.
     */
    private void updateObstacles() {
        Iterator<Obstacle> iterator = this.activeObstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.updateSpeedMultiplier(this.gameSpeed);

            Bounds bounds = obstacle.getBounds();
            if (bounds != null && bounds.getMaxX() < 0) {
                obstacle.removeFromPane();
                iterator.remove();
                if (obstacle.getObstacleType() == ObstacleType.LASER) {
                    this.activeLaserCount--;
                    if (this.activeLaserCount == 0) {
                        this.laserActive = false;
                    }
                }
            }
        }
    }

    /**
     * This method increments the obstacle timer and determines whether to spawn an obstacle
     * based on the interval time and the total current obstacle count. It spawns a laser
     * under a certain probability, and otherwise to generate a rocket or zapper,
     * and reset the obstacle timer to 0.
     */
    private void updateObstacleGeneration() {
        this.framesSinceLastObstacle++;

        int spawnInterval = (int) Math.max(70, (150 / this.gameSpeed));

        if (this.framesSinceLastObstacle >= spawnInterval &&
                this.activeObstacles.size() < Constants.MAX_SIMULTANEOUS_OBSTACLES &&
                !this.wouldExceedYAxisCoverage()) {
            if (!this.laserActive && Math.random() < Constants.LASER_PROBABILITY * this.gameSpeed) {
                this.generateLasers();
            }
            else if (!this.laserActive) {
                this.generateRegularObstacles();
            }

            this.framesSinceLastObstacle = 0;
        }
    }

    /**
     * This method adds one or two lasers depending on whether the gamespeed
     * reaches a certain threshold, and marks the laser as active.
     */
    private void generateLasers() {
        int laserCount = this.gameSpeed >= Constants.MULTI_LASER_THRESHOLD && Math.random() < 0.5 ? 2 : 1;

        for (int i = 0; i < laserCount; i++) {
            Laser laser = new Laser(this.currentVehicle, this.gamePane, this.gameSpeed);
            this.activeObstacles.add(laser);
            this.game.addObstacle(laser);
            this.activeLaserCount++;
        }
        this.laserActive = true;
    }

    /**
     * This method generates one or more zapper/rocket if no laser are active.
     * The type and count depend on the current game speed and randomization.
     */
    private void generateRegularObstacles() {
        int obstacleCount = Math.random() < this.gameSpeed/Constants.MAX_GAME_SPEED ? 2 : 1;

        for (int i = 0; i < obstacleCount; i++) {
            Obstacle obstacle = this.generateObstacle();
            if (obstacle != null && !this.wouldOverlap(obstacle)) {
                this.activeObstacles.add(obstacle);
                this.game.addObstacle(obstacle);
            }
        }
    }

    /**
     * Generates one regular obstacle (zapper or rocket) under weighted probability.
     * If the vehicle is motorcycle, it generates motorcycle specific obstacles.
     * @return
     */
    private Obstacle generateObstacle() {
        if (this.currentVehicle.getVehicleType() ==  VehicleType.MOTORCYCLE) {
            return this.generateMotorcycleObstacle();
        }

        double randomValue = Math.random();
        if (randomValue < 0.7) {
            return new Zapper(this.currentVehicle, this.gamePane, this.gameSpeed);
        } else {
            return new Rocket(this.currentVehicle, this.gamePane, this.gameSpeed);
        }
    }

    /**
     * This method generates obstacles specifically for when the player is riding a Motorcycle.
     * Adjusts the probabilities of obstacles and positions accordingly.
     * @return
     */
    private Obstacle generateMotorcycleObstacle() {
        double randomValue = Math.random();
        if (randomValue < Constants.MOTORCYCLE_LOW_ZAPPER_PROBABILITY) {
            Zapper zapper = new Zapper(this.currentVehicle, this.gamePane, this.gameSpeed);
            return zapper;
        } else if (randomValue < (1 - Constants.MOTORCYCLE_ROCKET_PROBABILITY)) {
            return new Zapper(this.currentVehicle, this.gamePane, this.gameSpeed);
        }
        return new Rocket(this.currentVehicle, this.gamePane, this.gameSpeed);
    }

    /**
     * This method determines whether obstacles would cover to much of the y-axis
     * of the playable area. This method maintains playability by ensuring the playable
     * area is never completely blocked by obstacles.
     * @return
     */
    private boolean wouldExceedYAxisCoverage() {
        double totalCoverage = 0;
        int playableHeight = Constants.FLOOR_HEIGHT - Constants.CEILING_HEIGHT;

        for (Obstacle obstacle : this.activeObstacles) {
            Bounds bounds = obstacle.getBounds();
            if (bounds != null) {
                totalCoverage += bounds.getHeight();
            }
        }

        return (totalCoverage / playableHeight) > Constants.MAX_Y_AXIS_COVERAGE;
    }

    /**
     * This method prevents obstacles from spawning too close to each other,
     * particularly for zappers. It also checks if the new obstacle
     * would overlap graphically with existing obstacles.
     * @param newObstacle
     * @return
     */
    private boolean wouldOverlap(Obstacle newObstacle) {
        if (!(newObstacle.getObstacleType() == ObstacleType.ZAPPER)) {
            return false;
        }

        Bounds newBounds = newObstacle.getBounds();
        if (newBounds == null) return false;

        Zapper newZapper = (Zapper) newObstacle;
        double zapperLength = newZapper.getZapperLength();
        double minSpacing = zapperLength;

        double newCenterX = newBounds.getMinX() + (newBounds.getWidth() / 2);
        double newCenterY = newBounds.getMinY() + (newBounds.getHeight() / 2);

        for (Obstacle existing : this.activeObstacles) {
            if (existing.getObstacleType() == ObstacleType.ZAPPER) {
                Bounds existingBounds = existing.getBounds();
                if (existingBounds != null) {
                    double existingCenterX = existingBounds.getMinX() + (existingBounds.getWidth() / 2);
                    double existingCenterY = existingBounds.getMinY() + (existingBounds.getHeight() / 2);

                    double xDistance = Math.abs(newCenterX - existingCenterX);
                    double yDistance = Math.abs(newCenterY - existingCenterY);

                    if (xDistance < Constants.MIN_OBSTACLE_SPACING && yDistance < minSpacing) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * This method adjusts the world after a vehicle transforms.
     * It clears all active obstacles, if under a powerup transfomation, locks
     * the game speed for a cooldown allowing the player to adjust,
     * and otherwise (non-vehicle to next vehicle in queue or avatar if empty),
     * partially resets the difficulty scaling.
     * @param isPowerUpTransform
     */
    public void onVehicleTransform(boolean isPowerUpTransform) {
        for (Obstacle obstacle : this.activeObstacles) {
            obstacle.removeFromPane();
        }
        this.activeObstacles.clear();
        this.laserActive = false;
        this.activeLaserCount = 0;

        if (isPowerUpTransform) {
            this.speedLocked = true;
            this.nonAvatarTimer = this.gameSpeed - 1;
            this.transformCooldown = Constants.VEHICLE_TRANSFORM_COOLDOWN;
        }
        else {
            this.gameSpeed = 1.0 + (this.gameSpeed - 1.0)/2;
            this.speedLocked = false;
            this.transformCooldown = 0;
        }
        this.recoveringFromHit = false;
        this.targetSpeed = this.gameSpeed;
        this.recoveryRatePerSecond = 0;
    }

    /**
     * This method removes a specific obstacle graphically and
     * logically, either from leaving the screen or resetting
     * after a transformation.
     * @param obstacle
     */
    public void removeObstacle(Obstacle obstacle) {
        this.activeObstacles.remove(obstacle);
        if (obstacle.getObstacleType() == ObstacleType.LASER) {
            this.activeLaserCount--;
            if (this.activeLaserCount == 0) {
                this.laserActive = false;
            }
        }
    }

    /**
     * Getter method for the current speed multiplier.
     * @return
     */
    public double getGameSpeed() {
        return this.gameSpeed;
    }

    /**
     * Temporarily locks the game speed preventing increase.
     * Used during powerups and transformations to stabilize difficulty.
     */
    public void lockSpeed() {
        this.speedLocked = true;
    }

    /**
     * Unlocks the game speed allowing it to resume increasing
     */
    public void unlockSpeed() {
        this.speedLocked = false;
    }

    /**
     * Setter method for the game speed
     * @param speed
     */
    public void setGameSpeed(double speed) {
        this.gameSpeed = speed;
    }

    /**
     * Temporarily slows the game speed and ramps back up to the previous value.
     * @param slowdownFactor multiplier to apply to current speed
     * @param recoverSeconds time to reach prior speed
     */
    public void applyTemporarySlowdown(double slowdownFactor, double recoverSeconds) {
        double oldSpeed = this.gameSpeed;
        this.gameSpeed = Math.max(0.5, this.gameSpeed * slowdownFactor);
        this.targetSpeed = oldSpeed;
        this.recoveryRatePerSecond = (this.targetSpeed - this.gameSpeed) / Math.max(0.5, recoverSeconds);
        this.recoveringFromHit = true;
        this.speedLocked = false;
        this.transformCooldown = 0;
    }
}
