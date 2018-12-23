package com.pcchin.uiplayground;

import android.content.Context;
import android.support.annotation.NonNull;

class GeneralFunctions{
    static int px2dp(float px,@NonNull Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(px / density);
    }
}
