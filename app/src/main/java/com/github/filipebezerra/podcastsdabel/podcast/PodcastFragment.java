package com.github.filipebezerra.podcastsdabel.podcast;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.github.filipebezerra.podcastsdabel.R;
import com.github.filipebezerra.podcastsdabel.api.constant.Keys;
import com.github.filipebezerra.podcastsdabel.api.models.Podcast;
import com.github.filipebezerra.podcastsdabel.api.models.Track;
import com.github.filipebezerra.podcastsdabel.base.BaseFragment;
import com.github.filipebezerra.podcastsdabel.helper.SnackbarHelper;
import com.github.filipebezerra.podcastsdabel.podcast.PodcastAdapter.OnListFragmentInteractionListener;
import java.io.IOException;
import timber.log.Timber;

import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END;
import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START;

public class PodcastFragment extends BaseFragment
        implements OnListFragmentInteractionListener,
        PodcastActivity.PodcastLoadedListener {

    private static final String TAG = PodcastFragment.class.getSimpleName();

    @Bind(R.id.list) RecyclerView mTrackListView;

    private OnListFragmentInteractionListener mListener;

    private PodcastAdapter mPodcastAdapter;

    private MediaPlayer mMediaPlayer;

    private boolean mIsMediaPlayerPreparing;

    private Track mTrackPlaying;

    @Override
    protected int provideFragmentLayout() {
        return R.layout.fragment_podcast;
    }

    public static PodcastFragment newInstance() {
        return new PodcastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the adapter
        /*
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new PodcastAdapter(DummyContent.ITEMS, mListener));
        }
        */

        if (mPodcastAdapter != null && mTrackListView.getAdapter() == null) {
            mTrackListView.setAdapter(mPodcastAdapter);
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(mp -> {
            Timber.d("Media is ready to play");
            mIsMediaPlayerPreparing = false;
            togglePlayPause();
        });

        mMediaPlayer.setOnCompletionListener(mp -> {
            Timber.d("Media has been reached during playback");
            togglePlayPause();
        });

        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Timber.d("Error with code %d and extra %d", what, extra);
            mMediaPlayer.reset();
            return true;
        });

        mMediaPlayer.setOnBufferingUpdateListener(
                (mp, percent) -> Timber.d("Buffer update to %d", percent));

        mMediaPlayer.setOnInfoListener((mp, what, extra) -> {
            Timber.d("Buffer info/warning is available with code %d and extra %d", what, extra);

            switch (what) {
                case MEDIA_INFO_BUFFERING_START:
                    //mPlayerControl.setIcon(R.drawable.ic_hourglass_empty);
                    //mBufferingSelectedTrack.setVisibility(View.VISIBLE);
                    break;
                case MEDIA_INFO_BUFFERING_END:
                    if (mMediaPlayer.isPlaying()) {
                        //mPlayerControl.setIcon(R.drawable.ic_pause);
                    } else {
                        //mPlayerControl.setIcon(R.drawable.ic_play);
                    }
                    //mBufferingSelectedTrack.setVisibility(View.GONE);
                    break;
                case 703: //MediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH
                    //mBufferingSelectedTrack.setText(getString(R.string.buffering, extra));
                    break;
            }
            return true;
        });
    }

    private void togglePlayPause() {
        if (mIsMediaPlayerPreparing) {
            return;
        }

        if (mMediaPlayer.isPlaying()) {
            Timber.d("Media will pause");
            mMediaPlayer.pause();
            //mPlayerControl.setIcon(R.drawable.ic_play); TODO update button state
        } else {
            Timber.d("Media will start playing");
            mMediaPlayer.start();
            //mPlayerControl.setIcon(R.drawable.ic_pause); TODO update button state
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onListFragmentInteraction(Track track) {
        if (mTrackPlaying == track) {
            togglePlayPause();
        } else {
            mTrackPlaying = track;

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.reset();

            try {
                mMediaPlayer.setDataSource(
                        track.streamUrl + "?client_id=" + Keys.SOUNDCLOUD_CLIENT_ID);
                mMediaPlayer.prepareAsync();
                mIsMediaPlayerPreparing = true;
                //mPlayerControl.setIcon(R.drawable.ic_hourglass_empty);
            } catch (IllegalStateException e) {
                SnackbarHelper.show(getRootViewLayout(), "Must be called in Idle state.", false);
            } catch (IOException e) {
                Timber.e(e, "Error trying to set the stream url");
                SnackbarHelper.show(getRootViewLayout(), "Error playing the music.", false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void whenLoaded(Podcast podcast) {
        if (podcast != null) {
            if (mTrackListView != null) {
                mTrackListView.setHasFixedSize(true);
                mTrackListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mTrackListView.setItemAnimator(new DefaultItemAnimator());
                mTrackListView.setAdapter(
                        mPodcastAdapter = new PodcastAdapter(this, podcast.tracks));
            } else {
                mPodcastAdapter = new PodcastAdapter(this, podcast.tracks);
            }
        }
    }
}
