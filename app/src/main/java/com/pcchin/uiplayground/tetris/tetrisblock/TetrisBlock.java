package com.pcchin.uiplayground.tetris.tetrisblock;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;

class TetrisBlock extends GameObject {
    TetrisBlock(@NonNull Bitmap image, int x, int y) {
        super(image, x, y);
    }
}
