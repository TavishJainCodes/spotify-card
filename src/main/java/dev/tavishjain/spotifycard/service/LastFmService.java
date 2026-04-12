package dev.tavishjain.spotifycard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tavishjain.spotifycard.client.ItunesClient;
import dev.tavishjain.spotifycard.client.LastFmClient;
import dev.tavishjain.spotifycard.model.TrackInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LastFmService {

    private final LastFmClient lastFmClient;
    private final ItunesClient itunesClient;

    public LastFmService(LastFmClient lastFmClient, ItunesClient itunesClient) {
        this.lastFmClient = lastFmClient;
        this.itunesClient = itunesClient;
    }

    private String getArtworkFromItunes(String trackName, String artistName) {
        try {
            String responseJson = itunesClient.search(trackName + " " + artistName);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);
            JsonNode results = root.get("results");
            if (results == null || results.size() == 0)
                return "";
            String artworkUrl = results.get(0).get("artworkUrl100").asText();
            return artworkUrl.replace("100x100bb", "600x600bb");
        } catch (Exception e) {
            return "";
        }
    }

    @Cacheable(value = "trackInfo", key = "#username")
    public TrackInfo getTrackInfo(String username) {
        try {
            String nowPlayingJson = lastFmClient.getNowPlaying(username);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(nowPlayingJson);

            JsonNode track = root.get("recenttracks").get("track").get(0);
            String trackName = track.get("name").asText();
            String artistName = track.get("artist").get("#text").asText();
            String artworkUrl = getArtworkFromItunes(trackName, artistName);

            return new TrackInfo(trackName, artistName, artworkUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Last.fm response", e);
        }
    }

    public List<TrackInfo> searchTracks(String query) {
        try {
            String responseJson = lastFmClient.searchTracks(query);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);
            JsonNode tracks = root.get("results").get("trackmatches").get("track");

            List<TrackInfo> trackInfoList = new ArrayList<>();
            for (int i = 0; i < tracks.size(); i++) {
                String trackName = tracks.get(i).get("name").asText();
                String artistName = tracks.get(i).get("artist").asText();
                String artworkUrl = getArtworkFromItunes(trackName, artistName);
                trackInfoList.add(new TrackInfo(trackName, artistName, artworkUrl));
            }
            return trackInfoList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse search response", e);
        }
    }

    public TrackInfo getSearch(String trackName, String artistName) {
        try {
            String artworkUrl = getArtworkFromItunes(trackName, artistName);
            return new TrackInfo(trackName, artistName, artworkUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get track info", e);
        }
    }
}