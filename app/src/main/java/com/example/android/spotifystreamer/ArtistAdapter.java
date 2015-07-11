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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by kchang on 7/7/15.
 *
 * Inspired by custom ArrayAdapter webcast:
 * https://github.com/udacity/android-custom-arrayadapter
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    public ArtistAdapter(Activity context, List<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist artist = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);

        ImageView artistImageView = (ImageView) rootView.findViewById(R.id.list_item_artist_imageview);

        if (!artist.images.isEmpty()) {
            // Using larger image because the smallest is fuzzy at 100x100dp
            Picasso.with(getContext()).load(artist.images.get(0).url).into(artistImageView);
        }

        TextView artistNameView = (TextView) rootView.findViewById(R.id.list_item_artist_name_textview);
        artistNameView.setText(artist.name);

        return rootView;
    }
}
