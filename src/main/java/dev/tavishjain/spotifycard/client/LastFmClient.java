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

    public String getNowPlaying(String username) {
        RestTemplate restTemplate = new RestTemplate();

        //https://ws.audioscrobbler.com/2.0?method=user.getrecenttracks&user=NightMare&api_key=abc123&format=json&limit=1

        String url =
            apiUrl +
            "?method=user.getrecenttracks&user=" +
            username +
            "&api_key=" +
            apiKey +
            "&format=json&limit=1";

        String response = restTemplate.getForObject(url, String.class);
        return response;
    }
}
