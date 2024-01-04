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
    private final float globalSedimentCapacity; // K_c

    public SimulationFlow(Terrain terrain, float globalRateParameter, float globalSedimentCapacity) {
        this.terrain = terrain;
        this.globalRateParameter = globalRateParameter;
        this.globalSedimentCapacity = globalSedimentCapacity;
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

    public float calculateSedimentTransportCapacity(int x, int y) {
        float Kc = globalSedimentCapacity;
        List<Float> velocityVector = calculateVelocityVector(x, y);
        double slopeAngle = calculateLocalTiltAngle(x, y);
        float magnitudeOfVelocity = (float) Math.sqrt(Math.pow(velocityVector.get(0), 2) + Math.pow(velocityVector.get(1), 2));
    
        // Calculate sediment transport capacity
        float sedimentTransportCapacity = Kc * (float) Math.sin(Math.toRadians(slopeAngle)) * magnitudeOfVelocity;
        return sedimentTransportCapacity; // Equation [9].
    }

    public double calculateLocalTiltAngle(int x, int y) {
        // Calculate the gradients in x and y directions
        float dHeightDx = (calculateDeltaH(x, y, x + 1, y) - calculateDeltaH(x, y, x - 1, y)) / 2.0f;
        float dHeightDy = (calculateDeltaH(x, y, x, y + 1) - calculateDeltaH(x, y, x, y - 1)) / 2.0f;

        // Calculate the magnitude of the gradient vector (which is the slope)
        float slope = (float) Math.sqrt(dHeightDx * dHeightDx + dHeightDy * dHeightDy);

        // Calculate the angle of the slope in radians
        float slopeAngle = (float) Math.atan(slope);

        // Return the slope angle in degrees
        return Math.toDegrees(slopeAngle); // Alpha
    }

    public List<Float> calculateVelocityVector(int x, int y) {
        Cell cell = terrain.getCell(x, y);
    
        float leftFlux = cell.getFlux(FluxIndex.LEFT_VALUE);
        float rightFlux = cell.getFlux(FluxIndex.RIGHT_VALUE);
        float topFlux = cell.getFlux(FluxIndex.TOP_VALUE);
        float bottomFlux = cell.getFlux(FluxIndex.BOTTOM_VALUE);

        float leftNeighborRightFlux = x > 0 ? terrain.getCell(x - 1, y).getFlux(FluxIndex.RIGHT_VALUE) : 0;
        float rightNeighborLeftFlux = x < terrain.getWidth() - 1 ? terrain.getCell(x + 1, y).getFlux(FluxIndex.LEFT_VALUE) : 0;
        float topNeighborBottomFlux = y > 0 ? terrain.getCell(x, y - 1).getFlux(FluxIndex.BOTTOM_VALUE) : 0;
        float bottomNeighborTopFlux = y < terrain.getHeight() - 1 ? terrain.getCell(x, y + 1).getFlux(FluxIndex.TOP_VALUE) : 0;

        float deltaWx = 0.5f * (leftNeighborRightFlux - leftFlux + rightFlux - rightNeighborLeftFlux);
        float deltaWy = 0.5f * (topNeighborBottomFlux - topFlux + bottomFlux - bottomNeighborTopFlux);

        return Arrays.asList(deltaWx, deltaWy); // Equation [8].
    }
}
