package com.pcchin.uiplayground.chess;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.pcchin.uiplayground.chess.chesspiece.ChessPiece;
import com.pcchin.uiplayground.gamedata.BitmapFunctions;
import com.pcchin.uiplayground.gamedata.GameObject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

class ChessGrid extends GameObject {
    private ChessPiece currentPiece;
    private boolean isSelected;
    private final Bitmap bgBitmap;
    private Bitmap displayBitmap;

    static final int TYPE_WHITE = 1;
    static final int TYPE_BLACK = -1;
    static final int TYPE_OUT = 2;

    ChessGrid(int x, int y, int widthHeight, int type) {
        super(BitmapFunctions.colorToBitmap(Color.TRANSPARENT, widthHeight, widthHeight), x, y);
        switch(type) {
            case TYPE_WHITE:
                this.bgBitmap = BitmapFunctions.colorToBitmap(Color.WHITE, widthHeight, widthHeight);
                break;
            case TYPE_BLACK:
                this.bgBitmap = BitmapFunctions.colorToBitmap(Color.GRAY, widthHeight, widthHeight);
                break;
            case TYPE_OUT:
            default:
                this.bgBitmap = BitmapFunctions.colorToBitmap(Color.TRANSPARENT, widthHeight, widthHeight);
                break;
        }
        this.displayBitmap = Bitmap.createBitmap(this.bgBitmap);
    }

    //****** Main Functions ******//

    void draw(Canvas canvas) {
        Bitmap bitmap = this.displayBitmap;
        canvas.drawBitmap(bitmap, x, y, null);
    }

    void onTouch() {}

    //****** Setters and Getters ******//

    private void bindPiece(@NotNull ChessPiece chessPiece) {
        this.currentPiece = chessPiece;
        this.displayBitmap = BitmapFunctions.overlayBitmap(this.bgBitmap, chessPiece.getBitmap());
    }

    void unbindPiece() {
        this.currentPiece = null;
        this.displayBitmap = Bitmap.createBitmap(this.bgBitmap);
    }

    @Contract(pure = true)
    private ChessPiece getCurrentPiece() {
        return this.currentPiece;
    }

    private void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Contract(pure = true)
    private boolean getSelected() {
        return this.isSelected;
    }
}