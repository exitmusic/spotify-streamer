package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArrayAdapter<String> mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        EditText editText = (EditText) rootView.findViewById(R.id.input_search_artist);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                // IME_ACTION_SEARCH on phone, IME_NULL on emulator keyboard
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL) {
                    //searchArtists(v.getText().toString());
                    Log.v(LOG_TAG, v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        String[] artistData = {
                "Grizzly Bear",
                "Dirty Projectors",
                "Beach House",
                "Radiohead"
        };
        List<String> artists = new ArrayList<String>(Arrays.asList(artistData));

        mArtistAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_artist,
                R.id.list_item_artist_textview,
                artists);
                //new ArrayList<String>());

        // Get reference to ListView and bind the adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistAdapter);

        return rootView;
    }

    private class SearchArtistsTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = SearchArtistsTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            // Connect to Spotify API with the wrapper
            SpotifyApi api = new SpotifyApi();

            // Create a SpotifyService object
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);

            return null;
        }
    }
}
