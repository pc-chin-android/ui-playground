package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisO extends TetrisBlock {
    public TetrisO(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisO", Color.WHITE, x, y);
    }

    @Override
    void setStartingCoords() {

    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        return null;
    }

    @Override
    public void rotate() {
        ArrayList<Integer> ctrGrid = this.getCtrGrid();
        ArrayList<ArrayList<Integer>> backupList = this.currentBlockCoords;
        if (this.checkCollision()) {
            this.currentBlockCoords = backupList;
        }
    }
}
