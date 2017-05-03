package com.fd.gobondg0.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {

    public static int dpToPx(int dp, Context context){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
}
