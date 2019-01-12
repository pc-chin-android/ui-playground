package com.pcchin.uiplayground.pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;
import com.pcchin.uiplayground.GeneralFunctions;
import com.pcchin.uiplayground.R;

class Paddle extends GameObject {
    // Velocity of game character (pixel/millisecond)

    private long lastDrawNanoTime =-1;

    private PongSurfaceView pongSurfaceView;
    private Bitmap paddle;

    Paddle(@NonNull PongSurfaceView pongSurfaceView, int x, int y) {
        super(GeneralFunctions.getBitmap(R.drawable.pong_paddle, pongSurfaceView.getContext()), x, y);
        this.paddle = GeneralFunctions.getBitmap(R.drawable.pong_paddle, pongSurfaceView.getContext());
        this.pongSurfaceView = pongSurfaceView;
    }

    void update(){
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }

    }

    void draw(Canvas canvas)  {
        Bitmap bitmap = this.paddle;
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }
}
