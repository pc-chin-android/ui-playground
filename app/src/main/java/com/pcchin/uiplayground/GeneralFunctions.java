package com.pcchin.uiplayground;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;

public class GeneralFunctions {
    static final int DRAW = 0;
    public static final int ONE_WIN = 1;
    public static final int ONE_LOSE = 2;
    public static final int TWO_1_WIN = -1;
    public static final int TWO_2_WIN = -2;

    public static Bitmap getBitmap(int drawableRes, @NonNull Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
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

    public static void displayDialog(Context context, int state, DialogInterface.OnDismissListener listener) {
        AlertDialog.Builder displayDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        displayDialogBuilder.setTitle(R.string.game_over);
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
        switch(state) {
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
}
