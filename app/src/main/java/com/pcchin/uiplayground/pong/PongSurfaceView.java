package com.pcchin.uiplayground.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PongSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TODO: Finish Pong game

    private PongThread pongThread;
    private Paddle paddleL;
    private Paddle paddleR;
    private PongBall ball;

    public PongSurfaceView(Context context) {
        super(context);

        this.setFocusable(true);
        this.getHolder().addCallback(this);

        pongThread = new PongThread(this, getHolder());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int PADDLE_WALL_DIST = 32;
        int PADDLE_HEIGHT = 80;
        int PADDLE_WIDTH = 16;
        int BALL_DIAMETER = 16;

        this.paddleL = new Paddle(this, PADDLE_WALL_DIST, getHeight()/2 - PADDLE_HEIGHT);
        this.paddleR = new Paddle(this, getWidth() - PADDLE_WALL_DIST - PADDLE_WIDTH, getHeight()/2 - PADDLE_HEIGHT);
        this.ball = new PongBall(this, getWidth()/2 - BALL_DIAMETER /2, getHeight()/2 - BALL_DIAMETER /2);

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if ((event.getX() == this.paddleL.getX()) && (event.getY() == this.paddleL.getY())) {
                    this.paddleL.setX((int)event.getX());
                    this.paddleL.setY((int)event.getY());
                } else if ((event.getX() == this.paddleL.getX()) && (event.getY() == this.paddleL.getY())) {
                    this.paddleR.setX((int)event.getX());
                    this.paddleR.setY((int)event.getY());
                }
        }
        return false;
    }

    public void update() {
        this.paddleL.update();
        this.paddleR.update();
        this.ball.update();
    }

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
