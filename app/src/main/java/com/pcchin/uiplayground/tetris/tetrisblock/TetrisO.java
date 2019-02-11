package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisO extends TetrisBlock {
    public TetrisO(@NonNull TetrisSurfaceView tetrisSurfaceView) {
        super(tetrisSurfaceView, "TetrisO", Color.WHITE);
    }

    @Override
    void setStartingCoords() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) + j);
                tempList.add(i);
                this.currentBlockCoords.add(tempList);
            }
        }
    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        return this.currentBlockCoords.get(0);
    }

    @Override
    public void rotate() {
        // It's a square, there's no rotation
    }
}
