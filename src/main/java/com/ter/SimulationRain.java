package com.ter;

import com.ter.proto.Cell;

public class SimulationRain implements Simulation {
    private Terrain terrain;
    private float rainRate; // This could be a constant or vary per cell
    private float globalRateParameter; // Kr value as described in the document

    public SimulationRain(Terrain terrain, float rainRate, float globalRateParameter) {
        this.terrain = terrain;
        this.rainRate = rainRate; // Constant rain rate r(x, y) for simplicity
        this.globalRateParameter = globalRateParameter; // Kr
    }

    @Override
    public void simulate(float deltaTime) {
        for (int x = 0; x < terrain.getWidth(); x++) {
            for (int y = 0; y < terrain.getHeight(); y++) {
                Cell cell = terrain.getCell(x, y);
                float newWaterHeight = cell.getWater() + deltaTime * rainRate * globalRateParameter;

                // Since we will be making changes to cells over time, it's probably for the
                // best that we replace the Cell type with a mutable class (non proto buffers)
                // otherwise it may be a little slower than intended...
                cell = cell.toBuilder().setWater(newWaterHeight).build();
                terrain.setCell(x, y, cell);
            }
        }
    }
}