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
import android.widget.ImageView;

import com.pcchin.uiplayground.gamedata.GeneralFunctions;
import com.pcchin.uiplayground.R;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisI;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisJ;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisL;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisO;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisS;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisT;
import com.pcchin.uiplayground.tetris.tetrisblock.TetrisZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class TetrisSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    static final int NORMAL = 0;
    static final int PAUSED = 1;
    static final int STOPPED = -1;
    int gameState;

    private TetrisThread tetrisThread;
    private Context context;
    private ArrayList<TetrisBlock> blockList = new ArrayList<>();
    public ArrayList<ArrayList<GridBlock>> gridList = new ArrayList<>(); // Order, <<C1R1, C1R2, C1R3>, <C2R1, C2R2 ...
    public TetrisBlock targetBlock;
    private TetrisBlock nextBlock;

    MediaPlayer mediaPlayer;
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
    public void onCreate(final Context context) {

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.blockList = new ArrayList<>();
        this.colCoords = new ArrayList<>();
        this.rowCoords = new ArrayList<>();
        this.gameState = STOPPED;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                GRID_TOTAL_X = getWidth() /GRID_WIDTH_HEIGHT;
                GRID_TOTAL_Y = getHeight() /GRID_WIDTH_HEIGHT;
                ((TetrisActivity) context).updateScore();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        // Set paint
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(GRID_LINE_WIDTH);

        // Draw gridBlocks
        for (int i = 0; i < gridList.size(); i++) {
            ArrayList<GridBlock> currentList = gridList.get(i);
            for (int j = 0; j < currentList.size(); j++) {
                currentList.get(j).draw(canvas);
            }
        }

        if (colCoords.size() > 0 && rowCoords.size() > 0) {
            // Draw lines
            for (int i = 0; i < GRID_TOTAL_X; i++) {
                canvas.drawLine(colCoords.get(i), 0, colCoords.get(i), getHeight(), paint);
            }
            for (int j = 0; j < GRID_TOTAL_Y; j++) {
                canvas.drawLine(0, rowCoords.get(j), getWidth(), rowCoords.get(j), paint);
            }
        }
    }

    void onGameStart() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(true);
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        resetGame();
        mediaPlayer.start();
        this.gameState = NORMAL;

        this.tetrisThread = new TetrisThread(this, this.getHolder());
        this.tetrisThread.setRunning(true);
        this.tetrisThread.start();
    }

    void onGamePause() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.resume);
        mediaPlayer.pause();
        this.gameState = PAUSED;

        boolean retryThread = true;
        while (retryThread) {
            try{
                this.tetrisThread.setRunning(false);
                this.tetrisThread.join();
                retryThread = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void onGameResume() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        mediaPlayer.start();
        this.gameState = NORMAL;

        this.tetrisThread = new TetrisThread(this, this.getHolder());
        this.tetrisThread.setRunning(true);
        this.tetrisThread.start();
    }

    void onGameStop() {
        (((Activity)context).findViewById(R.id.tetris_stop)).setEnabled(false);
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.start);
        mediaPlayer.stop();

        boolean retryPlayer = true;
        while (retryPlayer) {
            try {
                mediaPlayer.prepare();
                retryPlayer = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean retryThread = true;
        while (retryThread) {
            try{
                this.tetrisThread.setRunning(false);
                this.tetrisThread.join();
                retryThread = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onGameOver() {
        // Display alert dialog
        AlertDialog.Builder scoreDialogBuilder = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_Dialog_Alert);
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

        this.onGameStop();
    }

    public void update() {
        if (this.gameState == NORMAL) {
            iniArrays();

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TetrisActivity) context).updateScore();
                }
            });

            // Spawn block if null
            if (this.targetBlock == null) {
                // Only when game starts
                if (this.nextBlock == null) {
                    this.genNextBlock();
                }
                this.targetBlock = this.nextBlock;
                this.blockList.add(this.targetBlock);
                this.genNextBlock();
            }

            // Check if any block is empty
            for (TetrisBlock block: this.blockList) {
                if (block.currentBlockCoords.size() == 0) {
                    this.blockList.remove(block);
                }
            }

            this.checkRow();
        }
    }

    // Reset game when game starts
    public void resetGame() {
        // Unbind all blocks in gridList
        for (ArrayList<GridBlock> i: gridList) {
            for (GridBlock j: i) {
                j.unbindBlock();
            }
        }

        // Clear blockList
        this.blockList = new ArrayList<>();
        this.targetBlock = null;
        this.nextBlock = null;
    }

    // Only used in draw(), separated for clarity
    private void iniArrays() {
        int currentX = 0;
        int currentY = 0;

        // Initialize colCoords if empty
        if (colCoords.size() == 0) {
            for (int i = 0; i < GRID_TOTAL_X; i++) {
                // Draw columns
                colCoords.add(currentX);
                currentX = currentX + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
            }
        }

        // Initialize rowCoords if empty
        if (rowCoords.size() == 0) {
            for (int j = 0; j < GRID_TOTAL_Y; j++) {
                // Draw rows
                rowCoords.add(currentY);
                currentY = currentY + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
            }
        }

        // Initialize gridBlocks if empty
        if (gridList.size() == 0) {
            for (int k = 0; k < GRID_TOTAL_X; k++) {
                ArrayList<GridBlock> tempList = new ArrayList<>();
                for (int l = 0; l < GRID_TOTAL_Y; l++) {
                    tempList.add(new GridBlock(colCoords.get(k), rowCoords.get(l), GRID_WIDTH_HEIGHT));
                }
                gridList.add(tempList);
            }
        }
    }

    // Only used in update(), separated for clarity
    private void genNextBlock() {
        Random rand = new Random();
        ImageView nextImg = ((TetrisActivity) context).findViewById(R.id.tetris_next_img);
        switch (rand.nextInt(7)) {
            case 0:
                // I Block
                this.nextBlock = new TetrisI(this);
                nextImg.setImageResource(R.drawable.tetris_blk_i);
                break;
            case 1:
                // J Block
                this.nextBlock = new TetrisJ(this);
                nextImg.setImageResource(R.drawable.tetris_blk_j);
                break;
            case 2:
                // L Block
                this.nextBlock = new TetrisL(this);
                nextImg.setImageResource(R.drawable.tetris_blk_l);
                break;
            case 3:
                // O Block
                this.nextBlock = new TetrisO(this);
                nextImg.setImageResource(R.drawable.tetris_blk_o);
                break;
            case 4:
                // S Block
                this.nextBlock = new TetrisS(this);
                nextImg.setImageResource(R.drawable.tetris_blk_s);
                break;
            case 5:
                // T Block
                this.nextBlock = new TetrisT(this);
                nextImg.setImageResource(R.drawable.tetris_blk_t);
                break;
            case 6:
                // Z Block
                this.nextBlock = new TetrisZ(this);
                nextImg.setImageResource(R.drawable.tetris_blk_z);
                break;
        }
    }

    // Only used in update(), separated for clarity
    private void checkRow() {
        int rowsCleared = 0;
        for (int i = 0; i < this.gridList.size(); i++) {
            boolean rowFull = true;
            for (GridBlock j: this.gridList.get(i)) {
                if (j.getBlock() == null) {
                    rowFull = false;
                }
            }
            if (rowFull) {
                // Reset rows
                for (GridBlock j: this.gridList.get(i)) {
                    j.unbindBlock();
                }
                // Move rows down by one
                for (int j = i; j >= 0; j--) {
                    for (int k = 0; k < this.gridList.get(j).size(); k++) {
                        this.gridList.get(j).get(k).bindBlock(
                                this.gridList.get(j - 1).get(k).getBlock()
                        );
                    }
                }
                // Reset top row
                for (int j = 0; j < this.gridList.get(0).size(); j++) {
                    this.gridList.get(0).get(j).unbindBlock();
                }
                rowsCleared++;
            }
        }
        // Check points
        this.score += (10 * GRID_TOTAL_X * rowsCleared * rowsCleared);
    }
}
