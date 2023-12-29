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
        if (x == 0) {
            // Assuming there's no flow beyond the left boundary of the terrain
            return 0;
        }

        // Get the terrain and water height for the current cell
        float bCurrent = terrain.getCell(x, y).getHeight();
        float dCurrent = terrain.getCell(x, y).getWater();

        // Get the terrain and water height for the cell to the left
        float bLeft = terrain.getCell(x - 1, y).getHeight();
        float dLeft = terrain.getCell(x - 1, y).getWater();

        // Calculate the height difference
        float deltaHLeft = (bCurrent + dCurrent) - (bLeft + dLeft);

        return deltaHLeft;
    }

    // Calculate the change in height to the right of the current cell
    public float calculateDeltaHRight(int x, int y) {
        if (x == terrain.getWidth() - 1) {
            // Assuming there's no flow beyond the right boundary of the terrain
            return 0;
        }

        float bCurrent = terrain.getCell(x, y).getHeight();
        float dCurrent = terrain.getCell(x, y).getWater();

        float bRight = terrain.getCell(x + 1, y).getHeight();
        float dRight = terrain.getCell(x + 1, y).getWater();

        float deltaHRight = (bCurrent + dCurrent) - (bRight + dRight);

        return deltaHRight;
    }

    // Calculate the change in height below the current cell
    public float calculateDeltaHBottom(int x, int y) {
        if (y == terrain.getHeight() - 1) {
            // Assuming there's no flow beyond the bottom boundary of the terrain
            return 0;
        }

        float bCurrent = terrain.getCell(x, y).getHeight();
        float dCurrent = terrain.getCell(x, y).getWater();

        float bBottom = terrain.getCell(x, y + 1).getHeight();
        float dBottom = terrain.getCell(x, y + 1).getWater();

        float deltaHBottom = (bCurrent + dCurrent) - (bBottom + dBottom);

        return deltaHBottom;
    }

    // Calculate the change in height above the current cell
    public float calculateDeltaHTop(int x, int y) {
        if (y == 0) {
            // Assuming there's no flow beyond the top boundary of the terrain
            return 0;
        }

        float bCurrent = terrain.getCell(x, y).getHeight();
        float dCurrent = terrain.getCell(x, y).getWater();

        float bTop = terrain.getCell(x, y - 1).getHeight();
        float dTop = terrain.getCell(x, y - 1).getWater();

        float deltaHTop = (bCurrent + dCurrent) - (bTop + dTop);

        return deltaHTop;
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
