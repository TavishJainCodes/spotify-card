package dev.tavishjain.spotifycard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tavishjain.spotifycard.client.LastFmClient;
import dev.tavishjain.spotifycard.model.TrackInfo;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
public class LastFmService {

    private final LastFmClient lastFmClient;

    public LastFmService(LastFmClient lastFmClient) {
        this.lastFmClient = lastFmClient;
    }

    @Cacheable(value = "trackInfo", key = "#username")
    public TrackInfo getTrackInfo(String username) {
        String nowPlayingJson = lastFmClient.getNowPlaying(username);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(nowPlayingJson);

            String trackName = root
                    .get("recenttracks")
                    .get("track")
                    .get(0)
                    .get("name")
                    .asText();
            String artistName = root
                    .get("recenttracks")
                    .get("track")
                    .get(0)
                    .get("artist")
                    .get("#text")
                    .asText();
            String artworkUrl = root
                    .get("recenttracks")
                    .get("track")
                    .get(0)
                    .get("image")
                    .get(3)
                    .get("#text")
                    .asText();

            TrackInfo trackInfo = new TrackInfo(
                    trackName,
                    artistName,
                    artworkUrl);
            return trackInfo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Last.fm response", e);
        }
    }

    public TrackInfo getSearch(String trackName, String artworkUrl) {
        String responseJson = lastFmClient.getInfo(trackName);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);

            JsonNode trackNode = root
                    .get("results")
                    .get("trackmatches")
                    .get("track");

            for (int i = 0; i < 10; i++) {
                if (trackNode.get(i).get("image").get(3).get("#text").asText()
                        .equals("https://lastfm.freetls.fastly.net/i/u/300x300/" + artworkUrl + ".jpg")) {
                    String trackNameFromResponse = trackNode.get(i).get("name").asText();
                    String artistNameFromResponse = trackNode.get(i).get("artist").asText();
                    TrackInfo trackInfo = new TrackInfo(
                            trackNameFromResponse,
                            artistNameFromResponse,
                            artworkUrl);
                    return trackInfo;
                }
            }

            String trackNameFromResponse = trackNode.get(0).get("name").asText();
            String artistNameFromResponse = trackNode.get(0).get("artist").asText();
            TrackInfo trackInfo = new TrackInfo(
                    trackNameFromResponse,
                    artistNameFromResponse,
                    "https://lastfm.freetls.fastly.net/i/u/300x300/" + artworkUrl + ".jpg");

            return trackInfo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Last.fm response", e);
        }
    }
}
