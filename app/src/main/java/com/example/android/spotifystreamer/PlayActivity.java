package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PlayActivity extends AppCompatActivity {

    private static final String LOG_TAG = PlayActivity.class.getSimpleName();
    private static final String PLAYACTIVITYFRAGMENT_TAG = "PAFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        String artistName = intent.getStringExtra(PlayActivityFragment.ARTIST_NAME);
        String albumName = intent.getStringExtra(PlayActivityFragment.ALBUM);
        String coverUrl = intent.getStringExtra(PlayActivityFragment.COVER_URL);
        String trackName = intent.getStringExtra(PlayActivityFragment.TRACK_NAME);
        String previewUrl = intent.getStringExtra(PlayActivityFragment.PREVIEW_URL);

        if (savedInstanceState == null) {
            PlayActivityFragment fragment = new PlayActivityFragment();
            Bundle args = new Bundle();

            args.putString(PlayActivityFragment.ARTIST_NAME, artistName);
            args.putString(PlayActivityFragment.ALBUM, albumName);
            args.putString(PlayActivityFragment.COVER_URL, coverUrl);
            args.putString(PlayActivityFragment.TRACK_NAME, trackName);
            args.putString(PlayActivityFragment.PREVIEW_URL, previewUrl);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            if (mTwoPane) {
                fragment.show(fragmentManager, "PLAYACTIVITYFRAGMENT_TAG");
            } else {
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                transaction.add(R.id.play_container, fragment, PLAYACTIVITYFRAGMENT_TAG)
                        .addToBackStack(null).commit();
            }

        }

//        // The device is using a large layout, so show the fragment as a dialog
//        fragment.show(mFragmentManager, PLAYACTIVITYFRAGMENT_TAG);
//
//        // The device is smaller, so show the fragment fullscreen
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        // For a little polish, specify a transition animation
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        // To make it fullscreen, use the 'content' root view as the container
//        // for the fragment, which is always the root view for the activity
//        transaction.add(R.id.artist_tracks_container, fragment, PLAYACTIVITYFRAGMENT_TAG)
//                .addToBackStack(null).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
