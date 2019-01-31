package com.pcchin.uiplayground;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TttActivity extends AppCompatActivity {

    private boolean isTouchable= true;
    private boolean player1Playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_ttt);
        setSupportActionBar(toolbar);

        // Reset game
        resetGame(getCurrentFocus());

        // Set change state listeners for both buttons
        RadioButton button1 = findViewById(R.id.ttt_l_button);
        RadioButton button2 = findViewById(R.id.ttt_r_button);
        CompoundButton.OnCheckedChangeListener resetListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetGame(buttonView);
            }
        };
        button1.setOnCheckedChangeListener(resetListener);
        button2.setOnCheckedChangeListener(resetListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Objects.equals(item.getItemId(), R.id.menu_return)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onImgBtnSelected(View view) {
        DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetGame(getCurrentFocus());
            }
        };

        setTouchable(false);
        ImageView view1 = (ImageView)view;
        if (Objects.equals(view1.getTag(), 0)) {
            RadioButton radioButton = findViewById(R.id.ttt_r_button);

            // Add all imageButtons to array
            List<ImageButton> tttList = new ArrayList<>();
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_1));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_2));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_3));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_4));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_5));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_6));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_7));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_8));
            tttList.add((ImageButton)findViewById(R.id.ttt_btn_9));

            // Shows that this box can be selected
            if (player1Playing) {
                // If 1st player is playing
                view1.setImageResource(R.drawable.ttt_cross);
                view1.setTag(1);
            } else if (radioButton.isChecked()){
                // Checks if 2nd player is playing
                view1.setImageResource(R.drawable.ttt_circle);
                view1.setTag(-1);
            }

            if (checkWinner(tttList)) {
                // Check if game won
                if (radioButton.isChecked()) {
                    if (player1Playing) {
                        GeneralFunctions.displayDialog(this, GeneralFunctions.TWO_1_WIN, listener);
                        return;
                    } else {
                        GeneralFunctions.displayDialog(this, GeneralFunctions.TWO_2_WIN, listener);
                        return;
                    }
                } else {
                    GeneralFunctions.displayDialog(this, GeneralFunctions.ONE_WIN, listener);
                    return;
                }
            } else if (checkDraw(tttList)) {
                // Check if draw
                GeneralFunctions.displayDialog(this, GeneralFunctions.DRAW, listener);
                return;
            }

            player1Playing = !player1Playing;

            // Check if 1 player is selected
            if (! radioButton.isChecked()) {
                // Computer moves
                List<Integer> possibleMoves = new ArrayList<>();
                for (int i=0; i < tttList.size(); i++) {
                    if (((int) tttList.get(i).getTag() == 0)) {
                        possibleMoves.add(i);
                    }
                }

                // Check if possibleMoves is empty
                if (! (possibleMoves.size() == 0)) {
                    int moveTaken = bestMove(possibleMoves, tttList);

                    tttList.get(moveTaken).setImageResource(R.drawable.ttt_circle);
                    tttList.get(moveTaken).setTag(-1);
                }

                if (checkWinner(tttList)) {
                    GeneralFunctions.displayDialog(this, GeneralFunctions.ONE_LOSE, listener);
                    return;
                } else if (checkDraw(tttList)) {
                    GeneralFunctions.displayDialog(this, GeneralFunctions.DRAW, listener);
                    return;
                }

                player1Playing = !player1Playing;
            }
        }
        setTouchable(true);
    }

    public void resetGame(View view) {
        // Add all imageButtons to array
        List<ImageButton> tttList = new ArrayList<>();
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_1));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_2));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_3));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_4));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_5));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_6));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_7));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_8));
        tttList.add((ImageButton)findViewById(R.id.ttt_btn_9));

        // Reset all img in array back to white
        for (int i=0; i<tttList.size(); i++) {
            tttList.get(i).setImageResource(R.drawable.ttt_white);
            tttList.get(i).setTag(0);
        }

        // Reset turn
        player1Playing = true;
    }

    private boolean checkWinner(List<ImageButton> tttList) {
        List<Integer> tagList = new ArrayList<>();
        int tagRequired;
        if (player1Playing) {
            tagRequired = 1;
        } else {
            tagRequired = -1;
        }

        // Append tags to array
        for (int i=0; i<tttList.size(); i++) {
            tagList.add((int)tttList.get(i).getTag());
        }

        // Check individual statuses; makes it easier to understand
        boolean win1 = (tagList.get(0) == tagRequired);
        boolean win2 = (tagList.get(1) == tagRequired);
        boolean win3 = (tagList.get(2) == tagRequired);
        boolean win4 = (tagList.get(3) == tagRequired);
        boolean win5 = (tagList.get(4) == tagRequired);
        boolean win6 = (tagList.get(5) == tagRequired);
        boolean win7 = (tagList.get(6) == tagRequired);
        boolean win8 = (tagList.get(7) == tagRequired);
        boolean win9 = (tagList.get(8) == tagRequired);

        return (win1 && win2 && win3) || // Top row
                (win4 && win5 && win6) || // Middle row
                (win7 && win8 && win9) || // Bottom row
                (win1 && win4 && win7) || // Left column
                (win2 && win5 && win8) || // Middle column
                (win3 && win6 && win9) || // Right column
                (win1 && win5 && win9) || // Diagonal 1
                (win3 && win5 && win7);
    }

    private boolean checkDraw(@NonNull List<ImageButton> tttList) {
        // Check if any of the tags are blank
        for (int i=0; i<tttList.size(); i++) {
            if ((int)tttList.get(i).getTag() == 0) {
                return false;
            }
        }
        return true;
    }

    private int bestMove(@NonNull List<Integer> possibleMoves, List<ImageButton> tttList) {
        Random random = new Random();
        // Move taken priority: Win game --> Prevent player from winning --> Random

        // Check if any of the moves can result in victory
        for (int i=0; i < possibleMoves.size(); i++) {
            tttList.get(possibleMoves.get(i)).setTag(-1);
            if (checkWinner(tttList)) {
                return possibleMoves.get(i);
            }
            tttList.get(possibleMoves.get(i)).setTag(0);
        }

        // Prevent player from winning
        for (int i=0; i < possibleMoves.size(); i++) {
            tttList.get(possibleMoves.get(i)).setTag(1);
            player1Playing = !player1Playing;
            if (checkWinner(tttList)) {
                player1Playing = !player1Playing;
                tttList.get(possibleMoves.get(i)).setTag(0);
                return possibleMoves.get(i);
            }
            player1Playing = !player1Playing;
            tttList.get(possibleMoves.get(i)).setTag(0);
        }

        // Random move
        return possibleMoves.get(random.nextInt(possibleMoves.size()));

    }

    // Enable/disable user input

    public void setTouchable(boolean isTouchable) {
        this.isTouchable = isTouchable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isTouchable){
            return super.onTouchEvent(event); // Enable touch event
        }
        return false; // Block touch event
    }

    @Override
    public void onBackPressed() {
        // Return to main menu
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
