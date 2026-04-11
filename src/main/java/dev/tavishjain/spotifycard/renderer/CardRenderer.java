package dev.tavishjain.spotifycard.renderer;

import dev.tavishjain.spotifycard.model.TrackInfo;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class CardRenderer {

    public byte[] render(TrackInfo trackInfo) {
        BufferedImage image = new BufferedImage(
            800,
            200,
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = image.createGraphics();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 800, 200);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(trackInfo.getTrackName(), 20, 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(trackInfo.getArtistName(), 20, 100);

        g.dispose();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Last.fm response", e);
        }
    }
}
