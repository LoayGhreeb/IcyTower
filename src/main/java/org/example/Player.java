package org.example;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

public class Player extends Sprite {
    public final int playerSpeed = 4;
    public final int jumpHeight = 110;

    public int jumpY;
    public boolean isMovingLeft;
    public boolean isMovingRight;
    public boolean isJumping;
    public boolean isFalling;

    public Audio jumpClip;
    public Audio edgeClip;
    public int edgeTimer;

    private int leftEdgeIndex = 0;
    private int leftEdgeCounter = 0;
    private int rightEdgeIndex = 0;
    private int rightEdgeCounter = 0;
    private int walkLeftIndex = 0;
    private int walkLeftCounter = 0;
    private int walkRightIndex = 0;
    private int walkRightCounter = 0;

    private String direction;
    private Platform collidingPlatform;

    private Image jump;
    private int score;
    private int idleIndex = 0;
    private int idledNum = 0;

    private Image currentImage;
    private Image[] idle;
    private Image[] walkLeft;
    private Image[] walkRight;
    private Image[] leftEdge;
    private Image[] rightEdge;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        loadImages();
        loadSound();
        width = idle[0].getWidth(null);
        height = idle[0].getHeight(null);
        score = 0;
        x = GamePanel.getPanelWidth() / 2 - width / 2;
        y = 0;
        direction = "Idle";
    }

    private void loadImages() {
        idle = new Image[4];
        walkLeft = new Image[4];
        walkRight = new Image[4];
        leftEdge = new Image[2];
        rightEdge = new Image[2];
        jump = loadImage("/images/jump.png");

        for (int i = 0; i < 4; i++) {
            if (i < walkLeft.length)
                walkLeft[i] = loadImage("/images/walkLeft" + i + ".png");
            if (i < walkRight.length)
                walkRight[i] = loadImage("/images/walkRight" + i + ".png");
            if (i < leftEdge.length)
                leftEdge[i] = loadImage("/images/leftEdge" + i + ".png");
            if (i < rightEdge.length)
                rightEdge[i] = loadImage("/images/rightEdge" + i + ".png");
        }

        idle[0] = loadImage("/images/Idle0.png");
        idle[1] = loadImage("/images/Idle1.png");
        idle[2] = idle[0];
        idle[3] = loadImage("/images/Idle2.png");
    }

    private void loadSound() {
        jumpClip = new Audio("/audio/jumping.wav");
        edgeClip = new Audio("/audio/edge.wav");
        edgeTimer = 45;
    }

    public void move(boolean scroll) {
        if (isMovingLeft) {
            direction = "WalkLeft";
            if (x - playerSpeed <= (GamePanel.getPanelWidth() - (GamePanel.getBorderWidth() + width)) && x - playerSpeed >= GamePanel.getBorderWidth()) {
                x -= playerSpeed;
            }
            edgeTimer = 45;
        } else if (isMovingRight) {
            direction = "WalkRight";
            if (x + playerSpeed <= (GamePanel.getPanelWidth() - (GamePanel.getBorderWidth() + width)) && (x + playerSpeed) >= GamePanel.getBorderWidth()) {
                x += playerSpeed;
            }
            edgeTimer = 45;
        } else if (collidingPlatform != null && x >= collidingPlatform.right - width / 2) {
            direction = "RightEdge";
            if (edgeTimer == 45) {
                edgeClip.start();
                edgeTimer = 0;
            }
            edgeTimer++;
        } else if (collidingPlatform != null && x <= collidingPlatform.left - width / 2) {
            direction = "LeftEdge";
            if (edgeTimer == 45) {
                edgeClip.start();
                edgeTimer = 0;
            }
            edgeTimer++;
        } else if (!isJumping && !isFalling) {
            edgeTimer = 45;
            direction = "Idle";
        }

        // Jump
        if (isJumping) {
            y -= 8;
            direction = "Jump";
        }

        // Stop jumping if reached to the top of the jump, start falling
        if (y <= jumpY) {
            isJumping = false;
            isFalling = true;
        }

        //Falling
        if (isFalling) {
            y += playerSpeed;
        }

        // The player is falling down always
        if (scroll) {
            y++;
        }

        // Update the player bounders
        left = x;
        right = x + width;
        top = y;
        bottom = y + height;
    }

    public void draw(Graphics2D g) {
        switch (direction) {
            case "Idle" -> {
                idledNum++;
                if (idledNum == 15) {
                    idleIndex = ((idleIndex + 1) % idle.length);
                    idledNum = 0;
                }
                currentImage = idle[idleIndex];
            }
            case "WalkLeft" -> {
                walkLeftCounter++;
                if (walkLeftCounter == 10) {
                    walkLeftIndex = ((walkLeftIndex + 1) % walkLeft.length);
                    walkLeftCounter = 0;
                }
                currentImage = walkLeft[walkLeftIndex];
            }
            case "WalkRight" -> {
                walkRightCounter++;
                if (walkRightCounter == 10) {
                    walkRightIndex = ((walkRightIndex + 1) % walkRight.length);
                    walkRightCounter = 0;
                }
                currentImage = walkRight[walkRightIndex];
            }
            case "LeftEdge" -> {
                leftEdgeCounter++;
                if (leftEdgeCounter == 15) {
                    leftEdgeIndex = ((leftEdgeIndex + 1) % leftEdge.length);
                    leftEdgeCounter = 0;
                }
                currentImage = leftEdge[leftEdgeIndex];
            }
            case "RightEdge" -> {
                rightEdgeCounter++;
                if (rightEdgeCounter == 15) {
                    rightEdgeIndex = ((rightEdgeIndex + 1) % rightEdge.length);
                    rightEdgeCounter = 0;
                }
                currentImage = rightEdge[rightEdgeIndex];
            }
            case "Jump" ->
                    currentImage = jump;
        }
        g.drawImage(currentImage, getX(), getY(), null);
    }

    void fallOfPlatform(List<Platform> platforms) {
        boolean flag = false;
        collidingPlatform = null;
        if (!isJumping && y < GamePanel.getPanelHeight() - height) {
            for (Platform platform : platforms) {
                if (platform.collide(this)) {
                    flag = true;
                    collidingPlatform = platform;
                    score = platform.index * 10;
                    break;
                }
            }
            if (!flag) {
                isFalling = true;
            }
        }
    }

    public void reset() {
        x = GamePanel.getPanelWidth() / 2 - width / 2;
        y = 0;
        score = 0;
        isFalling = false;
        isJumping = false;
        isMovingLeft = false;
        isMovingRight = false;
        direction = "Idle";
    }

    public int getScore() {
        return score;
    }
}
