package com.pcchin.uiplayground;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TttActivity extends AppCompatActivity {
    private final int DRAW = 0;
    private final int ONE_WIN = 1;
    private final int ONE_LOSE = 2;
    private final int TWO_1_WIN = -1;
    private final int TWO_2_WIN = -2;

    private boolean player1Playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_ttt);
        setSupportActionBar(toolbar);

        // Hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Reset game
        resetGame(getCurrentFocus());
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
        ImageView view1 = (ImageView)view;
        if (Objects.equals(view1.getTag(), 0)) {
            RadioButton radioButton = findViewById(R.id.ttt_r_button);

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

            if (checkWinner()) {
                // Check if game won
                if (radioButton.isChecked()) {
                    if (player1Playing) {
                        displayDialog(TWO_1_WIN);
                    } else {
                        displayDialog(TWO_2_WIN);
                    }
                } else {
                    displayDialog(ONE_WIN);
                }
            } else if (checkDraw()) {
                // Check if draw
                displayDialog(DRAW);
            }

            player1Playing = !player1Playing;

            // Check if 1 player is selected
            if (! radioButton.isChecked()) {
                // TODO: Complete Algorithm
                if (checkWinner()) {
                    displayDialog(ONE_LOSE);
                } else if (checkDraw()) {
                    displayDialog(DRAW);
                }
            }
        }
    }

    @SuppressWarnings("unused")
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
            tttList.get(i).setImageResource(android.R.color.white);
            tttList.get(i).setTag(0);
        }

        // Reset turn
        player1Playing = true;
    }

    private boolean checkWinner() {
        List<Integer> tagList = new ArrayList<>();
        int tagRequired;
        if (player1Playing) {
            tagRequired = 1;
        } else {
            tagRequired = -1;
        }

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

        // Append tags to array
        for (int i=0; i<tttList.size(); i++) {
            tagList.add((int)tttList.get(i).getTag());
        }

        // FIXME: Fix giant || error

        // Check if any of the victory conditions are met
        return ((tagList.get(0) == tagRequired) && (tagList.get(1) == tagRequired) && (tagList.get(2) == tagRequired)) || // Top row
                ((tagList.get(3) == tagRequired) && (tagList.get(4) == tagRequired) && (tagList.get(5) == tagRequired)) || // Middle row
                ((tagList.get(6) == tagRequired) && (tagList.get(7) == tagRequired) && (tagList.get(8) == tagRequired)) || // Bottom row
                ((tagList.get(0) == tagRequired) && (tagList.get(3) == tagRequired) && (tagList.get(6) == tagRequired)) || // Left column
                ((tagList.get(1) == tagRequired) && (tagList.get(4) == tagRequired) && (tagList.get(7) == tagRequired)) || // Middle column
                ((tagList.get(2) == tagRequired) && (tagList.get(5) == tagRequired) && (tagList.get(8) == tagRequired)) || // Right column
                ((tagList.get(0) == tagRequired) && (tagList.get(4) == tagRequired) && (tagList.get(8) == tagRequired)) || // Diagonal 1
                ((tagList.get(2) == tagRequired) && (tagList.get(4) == tagRequired) && (tagList.get(6) == tagRequired));

    }

    private boolean checkDraw() {
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

        // Check if any of the tags are blank
        for (int i=0; i<tttList.size(); i++) {
            if ((int)tttList.get(i).getTag() == 0) {
                return true;
            }
        }

        return false;
    }

    private void displayDialog(int state) {
        AlertDialog.Builder displayDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        displayDialogBuilder.setTitle(R.string.game_over);
        // Bind OK button to dismiss dialog
        displayDialogBuilder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        displayDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetGame(getCurrentFocus());
            }
        });
        // Display game over dialog
        switch (state) {
            case DRAW:
                displayDialogBuilder.setMessage(getString(R.string.draw_details));
            case ONE_WIN:
                displayDialogBuilder.setMessage(getString(R.string.you_win));
            case ONE_LOSE:
                displayDialogBuilder.setMessage(getString(R.string.you_lost));
            case TWO_1_WIN:
                displayDialogBuilder.setMessage(getString(R.string.player_1_wins));
            case TWO_2_WIN:
                displayDialogBuilder.setMessage(getString(R.string.player_2_wins));
        }
        AlertDialog displayDialog = displayDialogBuilder.create();
        displayDialog.show();
    }
}
