package com.pcchin.uiplayground.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

class PongSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TODO: Scoring system

    private PongThread pongThread;
    Paddle paddleL;
    Paddle paddleR;
    private PongBall ball;
    private boolean twoUser;

    // Original constructor, keeping it as it is if needed
    public PongSurfaceView(Context context) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        pongThread = new PongThread(this, getHolder());
    }

    // Current constructor in use, allowing us to pass on the twoUser value
    public PongSurfaceView(Context context, boolean twoUser) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        pongThread = new PongThread(this, getHolder());

        this.twoUser = twoUser;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int PADDLE_WALL_DIST = 64;
        int PADDLE_HEIGHT = 80;
        int PADDLE_WIDTH = 16;
        int BALL_DIAMETER = 16;

        this.paddleL = new Paddle(this, PADDLE_WALL_DIST, getHeight()/2 - PADDLE_HEIGHT);
        this.paddleR = new Paddle(this, getWidth() - PADDLE_WALL_DIST - PADDLE_WIDTH, getHeight()/2 - PADDLE_HEIGHT);
        this.ball = new PongBall(this, getWidth()/2 - BALL_DIAMETER /2, getHeight()/2 - BALL_DIAMETER /2, this.paddleL, this.paddleR);

        this.pongThread.setRunning(true);
        this.pongThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
        drawDotted(canvas);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ptrCount = event.getPointerCount();
        // Get display height
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        // One for each pointer on screen
        for (int i = 0; i < ptrCount; i++) {
            // TODO: Solve IllegalArgumentException

            int ptrX = (int) event.getX(event.getPointerId(i));
            int ptrY = (int) event.getY(event.getPointerId(i));
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                /* Firstly, check if 2 players are playing. If not, disable the left paddle.
                 *  Then, check if pointer is within 20dp(x) and -10dp(y) of either paddle.
                 *  Thirdly, check if pointer's y-coordinates is within top boundary.
                 *  Lastly, check if its y-coordinates is within bottom boundary.
                 *  If all above prerequisites are met, the paddle's y-coordinates are set to that of the pointer's.
                */
                if (this.twoUser &&
                        (Math.abs(ptrX - this.paddleL.getX() - (this.paddleL.getWidth() / 2)) < ((this.paddleL.getWidth() / 2) + 20)) &&
                        (Math.abs(ptrY - this.paddleL.getY() - (this.paddleL.getHeight() / 2)) < ((this.paddleL.getHeight() / 2) - 10)) &&
                        ((ptrY - (this.paddleL.getHeight() / 2)) > 0) &&
                        ((ptrY + (this.paddleL.getHeight() / 2)) < height)) {
                    this.paddleL.setY(ptrY - (this.paddleL.getHeight() / 2));
                } else if ((Math.abs(ptrX - this.paddleR.getX() - (this.paddleR.getWidth() / 2)) < ((this.paddleR.getWidth() / 2) + 20)) &&
                        (Math.abs(ptrY - this.paddleR.getY() - (this.paddleR.getHeight() / 2)) < ((this.paddleR.getHeight() / 2) - 10)) &&
                        ((ptrY - (this.paddleR.getHeight() / 2)) > 0) &&
                        ((ptrY + (this.paddleR.getHeight() / 2)) < height)) {
                    this.paddleR.setY(ptrY - (this.paddleR.getHeight() / 2));
                }
            }
        }
        return true;
    }

    public void update() {
        this.paddleL.update();
        this.paddleR.update();
        this.ball.update();
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
}
