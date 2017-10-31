package io.rmiri.buttonloading.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;


/**
 * Created by R.Miri on 7/20/2016.
 */

public class FontUtils {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(Context context,String fontName) {
        Typeface tf = fontCache.get(fontName);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            fontCache.put(fontName, tf);
        }
        return tf;
    }

}
