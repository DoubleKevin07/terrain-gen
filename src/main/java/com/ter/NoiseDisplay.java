package com.ter;

import com.ter.Noise;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NoiseDisplay extends JFrame {
    private BufferedImage image;
    private Noise noiseGenerator;

    public NoiseDisplay() {
        // Initialize the window and other settings...

        noiseGenerator = new Noise(512, 512, 123, 0.005);

        setSize(512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an image buffer
        image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

        // Generate noise and store in the buffer
        generateImage();

        // Display the window
        setVisible(true);
    }

    private void generateImage() {
        image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 512; x++) {
            for (int y = 0; y < 512; y++) {
                float normalizedValue = noiseGenerator.getNormalizedValue(x, y);
                int colorValue = (int)(normalizedValue * 255);  // Scale to 0-255
                int color = (colorValue << 16) | (colorValue << 8) | colorValue;  // Grayscale color
                image.setRGB(x, y, color);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        // Draw the generated image
        g.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) {
        new NoiseDisplay();
    }
}