package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.BitmapFunctions;
import com.pcchin.uiplayground.gamedata.CoordsFunctions;
import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.tetris.TetrisSurfaceView;

import java.util.ArrayList;

public abstract class TetrisBlock extends GameObject {
    static int DIR_UP = 1;
    static int DIR_RIGHT = 2;
    // static int DIR_DOWN = 3;
    static int DIR_LEFT = 4;

    private TetrisSurfaceView tetrisSurfaceView;
    private int color;
    private String type;
    int block_dir;
    public ArrayList<ArrayList<Integer>> currentBlockCoords = new ArrayList<>(); // <<x1, y1>, <x2, y2>, <x3, y3> ... (In terms of gridBlock)

    TetrisBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, String type, int color) {
        super(BitmapFunctions.colorToBitmap(Color.TRANSPARENT,1, 1), 0, 0);
        this.tetrisSurfaceView = tetrisSurfaceView;
        this.type = type;
        this.color = color;
        this.block_dir = DIR_LEFT;

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

    void unbindGrid() {
        // Remove current gridBlocks from reference
        for (ArrayList<Integer> i: this.currentBlockCoords) {
            tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).unbindBlock();
        }
    }

    public void moveDown() {
        ArrayList<ArrayList<Integer>> originalList = CoordsFunctions.downCoords(CoordsFunctions.deepCopy(this.currentBlockCoords));

        for (ArrayList<Integer> i: originalList) {
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

    public void moveLeft() {
        ArrayList<ArrayList<Integer>> originalList = CoordsFunctions.leftCoords(CoordsFunctions.deepCopy(this.currentBlockCoords));

        boolean canMove = true;

        for (ArrayList<Integer> i: originalList) {
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

    public void moveRight() {
        ArrayList<ArrayList<Integer>> originalList = CoordsFunctions.rightCoords(CoordsFunctions.deepCopy(this.currentBlockCoords));

        boolean canMove = true;

        for (ArrayList<Integer> i: originalList) {
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

    public boolean checkCollision(ArrayList<ArrayList<Integer>> coordsList) {
        if (coordsList.size() > 0) {
            // Check collision
            for (ArrayList<Integer> i : coordsList) {
                for (Integer j : i) {
                    if (j < 0) {
                        return true;
                    }
                }
                if (tetrisSurfaceView.gridList.get(i.get(0)).get(i.get(1)).getBlock() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    void swapDir(ArrayList<ArrayList<Integer>> returnList) {
        // TODO: Fix
        if (this.block_dir == DIR_LEFT) {
            // Change to DIR_UP
            CoordsFunctions.rightCoords(returnList);
        } else if (this.block_dir == DIR_UP) {
            // Change to DIR_RIGHT
            CoordsFunctions.rightCoords(returnList);
        } else if (this.block_dir == DIR_RIGHT) {
            // Change to DIR_DOWN
            CoordsFunctions.downCoords(returnList);
        } else {
            // Change to DIR_LEFT
            CoordsFunctions.leftCoords(returnList);
        }

        if (!this.checkCollision(returnList)) {
            this.unbindGrid();

            this.block_dir++;
            if (this.block_dir > DIR_LEFT) {
                this.block_dir = DIR_UP;
            }

            this.currentBlockCoords = returnList;

            this.bindGrid();
        }
    }
}
