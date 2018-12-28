package com.pcchin.uiplayground.pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PongGame extends Activity {

    int playerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PongSurfaceView(this));

        Intent intent = getIntent();
        playerCount = intent.getIntExtra("Player", 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
