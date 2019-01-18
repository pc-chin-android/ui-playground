package com.pcchin.uiplayground.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;
import com.pcchin.uiplayground.GeneralFunctions;

class Score extends GameObject {
    private long lastDrawNanoTime =-1;
    int currentScore;
    private Bitmap currentBmp;

    private PongSurfaceView pongSurfaceView;

    Score(@NonNull PongSurfaceView pongSurfaceView, int x, int y) {
        super(scoreToBitmap(0, pongSurfaceView.getContext()), x, y);
        this.pongSurfaceView = pongSurfaceView;
        this.currentScore = 0;
        this.currentBmp = scoreToBitmap(0, pongSurfaceView.getContext());
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

    static Bitmap scoreToBitmap(int score, @NonNull Context context) {
        return GeneralFunctions.textToBitmap(Integer.toString(score), Color.WHITE, 120,
                "orbitron", Typeface.BOLD,true, context);
    }
}
