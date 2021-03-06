package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisZ extends TetrisBlock {
    public TetrisZ(@NonNull TetrisSurfaceView tetrisSurfaceView) {
        super(tetrisSurfaceView, Color.rgb(200, 50, 160));
    }

    @Override
    void setStartingCoords() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) + i + j - 1);
                tempList.add(i);
                this.currentBlockCoords.add(tempList);
            }
        }
    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        return this.currentBlockCoords.get(1);
    }

    @Override
    public void rotate() {
        ArrayList<Integer> ctrGrid = this.getCtrGrid();
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // S and Z Block structured differently because S has different ctr blocks
                ArrayList<Integer> tempList = new ArrayList<>();
                if (this.blockDir == TetrisBlock.DIR_UP) {
                    // Change to horizontal
                    tempList.add(ctrGrid.get(0) - 1 + i + j);
                    tempList.add(ctrGrid.get(1) + i);
                } else {
                    // Change to vertical
                    tempList.add(ctrGrid.get(0) - i);
                    tempList.add(ctrGrid.get(1) - 1 + i + j);
                }
                returnList.add(tempList);
            }
        }

        this.swapDir(returnList, true);
    }
}
