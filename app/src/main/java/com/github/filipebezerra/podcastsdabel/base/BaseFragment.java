package com.github.filipebezerra.podcastsdabel.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 05/12/2015
 * @since #
 */
public abstract class BaseFragment extends Fragment {
    private BaseActivity mBaseActivity;

    @LayoutRes
    protected abstract int provideFragmentLayout();

    public BaseFragment() {
        // no args
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBaseActivity = (BaseActivity) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "com.github.filipebezerra.belpescepodcast.base.BaseFragment "
                            + "must belongs to "
                            + "com.github.filipebezerra.belpescepodcast.base.BaseActivity.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(provideFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public BaseActivity getBaseActivity() {
        return mBaseActivity;
    }

    public ViewGroup getRootViewLayout() {
        return mBaseActivity != null ? mBaseActivity.getRootViewLayout() : null;
    }

    public Toolbar getActionBarToolbar() {
        return mBaseActivity != null ? mBaseActivity.getToolbarAsActionBar() : null;
    }
}
