package dev.tavishjain.spotifycard.renderer;

import dev.tavishjain.spotifycard.model.TrackInfo;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class CardRenderer {

        private void drawImageCentered(Graphics2D g, BufferedImage img, int cx, int cy, int width, int height) {
                int x = cx - width / 2;
                int y = cy - height / 2;
                g.drawImage(img, x, y, width, height, null);
        }

        public byte[] render(TrackInfo trackInfo) {
                try {
                        // fetch artwork first
                        BufferedImage artwork = null;
                        if (trackInfo.getArtworkUrl() != null && !trackInfo.getArtworkUrl().isEmpty()) {
                                URL imageUrl = new URL(trackInfo.getArtworkUrl());
                                artwork = ImageIO.read(imageUrl);
                        }

                        // create canvas
                        BufferedImage image = new BufferedImage(800, 1200, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = image.createGraphics();

                        // fill dark base in case no artwork
                        g.setColor(new Color(30, 30, 30));
                        g.fillRect(0, 0, 800, 1200);

                        // draw artwork scaled to fill full canvas for blur background
                        if (artwork != null) {
                                g.drawImage(artwork, -30, -30, 860, 1260, null);
                        }
                        g.dispose();

                        // apply blur
                        float[] matrix = new float[64];
                        java.util.Arrays.fill(matrix, 1f / 64);
                        ConvolveOp blurOp = new ConvolveOp(new Kernel(8, 8, matrix), ConvolveOp.EDGE_NO_OP, null);
                        for (int i = 0; i < 50; i++) {
                                image = blurOp.filter(image, null);
                        }

                        // get fresh graphics after blur
                        g = image.createGraphics();
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                        // dark overlay — reduced from 210 to 160
                        g.setColor(new Color(0, 0, 0, 140));
                        g.fillRect(0, 0, 800, 1200);

                        // rounded album art
                        if (artwork != null) {
                                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(30, 30, 740, 740, 60, 60);
                                g.setClip(roundedRect);
                                g.drawImage(artwork, 30, 30, 740, 740, null);
                                g.setClip(null);
                        }

                        // track name — SansSerif works on all platforms including headless servers
                        Font interBold = new Font("SansSerif", Font.BOLD, 56);
                        g.setColor(Color.WHITE);
                        g.setFont(interBold);
                        g.drawString(trackInfo.getTrackName(), 30, 860);

                        // artist name
                        Font inter = new Font("SansSerif", Font.PLAIN, 36);
                        g.setColor(new Color(179, 179, 179));
                        g.setFont(inter);
                        g.drawString(trackInfo.getArtistName(), 30, 910);

                        // duration bar background
                        g.setColor(new Color(80, 80, 80));
                        g.fillRoundRect(30, 980, 740, 8, 8, 8);

                        // duration bar progress (~40%)
                        g.setColor(Color.WHITE);
                        g.fillRoundRect(30, 980, 296, 8, 8, 8);

                        // progress dot
                        g.fillOval(318, 972, 24, 24);

                        // controls
                        BufferedImage imgShuffle = ImageIO.read(getClass().getResourceAsStream("/icons/Shuffle.png"));
                        BufferedImage imgPrev = ImageIO.read(getClass().getResourceAsStream("/icons/Prev.png"));
                        BufferedImage imgPause = ImageIO.read(getClass().getResourceAsStream("/icons/PauseButton.png"));
                        BufferedImage imgNext = ImageIO.read(getClass().getResourceAsStream("/icons/Next.png"));
                        BufferedImage imgLoop = ImageIO.read(getClass().getResourceAsStream("/icons/Loop.png"));

                        drawImageCentered(g, imgShuffle, 63, 1100, 55, 55);
                        drawImageCentered(g, imgPrev, 210, 1100, 50, 50);
                        drawImageCentered(g, imgPause, 400, 1100, 130, 130);
                        drawImageCentered(g, imgNext, 590, 1100, 50, 50);
                        drawImageCentered(g, imgLoop, 740, 1100, 50, 50);

                        g.dispose();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "PNG", baos);
                        return baos.toByteArray();
                } catch (Exception e) {
                        throw new RuntimeException("Failed to render card", e);
                }
        }
}