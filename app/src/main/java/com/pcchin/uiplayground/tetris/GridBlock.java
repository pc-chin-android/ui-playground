package com.pcchin.uiplayground.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.GameObject;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

public class GridBlock extends GameObject {
    private Bitmap bitmap;
    private Context context;
    private TetrisBlock block;
    private boolean isCenter;

    private int x;
    private int y;

    GridBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y, int widthHeight) {
        super(Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.ARGB_8888), x, y);
        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.context = tetrisSurfaceView.getContext();
    }

    public void bindBlock(TetrisBlock block) {
        this.block = block;
    }

    public void unbindBlock() {
        this.block = null;
    }

    public TetrisBlock getBlock() {
        return this.block;
    }

    public TetrisBlock checkBlock() {
        return this.block;
    }

    void setCtr(boolean center) {
        this.isCenter = center;
    }

    boolean getCtr() {
        return this.isCenter;
    }
}
