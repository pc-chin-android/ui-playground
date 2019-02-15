package com.pcchin.uiplayground.gamedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;

/** General Functions for bitmaps **/
public class BitmapFunctions {

    /** Converts drawable eg bmp, xml to Bitmap **/
    public static Bitmap getBitmap(int drawableRes, @NonNull Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableRes, context.getTheme());
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /** Convert a string of text with inputted properties to Bitmap.
     * Bitmap size is based on text size, font & typeface.
     **/
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

    /** Creates full colored bitmap based on given color, width and height **/
    public static Bitmap colorToBitmap(int bitmapColor, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(bitmapColor);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }
}
