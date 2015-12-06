package com.github.filipebezerra.podcastsdabel.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.filipebezerra.podcastsdabel.R;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 03/12/2015
 * @since #
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) protected Toolbar mToolbarAsActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideViewLayout());
        ButterKnife.bind(this);
        setupToolbarAsActionBar();
    }

    private void setupToolbarAsActionBar() {
        if (mToolbarAsActionBar != null) {
            setSupportActionBar(mToolbarAsActionBar);
            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);

                if (provideUpIndicator() != View.NO_ID) {
                    actionBar.setHomeAsUpIndicator(provideUpIndicator());
                }
            }
        }
    }

    public Toolbar getToolbarAsActionBar() {
        return mToolbarAsActionBar;
    }

    @DrawableRes protected int provideUpIndicator() {
        return View.NO_ID;
    }

    @LayoutRes protected abstract int provideViewLayout();

    @NonNull public abstract ViewGroup getRootViewLayout();
}
