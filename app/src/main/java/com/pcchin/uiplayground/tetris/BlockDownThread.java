package com.pcchin.uiplayground.tetris;

class BlockDownThread extends Thread {
    // Thread to deal with moving the block down

    private boolean running;
    private TetrisSurfaceView tetrisSurfaceView;

    BlockDownThread(TetrisSurfaceView tetrisSurfaceView) {
        this.tetrisSurfaceView = tetrisSurfaceView;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                // Variable set to allow for easy editing in future
                int WAIT_DURATION = 1000;
                if (WAIT_DURATION - (tetrisSurfaceView.blocksAdded * 2) < 50) {
                    sleep(50);
                } else {
                    sleep(WAIT_DURATION - (tetrisSurfaceView.blocksAdded * 5));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tetrisSurfaceView.targetBlock != null) {
                tetrisSurfaceView.targetBlock.moveDown();
            }
        }
    }

    void setRunning(boolean running)  {
        this.running= running;
    }
}
