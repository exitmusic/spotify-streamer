package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SearchArtistFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARTISTTRACKFRAGMENT_TAG = "ATFTAG";
    private static final String PLAYACTIVITYFRAGMENT_TAG = "PAFTAG";

    private boolean mTwoPane;
    private static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.artist_tracks_container) != null) {
            // The artists tracks container will only be present in two-pane mode
            mTwoPane = true;
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onArtistSelected(String artistId, String artistName) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by adding or replacing
            // the detail fragment using a fragment transaction.
            Bundle args = new Bundle();
            args.putString(ArtistTrackActivityFragment.ARTIST_ID, artistId);
            args.putString(ArtistTrackActivityFragment.ARTIST_NAME, artistName);

            ArtistTrackActivityFragment fragment = new ArtistTrackActivityFragment();
            fragment.setArguments(args);

            mFragmentManager.beginTransaction()
                    .replace(R.id.artist_tracks_container, fragment, ARTISTTRACKFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, ArtistTrackActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, artistId)
                    .putExtra(Intent.EXTRA_TITLE, artistName);
            startActivity(intent);
        }
    }

//    @Override
//    public void onTrackSelected(
//            String artistName,
//            String album,
//            String cover,
//            String track,
//            Long duration,
//            String previewUrl) {
//
//        //FragmentManager fragmentManager = getSupportFragmentManager();
//        PlayActivityFragment fragment = new PlayActivityFragment();
//        Bundle args = new Bundle();
//
//        args.putString(PlayActivityFragment.ARTIST_NAME, artistName);
//        args.putString(PlayActivityFragment.ALBUM, album);
//        args.putString(PlayActivityFragment.COVER_URL, cover);
//        args.putString(PlayActivityFragment.TRACK_NAME, track);
//        args.putLong(PlayActivityFragment.TRACK_DURATION, duration);
//        args.putString(PlayActivityFragment.PREVIEW_URL, previewUrl);
//        fragment.setArguments(args);
//
//        // The device is using a large layout, so show the fragment as a dialog
//        fragment.show(mFragmentManager, PLAYACTIVITYFRAGMENT_TAG);
//
//    }

//    public static class PlayActivityReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int playAction = intent.getIntExtra(Intent.ACTION_CALL, -1);
//            PlayActivityFragment paf = (PlayActivityFragment) mFragmentManager
//                    .findFragmentByTag(PLAYACTIVITYFRAGMENT_TAG);
//
//            if (paf != null) {
//                switch (playAction) {
//                    case PlayActivityFragment.TRACK_PLAY:
//                        paf.startSeekBar();
//                        break;
//                    case PlayActivityFragment.TRACK_PAUSE:
//                        break;
//                    case PlayActivityFragment.TRACK_PREVIOUS:
//                        break;
//                    case PlayActivityFragment.TRACK_NEXT:
//                        break;
//                }
//            }
//
//        }
//    }
}
