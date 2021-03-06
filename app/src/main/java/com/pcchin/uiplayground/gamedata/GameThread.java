package com.pcchin.uiplayground.gamedata;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/** Common thread used in all games **/
public class GameThread extends Thread {
    private boolean running;
    private boolean updateOnTop;
    private final SurfaceHolder surfaceHolder;
    private GameView surfaceView;

    public GameThread(GameView surfaceView, SurfaceHolder surfaceHolder, boolean updateOnTop) {
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceHolder;
        this.updateOnTop = updateOnTop;
    }

    @Override
    public void run()  {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        int targetFPS = 30;
        long targetTime = 1000 / targetFPS;

        while(this.running)  {
            startTime = System.nanoTime();
            Canvas canvas = null;
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();

                // Synchronized
                synchronized (surfaceHolder)  {
                    // Certain games are picky about which order these two are called
                    if (updateOnTop) {
                        this.surfaceView.update();
                        this.surfaceView.draw(canvas);
                    } else {
                        this.surfaceView.draw(canvas);
                        this.surfaceView.update();
                    }
                }
            }catch(Exception e)  {
                // Do nothing.
                e.printStackTrace();
            } finally {
                if(canvas != null)  {
                    // Unlock Canvas.
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                sleep(waitTime);
            } catch (Exception ignored) {}
            frameCount++;
            if (frameCount == targetFPS)        {
                frameCount = 0;
            }
        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}
