package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisT extends TetrisBlock {
    public TetrisT(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisT", Color.CYAN, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    void rotate() {

    }
}
