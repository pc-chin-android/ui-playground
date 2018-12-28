package com.pcchin.uiplayground.pong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pcchin.uiplayground.MainActivity;
import com.pcchin.uiplayground.R;

public class PongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);

        // Bind buttons
        findViewById(R.id.pong_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.pong_1p_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PongGame.class);
                intent.putExtra("Player", 1);
                startActivity(intent);
            }
        });

        findViewById(R.id.pong_2p_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PongGame.class);
                intent.putExtra("Player", 2);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
