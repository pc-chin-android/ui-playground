package com.pcchin.uiplayground.tetris.tetrisblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.gamedata.GeneralFunctions;
import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public abstract class TetrisBlock extends GameObject {
    private Bitmap block;
    private Context context;
    private TetrisSurfaceView tetrisSurfaceView;
    private int color;
    private String type;
    public ArrayList<ArrayList<Integer>> currentBlockCoords; // <<x1, y1>, <x2, y2>, <x3, y3> ... (In terms of gridBlock)

    TetrisBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, String type, int color, int x, int y) {
        super(GeneralFunctions.colorToBitmap(Color.TRANSPARENT,1, 1), x, y);
        this.tetrisSurfaceView = tetrisSurfaceView;
        this.context = tetrisSurfaceView.getContext();
        this.block = image;
        this.type = type;
        this.color = color;

        // Set coordinates to top and centre of code
        this.setStartingCoords();

        // Check collision
        boolean spawnOccupied = false;
        for (ArrayList<Integer> i: currentBlockCoords) {
            if (tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).getBlock() != null) {
                spawnOccupied = true;
            }
        }
        if (spawnOccupied) {
            tetrisSurfaceView.onGameOver();
        }
    }

    // Differ for each block
    abstract void setStartingCoords();

    abstract void rotate();

    private void bindGrid() {
        // Set gridBlocks according to currentBlockCoords
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).bindBlock(this);
        }
    }

    private void unbindGrid() {
        // Remove current gridBlocks from reference
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).unbindBlock();
        }
    }

    // Return true if able to move down, else return false;
    public void moveDown() {
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            // Check if bottom of grid reached
            if ((i.get(1) + 1) > TetrisSurfaceView.GRID_TOTAL_Y - 1) {
                tetrisSurfaceView.targetBlock = null;
                return;
                // Check if bottom of current block is occupied
            } else if ((tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1) + 1).getBlock()) != null) {
                tetrisSurfaceView.targetBlock = null;
                return;
            }
        }

        this.unbindGrid();

        // Updates coords
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            i.set(1, i.get(1) + 1);
        }

        this.bindGrid();
    }

    void moveLeft() {
        boolean canMove = true;

        for (ArrayList<Integer> i: this.currentBlockCoords) {
            // Exit if left of grid reached
            if ((i.get(0) - 1) < 0) {
                canMove = false;
            // Check if left of current block is occupied
            } else if ((tetrisSurfaceView.gridList.get(i.get(0) - 1).get(i.get(1))).getBlock() != null) {
                canMove = false;
            }
        }

        // Check if all gridBlocks fits argument before changing all x coords by -1
        if (canMove) {
            this.unbindGrid();

            for (ArrayList<Integer> i: this.currentBlockCoords) {
                i.set(0, i.get(0) - 1);
            }

            this.bindGrid();
        }
    }

    void moveRight() {
        boolean canMove = true;

        for (ArrayList<Integer> i: this.currentBlockCoords) {
            // Exit if right of grid reached
            if ((i.get(0) + 1) > TetrisSurfaceView.GRID_TOTAL_X - 1) {
                canMove = false;
                // Check if right of current block is occupied
            } else if ((tetrisSurfaceView.gridList.get(i.get(0) + 1).get(i.get(1)).getBlock()) != null) {
                canMove = false;
            }
        }

        // Check if all gridBlocks fits argument before changing all x coords by +1
        if (canMove) {
            this.unbindGrid();

            for (ArrayList<Integer> i: this.currentBlockCoords) {
                i.set(0, i.get(0) + 1);
            }

            this.bindGrid();
        }
    }

    public int getColor() {
        return this.color;
    }
}
