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

    public Cell getCell(int x, int y) {
        return terrainData[x][y];
    }

    public void setCell(int x, int y, Cell newCell) {
        terrainData[x][y] = newCell;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void generateTerrain(int seed, double scale) {
        Noise noiseGenerator = new Noise(width, height, seed, scale);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float noiseValue = noiseGenerator.getNormalizedValue(x, y);
                Cell cell = Cell.newBuilder()
                        .setHeight(noiseValue)
                        .setWater(0.0f)
                        .setSediment(0.0f)
                        .build();
                terrainData[x][y] = cell;
            }
        }
    }
}
