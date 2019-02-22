package com.pcchin.uiplayground.chess;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pcchin.uiplayground.R;

// Class is public for the fragment
public class ChessSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_chess);
        }
    }
}
