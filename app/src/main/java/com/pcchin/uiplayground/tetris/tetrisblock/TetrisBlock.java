package com.pcchin.uiplayground.tetris.tetrisblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public abstract class TetrisBlock extends GameObject {
    private long lastDrawNanoTime =-1;

    private Bitmap block;
    private Context context;
    private TetrisSurfaceView tetrisSurfaceView;
    private int color;
    String type;
    private ArrayList<ArrayList<Integer>> currentBlockCoords; // <<x1, y1>, <x2, y2>, <x3, y3> ... (In terms of gridBlock)

    TetrisBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, @NonNull Bitmap image, String type, int color, int x, int y) {
        super(image, x, y);
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
        } else {
            this.update();
        }
    }

    // Differ for each block
    abstract void setStartingCoords();

    abstract void rotate();

    private void update(){
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
    }

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
    public boolean moveDown() {
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            // Check if bottom of grid reached
            if ((i.get(1) + 1) > TetrisSurfaceView.GRID_TOTAL_Y - 1) {
                return false;
                // Check if bottom of current block is occupied
            } else if ((tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1) + 1).getBlock()) != null) {
                return false;
            }
        }

        this.unbindGrid();

        // Updates coords
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            i.set(1, i.get(1) + 1);
        }

        this.bindGrid();
        this.update();
        return true;
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
            this.update();
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
            this.update();
        }
    }

    public int getColor() {
        return this.color;
    }
}
