package com.pcchin.uiplayground.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.chess.chesspiece.ChessPiece;
import com.pcchin.uiplayground.gamedata.BitmapFunctions;
import com.pcchin.uiplayground.gamedata.GameObject;

class ChessGrid extends GameObject {
    private ChessPiece currentPiece;
    private Bitmap bitmap;

    public static int TYPE_WHITE = 1;
    public static int TYPE_BLACK = -1;
    public static int TYPE_OUT = 2;

    ChessGrid(Context context, int x, int y, int widthHeight, int type) {
        super(BitmapFunctions.colorToBitmap(Color.TRANSPARENT, widthHeight, widthHeight), x, y);

    }

    private void bindPiece(ChessPiece chessPiece) {
        this.currentPiece = chessPiece;
    }

    private void unbindPiece(ChessPiece chessPiece) {
        this.currentPiece = null;
    }

    void draw(Canvas canvas) {
        Bitmap bitmap = this.bitmap;
        canvas.drawBitmap(bitmap, x, y, null);
    }
}