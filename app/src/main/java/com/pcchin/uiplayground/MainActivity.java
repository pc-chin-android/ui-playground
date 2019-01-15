package com.pcchin.uiplayground;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pcchin.uiplayground.mainmenu.MainMenuFragment1;
import com.pcchin.uiplayground.mainmenu.MainMenuFragment2;
import com.pcchin.uiplayground.pong.PongActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // FIXME: LICENSE LIST BELOW
    // Pac Man: https://en.bandainamcoent.eu/register-a-game
    // Pong & Asteroids: No need
    // Tetris: https://tetris.com/contact-us
    // Space Invaders: https://iterator45.wordpress.com/2012/01/13/im-gonna-get-sued-by-medway/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set pager & pager adaptor (Swiping between screens)
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager mPager = findViewById(R.id.main_menu_pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                TextView leftText = findViewById(R.id.main_menu_l_text);
                TextView rightText = findViewById(R.id.main_menu_r_text);
                // Cannot use switch statement or it will not work, and must be in this order
                if (Objects.equals(i, 1)) {
                    rightText.setText("2");
                    leftText.setText(getString(R.string.dot));
                } else {
                    leftText.setText("1");
                    rightText.setText(getString(R.string.dot));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Objects.equals(item.getItemId(), R.id.action_about)) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onButtonClick(View view) {
        // Main on click listeners
        switch(view.getId()) {
            // Tic Tac Toe
            case (R.id.main_menu_ttt_img ):
            case (R.id.main_menu_ttt_text):
                Intent intent_ttt = new Intent(this, TttActivity.class);
                startActivity(intent_ttt);
                break;

            // Pong
            case (R.id.main_menu_pong_img ):
            case (R.id.main_menu_pong_text):
                Intent intent_pong = new Intent(this, PongActivity.class);
                startActivity(intent_pong);
                break;
            // TODO: Add on click listeners in the future
        }
    }

    @Override
    public void onBackPressed() {
        // Show exit popup
        AlertDialog.Builder exitAlertBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        exitAlertBuilder.setTitle(R.string.exit_popup_title);
        exitAlertBuilder.setMessage(getString(R.string.exit_popup_message));
        // Bind quit button
        exitAlertBuilder.setPositiveButton(getString(R.string.exit_popup_right_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface exitPopup, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                });
        exitAlertBuilder.setNegativeButton(getString(R.string.exit_popup_left_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface exitPopup, int which) {
                        exitPopup.dismiss();
                    }
                });
        AlertDialog exitAlert = exitAlertBuilder.create();
        exitAlert.show();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainMenuFragment1();
                case 1:
                    return new MainMenuFragment2();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
