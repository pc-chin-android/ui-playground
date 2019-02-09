package com.pcchin.uiplayground.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.gamedata.GeneralFunctions;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

public class GridBlock extends GameObject {
    private Bitmap bitmap;
    private Context context;
    private TetrisBlock block;
    private boolean isCenter;

    GridBlock(@NonNull TetrisSurfaceView tetrisSurfaceView, int x, int y, int widthHeight) {
        super(Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.ARGB_8888), x, y);
        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.context = tetrisSurfaceView.getContext();
    }

    public void bindBlock(TetrisBlock block) {
        this.block = block;
        this.bitmap = GeneralFunctions.colorToBitmap(this.block.getColor(), TetrisSurfaceView.GRID_WIDTH_HEIGHT, TetrisSurfaceView.GRID_WIDTH_HEIGHT);
    }

    public void unbindBlock() {
        this.block = null;
        this.bitmap = Bitmap.createBitmap(TetrisSurfaceView.GRID_WIDTH_HEIGHT, TetrisSurfaceView.GRID_WIDTH_HEIGHT, Bitmap.Config.ARGB_8888);
    }

    void draw(Canvas canvas)  {
        Bitmap bitmap = this.bitmap;
        canvas.drawBitmap(bitmap,x, y, null);
    }

    public TetrisBlock getBlock() {
        return this.block;
    }

    void setCtr(boolean center) {
        this.isCenter = center;
    }

    boolean getCtr() {
        return this.isCenter;
    }
}
