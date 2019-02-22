package com.pcchin.uiplayground.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pcchin.uiplayground.MainActivity;
import com.pcchin.uiplayground.R;

public class ChessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_chess);
        setSupportActionBar(toolbar);

        // Hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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
                // Intent settingsIntent = new Intent(this, ChessSettings.class);
                // startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}