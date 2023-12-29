package com.ter;

import com.ter.proto.Cell;

public class Terrain {

    private final int width;
    private final int height;
    private final Cell[][] terrainData;

    public Terrain(int width, int height, int seed, double scale) {
        this.width = width;
        this.height = height;
        this.terrainData = new Cell[width][height];
        generateTerrain(seed, scale);
    }

    private void generateTerrain(int seed, double scale) {
        Noise noiseGenerator = new Noise(width, height, seed, scale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float noiseValue = noiseGenerator.getNormalizedValue(x, y);
                Cell cell = Cell.newBuilder()
                        .setHeight(noiseValue)
                        .setWater(0.0f) // default value
                        .setSediment(0.0f) // default value
                        .build();
                terrainData[x][y] = cell;
            }
        }
    }

    public Cell getCell(int x, int y) {
        return terrainData[x][y];
    }
}
