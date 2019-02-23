package com.pcchin.uiplayground.chess;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class ChessSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private ArrayList<ChessGrid> gridList = new ArrayList<>();

    public ChessSurfaceView(Context context) {
        super(context);
        this.onCreate(context);
    }

    public ChessSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onCreate(context);
    }

    public ChessSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onCreate(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    private void onCreate(Context context) {
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
