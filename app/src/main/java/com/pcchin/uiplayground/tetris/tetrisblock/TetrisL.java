package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisL extends TetrisBlock {
    public TetrisL(@NonNull TetrisSurfaceView tetrisSurfaceView) {
        super(tetrisSurfaceView, Color.RED);
    }

    @Override
    void setStartingCoords() {
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) + i - 1);
            tempList.add(0);
            this.currentBlockCoords.add(tempList);
        }
        ArrayList<Integer> tempList = new ArrayList<>();
        tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) - 1);
        tempList.add(1);
        this.currentBlockCoords.add(tempList);
    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        return this.currentBlockCoords.get(1);
    }

    @Override
    public void rotate() {
        ArrayList<Integer> ctrGrid = this.getCtrGrid();
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();

        if (this.blockDir == TetrisBlock.DIR_LEFT || this.blockDir == TetrisBlock.DIR_RIGHT) {
            // Originally horizontal, now vertical
            for (int i = 0; i < 3; i++) {
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add(ctrGrid.get(0));
                tempList.add(ctrGrid.get(1) - i + 1);
                returnList.add(tempList);
            }

            // Final block
            ArrayList<Integer> tempList = new ArrayList<>();
            // X-Coords is 1 to left/right
            if (this.blockDir == TetrisBlock.DIR_LEFT) {
                tempList.add(ctrGrid.get(0) - 1);
                tempList.add(ctrGrid.get(1) - 1);
            } else {
                tempList.add(ctrGrid.get(0) + 1);
                tempList.add(ctrGrid.get(1) + 1);
            }
            returnList.add(tempList);

        } else {
            // Originally vertical, now horizontal
            for (int i = 0; i < 3; i++) {
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add(ctrGrid.get(0) - i + 1);
                tempList.add(ctrGrid.get(1));
                returnList.add(tempList);
            }

            // Final block
            ArrayList<Integer> tempList = new ArrayList<>();
            // Y-Coords is 1 up/down
            if (this.blockDir == TetrisBlock.DIR_UP) {
                tempList.add(ctrGrid.get(0) + 1);
                tempList.add(ctrGrid.get(1) - 1);
            } else {
                tempList.add(ctrGrid.get(0) - 1);
                tempList.add(ctrGrid.get(1) + 1);
            }
            returnList.add(tempList);
        }

        this.swapDir(returnList, false);
    }
}
