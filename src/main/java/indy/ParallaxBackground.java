package indy;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Lightweight parallax backdrop to give forward motion even
 * when few obstacles are present.
 */
public class ParallaxBackground {
    private final Pane pane;
    private final Group layer;
    private final List<ParallaxItem> items;

    public ParallaxBackground(Pane pane) {
        this.pane = pane;
        this.layer = new Group();
        this.items = new ArrayList<>();
        this.buildLayers();
        this.pane.getChildren().add(0, this.layer);
    }

    private void buildLayers() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        // Far stars
        for (int i = 0; i < 20; i++) {
            double size = rand.nextDouble(2, 4);
            Circle star = new Circle(size, Color.web("#8ab4ff", 0.35));
            double x = rand.nextDouble(0, Constants.APP_WIDTH);
            double y = rand.nextDouble(Constants.CEILING_HEIGHT + 10, Constants.FLOOR_HEIGHT - 10);
            star.setTranslateX(x);
            star.setTranslateY(y);
            this.items.add(new ParallaxItem(star, 0.4, size + 20));
            this.layer.getChildren().add(star);
        }

        // Near streaks
        for (int i = 0; i < 12; i++) {
            double width = rand.nextDouble(80, 140);
            double height = rand.nextDouble(3, 6);
            Rectangle streak = new Rectangle(width, height);
            streak.setArcWidth(height * 2);
            streak.setArcHeight(height * 2);
            streak.setFill(Color.web("#4ef3ff", 0.25));

            double x = rand.nextDouble(0, Constants.APP_WIDTH);
            double y = rand.nextDouble(Constants.CEILING_HEIGHT + 40, Constants.FLOOR_HEIGHT - 30);
            streak.setTranslateX(x);
            streak.setTranslateY(y);
            this.items.add(new ParallaxItem(streak, 0.9, width + 80));
            this.layer.getChildren().add(streak);
        }
    }

    /**
     * Move background elements left relative to current game speed.
     */
    public void update(double timeSeconds, double gameSpeed) {
        double baseSpeed = 160 * gameSpeed;
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (ParallaxItem item : this.items) {
            double deltaX = baseSpeed * item.speedMultiplier * timeSeconds;
            item.node.setTranslateX(item.node.getTranslateX() - deltaX);
            double width = item.wrapOffset;
            if (item.node.getTranslateX() + width < 0) {
                item.node.setTranslateX(Constants.APP_WIDTH + rand.nextDouble(40, 160));
                item.node.setTranslateY(rand.nextDouble(Constants.CEILING_HEIGHT + 10, Constants.FLOOR_HEIGHT - 10));
            }
        }
    }

    private static class ParallaxItem {
        final Node node;
        final double speedMultiplier;
        final double wrapOffset;

        ParallaxItem(Node node, double speedMultiplier, double wrapOffset) {
            this.node = node;
            this.speedMultiplier = speedMultiplier;
            this.wrapOffset = wrapOffset;
        }
    }
}
