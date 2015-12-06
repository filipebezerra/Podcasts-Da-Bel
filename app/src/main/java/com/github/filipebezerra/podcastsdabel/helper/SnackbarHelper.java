package com.github.filipebezerra.podcastsdabel.helper;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import com.github.filipebezerra.podcastsdabel.R;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.support.design.widget.Snackbar.make;
import static android.support.v4.content.ContextCompat.getColor;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 26/11/2015
 * @since #
 */
public class SnackbarHelper {
    private SnackbarHelper() {
        //
    }

    private static Snackbar setupSnackbar(@NonNull View view, @NonNull CharSequence text,
            boolean shortDuration) {
        final Snackbar snackbar = make(view, text, (shortDuration ? LENGTH_SHORT : LENGTH_LONG));
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(view.getContext(), R.color.primary));

        final TextView textView = (TextView) snackbarView.findViewById(
                android.support.design.R.id.snackbar_text);

        textView.setTextColor(getColor(view.getContext(),R.color.icons));

        return snackbar;
    }

    public static void show(@NonNull View view, @NonNull CharSequence text,
            boolean shortDuration) {
        setupSnackbar(view, text, shortDuration).show();
    }

    public static void showWithAction(@NonNull View view, @NonNull CharSequence text,
            boolean shortDuration, @NonNull String actionText, @NonNull View.OnClickListener action) {
        setupSnackbar(view, text, shortDuration).setAction(actionText, action).show();
    }

}
