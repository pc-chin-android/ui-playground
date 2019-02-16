package com.pcchin.uiplayground.gamedata;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.pcchin.uiplayground.R;
import com.pcchin.uiplayground.TttActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/** Functions used in all games **/
public class GeneralFunctions {

    public static final int DRAW = 0;
    public static final int ONE_WIN = 1;
    public static final int ONE_LOSE = 2;
    public static final int TWO_1_WIN = -1;
    public static final int TWO_2_WIN = -2;

    /** Display alert dialog with icon of current game and whether the game is won **/
    public static void displayDialog(Context context, int state, DialogInterface.OnDismissListener listener) {
        int iconType = R.drawable.ic_launcher_foreground;
        try {
            ((TttActivity)context).setVisible(true);
            iconType = R.drawable.ttt_icon;
        } catch (Exception e) {
            // Test if function came from TttActivity
        }

        AlertDialog.Builder displayDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        displayDialogBuilder.setTitle(R.string.game_over);
        displayDialogBuilder.setIcon(iconType);
        // Bind OK button to dismiss dialog
        displayDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        displayDialogBuilder.setOnDismissListener(listener);
        // Display game over dialog
        switch (state) {
            case DRAW:
                displayDialogBuilder.setMessage(R.string.draw_details);
                break;
            case ONE_WIN:
                displayDialogBuilder.setMessage(R.string.you_win);
                break;
            case ONE_LOSE:
                displayDialogBuilder.setMessage(R.string.you_lost);
                break;
            case TWO_1_WIN:
                displayDialogBuilder.setMessage(R.string.player_1_wins);
                break;
            case TWO_2_WIN:
                displayDialogBuilder.setMessage(R.string.player_2_wins);
                break;

        }
        AlertDialog displayDialog = displayDialogBuilder.create();
        displayDialog.show();
    }

    /** Returns a string of text from specific text file in the assets folder **/
    @NonNull
    public static String getReadTextFromAssets(@NonNull Context context, String textFileName) {
        String text;
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(textFileName);
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /** Returns MediaPlayer that will not trigger any subtitle warnings **/
    @SuppressLint("PrivateApi")
    public static MediaPlayer getMediaPlayer(Context context, int contentType) {
        // Removes "No subtitles" error for MediaPlayer
        MediaPlayer mediaplayer = new MediaPlayer();
        try {
            // Class.forName may be buggy, but is currently the only method to access internal API
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    Context.class, cMediaTimeProvider, iSubtitleControllerListener);
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setSubtitleAnchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setSubtitleAnchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaplayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(contentType)
                .build());

        return mediaplayer;
    }

    /** Returns a deep copy of the original coordinates list, useful in collision detection */
    public static ArrayList<ArrayList<Integer>> deepCopy(@NonNull ArrayList<ArrayList<Integer>> originalList) {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (ArrayList<Integer> e: originalList) {
            ArrayList<Integer> tempList = new ArrayList<>(e);
            returnList.add(tempList);
        }
        return returnList;
    }
}
