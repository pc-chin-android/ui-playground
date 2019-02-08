package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

public class TetrisI extends TetrisBlock {
    public TetrisI(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisI", Color.RED, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    void rotate() {

    }
}
