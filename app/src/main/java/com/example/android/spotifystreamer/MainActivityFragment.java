package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArtistAdapter mArtistAdapter;
    private Toast mToast;

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
        SearchView searchView = (SearchView) rootView.findViewById(R.id.input_search_artist);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchArtistsTask searchArtistsTask = new SearchArtistsTask();

                mToast = Toast.makeText(getActivity(), query + " not found.", Toast.LENGTH_SHORT);
                searchArtistsTask.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                        .putExtra(Intent.EXTRA_TEXT, artist.id)
                        .putExtra(Intent.EXTRA_TITLE, artist.name);

                startActivity(intent);
            }
        });

        return rootView;
    }

    private class SearchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = SearchArtistsTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {
            ArtistsPager artistsResult = new ArtistsPager();

            // Connect to Spotify API with the wrapper
            SpotifyApi api = new SpotifyApi();

            // Create a SpotifyService object
            SpotifyService spotify = api.getService();

            try {
                artistsResult = spotify.searchArtists(params[0]);
            } catch (RetrofitError retrofitError) {
                Log.e(LOG_TAG, retrofitError.toString());
            }
            return artistsResult;
        }

        @Override
        protected void onPostExecute(ArtistsPager results) {
            mArtistAdapter.clear();

            if (!results.artists.items.isEmpty()) {
                for (Artist artist : results.artists.items) {
                    mArtistAdapter.add(artist);
                }
            } else {
                mToast.show();
            }
        }
    }
}
