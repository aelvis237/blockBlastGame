package com.example.blackblastgame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BlockBlastGame extends Application {

    private Pane root;
    private Timeline timeline;
    private Circle ball;
    private Rectangle paddle;
    private boolean ballMoving = false;
    private ObservableList<Node> blocks;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 400, 600);

        ball = new Circle(200, 500, 10, Color.BLUE);
        paddle = new Rectangle(150, 550, 100, 10);
        paddle.setFill(Color.GREEN);

        root.getChildren().addAll(ball, paddle);

        // Create blocks
        blocks = root.getChildren();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle block = new Rectangle(col * 50, row * 20, 50, 20);
                block.setFill(Color.RED);
                blocks.add(block);
            }
        }

        scene.setOnMouseClicked(event -> {
            if (!ballMoving) {
                startBallMovement();
            }
        });

        scene.setOnMouseMoved(event -> {
            if (!ballMoving) {
                double x = event.getX();
                paddle.setX(x - paddle.getWidth() / 2);
            }
        });

        primaryStage.setTitle("Block Blast Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startBallMovement() {
        ballMoving = true;
        final double[] ballSpeedX = {2};
        final double[] ballSpeedY = {-2};

        timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            ball.setCenterX(ball.getCenterX() + ballSpeedX[0]);
            ball.setCenterY(ball.getCenterY() + ballSpeedY[0]);

            if (ball.getCenterX() <= 0 || ball.getCenterX() >= root.getWidth()) {
                ballSpeedX[0] *= -1;
            }
            if (ball.getCenterY() <= 0 || ball.intersects(paddle.getBoundsInLocal())) {
                ballSpeedY[0] *= -1;
            }

            // Check collisions with blocks
            for (Node block : blocks) {
                if (ball.getBoundsInParent().intersects(block.getBoundsInParent())) {
                    ballSpeedY[0] *= -1;
                    blocks.remove(block);
                    score += 10;
                    break;
                }
            }

            // Check game over condition
            if (ball.getCenterY() >= root.getHeight()) {
                timeline.stop();
                ballMoving = false;
                System.out.println("Game Over");
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}