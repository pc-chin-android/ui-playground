package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisZ extends TetrisBlock {
    public TetrisZ(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisZ", Color.MAGENTA, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    void rotate() {

    }
}
