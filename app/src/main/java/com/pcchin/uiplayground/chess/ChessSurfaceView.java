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
    public static int MOVE_NORMAL = 10;
    public static int MOVE_CHECK = 15;
    public static int MOVE_FINAL = 20;

    private Context context;

    private ArrayList<ChessGrid> gridList = new ArrayList<>();
    private ArrayList<ChessGrid> blackOutList = new ArrayList<>();
    private ArrayList<ChessGrid> whiteOutList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> moveList = new ArrayList<>(); // <Piece type, new x, new y, move type>
    ArrayList<ArrayList<Integer>> currentBoardCoords = new ArrayList<>(); // <Piece type, current x, current y>

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

    // Carry forward function
    public void carryForwardDraw() {
        Canvas canvas = this.getHolder().lockCanvas();
        this.draw(canvas);
        if (canvas != null) {
            this.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (ChessGrid c: gridList) {
            c.draw(canvas);
        }
        for (ChessGrid c: blackOutList) {
            c.draw(canvas);
        }
        for (ChessGrid c: whiteOutList) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean performClick() {
        // Placeholder function
        super.performClick();
        return true;
    }

    // Only used in onCreate, separated for clarity
    private void onTouchCalled(float x, float y) {
        this.carryForwardDraw();
    }
}
