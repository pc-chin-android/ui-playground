package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisL extends TetrisBlock {
    public TetrisL(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisL", Color.BLUE, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    void rotate() {

    }
}
