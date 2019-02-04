package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisT extends TetrisBlock {
    public TetrisT(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, int x, int y) {
        super(tetrisSurfaceView, image, "TetrisT", Color.CYAN, x, y);
    }

    @Override
    void setStartingCoords() {

    }
}
