package dev.tavishjain.spotifycard.controller;

import dev.tavishjain.spotifycard.client.LastFmClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    private final LastFmClient lastFmClient;

    public CardController(LastFmClient lastFmClient) {
        this.lastFmClient = lastFmClient;
    }

    @GetMapping("/api/nowplaying/{username}")
    public String getNowPlaying(@PathVariable String username) {
        String response = lastFmClient.getNowPlaying(username);
        return response;
    }
}
