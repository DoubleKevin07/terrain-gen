package com.ter;

import com.ter.proto.Cell;
import com.ter.proto.FluxIndex;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationFlow implements Simulation {
    private static final float GRAVITY = 9.81f; // Earth's gravity in m/s²
    private static final float AREA_OF_PIPE = 1.0f; // Assuming a unit area for simplicity
    private static final float LENGTH_OF_PIPE = 1.0f; // Assuming a unit length for simplicity

    private Terrain terrain;
    private final float globalRateParameter; // K_r

    public SimulationFlow(Terrain terrain, float globalRateParameter) {
        this.terrain = terrain;
        this.globalRateParameter = globalRateParameter;
    }

    @Override
    public void simulate(float deltaTime) {
        // Calculate flux for each cell.

        // Update water height for each cell.

        // Update sediment for each cell.

        // Update height.
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

    public List<Float> calculateOutflowFlux(int x, int y, float deltaTime) {
        float leftFlux = calculateDeltaF(x, y, x - 1, y, deltaTime);
        float rightFlux = calculateDeltaF(x, y, x + 1, y, deltaTime);
        float topFlux = calculateDeltaF(x, y, x, y - 1, deltaTime);
        float bottomFlux = calculateDeltaF(x, y, x, y + 1, deltaTime);
        List<Float> preScaledFlux = Arrays.asList(leftFlux, rightFlux, topFlux, bottomFlux);

        Cell cell = terrain.getCell(x, y);
        float k = calcK(cell, preScaledFlux, deltaTime);

        return preScaledFlux.stream().map(flux -> k * flux).collect(Collectors.toList()); // Equation [5].
    }

    private float calculateDeltaF(int x1, int y1, int x2, int y2, float deltaTime) {
        // Check boundary conditions
        if (x2 < 0 || x2 >= terrain.getWidth() || y2 < 0 || y2 >= terrain.getHeight()) {
            return 0; // No flow beyond the boundaries of the terrain
        }

        // Determine direction - assume no diagonals.
        FluxIndex direction = FluxIndex.LEFT;
        if (x1 < x2) {
            direction = FluxIndex.RIGHT;
        } else if (x1 > x2) {
            direction = FluxIndex.LEFT;
        } else if (y1 < y2) {
            direction = FluxIndex.BOTTOM;
        } else if (y1 > y2) {
            direction = FluxIndex.TOP;
        }

        Cell targetCell = terrain.getCell(x1, y1);
        float currFlux = targetCell.getFlux(direction.getNumber());
        float deltaH = calculateDeltaH(x1, y1, x2, y2);

        float newFlux = currFlux + deltaTime * AREA_OF_PIPE * ((GRAVITY * deltaH) / LENGTH_OF_PIPE); // Equation [2].

        return Math.max(0, newFlux);
    }

    private float calcK(Cell cell, List<Float> outflowFlux, float deltaTime) {
        Float scale = (cell.getWater() * LENGTH_OF_PIPE * LENGTH_OF_PIPE)
                / ((outflowFlux.stream().reduce(0.0f, Float::sum))
                        * deltaTime);
        if (scale.isInfinite()) {
            return 1;
        }
        return Math.max(1, scale); // Equation [4].
    }
}
