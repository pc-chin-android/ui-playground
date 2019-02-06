package com.pcchin.uiplayground.gamedata;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public abstract class GameObject {
    protected Bitmap image;

    public final int width;
    public final int height;
    public int x;
    public int y;

    public GameObject(@NonNull Bitmap image, int x, int y)  {

        this.image = image;

        this.x= x;
        this.y= y;

        this.width = image.getWidth();
        this.height= image.getHeight();
    }

    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
