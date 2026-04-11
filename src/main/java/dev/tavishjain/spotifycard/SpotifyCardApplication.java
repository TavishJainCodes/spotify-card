package dev.tavishjain.spotifycard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpotifyCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyCardApplication.class, args);
    }
}
