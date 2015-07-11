package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArtistAdapter mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                    SearchArtistsTask searchArtistsTask = new SearchArtistsTask();

                    searchArtistsTask.execute(v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        if (savedInstanceState == null) {
            mArtistAdapter = new ArtistAdapter(getActivity(), new ArrayList<Artist>(0));
        }

        // Get reference to ListView and bind the adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);

        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mArtistAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ArtistTracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist.id);

                startActivity(intent);
            }
        });

        return rootView;
    }

    private class SearchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = SearchArtistsTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {
            // Connect to Spotify API with the wrapper
            SpotifyApi api = new SpotifyApi();

            // Create a SpotifyService object
            SpotifyService spotify = api.getService();

            return spotify.searchArtists(params[0]);
        }

        @Override
        protected void onPostExecute(ArtistsPager results) {
            mArtistAdapter.clear();

            for (Artist artist : results.artists.items) {
                mArtistAdapter.add(artist);
            }
        }
    }
}
