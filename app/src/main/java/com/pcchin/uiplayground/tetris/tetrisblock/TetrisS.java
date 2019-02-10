package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public class TetrisS extends TetrisBlock {
    public TetrisS(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y) {
        super(tetrisSurfaceView, "TetrisS", Color.YELLOW, x, y);
    }

    @Override
    void setStartingCoords() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add((TetrisSurfaceView.GRID_TOTAL_X/2) - i + j);
                tempList.add(i);
                this.currentBlockCoords.add(tempList);
            }
        }
    }

    @Override
    ArrayList<Integer> getCtrGrid() {
        if (this.block_dir == TetrisBlock.DIR_UP) {
            return this.currentBlockCoords.get(0);
        } else {
            return this.currentBlockCoords.get(1);
        }
    }

    @Override
    public void rotate() {
        ArrayList<Integer> ctrGrid = this.getCtrGrid();
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // S and Z Block structured differently because S has different ctr blocks
                ArrayList<Integer> tempList = new ArrayList<>();
                if (this.block_dir == TetrisBlock.DIR_UP) {
                    // Change to horizontal
                    tempList.add(ctrGrid.get(0) - i + j);
                    tempList.add(ctrGrid.get(1) + i);
                } else {
                    // Change to vertical
                    tempList.add(ctrGrid.get(0) + i);
                    tempList.add(ctrGrid.get(1) + j + i - 1);
                }
                returnList.add(tempList);
            }
        }

        if (! this.checkCollision(returnList)) {
            this.unbindGrid();

            if (this.block_dir == TetrisBlock.DIR_UP) {
                this.block_dir = TetrisBlock.DIR_LEFT;
            } else {
                this.block_dir = TetrisBlock.DIR_UP;
            }
            this.currentBlockCoords = returnList;

            this.bindGrid();
        }
    }
}
