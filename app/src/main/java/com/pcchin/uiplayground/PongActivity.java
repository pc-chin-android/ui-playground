package com.pcchin.uiplayground;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

public class PongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);

        // Set buttons to reset when pressed
        CompoundButton.OnCheckedChangeListener resetListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stopGame(buttonView);
            }
        };
        RadioButton btn_1p = findViewById(R.id.pong_1p_btn);
        RadioButton btn_2p = findViewById(R.id.pong_2p_btn);
        btn_1p.setOnCheckedChangeListener(resetListener);
        btn_2p.setOnCheckedChangeListener(resetListener);

        stopGame(findViewById(R.id.pong_game));
    }

    public void startGame(View view) {
        RadioButton p2_btn = findViewById(R.id.pong_2p_btn);
        // Set drag listener for paddles
        findViewById(R.id.pong_paddle_r).setOnTouchListener(new DragListener());
        if (p2_btn.isChecked()) {
            findViewById(R.id.pong_paddle_l).setOnTouchListener(new DragListener());
        }
    }

    public void pauseGame(View view) {}

    public void resumeGame(View view) {}

    public void stopGame(final View view) {
        // Disable touch input
        ViewGroup gameView = findViewById(R.id.pong_game);
        disableEnableControls(false, gameView);


        // Reset score
        RadioButton oneP = findViewById(R.id.pong_1p_btn);
        TextView textView = findViewById(R.id.pong_1p_score);
        TextView lText = findViewById(R.id.pong_l_score);
        TextView rText = findViewById(R.id.pong_r_score);

        if (oneP.isChecked()) {
            textView.setText(String.format(Locale.ENGLISH, "%s", Integer.toString(0)));
            lText.setText("");
            rText.setText("");
        } else {
            textView.setText("");
            lText.setText(String.format(Locale.ENGLISH, "%s", Integer.toString(0)));
            rText.setText(String.format(Locale.ENGLISH, "%s", Integer.toString(0)));
        }

        // Update states
        Button generalButton = findViewById(R.id.pong_general_btn);
        generalButton.setText(R.string.start);
        TextView stateText = findViewById(R.id.pong_status);
        stateText.setText(R.string.stopped);

        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        // Reset ball
                        ViewGroup gameView = findViewById(R.id.pong_game);
                        View ball = findViewById(R.id.pong_ball);
                        ball.setX(gameView.getWidth()/2 - ball.getWidth()/2);
                        ball.setY(gameView.getHeight()/2 - ball.getHeight()/2);

                        // Reset heights of paddles
                        View paddle_l = findViewById(R.id.pong_paddle_l);
                        View paddle_r = findViewById(R.id.pong_paddle_r);
                        paddle_l.setY(gameView.getHeight()/2 - paddle_l.getHeight()/2);
                        paddle_r.setY(gameView.getHeight()/2 - paddle_r.getHeight()/2);

                        // Don't forget to remove your listener when you are done with it.
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        // TODO: Remove after startGame added
        disableEnableControls(true, gameView);
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void generalBtnFunc(Button view) {
        /*
        switch(view.getText().toString()) {
            // TODO: Complete switch statement

            case "Start":
                break;
            case "Resume":
                break;
            case "Pause":
                break;
            */
        }

    private class DragListener implements View.OnTouchListener {
        float current;
        ViewGroup gameView = findViewById(R.id.pong_game);

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            current = event.getRawY();
            switch(event.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if ((current - v.getHeight() > gameView.getY()) && (current < gameView.getY() + gameView.getHeight())) {
                        v.setY(current - gameView.getY() - v.getHeight());
                        break;
                    }
            }
            return true;
        }
    }

    private void disableEnableControls(boolean enable, @NonNull ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }
}
