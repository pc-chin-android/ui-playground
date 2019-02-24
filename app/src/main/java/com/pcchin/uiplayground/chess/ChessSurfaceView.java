package com.pcchin.uiplayground.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;

public class ChessSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;

    private ArrayList<ChessGrid> gridList = new ArrayList<>();
    private ArrayList<ChessGrid> blackOutList = new ArrayList<>();
    private ArrayList<ChessGrid> whiteOutList = new ArrayList<>();

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
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                onTouchCalled(event.getX(), event.getY());
                return false;
            }
        });

        this.context = context;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // TODO: Draw
    }

    @Override
    public boolean performClick() {
        // Placeholder function
        super.performClick();
        return true;
    }

    // Only used in onCreate, separated for clarity
    private void onTouchCalled(float x, float y) {}
}
