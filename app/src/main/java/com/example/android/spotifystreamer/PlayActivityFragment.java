package com.example.android.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayActivityFragment extends Fragment {

    public PlayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        // Use preview track url to play track
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String previewUrl = intent.getStringExtra(Intent.EXTRA_TEXT);

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
