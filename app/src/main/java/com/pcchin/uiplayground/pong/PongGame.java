package com.pcchin.uiplayground.pong;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class PongGame extends Activity {
    static final int NORMAL = 0;
    static final int PAUSED = 1;
    static final int GAME_OVER = -1;

    int playerCount;
    int winCount;
    int gameState;
    private boolean doubleBackToExitPressedOnce = false;

    private PongSurfaceView pongSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        playerCount = intent.getIntExtra("Player", 1);
        winCount = intent.getIntExtra("WinCount", 0);

        this.pongSurfaceView = new PongSurfaceView(this, playerCount != 1, winCount);
        setContentView(this.pongSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableAll(false);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.gameState = GAME_OVER;
        }

        switch (this.gameState) {
            // The enableAll are inverted here to change the current game state
            // (From normal to paused and vice versa)
            case NORMAL:
                enableAll(false);
                pressExit();
                break;
            case PAUSED:
                enableAll(true);
                pressExit();
                break;
            case GAME_OVER:
                Intent intent = new Intent(this, PongActivity.class);
                startActivity(intent);
                break;
        }
    }

    // Disable all touch inputs and movements in PongSurfaceView
    void enableAll(boolean enabled) {
        // Prevent error at start of game
        if ((this.pongSurfaceView != null) && (this.pongSurfaceView.ball != null) && (this.pongSurfaceView.paused != null)) {
            if (enabled) {
                this.gameState = NORMAL;
                this.pongSurfaceView.paused.pauseShow(false);
            } else {
                this.gameState = PAUSED;
                this.pongSurfaceView.paused.pauseShow(true);
            }

            this.pongSurfaceView.touchEnabled = enabled;
            // Directly modifying the variable to prevent the ball's velocity from being reset
            this.pongSurfaceView.ball.enabled = enabled;
        }
    }

    private void pressExit() {
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1500);
    }
}
