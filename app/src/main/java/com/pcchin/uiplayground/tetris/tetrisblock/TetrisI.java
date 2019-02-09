package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisI extends TetrisBlock {
    public TetrisI(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisI", Color.RED, x, y);
    }

    @Override
    void setStartingCoords() {
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) + i - 2);
            tempList.add(0);
            this.currentBlockCoords.add(tempList);
        }
    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        // Ctr block is located in second block
        return this.currentBlockCoords.get(1);
    }

    @Override
    public void rotate() {
        ArrayList<Integer> ctrGrid = this.getCtrGrid();
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> tempList = new ArrayList<>();
            if (this.isHorizontal) {
                // Vertical
                tempList.add(ctrGrid.get(0));
                tempList.add(ctrGrid.get(1) + i - 1);
            } else {
                // Horizontal
                tempList.add(ctrGrid.get(0) + i - 1);
                tempList.add(ctrGrid.get(1));
            }
            returnList.add(tempList);
        }

        if (! this.checkCollision(returnList)) {
            this.isHorizontal = !this.isHorizontal;
            this.currentBlockCoords = returnList;
        }
    }
}
