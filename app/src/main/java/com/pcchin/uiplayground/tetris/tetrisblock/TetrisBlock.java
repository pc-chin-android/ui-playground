package com.pcchin.uiplayground.tetris.tetrisblock;

import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GeneralFunctions;
import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

import static com.pcchin.uiplayground.tetris.TetrisSurfaceView.GRID_TOTAL_X;
import static com.pcchin.uiplayground.tetris.TetrisSurfaceView.GRID_TOTAL_Y;

public abstract class TetrisBlock {
    static int DIR_UP = 1;
    static int DIR_RIGHT = 2;
    // static int DIR_DOWN = 3;
    static int DIR_LEFT = 4;

    private TetrisSurfaceView tetrisSurfaceView;
    private int color;
    int blockDir;
    public ArrayList<ArrayList<Integer>> currentBlockCoords = new ArrayList<>(); // <<x1, y1>, <x2, y2>, <x3, y3> ... (In terms of gridBlock)

    TetrisBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, int color) {
        this.tetrisSurfaceView = tetrisSurfaceView;
        this.color = color;
        this.blockDir = DIR_LEFT;

        // Set coordinates to top and centre of code
        this.setStartingCoords();
    }

    // Differ for each block
    abstract void setStartingCoords();

    abstract ArrayList<Integer> getCtrGrid();

    public abstract void rotate();

    public void bindGrid() {
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

    public void moveDown() {
        ArrayList<ArrayList<Integer>> originalList = GeneralFunctions.deepCopy(this.currentBlockCoords);

        for (ArrayList<Integer> i: originalList) {
            // Check if bottom of grid reached
            if ((i.get(1) + 1) > tetrisSurfaceView.rowCoords.size() - 1) {
                tetrisSurfaceView.targetBlock = null;
                return;
                // These two are separated to prevent OutOfBoundsException
                // Check if bottom of current block is occupied
            } else if (((tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1) + 1).getBlock()) != null)
            && ((tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1) + 1).getBlock()) != this)) {
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

    public void moveLeft() {
        ArrayList<ArrayList<Integer>> originalList = GeneralFunctions.deepCopy(this.currentBlockCoords);

        boolean canMove = true;

        for (ArrayList<Integer> i: originalList) {
            // Exit if left of grid reached
            if ((i.get(0) - 1) < 0) {
                canMove = false;
            // Check if left of current block is occupied
            } else if (((tetrisSurfaceView.gridList.get(i.get(0) - 1).get(i.get(1))).getBlock() != null)
            && ((tetrisSurfaceView.gridList.get(i.get(0) - 1).get(i.get(1))).getBlock() != this)) {
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

    public void moveRight() {
        ArrayList<ArrayList<Integer>> originalList = GeneralFunctions.deepCopy(this.currentBlockCoords);

        boolean canMove = true;

        for (ArrayList<Integer> i: originalList) {
            // Exit if right of grid reached
            if ((i.get(0) + 2) > GRID_TOTAL_X) {
                canMove = false;
                // Check if right of current block is occupied
            } else if (((tetrisSurfaceView.gridList.get(i.get(0) + 1).get(i.get(1)).getBlock()) != null)
            && ((tetrisSurfaceView.gridList.get(i.get(0) + 1).get(i.get(1)).getBlock()) != this)) {
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

    public boolean checkCollision(ArrayList<ArrayList<Integer>> coordsList) {
        if (coordsList.size() > 0) {
            // Check collision
            for (ArrayList<Integer> i : coordsList) {
                if (i.get(0) < 0 || i.get(1) < 0 || i.get(0) >= GRID_TOTAL_X || i.get(1) >= GRID_TOTAL_Y) {
                    return true;
                    // Separated to prevent ArrayIndexOutOfBoundsException
                } else if ((tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).getBlock() != null)
                && (tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).getBlock() != this)) {
                    return true;
                }
            }
        }
        return false;
    }

    void swapDir(ArrayList<ArrayList<Integer>> targetList, boolean isFlip) {
        ArrayList<ArrayList<Integer>> returnList = GeneralFunctions.deepCopy(targetList);

        if (!this.checkCollision(returnList)) {
            this.unbindGrid();

            if (isFlip) {
                // For blocks with 2 sides
                if (this.blockDir == TetrisBlock.DIR_UP) {
                    this.blockDir = TetrisBlock.DIR_LEFT;
                } else {
                    this.blockDir = TetrisBlock.DIR_UP;
                }
            } else {
                // For blocks with 4 sides
                this.blockDir++;
                if (this.blockDir > DIR_LEFT) {
                    this.blockDir = DIR_UP;
                }
            }

            this.currentBlockCoords = targetList;
            this.bindGrid();
        }
    }
}