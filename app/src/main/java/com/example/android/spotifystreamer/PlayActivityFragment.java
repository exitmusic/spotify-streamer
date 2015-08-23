package com.example.android.spotifystreamer;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import com.example.android.spotifystreamer.data.ParcelableTrack;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayActivityFragment extends DialogFragment {

    private static final String LOG_TAG = PlayActivityFragment.class.getSimpleName();

//    static final String ARTIST_NAME = "ARTIST_NAME";
//    static final String ALBUM = "ALBUM";
//    static final String COVER_URL = "COVER_URL";
//    static final String TRACK_NAME = "TRACK_NAME";
//    static final String TRACK_DURATION = "TRACK_DURATION";
//    static final String PREVIEW_URL = "PREVIEW_URL";
    static final String PARCEL_TRACK = "PARCEL_TRACK";
    static final String PLAYLIST = "PLAYLIST";
    static final String POSITION = "POSITION";

    public static final String TRACK_PLAY = "PLAY";
    public static final String TRACK_PAUSE = "PAUSE";
    public static final String TRACK_PREVIOUS = "PREVIOUS";
    public static final String TRACK_NEXT = "NEXT";

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

    private MediaPlayer mMediaPlayer;
    private ParcelableTrack mNowPlaying;
    private ArrayList<ParcelableTrack> mPlaylist;
    private int mPosition;

    public PlayActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        mNowPlaying = args.getParcelable(PARCEL_TRACK);
        mPlaylist = args.getParcelableArrayList(PLAYLIST);
        mPosition = args.getInt(POSITION);

        getViews(rootView);
        setPlayControls();
        loadTrackDetails();
        prepareMediaPlayer();

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
                if (mPosition > 0) {
                    mPosition = mPosition - 1;
                    mNowPlaying = mPlaylist.get(mPosition);
                    loadTrackDetails();
                    prepareMediaPlayer();
                }
            }
        });

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mPlayPause.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mMediaPlayer.start();
                    mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition < mPlaylist.size()-1) {
                    mPosition = mPosition + 1;
                    mNowPlaying = mPlaylist.get(mPosition);
                    loadTrackDetails();
                    prepareMediaPlayer();
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayPause.setImageResource(android.R.drawable.ic_media_play);
            }
        });
    }

    private void loadTrackDetails() {
        mArtistNameView.setText(mNowPlaying.artistName);
        mAlbumNameView.setText(mNowPlaying.albumName);
        if (mNowPlaying.coverUrl != "") {
            Picasso.with(getActivity()).load(mNowPlaying.coverUrl).into(mCoverView);
        }
        mTrackNameView.setText(mNowPlaying.trackName);
        mSeekBar.setMax(30);
        mTrackDurationStartView.setText("0:00");
        mTrackDurationEndView.setText("0:30");
    }

    private void prepareMediaPlayer() {
        // Use preview track url to play track
        if (mNowPlaying.previewUrl != null) {
            mMediaPlayer.reset();

            try {
                mMediaPlayer.setDataSource(mNowPlaying.previewUrl);
                mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {

            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayer.release();
    }
}
