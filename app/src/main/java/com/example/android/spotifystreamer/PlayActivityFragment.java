package com.example.android.spotifystreamer;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

    static final String PARCEL_TRACK = "PARCEL_TRACK";
    static final String PLAYLIST = "PLAYLIST";
    static final String POSITION = "POSITION"; // Playlist position
    static final String SEEKBAR_POSITION = "SEEKBAR_POSITION";

    private TextView mArtistNameView;
    private TextView mAlbumNameView;
    private ImageView mCoverView;
    private TextView mTrackNameView;
    private SeekBar mSeekBar;
    private TextView mTrackDurationProgressView;
    private TextView mTrackDurationEndView;
    private ImageView mPrevious;
    private ImageView mPlayPause;
    private ImageView mNext;

    private MediaPlayer mMediaPlayer;
    private ParcelableTrack mNowPlaying;
    private ArrayList<ParcelableTrack> mPlaylist;
    private int mPosition;
    private Handler mHandler = new Handler();
    private Runnable r;
    private boolean mIsPreparing = false;
    private int mSeekProgress;

    public PlayActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        // Resume playback of saved track
        if (savedInstanceState != null) {
            mPlaylist = savedInstanceState.getParcelableArrayList(PLAYLIST);
            mPosition = savedInstanceState.getInt(POSITION);
            mSeekProgress = savedInstanceState.getInt(SEEKBAR_POSITION);
            mNowPlaying = mPlaylist.get(mPosition);
        } else {
            mSeekProgress = 0;
            mNowPlaying = args.getParcelable(PARCEL_TRACK);
            mPosition = args.getInt(POSITION);
            mPlaylist = args.getParcelableArrayList(PLAYLIST);
        }

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
        mTrackDurationProgressView = (TextView) rootView.findViewById(R.id.play_track_duration_progress);
        mTrackDurationEndView = (TextView) rootView.findViewById(R.id.play_track_duration_end);
        mPrevious = (ImageView) rootView.findViewById(R.id.play_previous_icon);
        mPlayPause = (ImageView) rootView.findViewById(R.id.play_pause_play_icon);
        mNext = (ImageView) rootView.findViewById(R.id.play_next_icon);
    }

    private void setPlayControls() {
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPreviousTrack();
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
                    animateSeekBar();
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextTrack();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String time = String.format("0:%02d", progress / 1000);

                mSeekProgress = progress;
                mTrackDurationProgressView.setText(time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekProgress = seekBar.getProgress();

                mSeekProgress = seekProgress;
                mMediaPlayer.seekTo(seekProgress);
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
        mSeekBar.setMax(30000);
        mTrackDurationProgressView.setText("0:00");
        mTrackDurationEndView.setText("0:30");
    }

    private void prepareMediaPlayer() {
        // Use preview track url to play track
        if (mNowPlaying.previewUrl != null) {
            mMediaPlayer.reset();

            try {
                mMediaPlayer.setDataSource(mNowPlaying.previewUrl);
                mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
                mIsPreparing = true;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPreparing = false;
                mp.seekTo(mSeekProgress);
                mp.start();
                animateSeekBar();
                mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayPause.setImageResource(android.R.drawable.ic_media_play);
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v(LOG_TAG, "In MediaPlayer.onError");
                return false;
            }
        });
    }

    private void loadTrack() {
        if (!mIsPreparing) {
            mMediaPlayer.stop();
        }
        loadTrackDetails();
        prepareMediaPlayer();
    }

    private void loadNextTrack() {
        if (mPosition < mPlaylist.size() - 1) {
            mPosition = mPosition + 1;
            mNowPlaying = mPlaylist.get(mPosition);
            loadTrack();
        }
    }

    private void loadPreviousTrack() {
        if (mPosition > 0) {
            mPosition = mPosition - 1;
            mNowPlaying = mPlaylist.get(mPosition);
            loadTrack();
        }
    }

    private void animateSeekBar() {
        r = new Runnable() {
            public void run() {
                int timeMs = mMediaPlayer.getCurrentPosition();
                String progress = String.format("0:%02d", timeMs/1000);

                mSeekBar.setProgress(timeMs);
                mTrackDurationProgressView.setText(progress);

                if (mMediaPlayer.isPlaying()) {
                    mHandler.postDelayed(this, 500);
                }
            }
        };

        mHandler.postDelayed(r, 500);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Possible media player state handling here
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(PLAYLIST, mPlaylist);
        outState.putInt(POSITION, mPosition);
        outState.putInt(SEEKBAR_POSITION, mSeekProgress);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(r);
        mMediaPlayer.stop();
    }
}
