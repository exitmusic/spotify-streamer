package com.example.android.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTrackActivityFragment extends Fragment {

    private final String LOG_TAG = ArtistTrackActivityFragment.class.getSimpleName();
    static final String ARTIST_ID = "ARTIST_ID";
    static final String ARTIST_NAME = "ARTIST_NAME";

    private ArtistTrackAdapter mArtistTrackAdapter;
    private String mArtistId;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * PlayCallback for when an item has been selected.
         */
        void onTrackSelected(
                String artistName,
                String album,
                String cover,
                String track,
                Long duration,
                String previewUrl
        );
    }

    public ArtistTrackActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {
            mArtistId = args.getString(ARTIST_ID);
        }

        //Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_artist_track, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist_tracks);

        if (savedInstanceState == null) {
            mArtistTrackAdapter = new ArtistTrackAdapter(getActivity(), new ArrayList<Track>());

            // Use artistId passed from intent to get artist's top tracks
            if (mArtistId != null) {
                getTopTracks(mArtistId);
            }
        }
        listView.setAdapter(mArtistTrackAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = mArtistTrackAdapter.getItem(position);

                ((Callback) getActivity()).onTrackSelected(
                        track.artists.get(0).name,
                        track.album.name,
                        track.album.images.get(0).url,
                        track.name,
                        track.duration_ms,
                        track.preview_url);
//                Intent intent = new Intent(getActivity(), PlayActivity.class)
//                        .putExtra(PlayActivityFragment.ARTIST_NAME, track.artists.get(0).name)
//                        .putExtra(PlayActivityFragment.ALBUM, track.album.name)
//                        .putExtra(PlayActivityFragment.COVER_URL, track.album.images.get(0).url)
//                        .putExtra(PlayActivityFragment.TRACK_NAME, track.name)
//                        .putExtra(PlayActivityFragment.TRACK_DURATION, track.duration_ms)
//                        .putExtra(PlayActivityFragment.PREVIEW_URL, track.preview_url);
//
//                startActivity(intent);
            }
        });

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
            Tracks tracksResult = new Tracks();

            queryMap.put("country", "US");
            try {
                tracksResult = spotify.getArtistTopTrack(params[0], queryMap);
            } catch (RetrofitError retrofitError) {
                Log.e(LOG_TAG, retrofitError.toString());
            }

            return tracksResult;
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
