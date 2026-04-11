package dev.tavishjain.spotifycard.model;

public class TrackInfo {

    private String trackName;
    private String artistName;
    private String artworkUrl;

    public TrackInfo(String trackName, String artistName, String artworkUrl) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.artworkUrl = artworkUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }
}
