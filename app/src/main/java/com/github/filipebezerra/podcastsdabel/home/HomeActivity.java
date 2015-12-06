package com.github.filipebezerra.podcastsdabel.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.devspark.robototextview.widget.RobotoTextView;
import com.github.filipebezerra.podcastsdabel.R;
import com.github.filipebezerra.podcastsdabel.api.constant.Properties;
import com.github.filipebezerra.podcastsdabel.api.models.User;
import com.github.filipebezerra.podcastsdabel.base.BaseActivity;
import com.github.filipebezerra.podcastsdabel.helper.SnackbarHelper;
import com.github.filipebezerra.podcastsdabel.network.NetworkController;
import com.github.filipebezerra.podcastsdabel.podcast.PodcastActivity;
import com.github.filipebezerra.podcastsdabel.util.DrawableUtil;
import com.github.filipebezerra.podcastsdabel.widgets.ColorCircleView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mNavigationView;
    @Bind(R.id.root_layout) CoordinatorLayout mRootLayout;
    @Bind(R.id.app_bar_layout) AppBarLayout mAppBarLayout;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.avatar) ImageView mAvatarView;

    @Bind(R.id.user_name) RobotoTextView mUserNameView;
    @Bind(R.id.full_name) RobotoTextView mFullNameView;
    @Bind(R.id.country) RobotoTextView mCountryView;
    @Bind(R.id.city) RobotoTextView mCityView;
    @Bind(R.id.bio) RobotoTextView mBioView;
    @Bind(R.id.site) RobotoTextView mSiteView;
    @Bind(R.id.profile) RobotoTextView mProfileView;
    @Bind(R.id.track_count) RobotoTextView mTrackCountView;
    @Bind(R.id.followers_count) RobotoTextView mFollowersCountView;
    @Bind(R.id.status) ColorCircleView mStatusView;
    @Bind(R.id.visit_website) Button mVisitWebsiteButton;
    @Bind(R.id.view_profile) Button mViewProfileButton;

    private MaterialDialog mProgressDialog;

    private User mUser;

    @Override
    protected int provideViewLayout() {
        return R.layout.activity_home;
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbarAsActionBar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                if (!mCollapsingToolbarLayout.isTitleEnabled()) {
                    mCollapsingToolbarLayout.setTitleEnabled(true);
                    mCollapsingToolbarLayout.setTitle(getString(R.string.nav_about));
                }
            } else {
                if (mCollapsingToolbarLayout.isTitleEnabled()) {
                    mCollapsingToolbarLayout.setTitleEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestUser();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);

        switch (id) {
            case R.id.nav_about:
                //mCollapsingToolbarLayout.setTitle(item.getTitle());
                break;

            case R.id.nav_podcast:
                startActivity(new Intent(this, PodcastActivity.class));
                break;

            case R.id.nav_share:
                break;
        }

        return true;
    }

    @OnClick(R.id.visit_website)
    public void visitWebsiteClick() {
        startActivity(new Intent(
                Intent.ACTION_VIEW, Uri.parse(mSiteView.getText().toString())));
    }

    @OnClick(R.id.view_profile)
    public void viewProfileClick() {
        startActivity(new Intent(
                Intent.ACTION_VIEW, Uri.parse(mProfileView.getText().toString())));
    }

    @OnClick(R.id.status)
    public void statusClick() {
        if (isLoadingUser()) {
            SnackbarHelper.show(getRootViewLayout(),
                    "As informações estão sendo carregadas!", false);
        } else if (mUser == null) {
            SnackbarHelper.show(getRootViewLayout(),
                    "Dados indisponíveis", false);
        } else {
            final String message = String.format("%s está %s!",
                    mFullNameView.getText().toString(), (mUser.online ? "online" : "offline"));

            SnackbarHelper.show(getRootViewLayout(), message, false);
        }
    }

    private void requestUser() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .content("Carregando perfil, por favor aguarde...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        NetworkController
                .soundCloundService(this)
                .user(Properties.USER_ID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        if (isLoadingUser()) {
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BelPescePodcast", "Loading user", e);

                        if (isLoadingUser()) {
                            mProgressDialog.dismiss();
                        }
                        SnackbarHelper.showWithAction(getRootViewLayout(), e.getMessage(), false,
                                "Tentar Novamente", v -> requestUser());
                    }

                    @Override
                    public void onNext(User user) {
                        displayUser(user);
                    }
                });
    }

    private boolean isLoadingUser() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    private void displayUser(User user) {
        if (user != null) {
            mUser = user;

            mVisitWebsiteButton.setEnabled(true);
            mViewProfileButton.setEnabled(true);
            mUserNameView.setText(user.username);
            mFullNameView.setText(user.fullName);
            mCountryView.setText(user.country);
            mCityView.setText(user.city);
            mBioView.setText(user.description);
            mSiteView.setText(user.website);
            mProfileView.setText(user.permalinkUrl);
            mTrackCountView.setText(String.valueOf(user.trackCount));
            mFollowersCountView.setText(String.valueOf(user.followersCount));

            mStatusView.setVisibility(View.VISIBLE);
            if (user.online) {
                mStatusView.setColor(ContextCompat.getColor(this, R.color.online));
            } else {
                mStatusView.setColor(ContextCompat.getColor(this, R.color.offline));
            }

            /*
            final String avatarUrl = user.avatarUrl.replace(Properties.USER_AVATAR_LARGE,
                    Properties.USER_AVATAR_T500);

            Picasso.with(this)
                    .load(avatarUrl)
                    .resize(mAvatarView.getWidth(), 0)
                    .centerCrop()
                    .into(mAvatarView);
            */
        }
    }
}
