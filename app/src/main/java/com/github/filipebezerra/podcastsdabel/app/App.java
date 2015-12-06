package com.github.filipebezerra.podcastsdabel.app;

import android.app.Application;
import com.github.filipebezerra.podcastsdabel.BuildConfig;
import timber.log.Timber;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 06/12/2015
 * @since #
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupTimber();
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
