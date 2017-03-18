package com.introlayout.utils;

import android.content.res.Resources;

/**
 * Created by kisha_000 on 2/28/2017.
 */

public class Utils {
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
