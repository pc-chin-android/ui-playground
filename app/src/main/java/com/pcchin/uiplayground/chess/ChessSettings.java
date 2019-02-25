package com.pcchin.uiplayground.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pcchin.uiplayground.R;

import java.util.ArrayList;

// Class is public for the fragment
public class ChessSettings extends Activity {
    private ArrayList<ArrayList<Integer>> moveList = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> currentCoordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
        moveList = ChessActivity.getIntList(savedInstanceState, "ChessMoveList");
        currentCoordsList = ChessActivity.getIntList(savedInstanceState, "ChessBoardCoords");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChessActivity.class);
        ChessActivity.putIntList(intent, moveList, "ChessHistMoveList");
        ChessActivity.putIntList(intent, currentCoordsList, "ChessHistBoardCoords");
        startActivity(intent);
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_chess);
        }
    }
}
