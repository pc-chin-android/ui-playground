package com.pcchin.uiplayground.tetris;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

import java.util.ArrayList;

public class TetrisSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TODO: Finish Tetris
    private TetrisThread tetrisThread;
    private Context context;
    private boolean gameOverDisplayed;
    private ArrayList blockList;

    public int score;

    // Constructor for Java file
    public TetrisSurfaceView(Context context) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.gameOverDisplayed = false;
        this.blockList = new ArrayList<TetrisBlock>();
    }

    // Constructor for XML file
    public TetrisSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.gameOverDisplayed = false;
        this.blockList = new ArrayList<TetrisBlock>();
    }

    // Constructor for XML file
    public TetrisSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.gameOverDisplayed = false;
        this.blockList = new ArrayList<TetrisBlock>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        tetrisThread = new TetrisThread(this, getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try{
                this.tetrisThread.setRunning(false);
                this.tetrisThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void update() {

        checkScore();

        checkLose();
    }

    public void checkScore() {}

    public void checkLose() {}

    public void resetGame() {
        this.score = 0;
        // TODO: Finish reset score
    }

    // Triggered when game ends
    private void gameOver() {
        this.gameOverDisplayed = true;

        // TODO: Custom dialog to show score
    }
}
