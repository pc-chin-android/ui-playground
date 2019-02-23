package com.pcchin.uiplayground.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pcchin.uiplayground.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChessHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_history);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChessActivity.class);
        startActivity(intent);
    }

    public void exportTxt(View view) {
        TextView historyText = findViewById(R.id.chess_hist_text);
        File exportDir = new File("/storage/emulated/0/Download");
        String EXPORT_FILENAME = "/UIPlayground-Chess";

        // Directory check
        if (historyText.getText() != null) {
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
            File export_txt = new File(exportDir.getAbsolutePath() + EXPORT_FILENAME + ".txt");
            File target_txt;
            if (export_txt.exists()) {
                int index = 1;
                while (true) {
                    // Add index to file name
                    target_txt = new File(exportDir.getAbsolutePath() + EXPORT_FILENAME
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
                FileWriter fileWriter = new FileWriter(target_txt);
                fileWriter.append(String.valueOf(historyText.getText()));
                fileWriter.append("\n\n");
                fileWriter.flush();
                fileWriter.close();
                Toast.makeText(this, "Text file saved to" + target_txt.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
