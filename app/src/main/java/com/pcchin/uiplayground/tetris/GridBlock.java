package com.pcchin.uiplayground.tetris;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.pcchin.uiplayground.gamedata.BitmapFunctions;
import com.pcchin.uiplayground.gamedata.GameObject;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

public class GridBlock extends GameObject {
    private Bitmap bitmap;
    private TetrisBlock block;

    GridBlock(int x, int y, int widthHeight) {
        super(Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.ARGB_8888), x, y);
        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public void bindBlock(TetrisBlock block) {
        this.block = block;
        if (this.block == null) {
            this.bitmap = Bitmap.createBitmap(TetrisSurfaceView.GRID_WIDTH_HEIGHT, TetrisSurfaceView.GRID_WIDTH_HEIGHT, Bitmap.Config.ARGB_8888);
        } else {
            this.bitmap = BitmapFunctions.colorToBitmap(this.block.getColor(), TetrisSurfaceView.GRID_WIDTH_HEIGHT, TetrisSurfaceView.GRID_WIDTH_HEIGHT);
        }
    }

    public void unbindBlock() {
        this.block = null;
        this.bitmap = Bitmap.createBitmap(TetrisSurfaceView.GRID_WIDTH_HEIGHT, TetrisSurfaceView.GRID_WIDTH_HEIGHT, Bitmap.Config.ARGB_8888);
    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.bitmap;
        canvas.drawBitmap(bitmap,x, y, null);
    }

    public TetrisBlock getBlock() {
        return this.block;
    }
}
