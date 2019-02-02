package com.pcchin.uiplayground.pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;

import java.util.Random;

class PongBall extends GameObject {
    // Velocity of game character (pixel/millisecond)
    private float VELOCITY;

    private int movingVectorX = 10;
    private int movingVectorY = 5;

    private long lastDrawNanoTime =-1;
    private long lastVectorXChange;
    boolean enabled;
    private boolean isReal;

    private Paddle paddleL;
    private Paddle paddleR;
    private Bitmap pongImg;

    private PongSurfaceView pongSurfaceView;

    PongBall(@NonNull PongSurfaceView pongSurfaceView, int x, int y, Paddle paddleL,
             Paddle paddleR, Bitmap pongImg, boolean isReal) {
        super(pongImg, x, y);
        this.pongImg = pongImg;
        this.pongSurfaceView = pongSurfaceView;
        this.paddleL = paddleL;
        this.paddleR = paddleR;
        this.isReal = isReal;
        VELOCITY = 0.4f;
        enabled = true;
    }

    void update() {
        if (enabled) {
            // Current time in nanoseconds
            long now = System.nanoTime();

            // Never once did draw.
            if (lastDrawNanoTime == -1) {
                lastDrawNanoTime = now;
            }
            // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
            int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);

            // Distance moves
            float distance = VELOCITY * deltaTime;

            double movingVectorLength = Math.sqrt(movingVectorX * movingVectorX + movingVectorY * movingVectorY);

            // Calculate the new position of the game character.
            this.x = x + (int) (distance * movingVectorX / movingVectorLength);
            this.y = y + (int) (distance * movingVectorY / movingVectorLength);

            objCollision(now);

            float deltaV;
            if (isReal) {
                if (this.VELOCITY < 1) {
                    deltaV = (float) 0.0012;
                } else {
                    deltaV = (float) 0.0012 / this.VELOCITY;
                }
            } else {
                if (this.VELOCITY < 1) {
                    deltaV = (float) 0.00123;
                } else {
                    deltaV = (float) 0.00123/ this.VELOCITY;
                }
            }
            this.VELOCITY += deltaV;
        }
    }

    void draw(Canvas canvas)  {
        Bitmap bitmap = this.pongImg;
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    void setX(int x) {this.x = x;}

    void setY(int y) {this.y = y;}

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.movingVectorX = 10;
        this.movingVectorY = 5;
        VELOCITY = 0.4f;
    }

    // Only used in update(), separated for clarity
    private void objCollision(long now) {
        Random random = new Random();

        // Object collision with paddle (Exact Dimensions) & at least 0.2 secs after last change
        if (((
                Math.abs(this.x + this.width / 2 - paddleL.x - paddleL.width / 2) < (this.width + this.paddleL.getWidth()) / 2
        ) && (
                Math.abs(this.y + this.height / 2 - paddleL.y - paddleL.height / 2) < (this.height + this.paddleL.getHeight()) / 2
        ))
                || ((
                Math.abs(this.x + this.width / 2 - paddleR.x - paddleR.width / 2) < (this.width + this.paddleR.getWidth()) / 2
        ) && (
                Math.abs(this.y + this.height / 2 - paddleR.y - paddleR.height / 2) < (this.height + this.paddleR.getHeight()) / 2
        )) && (
                ((lastDrawNanoTime - lastVectorXChange) / 1000000) > 50
        )) {
            // Play R.raw.beep
            pongSurfaceView.soundPool.play(pongSurfaceView.soundIds[0], 1, 1, 1, 0, (float) 1.0);
            // Reduce randomness when playing with AI
            if (pongSurfaceView.twoUser) {
                this.movingVectorX = -this.movingVectorX + 2 - random.nextInt(4);
            } else {
                this.movingVectorX = -this.movingVectorX;
            }
            this.lastVectorXChange = now;
        }
        // When the game's character touches the top/bottom of the screen, then change direction
        if (this.y < 0) {
            this.y = 0;
            // Remove randomness when playing with AI
            if (pongSurfaceView.twoUser) {
                this.movingVectorY = -this.movingVectorY - 2 + random.nextInt(4);
            } else {
                this.movingVectorY = -this.movingVectorY;
            }
        } else if (this.y > this.pongSurfaceView.getHeight() - height) {
            this.y = this.pongSurfaceView.getHeight() - height;
            // Remove randomness when playing with AI
            if (pongSurfaceView.twoUser) {
                this.movingVectorY = -this.movingVectorY + 2 - random.nextInt(4);
            } else {
                this.movingVectorY = -this.movingVectorY;
            }
        }
    }
}
