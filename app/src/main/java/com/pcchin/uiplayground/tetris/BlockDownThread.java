package com.pcchin.uiplayground.tetris;

class BlockDownThread extends Thread {
    // Thread to deal with moving the block down

    private int WAIT_DURATION = 1000;
    private boolean running;
    private TetrisSurfaceView tetrisSurfaceView;

    BlockDownThread(TetrisSurfaceView tetrisSurfaceView) {
        this.tetrisSurfaceView = tetrisSurfaceView;
    }

    BlockDownThread(TetrisSurfaceView tetrisSurfaceView, int waitDuration) {
        this.tetrisSurfaceView = tetrisSurfaceView;
        WAIT_DURATION = waitDuration;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                sleep(WAIT_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tetrisSurfaceView.targetBlock != null) {
                tetrisSurfaceView.targetBlock.moveDown();
            }

            WAIT_DURATION -= 2;

            if (WAIT_DURATION <= 0) {
                WAIT_DURATION = 2;
            }
        }
    }

    void setRunning(boolean running)  {
        this.running= running;
    }

    int getWaitDuration() {
        return this.WAIT_DURATION;
    }
}
