package dev.tavishjain.spotifycard.controller;

import dev.tavishjain.spotifycard.model.TrackInfo;
import dev.tavishjain.spotifycard.service.LastFmService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    private final LastFmService lastFmService;

    public CardController(LastFmService lastFmService) {
        this.lastFmService = lastFmService;
    }

    @GetMapping("/api/nowplaying/{username}")
    public TrackInfo getNowPlaying(@PathVariable String username) {
        TrackInfo trackInfo = lastFmService.getTrackInfo(username);
        return trackInfo;
    }
}
