package com.ter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoiseTest {

    @Test
    public void testNoiseValuesInRange() {
        Noise noise = new Noise(100, 100, 12345, 0.005);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float value = noise.getNormalizedValue(x, y);
                assertTrue(value >= 0.0f && value <= 1.0f, "Noise value should be between 0 and 1");
            }
        }
    }

    @Test
    public void testNoiseDeterminism() {
        Noise noise1 = new Noise(10, 10, 12345, 0.005);
        Noise noise2 = new Noise(10, 10, 12345, 0.005);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                assertEquals(noise1.getNormalizedValue(x, y), noise2.getNormalizedValue(x, y), "Noise values should be deterministic for the same seed");
            }
        }
    }

    @Test
    public void testNoiseArraySize() {
        int width = 50, height = 60;
        Noise noise = new Noise(width, height, 12345, 0.005);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                assertNotNull(noise.getNormalizedValue(x, y), "Each noise value should be initialized");
            }
        }
    }
}
