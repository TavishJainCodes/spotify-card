package dev.tavishjain.spotifycard.controller;

import dev.tavishjain.spotifycard.model.TrackInfo;
import dev.tavishjain.spotifycard.renderer.CardRenderer;
import dev.tavishjain.spotifycard.service.LastFmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CardController {

    private final LastFmService lastFmService;
    private final CardRenderer cardRenderer;

    public CardController(
            LastFmService lastFmService,
            CardRenderer cardRenderer) {
        this.lastFmService = lastFmService;
        this.cardRenderer = cardRenderer;
    }

    @GetMapping("/api/nowplaying/{username}")
    public TrackInfo getNowPlaying(@PathVariable String username) {
        TrackInfo trackInfo = lastFmService.getTrackInfo(username);
        return trackInfo;
    }

    @GetMapping("/api/card/{username}")
    public ResponseEntity<byte[]> getCardRender(@PathVariable String username) {
        TrackInfo trackInfo = lastFmService.getTrackInfo(username);

        byte[] card = cardRenderer.render(trackInfo);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(card);
    }

    @GetMapping("/api/card/search")
    public ResponseEntity<byte[]> getSearchCardRender(@RequestParam String trackName, @RequestParam String artworkUrl) {
        TrackInfo trackInfo = lastFmService.getSearch(trackName, artworkUrl);

        byte[] card = cardRenderer.render(trackInfo);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(card);
    }
}
