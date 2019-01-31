package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisL extends TetrisBlock {
    public TetrisL(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, int x, int y) {
        super(tetrisSurfaceView, image, "TetrisL", x, y);
    }
}
