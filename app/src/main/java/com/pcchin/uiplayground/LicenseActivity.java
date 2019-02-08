package com.pcchin.uiplayground;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pcchin.uiplayground.gamedata.GeneralFunctions;

import java.util.Objects;

public class LicenseActivity extends AppCompatActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_license);
        setSupportActionBar(toolbar);

        // Hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set license text
        Spanned license;
        String licenseText = GeneralFunctions.getReadTextFromAssets(this, "license.txt");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            license = Html.fromHtml(licenseText, Html.FROM_HTML_MODE_LEGACY); // Adds hyperlink to text
        } else {
            license = Html.fromHtml(licenseText);
        }
        TextView licenseView = findViewById(R.id.license_text);
        licenseView.setTextSize(18);
        licenseView.setText(license);
        licenseView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Return button at top
        if (Objects.equals(item.getItemId(), R.id.menu_return)) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Return to about menu
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
