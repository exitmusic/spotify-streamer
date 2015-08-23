package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kchang on 8/22/15.
 */
public class ParcelableTrack implements Parcelable {

    public String artistName;
    public String albumName;
    public String coverUrl;
    public String trackName;
    public String previewUrl;

    public ParcelableTrack(String artistName, String albumName, String coverUrl, String trackName, String previewUrl) {
        this.artistName = artistName;
        this.albumName = albumName;
        this.coverUrl = coverUrl;
        this.trackName = trackName;
        this.previewUrl = previewUrl;
    }

    protected ParcelableTrack(Parcel in) {
        artistName = in.readString();
        albumName = in.readString();
        coverUrl = in.readString();
        trackName = in.readString();
        previewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(albumName);
        dest.writeString(coverUrl);
        dest.writeString(trackName);
        dest.writeString(previewUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelableTrack> CREATOR = new Parcelable.Creator<ParcelableTrack>() {
        @Override
        public ParcelableTrack createFromParcel(Parcel in) {
            return new ParcelableTrack(in);
        }

        @Override
        public ParcelableTrack[] newArray(int size) {
            return new ParcelableTrack[size];
        }
    };
}
