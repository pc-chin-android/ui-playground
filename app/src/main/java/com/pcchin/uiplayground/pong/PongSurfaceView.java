package com.pcchin.uiplayground.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pcchin.uiplayground.GeneralFunctions;
import com.pcchin.uiplayground.R;

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
        int PADDLE_HEIGHT = 64;
        int PADDLE_WIDTH = 16;
        int BALL_DIAMETER = 16;

        this.paddleL = new Paddle(this, PADDLE_WALL_DIST, getHeight()/2 - PADDLE_HEIGHT /2);
        this.paddleR = new Paddle(this, getWidth() - PADDLE_WALL_DIST - PADDLE_WIDTH, getHeight()/2 - PADDLE_HEIGHT /2);
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
        int DASH_WIDTH = 11;

        super.draw(canvas);
        this.paddleL.draw(canvas);
        this.paddleR.draw(canvas);
        this.ball.draw(canvas);
        // TODO: Fix Dash not appearing
        canvas.drawBitmap(GeneralFunctions.getBitmap(R.drawable.white_dash, this.getContext()), getWidth()/2 - DASH_WIDTH/2, 0, null);
    }

    public void update() {
        this.paddleL.update();
        this.paddleR.update();
        this.ball.update();
    }
}
