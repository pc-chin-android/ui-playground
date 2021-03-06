package com.pcchin.uiplayground.tetris;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pcchin.uiplayground.MainActivity;
import com.pcchin.uiplayground.R;

import java.util.Locale;
import java.util.Objects;

public class TetrisActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private TetrisSurfaceView tetrisSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_tetris);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        (findViewById(R.id.tetris_stop)).setEnabled(false);
        (findViewById(R.id.tetris_rotate)).setEnabled(false);
        tetrisSurfaceView = findViewById(R.id.tetrisSurfaceView);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tetrisSurfaceView.gameState == TetrisSurfaceView.NORMAL) {
            tetrisSurfaceView.onGamePause();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            tetrisSurfaceView.mediaPlayer.stop();
            tetrisSurfaceView.gameState = TetrisSurfaceView.STOPPED;

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Return button at top
        if (Objects.equals(item.getItemId(), R.id.menu_return)) {
            tetrisSurfaceView.mediaPlayer.stop();
            tetrisSurfaceView.gameState = TetrisSurfaceView.STOPPED;

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onMainBtnPressed(View view) {
        String text = ((Button) view).getText().toString();
        if (text.equals(getString(R.string.start))) {
            tetrisSurfaceView.onGameStart();
        } else if (text.equals(getString(R.string.pause))) {
            tetrisSurfaceView.onGamePause();
        } else if (text.equals(getString(R.string.resume))) {
            tetrisSurfaceView.onGameResume();
        }
    }

    // Carry over function
    public void onResetBtnPressed(View view) {
        tetrisSurfaceView.onGameStop();
    }

    // Carry over function
    public void onRotateBtnPressed(View view) {
        if (tetrisSurfaceView.targetBlock != null) {
            tetrisSurfaceView.targetBlock.rotate();
        }
    }

    // Carry over function
    public void onDirBtnPressed(View view) {
        if (tetrisSurfaceView.gameState == TetrisSurfaceView.NORMAL && tetrisSurfaceView.targetBlock != null) {
            switch (view.getId()) {
                case R.id.tetris_dir_left:
                    tetrisSurfaceView.targetBlock.moveLeft();
                    break;
                case R.id.tetris_dir_right:
                    tetrisSurfaceView.targetBlock.moveRight();
                    break;
                case R.id.tetris_dir_down:
                    tetrisSurfaceView.targetBlock.moveDown();
                    break;
            }
        }
    }

    void updateScore() {
        TetrisSurfaceView tetrisSurfaceView = findViewById(R.id.tetrisSurfaceView);
        TextView tetrisScore = findViewById(R.id.tetris_score);
        tetrisScore.setText(String.format(Locale.ENGLISH, "Score: %d", tetrisSurfaceView.score));
    }
}
