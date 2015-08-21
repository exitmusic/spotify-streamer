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

        Bundle args = getArguments();
        //Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        mArtistNameView = (TextView) rootView.findViewById(R.id.play_artist_name);
        mAlbumNameView = (TextView) rootView.findViewById(R.id.play_artist_album);
        mCoverView = (ImageView) rootView.findViewById(R.id.play_album_cover);
        mTrackNameView = (TextView) rootView.findViewById(R.id.play_track_name);
        mTrackDurationStartView = (TextView) rootView.findViewById(R.id.play_track_duration_start);
        mTrackDurationEndView = (TextView) rootView.findViewById(R.id.play_track_duration_end);

        String artistName = args.getString(ARTIST_NAME);
        String albumName = args.getString(ALBUM);
        String coverUrl = args.getString(COVER_URL);
        String trackName = args.getString(TRACK_NAME);
        String previewUrl = args.getString(PREVIEW_URL);

        mArtistNameView.setText(artistName);
        mAlbumNameView.setText(albumName);
        Picasso.with(getActivity()).load(coverUrl).into(mCoverView);
        mTrackNameView.setText(trackName);
        mTrackDurationStartView.setText("0:00");
        mTrackDurationEndView.setText("0:30");

        // Use preview track url to play track
        if (previewUrl != null) {
            Intent intent = new Intent(getActivity(), PlayService.class);

            intent.putExtra(Intent.EXTRA_TEXT, previewUrl);
            getActivity().startService(intent);
        }

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }
}
