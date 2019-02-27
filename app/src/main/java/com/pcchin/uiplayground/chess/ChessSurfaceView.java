package com.pcchin.uiplayground.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class ChessSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int MOVE_NORMAL = 10;
    public static final int MOVE_CHECK = 15;
    public static final int MOVE_FINAL = 20;

    public static int PIECE_WIDTH_HEIGHT;

    private ArrayList<ArrayList<ChessGrid>> gridList = new ArrayList<>(); // <<x1y1, x1y2, x1y3>, <x2y1, x2y2..
    private ArrayList<ChessGrid> blackOutList = new ArrayList<>();
    private ArrayList<ChessGrid> whiteOutList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> moveList = new ArrayList<>(); // <Piece type, new x, new y, move type>
    ArrayList<ArrayList<Integer>> currentBoardCoords = new ArrayList<>(); // <Piece type, current x, current y>

    public ChessSurfaceView(Context context) {
        super(context);
        this.onCreate();
    }

    public ChessSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onCreate();
    }

    public ChessSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onCreate();
    }

    private void onCreate() {
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

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                PIECE_WIDTH_HEIGHT = getWidth() / 8;
                if (12 * PIECE_WIDTH_HEIGHT > getHeight()) {
                    PIECE_WIDTH_HEIGHT = getHeight() / 12;
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Set up Grids
        for (int currentX = 0; currentX < 8; currentX++) {
            ArrayList<ChessGrid> tempList = new ArrayList<>();
            for (int currentY = 0; currentY < 8; currentY++) {
                // Check current type
                int currentType;
                if ((currentX + currentY) % 2 != 0) {
                    currentType = ChessGrid.TYPE_WHITE;
                } else {
                    currentType = ChessGrid.TYPE_BLACK;
                }

                tempList.add(new ChessGrid((getWidth() / 2) - (4 * PIECE_WIDTH_HEIGHT) + (currentX * PIECE_WIDTH_HEIGHT),
                        (getHeight() / 2) - (4 * PIECE_WIDTH_HEIGHT) + (currentY * PIECE_WIDTH_HEIGHT),
                        PIECE_WIDTH_HEIGHT, currentType));
            }
            gridList.add(tempList);
        }

        // Set up black/white out grid
        for (int currentX = 0; currentX < 8; currentX++) {
            for (int currentY = 0; currentY < 2; currentY++) {
                whiteOutList.add(new ChessGrid((getWidth() / 2) - (4 * PIECE_WIDTH_HEIGHT) + (currentX * PIECE_WIDTH_HEIGHT),
                        (getHeight() / 2) - (6 * PIECE_WIDTH_HEIGHT) + (currentY * PIECE_WIDTH_HEIGHT),
                        PIECE_WIDTH_HEIGHT, ChessGrid.TYPE_OUT));

                blackOutList.add(new ChessGrid((getWidth() / 2) - (4 * PIECE_WIDTH_HEIGHT) + (currentX * PIECE_WIDTH_HEIGHT),
                        (getHeight() / 2) + (5 * PIECE_WIDTH_HEIGHT) - (currentY * PIECE_WIDTH_HEIGHT),
                        PIECE_WIDTH_HEIGHT, ChessGrid.TYPE_OUT));
            }
        }
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
        for (ArrayList<ChessGrid> list: gridList) {
            for (ChessGrid c: list) {
                c.draw(canvas);
            }
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

    private void resetGame() {
        for (ArrayList<ChessGrid> list: gridList) {
            for (ChessGrid c: list) {
                c.unbindPiece();
            }
        }
        for (ChessGrid c: blackOutList) {
            c.unbindPiece();
        }
        for (ChessGrid c: whiteOutList) {
            c.unbindPiece();
        }
        this.carryForwardDraw();

        moveList = new ArrayList<>();
        currentBoardCoords = new ArrayList<>();
    }
}
