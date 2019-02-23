package com.pcchin.uiplayground.chess;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;

class ChessGrid extends GameObject {
    ChessGrid(@NonNull Bitmap image, int x, int y) {
        super(image, x, y);
    }
}