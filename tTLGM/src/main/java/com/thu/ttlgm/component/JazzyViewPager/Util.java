package com.thu.ttlgm.component.JazzyViewPager;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by SemonCat on 2014/4/8.
 */
public class Util {
    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}
