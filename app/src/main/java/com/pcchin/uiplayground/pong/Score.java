package com.pcchin.uiplayground.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.gamedata.GeneralFunctions;

class Score extends GameObject {
    private long lastDrawNanoTime =-1;
    int currentScore;
    private Bitmap currentBmp;
    private String content;

    private PongSurfaceView pongSurfaceView;

    // For normal score
    Score(@NonNull PongSurfaceView pongSurfaceView, int x, int y) {
        super(scoreToBitmap(0, pongSurfaceView.getContext()), x, y);
        this.pongSurfaceView = pongSurfaceView;
        this.currentScore = 0;
        this.currentBmp = scoreToBitmap(0, pongSurfaceView.getContext());
    }

    // For paused
    Score(@NonNull PongSurfaceView pongSurfaceView, int x, int y, String content) {
        super(Bitmap.createBitmap(16, 1, Bitmap.Config.ARGB_8888), x, y);
        this.pongSurfaceView = pongSurfaceView;
        this.content = content;
        this.currentBmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    void update(){
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }

        // Increase score
        this.currentScore++;
        this.currentBmp = scoreToBitmap(this.currentScore, this.pongSurfaceView.getContext());
    }

    void draw(Canvas canvas)  {
        Bitmap bitmap = this.currentBmp;
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    void pauseShow(boolean show) {

        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }

        if (show) {
            this.currentBmp = GeneralFunctions.textToBitmap(content, Color.RED, 120,
                    "orbitron", Typeface.BOLD, true, pongSurfaceView.getContext());
        } else {
            this.currentBmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
    }

    static Bitmap scoreToBitmap(int score, @NonNull Context context) {
        return GeneralFunctions.textToBitmap(Integer.toString(score), Color.WHITE, 120,
                "orbitron", Typeface.BOLD,true, context);
    }
}
