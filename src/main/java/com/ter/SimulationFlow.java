package com.ter;

import com.ter.proto.Cell;

public class SimulationFlow implements Simulation {
    private Terrain terrain;
    private final float globalRateParameter; // K_r

    public SimulationFlow(Terrain terrain, float globalRateParameter) {
        this.terrain = terrain;
        this.globalRateParameter = globalRateParameter;
    }

    @Override
    public void simulate(float deltaTime) {
        // Calculate the outflow for each cell
        float[][][] outflows = calculateOutflows(deltaTime);

        // Update the water height in each cell based on the outflows
        updateWaterHeights(outflows, deltaTime);
    }

    public float calculateDeltaHLeft(int x, int y) {
        return calculateDeltaH(x, y, x - 1, y);
    }

    public float calculateDeltaHRight(int x, int y) {
        return calculateDeltaH(x, y, x + 1, y);
    }

    public float calculateDeltaHBottom(int x, int y) {
        return calculateDeltaH(x, y, x, y + 1);
    }

    public float calculateDeltaHTop(int x, int y) {
        return calculateDeltaH(x, y, x, y - 1);
    }

    private float calculateDeltaH(int x1, int y1, int x2, int y2) {
        // Check boundary conditions
        if (x2 < 0 || x2 >= terrain.getWidth() || y2 < 0 || y2 >= terrain.getHeight()) {
            return 0; // No flow beyond the boundaries of the terrain
        }

        float bCurrent = terrain.getCell(x1, y1).getHeight();
        float dCurrent = terrain.getCell(x1, y1).getWater();
        float bNeighbor = terrain.getCell(x2, y2).getHeight();
        float dNeighbor = terrain.getCell(x2, y2).getWater();

        return (bCurrent + dCurrent) - (bNeighbor + dNeighbor);
    }

    private float[][][] calculateOutflows(float deltaTime) {
        int width = terrain.getWidth();
        int height = terrain.getHeight();
        float[][][] outflows = new float[width][height][4]; // For each direction: left, right, top, bottom

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                outflows[x][y] = calculateCellOutflow(x, y, deltaTime);
            }
        }

        return outflows;
    }

    private float[] calculateCellOutflow(int x, int y, float deltaTime) {
        // Placeholder for calculating outflow from a single cell.
        // This method will use the formulas (2) to (5) from the document.
        // Return an array of four float values representing the outflow to each
        // neighbor.
        return new float[4];
    }

    private void updateWaterHeights(float[][][] outflows, float deltaTime) {
        int width = terrain.getWidth();
        int height = terrain.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                applyOutflowsToCell(x, y, outflows, deltaTime);
            }
        }
    }

    private void applyOutflowsToCell(int x, int y, float[][][] outflows, float deltaTime) {
        // Placeholder for applying outflows to the water height of the current cell and
        // adding the inflows from the neighboring cells.
        // This method will use the formula (6) from the document.
    }
}
