package dev.tavishjain.spotifycard.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ItunesClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String search(String query) {
        String url = "https://itunes.apple.com/search?term=" +
                query.replace(" ", "+") +
                "&entity=song&limit=5";
        return restTemplate.getForObject(url, String.class);
    }
}