package com.pcchin.uiplayground.chess.chesspiece;

import android.graphics.Bitmap;

import com.pcchin.uiplayground.chess.ChessSurfaceView;
import com.pcchin.uiplayground.gamedata.BitmapFunctions;

import java.util.ArrayList;

public abstract class ChessPiece {
    private int[] coordsList = new int[2];
    private Bitmap pieceBitmap;
    private boolean isWhite;

    public ChessPiece(ChessSurfaceView chessSurfaceView, int x, int y, boolean isWhite, Bitmap bitmap) {
        this.coordsList[0] = x;
        this.coordsList[1] = y;
        this.isWhite = isWhite;
        this.pieceBitmap = BitmapFunctions.getResizedBitmap(bitmap, ChessSurfaceView.PIECE_WIDTH_HEIGHT, ChessSurfaceView.PIECE_WIDTH_HEIGHT);
    }

    //****** Setters and getters ******//

    public void setCoordsList(int x, int y) {
        this.coordsList[0] = x;
        this.coordsList[1] = y;
    }

    public int[] getCoordsList() {
        return this.coordsList;
    }

    public boolean getWhite() {
        return this.isWhite;
    }

    //****** Abstract functions ******//

    abstract ArrayList<int[]> possibleMoves();

    // Check if a move to that position is valid
    abstract boolean checkMove(int targetX, int targetY);
}
