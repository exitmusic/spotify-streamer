package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTracksActivityFragment extends Fragment {

    private final String LOG_TAG = ArtistTracksActivityFragment.class.getSimpleName();
    private ArtistTrackAdapter mArtistTrackAdapter;

    public ArtistTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist_tracks, container, false);
        Intent intent = getActivity().getIntent();

        // Use artistId passed from intent to get artist's top tracks
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            getTopTracks(intent.getStringExtra(Intent.EXTRA_TEXT));
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist_tracks);

        mArtistTrackAdapter = new ArtistTrackAdapter(getActivity(), new ArrayList<Track>());
        listView.setAdapter(mArtistTrackAdapter);

        return rootView;
    }

    private void getTopTracks(String artistId) {
        TopTracksTask tracksTask = new TopTracksTask();

        tracksTask.execute(artistId);
    }

    private class TopTracksTask extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = TopTracksTask.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            HashMap<String, Object> queryMap = new HashMap<>();

            queryMap.put("country", "US");
            return spotify.getArtistTopTrack(params[0], queryMap);
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            mArtistTrackAdapter.clear();

            if (!tracks.tracks.isEmpty()) {
                for (Track track : tracks.tracks) {
                    mArtistTrackAdapter.add(track);
                }
            } else {
                Toast.makeText(getActivity(), "No tracks available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
