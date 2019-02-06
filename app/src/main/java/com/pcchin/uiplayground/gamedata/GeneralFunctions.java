package com.pcchin.uiplayground.gamedata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextPaint;

import com.pcchin.uiplayground.R;
import com.pcchin.uiplayground.TttActivity;
import com.pcchin.uiplayground.pong.PongGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class GeneralFunctions {
    public static final int DRAW = 0;
    public static final int ONE_WIN = 1;
    public static final int ONE_LOSE = 2;
    public static final int TWO_1_WIN = -1;
    public static final int TWO_2_WIN = -2;

    public static Bitmap getBitmap(int drawableRes, @NonNull Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableRes, context.getTheme());
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap textToBitmap(String text, int textColor, float textSize, String fontFamily,
                                      int typefaceType, boolean importFont, @NonNull Context context) {
        // Set up text properties
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        // Special for imported text
        if (importFont) {
            paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontFamily + ".ttf"));
        } else {
            paint.setTypeface(Typeface.create(fontFamily, typefaceType));
        }
        paint.setTextAlign(Paint.Align.LEFT);

        // Drawing actual text
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static Bitmap colorToBitmap(int bitmapColor, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(bitmapColor);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

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
}
