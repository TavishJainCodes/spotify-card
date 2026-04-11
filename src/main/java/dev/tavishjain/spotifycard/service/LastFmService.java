package dev.tavishjain.spotifycard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tavishjain.spotifycard.client.LastFmClient;
import dev.tavishjain.spotifycard.model.TrackInfo;
import org.springframework.stereotype.Service;

@Service
public class LastFmService {

    private final LastFmClient lastFmClient;

    public LastFmService(LastFmClient lastFmClient) {
        this.lastFmClient = lastFmClient;
    }

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
                artworkUrl
            );
            return trackInfo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Last.fm response", e);
        }
    }
}
