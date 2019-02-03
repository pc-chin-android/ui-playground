package com.pcchin.uiplayground.tetris.tetrisblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;
import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisBlock extends GameObject {
    private long lastDrawNanoTime =-1;

    private Bitmap block;
    private Context context;
    private int color;
    String type;

    TetrisBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, String type, int color, int x, int y) {
        super(image, x, y);
        this.context = tetrisSurfaceView.getContext();
        this.block = image;
        this.type = type;
        this.color = color;
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
        Bitmap bitmap = this.block;
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    void setX(int x) { this.x = x; }

    void setY(int y) {
        this.y = y;
    }

    int getColor() {
        return this.color;
    }

    void rotate() {}
}
