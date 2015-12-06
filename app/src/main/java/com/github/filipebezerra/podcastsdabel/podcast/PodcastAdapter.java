package com.github.filipebezerra.podcastsdabel.podcast;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devspark.robototextview.widget.RobotoButton;
import com.devspark.robototextview.widget.RobotoTextView;
import com.github.filipebezerra.podcastsdabel.R;
import com.github.filipebezerra.podcastsdabel.api.models.Track;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder> {

    @NonNull private final List<Track> mTracks;
    @NonNull private final OnListFragmentInteractionListener mListener;

    private ViewHolder mLastPlayingPosition;

    public PodcastAdapter(@NonNull OnListFragmentInteractionListener listener,
            @NonNull List<Track> tracks) {
        mTracks = tracks;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Track track = mTracks.get(position);
        holder.mTitle.setText(track.title);
        holder.mCreatedAt.setText(track.createdAt);
        holder.mDuration.setText(
                String.format("%d minutos de duração", MILLISECONDS.toMinutes(track.duration)));
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card) CardView mCard;
        @Bind(R.id.title) RobotoTextView mTitle;
        @Bind(R.id.created_at) RobotoTextView mCreatedAt;
        @Bind(R.id.duration) RobotoTextView mDuration;
        @Bind(R.id.play_track) RobotoButton mPlayTrack;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.play_track)
        public void playTrackClick() {
            if (mLastPlayingPosition == null) {
                mPlayTrack.setText("Pausar");
                mLastPlayingPosition = this;
                mCard.setCardBackgroundColor(
                        ContextCompat.getColor(mCard.getContext(), R.color.side_nav_bar_start));
            } else {
                if (mLastPlayingPosition.getAdapterPosition() == getAdapterPosition()) {
                    if (mPlayTrack.getText().toString().equalsIgnoreCase("reproduzir")) {
                        mPlayTrack.setText("Pausar");
                    } else {
                        mPlayTrack.setText("Reproduzir");
                    }
                } else {
                    mPlayTrack.setText("Pausar");
                    mCard.setCardBackgroundColor(
                            ContextCompat.getColor(mCard.getContext(), R.color.side_nav_bar_start));

                    mLastPlayingPosition.mPlayTrack.setText("Reproduzir");
                    mLastPlayingPosition.mCard.setCardBackgroundColor(
                            ContextCompat.getColor(mCard.getContext(), R.color.window_background));

                    mLastPlayingPosition = this;
                }
            }

            mListener.onListFragmentInteraction(mTracks.get(getAdapterPosition()));
        }

        @OnClick(R.id.show_more)
        public void showMoreButton() {

        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Track track);
    }
}