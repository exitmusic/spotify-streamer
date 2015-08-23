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

import com.example.android.spotifystreamer.data.ParcelableTrack;

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
    private ArrayList<ParcelableTrack> mPlaylist;
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
        void onTrackSelected(ParcelableTrack track, ArrayList<ParcelableTrack> playlist, int pos);
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
                ParcelableTrack pTrack = new ParcelableTrack(
                        track.artists.get(0).name,
                        track.album.name,
                        track.album.images.get(0).url,
                        track.name,
                        track.preview_url
                );

                ((Callback) getActivity()).onTrackSelected(pTrack, mPlaylist, position);
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
            mPlaylist = new ArrayList<>();

            if (!tracks.tracks.isEmpty()) {
                for (Track track : tracks.tracks) {
                    mArtistTrackAdapter.add(track);

                    // Create playlist for PlayActivityFragment
                    // Setting to empty string because certain tracks have null fields
                    String artistName = "";
                    String albumName = "";
                    String coverUrl = "";
                    String trackName = "";
                    String previewUrl = "";

                    artistName = track.artists.get(0).name;
                    albumName = track.album.name;
                    coverUrl = track.album.images.get(0).url;
                    trackName = track.name;
                    previewUrl = track.preview_url;

                    ParcelableTrack pTrack = new ParcelableTrack(
                            artistName,
                            albumName,
                            coverUrl,
                            trackName,
                            previewUrl
                    );

                    mPlaylist.add(pTrack);
                }
            } else {
                Toast.makeText(getActivity(), "No tracks available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
