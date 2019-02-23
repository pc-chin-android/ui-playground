package com.pcchin.uiplayground.chess;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.pcchin.uiplayground.gamedata.GameThread;

public class ChessSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public ChessSurfaceView(Context context) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
