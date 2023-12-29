package com.ter;

import com.thirdparty.OpenSimplex2S;

public class Noise {

    private final int width;
    private final int height;
    private final float[][] noiseData;

    public Noise(int width, int height, int seed, double scale) {
        this.width = width;
        this.height = height;
        this.noiseData = new float[width][height];
        generateNoise(seed, scale);
    }

    private void generateNoise(int seed, double scale) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Generate noise value between -1 and 1
                float noiseValue = OpenSimplex2S.noise2_ImproveX(seed, x * scale, y * scale);

                // Normalize the noise value to be between 0 and 1
                noiseData[x][y] = (noiseValue + 1) / 2.0f;
            }
        }
    }

    public float getNormalizedValue(int x, int y) {
        return noiseData[x][y];
    }
}
