package com.pcchin.uiplayground.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pcchin.uiplayground.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ChessHistory extends AppCompatActivity {
    private ArrayList<ArrayList<Integer>> moveList = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> currentCoordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveList = ChessActivity.getIntList(savedInstanceState, "ChessMoveList");
        currentCoordsList = ChessActivity.getIntList(savedInstanceState, "ChessBoardCoords");
        setContentView(R.layout.activity_chess_history);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChessActivity.class);
        ChessActivity.putIntList(intent, moveList, "ChessHistMoveList");
        ChessActivity.putIntList(intent, currentCoordsList, "ChessHistBoardCoords");
        startActivity(intent);
    }

    public void exportTxt(View view) {
        // Note: this function does not work on emulators
        TextView historyText = findViewById(R.id.chess_hist_text);
        File exportDir = new File("/storage/emulated/0/Download");
        String EXPORT_FILENAME = "UIPlayground-Chess";

        // Directory check
        if (historyText.getText().length() > 0) {
            // Check if directory exists
            if (! (exportDir.exists() && exportDir.isDirectory())) {
                // Make file
                boolean success = exportDir.mkdir();
                if (!success) {
                    // Fall back to root directory
                    exportDir = new File("/storage/emulated/0");
                }
            }

            // Check if file name exists
            File export_txt = new File(exportDir, EXPORT_FILENAME + ".txt");
            File target_txt;
            if (export_txt.exists()) {
                int index = 1;
                while (true) {
                    // Add index to file name
                    target_txt = new File(exportDir, EXPORT_FILENAME
                            + "(" + Integer.toString(index) + ").txt");
                    if (target_txt.exists()) {
                        index++;
                    } else {
                        break;
                    }
                }
            } else {
                target_txt = export_txt;
            }

            // Write to file
            try {
                // Separated to avoid IOException if mkdirs == false
                if (target_txt.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(target_txt);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    outputStreamWriter.write(String.valueOf(historyText.getText()));
                    outputStreamWriter.close();
                    Toast.makeText(this, "Text file saved to" + target_txt.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
