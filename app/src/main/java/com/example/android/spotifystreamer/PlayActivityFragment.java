package com.example.android.spotifystreamer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.spotifystreamer.service.PlayService;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayActivityFragment extends DialogFragment {

    private static final String LOG_TAG = PlayActivityFragment.class.getSimpleName();

    static final String ARTIST_NAME = "ARTIST_NAME";
    static final String ALBUM = "ALBUM";
    static final String COVER_URL = "COVER_URL";
    static final String TRACK_NAME = "TRACK_NAME";
    static final String TRACK_DURATION = "TRACK_DURATION";
    static final String PREVIEW_URL = "PREVIEW_URL";

    public static final int TRACK_PLAY = 0;
    public static final int TRACK_PAUSE = 1;
    public static final int TRACK_PREVIOUS = 2;
    public static final int TRACK_NEXT = 3;

    private TextView mArtistNameView;
    private TextView mAlbumNameView;
    private ImageView mCoverView;
    private TextView mTrackNameView;
    private SeekBar mSeekBar;
    private TextView mTrackDurationStartView;
    private TextView mTrackDurationEndView;
    private ImageView mPrevious;
    private ImageView mPlayPause;
    private ImageView mNext;


    public PlayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        //Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        String artistName = "";
        String albumName = "";
        String coverUrl = "";
        String trackName = "";
        String previewUrl = "";

        getViews(rootView);
        setPlayControls();

        if (args != null) {
            artistName = args.getString(ARTIST_NAME);
            albumName = args.getString(ALBUM);
            coverUrl = args.getString(COVER_URL);
            trackName = args.getString(TRACK_NAME);
            previewUrl = args.getString(PREVIEW_URL);

            mArtistNameView.setText(artistName);
            mAlbumNameView.setText(albumName);
            Picasso.with(getActivity()).load(coverUrl).into(mCoverView);
            mTrackNameView.setText(trackName);
            mSeekBar.setMax(30);
            mTrackDurationStartView.setText("0:00");
            mTrackDurationEndView.setText("0:30");
        }



        // Use preview track url to play track
        if (previewUrl != null) {
            Intent playIntent = new Intent(getActivity(), PlayService.class);

            playIntent.putExtra(Intent.EXTRA_TEXT, previewUrl);
            getActivity().startService(playIntent);
        }

        return rootView;
    }

    private void getViews(View rootView) {
        mArtistNameView = (TextView) rootView.findViewById(R.id.play_artist_name);
        mAlbumNameView = (TextView) rootView.findViewById(R.id.play_artist_album);
        mCoverView = (ImageView) rootView.findViewById(R.id.play_album_cover);
        mTrackNameView = (TextView) rootView.findViewById(R.id.play_track_name);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.play_track_seekbar);
        mTrackDurationStartView = (TextView) rootView.findViewById(R.id.play_track_duration_start);
        mTrackDurationEndView = (TextView) rootView.findViewById(R.id.play_track_duration_end);
        mPrevious = (ImageView) rootView.findViewById(R.id.play_previous_icon);
        mPlayPause = (ImageView) rootView.findViewById(R.id.play_pause_play_icon);
        mNext = (ImageView) rootView.findViewById(R.id.play_next_icon);
    }

    private void setPlayControls() {
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    public void startSeekBar() {
        int progress = mSeekBar.getProgress();
        mSeekBar.setProgress(progress + 1);
    }
}
