package com.pcchin.uiplayground.tetris;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.pcchin.uiplayground.MainActivity;
import com.pcchin.uiplayground.R;

import java.util.Locale;

public class TetrisActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            pressExit();
        }
    }

    void pressExit() {
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1500);
    }

    void updateScore() {
        TetrisSurfaceView tetrisSurfaceView = findViewById(R.id.tetrisSurfaceView);
        TextView tetrisScore = findViewById(R.id.tetris_score);
        tetrisScore.setText(String.format(Locale.ENGLISH, "Score: %d", tetrisSurfaceView.score));
    }
}
