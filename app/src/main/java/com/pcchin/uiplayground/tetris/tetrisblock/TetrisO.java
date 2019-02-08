package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisO extends TetrisBlock {
    public TetrisO(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisO", Color.WHITE, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    void rotate() {

    }
}
