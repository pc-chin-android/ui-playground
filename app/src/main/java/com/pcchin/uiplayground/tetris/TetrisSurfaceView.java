package com.pcchin.uiplayground.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.pcchin.uiplayground.gamedata.GeneralFunctions;
import com.pcchin.uiplayground.R;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class TetrisSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TODO: Finish Tetris
    TetrisThread tetrisThread;
    private Context context;
    private boolean gameOverDisplayed;
    private ArrayList<TetrisBlock> blockList;
    public ArrayList<ArrayList<GridBlock>> gridList; // Order, <<C1R1, C1R2, C1R3>, <C2R1, C2R2 ...

    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor assetBgmDescriptor;

    public int score;
    public static int GRID_TOTAL_X; // Total number of columns in the grid
    public static int GRID_TOTAL_Y; // Total number of rows in the grid
    public static final int GRID_WIDTH_HEIGHT = 50; // Width and height of each box in pixels
    private static final int GRID_LINE_WIDTH = 10; // Width of each lineA
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

        this.tetrisThread = new TetrisThread(this, getHolder());

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
        this.tetrisThread.setRunning(true);
        if (!this.tetrisThread.isAlive()) {
            this.tetrisThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Close bgm
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

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
        for (ArrayList<GridBlock> gridBlockList: gridList) {
            for (GridBlock gridBlock: gridBlockList) {
                gridBlock.draw(canvas);
            }
        }
    }

    void onGameStart() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(true);
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        resetGame();
        mediaPlayer.start();
    }

    void onGamePause() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.resume);
        mediaPlayer.pause();
    }

    void onGameResume() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        mediaPlayer.start();
    }

    void onGameStop() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(false);
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
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
    public void onGameOver() {
        this.gameOverDisplayed = true;

        // Display alert dialog
        AlertDialog.Builder scoreDialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert);
        scoreDialogBuilder.setTitle(R.string.game_over);
        scoreDialogBuilder.setIcon(R.drawable.tetris_icon);
        scoreDialogBuilder.setMessage(String.format(Locale.ENGLISH, "Your score is %d", score));
        scoreDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog scoreDialog = scoreDialogBuilder.create();
        scoreDialog.show();

        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(false);
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.start);
        mediaPlayer.stop();
    }

    public void update() {

        ((TetrisActivity)context).updateScore();
        for (ArrayList<GridBlock> gridBlockList: gridList) {
            for (GridBlock gridBlock: gridBlockList) {
                gridBlock.update();
            }
        }

        checkLose();
    }

    public void checkLose() {}

    // Reset game when game starts
    public void resetGame() {
    }

    private void drawGrid(Canvas canvas) {
        // Set paint
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(GRID_LINE_WIDTH);

        int currentX = 0;
        int currentY = 0;

        for(int i = 0; i < GRID_TOTAL_X; i++) {
            canvas.drawLine(currentX, 0, currentX, getHeight(), paint);
            // Draw columns
            colCoords.add(currentX);
            currentX = currentX + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
        }

        for(int j = 0; j < GRID_TOTAL_Y; j++) {
            canvas.drawLine(0, currentY, getWidth(), currentY, paint);
            // Draw rows
            rowCoords.add(currentY);
            currentY = currentY + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
        }

        // Insert gridBlocks
        for (int k = 0; k < GRID_TOTAL_X; k++) {
            ArrayList<GridBlock> tempList = new ArrayList<>();
            for (int l = 0; l < GRID_TOTAL_Y; l++) {
                tempList.add(new GridBlock(this, colCoords.get(k), rowCoords.get(l), GRID_WIDTH_HEIGHT));
            }
            gridList.add(tempList);
        }
    }
}
