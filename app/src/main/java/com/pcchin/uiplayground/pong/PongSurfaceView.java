package com.pcchin.uiplayground.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.pcchin.uiplayground.GeneralFunctions;
import com.pcchin.uiplayground.R;

import java.util.Objects;

class PongSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private PongThread pongThread;
    private Context context;
    private int winCount;
    Paddle paddleL;
    Paddle paddleR;
    PongBall ball;
    private PongBall fakeBall;
    Score paused;
    private Score scoreL;
    private Score scoreR;
    boolean twoUser;
    boolean touchEnabled;
    boolean gameOverDisplayed;
    MediaPlayer mediaPlayer;

    private static final int PADDLE_WALL_DIST = 64;
    private static final int PADDLE_HEIGHT = 80;
    private static final int PADDLE_WIDTH = 16;
    private static final int BALL_DIAMETER = 16;

    // Original constructor, keeping it as it is if needed
    public PongSurfaceView(Context context) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        this.context = context;
        this.gameOverDisplayed = false;
        pongThread = new PongThread(this, getHolder());
    }

    // Current constructor in use, allowing us to pass on the twoUser value
    public PongSurfaceView(Context context, boolean twoUser, int winCount) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        pongThread = new PongThread(this, getHolder());

        this.context = context;
        this.twoUser = twoUser;
        this.winCount = winCount;
        this.gameOverDisplayed = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap pausedBitmap = GeneralFunctions.textToBitmap("Game paused", Color.RED,
                120, "orbitron", Typeface.BOLD, true, this.getContext());

        this.paddleL = new Paddle(this, PADDLE_WALL_DIST, getHeight()/2 - PADDLE_HEIGHT);
        this.paddleR = new Paddle(this, getWidth() - PADDLE_WALL_DIST - PADDLE_WIDTH, getHeight()/2 - PADDLE_HEIGHT);
        this.ball = new PongBall(this, getWidth()/2 - BALL_DIAMETER/2 - 8, getHeight()/2 - BALL_DIAMETER /2, this.paddleL, this.paddleR,
                GeneralFunctions.getBitmap(R.drawable.white_circle, getContext()), true);
        this.fakeBall = new PongBall(this, getWidth()/2 - BALL_DIAMETER/2 - 8, getHeight()/2 - BALL_DIAMETER /2, this.paddleL, this.paddleR,
                Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888), false);
        this.scoreL = new Score(this,Double.valueOf(getWidth()*0.25).intValue(), 10);
        this.scoreR = new Score(this,Double.valueOf(getWidth()*0.75).intValue() - Score.scoreToBitmap(0, this.getContext()).getWidth(), 10);
        this.paused = new Score(this, getWidth()/2 - pausedBitmap.getWidth()/2, getHeight()/2 - pausedBitmap.getHeight()/2, "Game paused");
        this.touchEnabled = true;
        this.pongThread.setRunning(true);
        this.pongThread.start();
        ((PongGame) context).gameState = PongGame.NORMAL;

        mediaPlayer = GeneralFunctions.mediaPlayerCreator(context, AudioAttributes.CONTENT_TYPE_SONIFICATION);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.touchEnabled = false;

        boolean retry = true;
        while (retry) {
            try{
                this.pongThread.setRunning(false);
                this.pongThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        this.paddleL.draw(canvas);
        this.paddleR.draw(canvas);
        this.ball.draw(canvas);
        this.fakeBall.draw(canvas);
        this.scoreL.draw(canvas);
        this.scoreR.draw(canvas);
        drawDotted(canvas);
        this.paused.draw(canvas);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchEnabled) {
            int ptrCount = event.getPointerCount();
            // Get display height
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            // One for each pointer on screen
            for (int i = 0; i < ptrCount; i++) {
                // IllegalArgumentException will appear here
                // However, it cannot be suppressed with try/catch due to reasons unknown

                int ptrX = (int) event.getX(event.getPointerId(i));
                int ptrY = (int) event.getY(event.getPointerId(i));
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        /* Firstly, check if 2 players are playing. If not, disable the left paddle.
                         *  Then, check if pointer is within 40dp(x) and 30dp(y) of either paddle.
                         *  Thirdly, check if pointer's y-coordinates is within top boundary.
                         *  Lastly, check if its y-coordinates is within bottom boundary.
                         *  If all above prerequisites are met, the paddle's y-coordinates are set to that of the pointer's.
                         */
                        if (this.twoUser &&
                                (Math.abs(ptrX - this.paddleL.getX() - (this.paddleL.getWidth() / 2)) < ((this.paddleL.getWidth() / 2) + 40)) &&
                                (Math.abs(ptrY - this.paddleL.getY() - (this.paddleL.getHeight() / 2)) < ((this.paddleL.getHeight() / 2) + 30)) &&
                                ((ptrY - (this.paddleL.getHeight() / 2)) > 0) &&
                                ((ptrY + (this.paddleL.getHeight() / 2)) < height)) {
                            this.paddleL.setY(ptrY - (this.paddleL.getHeight() / 2));
                        } else if ((Math.abs(ptrX - this.paddleR.getX() - (this.paddleR.getWidth() / 2)) < ((this.paddleR.getWidth() / 2) + 40)) &&
                                (Math.abs(ptrY - this.paddleR.getY() - (this.paddleR.getHeight() / 2)) < ((this.paddleR.getHeight() / 2) + 30)) &&
                                ((ptrY - (this.paddleR.getHeight() / 2)) > 0) &&
                                ((ptrY + (this.paddleR.getHeight() / 2)) < height)) {
                            this.paddleR.setY(ptrY - (this.paddleR.getHeight() / 2));
                        }
                }
            }
        } else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            switch (((PongGame) context).gameState) {
                case PongGame.NORMAL:
                    // Restart game if the game is not paused
                    this.pongThread.start();
                    this.touchEnabled = true;
                    this.ball.setEnabled(true);
                    this.fakeBall.setEnabled(true);
                    break;
                case PongGame.PAUSED:
                    // Resume game if game is paused
                    ((PongGame) context).enableAll(true);
                    break;
                case PongGame.GAME_OVER:
                    // Return to selection menu
                    mediaPlayer.release();
                    Intent intent = new Intent(context, PongActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
        return true;
    }

    public void update() {
        if ((!twoUser) && this.touchEnabled) {
            this.updateAi();
        }
        this.paddleL.update();
        this.paddleR.update();
        this.ball.update();
        this.fakeBall.update();

        checkScore();

        checkWin();
    }

    // Only used in update(), split to increase readability
    private void checkScore() {
        // Score is made
        if ((this.ball.getX() < 0) || (this.ball.getX() > getWidth())) {
            GeneralFunctions.playAudioOnce(context, R.raw.bleep, mediaPlayer);

            this.touchEnabled = false;
            this.pongThread = new PongThread(this, getHolder());

            // Right scores
            if (this.ball.getX() < 0) {
                this.scoreR.update();
                // Left scores
            } else if (this.ball.getX() > getWidth()) {
                this.scoreL.update();
            }

            // Reset positions of sprites
            this.paddleL.setX(PADDLE_WALL_DIST);
            this.paddleL.setY(getHeight()/2 - PADDLE_HEIGHT);
            this.paddleR.setX(getWidth() - PADDLE_WALL_DIST - PADDLE_WIDTH);
            this.paddleR.setY(getHeight()/2 - PADDLE_HEIGHT);
            this.ball.setX(getWidth()/2 - 16 - BALL_DIAMETER/2);
            this.ball.setY(getHeight()/2 - BALL_DIAMETER /2);
            this.ball.setEnabled(false);
            this.fakeBall.setX(getWidth()/2 - 16 - BALL_DIAMETER/2);
            this.fakeBall.setY(getHeight()/2 - BALL_DIAMETER /2);
            this.fakeBall.setEnabled(false);

        }
    }

    // Only used in update(), split to increase readability
    private void checkWin() {
        // Check if a dialog is already displayed
        if (!this.gameOverDisplayed) {
            // Check left paddle
            if (Objects.equals(this.scoreL.currentScore, this.winCount) && (this.scoreL.currentScore != 0)) {
                if (twoUser) {
                    gameOver(GeneralFunctions.TWO_1_WIN);
                } else {
                    gameOver(GeneralFunctions.ONE_LOSE);
                }
                // Right paddle
            } else if (Objects.equals(this.scoreR.currentScore, this.winCount) && (this.scoreR.currentScore != 0)) {
                if (twoUser) {
                    gameOver(GeneralFunctions.TWO_2_WIN);
                } else {
                    gameOver(GeneralFunctions.ONE_WIN);
                }
            }
        }
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawDotted(@NonNull Canvas canvas) {
        // Start x, start y, end x, end y
        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] {50, 30}, 0));
        paint.setStrokeWidth(16);

        canvas.drawLine(getWidth()/2 - 8, 0, getWidth()/2 + 8, getHeight(), paint);
    }

    // Triggered when game ends
    private void gameOver(final int status) {
        GeneralFunctions.playAudioOnce(context, R.raw.robot_bleep, mediaPlayer);
        this.gameOverDisplayed = true;
        ((PongGame) context).gameState = PongGame.GAME_OVER;
        this.touchEnabled = false;

        ((PongGame) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(@NonNull DialogInterface dialog) {
                        dialog.dismiss();
                    }
                };
                GeneralFunctions.displayDialog(context, status, listener);
            }
        });
    }

    // To update location of left paddle if 1 player
    private void updateAi() {
        this.paddleL.setY(this.fakeBall.getY() - this.paddleL.getHeight()/2 + this.fakeBall.getHeight()/2);

        // Boundary check
        if (this.paddleL.y < 0) {
            this.paddleL.setY(0);
        } else if (this.paddleL.y + this.paddleL.getHeight() > this.getHeight()) {
            this.paddleL.setY(this.getHeight() - this.paddleL.getHeight());
        }
    }
}
