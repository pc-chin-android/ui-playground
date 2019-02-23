package com.pcchin.uiplayground.chess.chesspiece;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;

public class ChessBlock extends GameObject {
    public ChessBlock(@NonNull Bitmap image, int x, int y) {
        super(image, x, y);
    }
}
