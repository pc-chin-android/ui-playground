package com.pcchin.uiplayground.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.pcchin.uiplayground.GeneralFunctions;
import com.pcchin.uiplayground.R;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

import java.io.IOException;
import java.util.ArrayList;

public class TetrisSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TODO: Finish Tetris
    private TetrisThread tetrisThread;
    private Context context;
    private boolean gameOverDisplayed;
    private ArrayList<TetrisBlock> blockList;
    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor assetBgmDescriptor;

    public int score;
    private static int GRID_TOTAL_X; // Total number of columns in the grid
    private static int GRID_TOTAL_Y; // Total number of rows in the grid
    private static final int GRID_WIDTH_HEIGHT = 20; // Width and height of each box in pixels
    private static final int GRID_LINE_WIDTH = 2; // Width of each line
    private ArrayList<Integer> rowCoords; // Y-coordinates of each row (Reference pt tetrisSurfaceView);
    private ArrayList<Integer> colCoords; // X-coordinates of each column (Reference pt tetrisSurfaceView);

    // Constructor for Java file
    public TetrisSurfaceView(Context context) {
        super(context);
        this.onCreate(context);
    }

    // Constructor for XML file
    public TetrisSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onCreate(context);
    }

    // Constructor for XML file
    public TetrisSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onCreate(context);
    }

    // Used in all constructors
    public void onCreate(Context context) {

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.gameOverDisplayed = false;
        this.blockList = new ArrayList<>();
        this.colCoords = new ArrayList<>();
        this.rowCoords = new ArrayList<>();

        // Set up music
        mediaPlayer = GeneralFunctions.getMediaPlayer(context, AudioAttributes.CONTENT_TYPE_MUSIC);
        assetBgmDescriptor = context.getResources().openRawResourceFd(R.raw.tetris);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean retry = true;
                while (retry) {
                    try {
                        mediaPlayer.setDataSource(assetBgmDescriptor.getFileDescriptor(),
                                assetBgmDescriptor.getStartOffset(), assetBgmDescriptor.getLength());
                        mediaPlayer.setLooping(true);
                        mediaPlayer.prepare();
                        retry = false;
                    } catch (IOException e) {
                        // Refer to IllegalStateException below
                    } catch (IllegalStateException e) {
                    /* This error fires if two mediaPlayer is called very rapidly.
                     As this does not seem to affect the game, it is ignored. */
                    }
                }
            }
        }).start();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                GRID_TOTAL_X = getWidth() /GRID_WIDTH_HEIGHT;
                GRID_TOTAL_Y = getHeight() /GRID_WIDTH_HEIGHT;
            }
        });
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
        drawGrid(canvas);
    }

    void onGameStart() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        resetGame();
        mediaPlayer.start();
    }

    void onGamePause() {
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.resume);
        mediaPlayer.pause();
    }

    void onGameResume() {
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        mediaPlayer.start();
    }

    void onGameStop() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.start);
        mediaPlayer.stop();

        boolean retry = true;
        while (retry) {
            try {
                mediaPlayer.prepare();
                retry = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Triggered when game ends
    private void onGameOver() {
        this.gameOverDisplayed = true;
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.start);
        mediaPlayer.stop();
    }

    public void update() {

        ((TetrisActivity)context).updateScore();

        checkLose();
    }

    public void checkLose() {}

    // Reset game when game starts
    public void resetGame() {
    }

    private void drawGrid(@NonNull Canvas canvas) {
        // Set paint
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        int currentX = 0;
        int currentY = 0;

        for(int i = 0; i < GRID_TOTAL_X; i++) {
            canvas.drawLine(currentX, 0, currentX + GRID_LINE_WIDTH, getHeight(), paint);
            // Draw columns
            colCoords.add(currentX);
            currentX = currentX + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
        }

        for(int j = 0; j < GRID_TOTAL_Y; j++) {
            canvas.drawLine(0, currentY, getWidth(), currentY + GRID_LINE_WIDTH, paint);
            // Draw rows
            rowCoords.add(currentY);
            currentY = currentY + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
        }
    }
}