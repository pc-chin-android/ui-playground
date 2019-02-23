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
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.pcchin.uiplayground.gamedata.GameThread;
import com.pcchin.uiplayground.gamedata.GameView;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class TetrisSurfaceView extends GameView implements SurfaceHolder.Callback {
    static final int NORMAL = 0;
    static final int PAUSED = 1;
    static final int STOPPED = -1;
    int gameState;
    int blocksAdded;

    private GameThread tetrisThread;
    private BlockDownThread blockDownThread;
    private Context context;
    private ArrayList<TetrisBlock> blockList = new ArrayList<>();
    public ArrayList<ArrayList<GridBlock>> gridList = new ArrayList<>(); // Order, <<C1R1, C1R2, C1R3>, <C2R1, C2R2 ...
    public TetrisBlock targetBlock;
    private TetrisBlock nextBlock;

    MediaPlayer mediaPlayer;
    private AssetFileDescriptor assetBgmDescriptor;

    private SoundPool soundPool;
    private int soundId;
    public int score;
    public static final int GRID_TOTAL_X = 10; // Total number of columns in the grid
    public static int GRID_TOTAL_Y; // Total number of rows in the grid
    public static int GRID_WIDTH_HEIGHT; // Width and height of each box in pixels
    private static int GRID_LINE_WIDTH; // Width of each line
    public ArrayList<Integer> rowCoords; // Y-coordinates of each row (Reference pt tetrisSurfaceView);
    public ArrayList<Integer> colCoords; // X-coordinates of each column (Reference pt tetrisSurfaceView);

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
                GRID_WIDTH_HEIGHT = Math.round(((float) getWidth() / GRID_TOTAL_X) * 5 / 6);
                GRID_LINE_WIDTH = Math.round((float)GRID_WIDTH_HEIGHT / 5);
                GRID_TOTAL_Y = Math.round((float) getHeight() /(GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH));
                ((TetrisActivity) context).updateScore();
            }
        });

        // Setting up sound
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attrs)
                .build();
        soundId = soundPool.load(getContext(), R.raw.robot_bleep, 1);
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
        this.onGameStop();

        // Close bgm
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        soundPool.release();
        soundPool = null;
    }

    // Not run on UI thread
    @Override
    public void draw(Canvas canvas) {
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
            for (int i = 0; i < colCoords.size(); i++) {
                canvas.drawLine(colCoords.get(i), 0, colCoords.get(i), getHeight(), paint);
            }
            for (int j = 0; j < rowCoords.size(); j++) {
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

        this.tetrisThread = new GameThread(this, this.getHolder(), false);
        this.tetrisThread.setRunning(true);
        this.tetrisThread.start();

        this.blockDownThread = new BlockDownThread(this);
        this.blocksAdded = 0;
        this.blockDownThread.setRunning(true);
        this.blockDownThread.start();
    }

    void onGamePause() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(false);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.resume);
        mediaPlayer.pause();
        this.gameState = PAUSED;

        this.stopThreads();
    }

    void onGameResume() {
        (((Activity)context).findViewById(R.id.tetris_rotate)).setEnabled(true);
        ((Button)((Activity)context).findViewById(R.id.tetris_button)).setText(R.string.pause);
        mediaPlayer.start();
        this.gameState = NORMAL;

        this.tetrisThread = new GameThread(this, this.getHolder(), false);
        this.tetrisThread.setRunning(true);
        this.tetrisThread.start();

        this.blockDownThread = new BlockDownThread(this);
        this.blockDownThread.setRunning(true);
        this.blockDownThread.start();
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

        this.stopThreads();
    }

    private void onGameOver() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Display alert dialog
                AlertDialog.Builder scoreDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
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

                // Equivalent of this.onGameStop()
                ((TetrisActivity)context).onResetBtnPressed(getRootView());
            }
        });
    }

    // Not run on UI Thread
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
                // Play R.raw.robot_bleep
                soundPool.play(soundId, 1, 1, 1, 0, 1);
                this.targetBlock = this.nextBlock;
                this.blockList.add(this.targetBlock);
                this.genNextBlock();

                this.checkRow();

                // Checks for any collision after new block spawns
                if (this.targetBlock != null) {
                    if (this.targetBlock.checkCollision(this.targetBlock.currentBlockCoords)) {
                        this.onGameOver();
                    } else {
                        this.targetBlock.bindGrid();
                    }
                }
            }

            // Check if any block is empty
            for (Iterator<TetrisBlock> iterator = this.blockList.iterator(); iterator.hasNext();) {
                TetrisBlock block = iterator.next();
                if (block == null || block.currentBlockCoords.size() == 0) {
                    iterator.remove();
                }
            }
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

    // Only used in update(), separated for clarity
    private void iniArrays() {
        int currentX = 0;
        int currentY = 0;

        /*
        Due to a problem with the bottom grid of the canvas extending out of the screen,
        both rowCoords and colCoords will check if their respective coordinates exceed the bottom
         */

        // Initialize rowCoords if empty
        if (rowCoords.size() == 0) {
            for (int j = 0; j < GRID_TOTAL_Y; j++) {
                if (currentY + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH < getHeight()) {
                    // Draw rows
                    rowCoords.add(currentY);
                    currentY = currentY + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
                }
            }
        }

        // Initialize colCoords if empty
        if (colCoords.size() == 0) {
            for (int i = 0; i < GRID_TOTAL_X; i++) {
                // Draw columns
                colCoords.add(currentX);
                currentX = currentX + GRID_WIDTH_HEIGHT + GRID_LINE_WIDTH;
            }
        }

        // Initialize gridBlocks if empty
        if (gridList.size() == 0) {
            for (int k = 0; k < colCoords.size(); k++) {
                ArrayList<GridBlock> tempList = new ArrayList<>();
                for (int l = 0; l < rowCoords.size(); l++) {
                    tempList.add(new GridBlock(colCoords.get(k), rowCoords.get(l), GRID_WIDTH_HEIGHT));
                }
                gridList.add(tempList);
            }
        }
    }

    // Only used in update(), separated for clarity
    // Needed to be run on UI thread
    private void genNextBlock() {
        this.blocksAdded += 1;
        final ImageView nextImg = ((TetrisActivity) context).findViewById(R.id.tetris_next_img);
        final TetrisSurfaceView currentView = this;
        ((Activity)this.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                switch (rand.nextInt(7)) {
                    case 0:
                        // I Block
                        currentView.nextBlock = new TetrisI(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_i);
                        break;
                    case 1:
                        // J Block
                        currentView.nextBlock = new TetrisJ(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_j);
                        break;
                    case 2:
                        // L Block
                        currentView.nextBlock = new TetrisL(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_l);
                        break;
                    case 3:
                        // O Block
                        currentView.nextBlock = new TetrisO(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_o);
                        break;
                    case 4:
                        // S Block
                        currentView.nextBlock = new TetrisS(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_s);
                        break;
                    case 5:
                        // T Block
                        currentView.nextBlock = new TetrisT(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_t);
                        break;
                    case 6:
                        // Z Block
                        currentView.nextBlock = new TetrisZ(currentView);
                        nextImg.setImageResource(R.drawable.tetris_blk_z);
                        break;
                }
            }
        });
    }

    // Only used in update(), separated for clarity
    private void checkRow() {
        ArrayList<Integer> rowsCleared = new ArrayList<>();

        // Check rows from bottom up
        for (int currentY = GRID_TOTAL_Y - 1; currentY >= 0; currentY--) {
            /*
            Occasionally, the GRID_TOTAL_Y would have an extra 1, causing it to be out of bounds.
            Thus, this check is implemented to prevent it from occurring.
             */
            try {
                //noinspection ResultOfMethodCallIgnored
                gridList.get(0).get(currentY);
            } catch (ArrayIndexOutOfBoundsException e){
                currentY--;
            }

            boolean rowFull = true;
            // Check columns from left to right
            for (int currentX = 0; currentX < GRID_TOTAL_X; currentX++) {
                if (gridList.get(currentX).get(currentY).getBlock() == null) {
                    rowFull = false;
                }
            }

            if (rowFull) {
                rowsCleared.add(currentY);
            }
        }

        // Reset top row
        if (rowsCleared.size() > 0) {
            // Remove counted rows
            for (int currentY = rowsCleared.size() - 1; currentY >= 0; currentY--) {
                // Move rows down by 1
                for (int tempY = rowsCleared.get(currentY); tempY > 0; tempY--) {
                    for (int tempX = 0; tempX < GRID_TOTAL_X; tempX++) {
                        gridList.get(tempX).get(tempY).bindBlock(
                                gridList.get(tempX).get(tempY - 1).getBlock()
                        );
                    }
                }
            }

            for (int tempX = 0; tempX < GRID_TOTAL_X; tempX++) {
                gridList.get(tempX).get(0).unbindBlock();
            }
        }

        // Check points
        this.score += (100 * rowsCleared.size() * rowsCleared.size());
    }

    // Stop tetrisThread and blockDownThread
    private void stopThreads() {
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

        boolean retryDownThread = true;
        while (retryDownThread) {
            try {
                this.blockDownThread.setRunning(false);
                this.blockDownThread.join();
                retryDownThread = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
