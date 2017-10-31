package io.rmiri.buttonloading.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Rasoul on 7/11/17.
 */

public class DeviceScreenUtils {

    private static int deviceScreenUtilsWidth;
    private static int deviceScreenUtilsHeight;

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int height(Context context) {
        if (deviceScreenUtilsHeight == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            deviceScreenUtilsHeight = displayMetrics.heightPixels;
        }
        return deviceScreenUtilsHeight;
    }

    public static int width(Context context) {
        if (deviceScreenUtilsWidth == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            deviceScreenUtilsWidth = displayMetrics.widthPixels;
        }
        return deviceScreenUtilsWidth;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
