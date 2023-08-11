package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;
import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_WIDTH;

public class PongController {

    private static final double SPEED = 8;
    @FXML
    private Rectangle leftRectangle;
    @FXML
    private Rectangle rightRectangle;
    @FXML
    private Circle ball;
    @FXML
    private Text scoreText1;
    @FXML
    private Text scoreText2;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int score1 = 0;
    private int score2 = 0;
    private double ballSpeed = 5d;
    private final Random random = new Random();
    private double ballCurrentX = WINDOW_WIDTH / 2;
    private double ballCurrentY = WINDOW_HEIGHT / 2;
    private double ballTargetX;
    private double ballTargetY;

    private double xSpeed;
    private double ySpeed;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleBall();
            handleRectangles();
        }
    };

    private void handleBall() {
        if (ballCurrentX < ballTargetX) {
            ballCurrentX = Math.min(ballCurrentX + xSpeed, ballTargetX);
        } else {
            ballCurrentX = Math.max(ballCurrentX - xSpeed, ballTargetX);
        }
        if (ballCurrentY < ballTargetY) {
            ballCurrentY += Math.min(ballCurrentY + ySpeed, ballTargetY);
        } else {
            ballCurrentY = Math.max(ballCurrentY - ySpeed, ballTargetY);
        }
        ball.setLayoutX(ballCurrentX);
        ball.setLayoutY(ballCurrentY);
    }

    private void handleRectangles() {
        if (upPressed) {
            handleUpKey();
        } else if (downPressed) {
            handleDownKey();
        }
        if (wPressed) {
            handleWKey();
        } else if (sPressed) {
            handleSKey();
        }
    }

    public void initialize() {
        this.ballTargetX = random.nextInt(0, (int) WINDOW_WIDTH + 1);
        double[] possibleY = {0, WINDOW_HEIGHT};
        int randomIndex = random.nextInt(possibleY.length);
        this.ballTargetY = possibleY[randomIndex];
        double deltaX = Math.abs(ballTargetX - ballCurrentX);
        double deltaY = Math.abs(ballCurrentY - ballTargetY);
        if (deltaX == deltaY) {
            xSpeed = ballSpeed;
            ySpeed = ballSpeed;
        } else if (deltaX > deltaY) {
            xSpeed = ballSpeed;
            ySpeed = deltaY / (deltaX / ballSpeed);
        } else {
            xSpeed = deltaX / (deltaY / ballSpeed);
            ySpeed = ballSpeed;
        }

        timer.start();
        ball.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        ball.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkRectangleCollision());
    }

    private void checkRectangleCollision() {
        Bounds bounds = leftRectangle.getBoundsInParent();
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent()) ||
                bounds.getMaxY() >= WINDOW_HEIGHT ||
                bounds.getMinY() <= 0;
        if (isColliding) {
            scoreText1.setText(Integer.toString(++score1));
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    private void checkBallCollision() {
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent());
        if (isColliding) {
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> upPressed = true;
            case DOWN -> downPressed = true;
            case W -> wPressed = true;
            case S -> sPressed = true;
        }
    }

    //fixme если один игрок отпускает кнопку, то сбивается движение второго
    public void handleKeyReleased(KeyEvent ignoredKey) {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    public void handleUpKey() {
        if ((rightRectangle.getLayoutY() - SPEED) <= 0) {
            rightRectangle.setLayoutY(0);
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() - SPEED);
        }
    }

    public void handleDownKey() {
        if ((rightRectangle.getLayoutY() + SPEED) >= (WINDOW_HEIGHT - rightRectangle.getHeight())) {
            rightRectangle.setLayoutY(WINDOW_HEIGHT - rightRectangle.getHeight());
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() + SPEED);
        }
    }

    public void handleWKey() {
        if ((leftRectangle.getLayoutY() - SPEED) <= 0) {
            leftRectangle.setLayoutY(0);
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() - SPEED);
        }
    }

    public void handleSKey() {
        if ((leftRectangle.getLayoutY() + SPEED) >= (WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + SPEED);
        }
    }
}