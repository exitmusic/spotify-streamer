package com.example.android.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by kchang on 7/10/15.
 */
public class ArtistTrackAdapter extends ArrayAdapter<Track> {

    private static final String LOG_TAG = ArtistTrackAdapter.class.getSimpleName();

    public ArtistTrackAdapter(Activity context, List<Track> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist_track, parent, false);

        ImageView albumView = (ImageView) rootView.findViewById(R.id.list_item_artist_track_imageview);

        if (!track.album.images.isEmpty()) {
            Picasso.with(getContext()).load(track.album.images.get(0).url).into(albumView);
        }

        TextView artistTrackView = (TextView) rootView.findViewById(R.id.list_item_artist_track_name_textview);
        artistTrackView.setText(track.name);

        TextView artistAlbumView = (TextView) rootView.findViewById(R.id.list_item_artist_track_album_name_textview);
        artistAlbumView.setText(track.album.name);

        return rootView;
    }
}
