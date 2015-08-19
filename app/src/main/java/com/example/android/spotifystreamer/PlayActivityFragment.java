package com.example.android.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayActivityFragment extends Fragment {

    private static final String LOG_TAG = PlayActivityFragment.class.getSimpleName();
    static final String ARTIST_NAME = "ARTIST_NAME";
    static final String ALBUM = "ALBUM";
    static final String COVER_URL = "COVER_URL";
    static final String TRACK_NAME = "TRACK_NAME";
    static final String TRACK_DURATION = "TRACK_DURATION";
    static final String PREVIEW_URL = "PREVIEW_URL";

    private TextView mArtistNameView;
    private TextView mAlbumNameView;
    private ImageView mCoverView;
    private TextView mTrackNameView;
    private TextView mTrackDurationStartView;
    private TextView mTrackDurationEndView;


    public PlayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        mArtistNameView = (TextView) rootView.findViewById(R.id.play_artist_name);
        mAlbumNameView = (TextView) rootView.findViewById(R.id.play_artist_album);
        mCoverView = (ImageView) rootView.findViewById(R.id.play_album_cover);
        mTrackNameView = (TextView) rootView.findViewById(R.id.play_track_name);
        mTrackDurationStartView = (TextView) rootView.findViewById(R.id.play_track_duration_start);
        mTrackDurationEndView = (TextView) rootView.findViewById(R.id.play_track_duration_end);

        mArtistNameView.setText(intent.getStringExtra(PlayActivityFragment.ARTIST_NAME));
        mAlbumNameView.setText(intent.getStringExtra(PlayActivityFragment.ALBUM));
        Picasso.with(getActivity()).load(intent.getStringExtra(PlayActivityFragment.COVER_URL)).into(mCoverView);


        // Use preview track url to play track
        if (intent != null) {
            String previewUrl = intent.getStringExtra(PlayActivityFragment.PREVIEW_URL);

            // Prepare and start media player
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(previewUrl);
                mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {

            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }

        return rootView;
    }
}
