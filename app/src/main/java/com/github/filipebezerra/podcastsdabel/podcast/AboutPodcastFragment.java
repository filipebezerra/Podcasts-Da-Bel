package com.github.filipebezerra.podcastsdabel.podcast;

import android.content.Intent;
import android.net.Uri;
import butterknife.Bind;
import butterknife.OnClick;
import com.devspark.robototextview.widget.RobotoButton;
import com.devspark.robototextview.widget.RobotoTextView;
import com.github.filipebezerra.podcastsdabel.R;
import com.github.filipebezerra.podcastsdabel.api.models.Podcast;
import com.github.filipebezerra.podcastsdabel.base.BaseFragment;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 06/12/2015
 * @since #
 */
public class AboutPodcastFragment extends BaseFragment
    implements PodcastActivity.PodcastLoadedListener {

    @Bind(R.id.tags) RobotoTextView mTagsView;
    @Bind(R.id.webpage) RobotoTextView mWebpageView;
    @Bind(R.id.created_at) RobotoTextView mCreatedAtView;
    @Bind(R.id.tracks) RobotoTextView mTracksView;
    @Bind(R.id.modified_at) RobotoTextView mModifiedAtView;
    @Bind(R.id.visit_webpage) RobotoButton mVisitWebpageButton;
    @Bind(R.id.view_podcasts) RobotoButton mViewPodcastsButton;

    @Override
    protected int provideFragmentLayout() {
        return R.layout.fragment_about_podcast;
    }

    public static AboutPodcastFragment newInstance() {
        return new AboutPodcastFragment();
    }

    @Override
    public void whenLoaded(Podcast podcast) {
        if (podcast != null) {
            mVisitWebpageButton.setEnabled(true);
            mViewPodcastsButton.setEnabled(true);
            mTagsView.setText(podcast.tagList);
            mWebpageView.setText(podcast.permalinkUrl);
            mCreatedAtView.setText(podcast.createdAt);
            mTracksView.setText(String.valueOf(podcast.trackCount));
            mModifiedAtView.setText(podcast.lastModified);
        }
    }

    @OnClick(R.id.visit_webpage)
    public void visitWebpageClick() {
        startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(mWebpageView.getText().toString())));
    }

    @OnClick(R.id.view_podcasts)
    public void viewPodcastsClick() {

    }
}
