package com.example.android.spotifystreamer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
public class SearchArtistFragment extends Fragment {

    private final String LOG_TAG = SearchArtistFragment.class.getSimpleName();
    private ArtistAdapter mArtistAdapter;
    private Toast mToast;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * ArtistTrackCallback for when an item has been selected.
         */
        void onArtistSelected(String artistId, String artistName);
    }

    public SearchArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_artist, container, false);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.input_search_artist);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchArtistsTask searchArtistsTask = new SearchArtistsTask();

                mToast = Toast.makeText(getActivity(), query + " not found.", Toast.LENGTH_SHORT);

                // Check if network is available first before executing query
                if (isNetworkAvailable()) {
                    searchArtistsTask.execute(query);
                }
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

                ((Callback) getActivity()).onArtistSelected(artist.id, artist.name);
            }
        });

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
