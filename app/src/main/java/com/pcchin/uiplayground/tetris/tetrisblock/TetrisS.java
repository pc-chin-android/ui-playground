package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisS extends TetrisBlock {
    public TetrisS(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, int x, int y) {
        super(tetrisSurfaceView, image, "TetrisS", Color.YELLOW, x, y);
    }
}
