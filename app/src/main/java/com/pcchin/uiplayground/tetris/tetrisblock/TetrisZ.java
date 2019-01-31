package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisZ extends TetrisBlock {
    public TetrisZ(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, int x, int y) {
        super(tetrisSurfaceView, image, "TetrisZ", x, y);
    }
}
