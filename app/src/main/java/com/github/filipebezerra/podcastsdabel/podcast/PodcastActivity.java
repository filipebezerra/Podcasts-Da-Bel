package com.github.filipebezerra.podcastsdabel.podcast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.filipebezerra.podcastsdabel.R;
import com.github.filipebezerra.podcastsdabel.api.models.Podcast;
import com.github.filipebezerra.podcastsdabel.base.BaseActivity;
import com.github.filipebezerra.podcastsdabel.helper.SnackbarHelper;
import com.github.filipebezerra.podcastsdabel.network.NetworkController;
import com.github.filipebezerra.podcastsdabel.util.DrawableUtil;
import com.github.filipebezerra.podcastsdabel.widgets.ToolbarSpinnerAdapter;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PodcastActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.root_layout) CoordinatorLayout mRootLayout;
    @Bind(R.id.avatar) ImageView mAvatarView;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout mTabLayout;

    private MaterialDialog mProgressDialog;

    private FragmentAdapter mFragmentAdapter;

    private List<PodcastLoadedListener> mPodcastLoadedListeners = new ArrayList<>();

    @Override
    protected int provideViewLayout() {
        return R.layout.activity_podcast;
    }

    @NonNull
    @Override
    public ViewGroup getRootViewLayout() {
        return mRootLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
        DrawableUtil.tint(fab.getDrawable(), R.color.icons, this);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        final Toolbar toolbar = getToolbarAsActionBar();
        if (toolbar != null) {
            View spinnerContainer = LayoutInflater.from(this)
                    .inflate(R.layout.toolbar_spinner, toolbar, false);

            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            toolbar.addView(spinnerContainer, lp);

            ToolbarSpinnerAdapter spinnerAdapter = new ToolbarSpinnerAdapter(this,
                    Arrays.asList(getResources().getStringArray(R.array.playlists)));

            Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(this);
        }

        final AboutPodcastFragment aboutPodcastFragment = AboutPodcastFragment.newInstance();
        mPodcastLoadedListeners.add(aboutPodcastFragment);

        final PodcastFragment podcastFragment = PodcastFragment.newInstance();
        mPodcastLoadedListeners.add(podcastFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.addFragment(aboutPodcastFragment, "Sobre o podcast");
        mFragmentAdapter.addFragment(podcastFragment, "Episódios");
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Picasso.with(this)
                        .load(R.drawable.podcast_bel_t500x500)
                        .into(mAvatarView);
                break;
            case 1:
                Picasso.with(this)
                        .load(R.drawable.menina_vale_t500x500)
                        .into(mAvatarView);
                break;
            case 2:
                Picasso.with(this)
                        .load(R.drawable.menina_vale_2_t500x500)
                        .into(mAvatarView);
                break;
        }

        final String playlistId = getResources().getStringArray(R.array.playlists_ids)[position];
        requestPodcast(Integer.valueOf(playlistId));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void requestPodcast(int playlistId) {
        mProgressDialog = new MaterialDialog.Builder(this)
                .content("Carregando podcast, por favor aguarde...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        NetworkController
                .soundCloundService(this)
                .playlist(playlistId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Podcast>() {
                    @Override
                    public void onCompleted() {
                        if (isLoadingPodcast()) {
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BelPescePodcast", "Loading podcast", e);

                        if (isLoadingPodcast()) {
                            mProgressDialog.dismiss();
                        }
                        SnackbarHelper.showWithAction(getRootViewLayout(), e.getMessage(), false,
                                "Tentar Novamente", v -> requestPodcast(playlistId));
                    }

                    @Override
                    public void onNext(Podcast podcast) {
                        displayPodcast(podcast);
                    }
                });
    }

    private void displayPodcast(Podcast podcast) {
        if (podcast != null) {
            if (mPodcastLoadedListeners != null) {
                for (PodcastLoadedListener listener : mPodcastLoadedListeners) {
                    listener.whenLoaded(podcast);
                }
            }
        }
    }

    private boolean isLoadingPodcast() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        public void addFragment(@NonNull Fragment fragment, @NonNull String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }
    }

    public interface PodcastLoadedListener {
        void whenLoaded(Podcast podcast);
    }


}
