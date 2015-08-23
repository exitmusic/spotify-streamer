package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class ArtistTrackActivity extends ActionBarActivity implements ArtistTrackActivityFragment.Callback {

    private static final String PLAYACTIVITYFRAGMENT_TAG = "PAFTAG";

    //private static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_track);
        //mFragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        String artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        String artistName = intent.getStringExtra(Intent.EXTRA_TITLE);

        if (intent != null) {
            ActionBar actionBar = getSupportActionBar();

            // Help from discussion forum:
            // https://discussions.udacity.com/t/getactionbar-returns-null/22885/2?u=kai_3206503580210654
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setSubtitle(artistName);
            }
        }

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString(ArtistTrackActivityFragment.ARTIST_ID, artistId);
            args.putString(ArtistTrackActivityFragment.ARTIST_NAME, artistName);

            ArtistTrackActivityFragment fragment = new ArtistTrackActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.artist_tracks_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_track, menu);
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

    @Override
    public void onTrackSelected(ParcelableTrack track, ArrayList<ParcelableTrack> playlist, int pos) {

        PlayActivityFragment fragment = new PlayActivityFragment();
        Bundle args = new Bundle();

        args.putParcelable(PlayActivityFragment.PARCEL_TRACK, track);
        args.putParcelableArrayList(PlayActivityFragment.PLAYLIST, playlist);
        args.putInt(PlayActivityFragment.POSITION, pos);
//        args.putString(PlayActivityFragment.ARTIST_NAME, artistName);
//        args.putString(PlayActivityFragment.ALBUM, album);
//        args.putString(PlayActivityFragment.COVER_URL, cover);
//        args.putString(PlayActivityFragment.TRACK_NAME, track);
//        args.putLong(PlayActivityFragment.TRACK_DURATION, duration);
//        args.putString(PlayActivityFragment.PREVIEW_URL, previewUrl);
        fragment.setArguments(args);

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(R.id.artist_tracks_container, fragment, PLAYACTIVITYFRAGMENT_TAG)
                .addToBackStack(null).commit();
    }
}
