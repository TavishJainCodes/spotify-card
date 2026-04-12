package dev.tavishjain.spotifycard.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LastFmClient {

    @Value("${lastfm.api.key}")
    private String apiKey;

    @Value("${lastfm.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getNowPlaying(String username) {
        String url = apiUrl +
                "?method=user.getrecenttracks&user=" +
                username +
                "&api_key=" +
                apiKey +
                "&format=json&limit=1";
        return restTemplate.getForObject(url, String.class);
    }

    public String searchTracks(String query) {
        String url = apiUrl +
                "?method=track.search&track=" +
                query +
                "&api_key=" +
                apiKey +
                "&format=json&limit=10";
        return restTemplate.getForObject(url, String.class);
    }
}