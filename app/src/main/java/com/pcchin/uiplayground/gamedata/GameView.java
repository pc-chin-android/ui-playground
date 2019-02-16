package com.pcchin.uiplayground.gamedata;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/** SurfaceView for all games
 * This is added to merge all GameThreads into one, reducing file size **/
public abstract class GameView extends SurfaceView {

    /** Constructor when called from Java **/
    public GameView(Context context) {
        super(context);
    }

    /** Constructor when called from XML **/
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Constructor when called from XML **/
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Needed for GameThread **/
    public abstract void update();
}