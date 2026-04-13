package dev.tavishjain.spotifycard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpotifyCardApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        SpringApplication.run(SpotifyCardApplication.class, args);
    }
}
