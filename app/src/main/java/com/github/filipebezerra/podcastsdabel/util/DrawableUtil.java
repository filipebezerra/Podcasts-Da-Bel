package com.github.filipebezerra.podcastsdabel.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 05/12/2015
 * @since #
 */
public class DrawableUtil {
    private DrawableUtil() {
        // no instances
    }

    public static void tint(@NonNull Drawable drawable, @ColorRes int colorRes, @NonNull
            Context context) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
    }
}
