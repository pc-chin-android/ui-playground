package com.pcchin.uiplayground.chess;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pcchin.uiplayground.MainActivity;
import com.pcchin.uiplayground.R;

public class ChessActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private ChessSurfaceView chessSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_chess);
        setSupportActionBar(toolbar);

        // Set surface view
        chessSurfaceView = findViewById(R.id.chessSurfaceView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_settings_return:
                Intent returnIntent = new Intent(this, MainActivity.class);
                startActivity(returnIntent);
                return true;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, ChessSettings.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_history:
                Intent historyIntent = new Intent(this, ChessHistory.class);
                startActivity(historyIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chessSurfaceView.draw(chessSurfaceView.getHolder().lockCanvas());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
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
}