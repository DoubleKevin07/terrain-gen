package com.ter;

import com.ter.proto.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationFlowTest {
    private Terrain terrain;
    private SimulationFlow simulationFlow;

    @BeforeEach
    void setUp() {
        // Create a mock terrain with known values for height and water
        // For simplicity, let's assume terrain is 3x3
        terrain = new Terrain(3, 3, 1, 1);
        simulationFlow = new SimulationFlow(terrain, 0.1f); // Assuming a global rate parameter Kr

        // Initialize the cells with known values
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                // Let's assume all cells have the same terrain height but vary in water height
                terrain.setCell(x, y, Cell.newBuilder().setHeight(10).setWater(Math.max(x, y)).build());
                // Increase water height as we go diagonally down to the right.
                /*
                 * 0 1 2
                 * 1 1 2
                 * 2 2 2
                 */
            }
        }
    }

    @Test
    void testCalculateDeltaHLeft() {
        // Middle right cell should have a difference in water height of 1 compared to
        // its left neighbor
        float deltaHLeft = simulationFlow.calculateDeltaHLeft(2, 1);
        assertEquals(1, deltaHLeft, "The deltaHLeft should be 1");

        // Left edge cell should have a deltaHRight of 0 since it has no right neighbor
        deltaHLeft = simulationFlow.calculateDeltaHLeft(0, 1);
        assertEquals(0, deltaHLeft, "The deltaHLeft at the left edge should be 0");
    }

    @Test
    void testCalculateDeltaHRight() {
        // Middle cell should have a difference in water height of 1 compared to its
        // right neighbor
        float deltaHRight = simulationFlow.calculateDeltaHRight(1, 1);
        assertEquals(-1, deltaHRight, "The deltaHRight should be -1");

        // Right edge cell should have a deltaHRight of 0 since it has no right neighbor
        deltaHRight = simulationFlow.calculateDeltaHRight(2, 1);
        assertEquals(0, deltaHRight, "The deltaHRight at the right edge should be 0");
    }

    @Test
    void testCalculateDeltaHBottom() {
        // Middle cell should have a difference in water height of -1 compared to its
        // bottom neighbor
        float deltaHBottom = simulationFlow.calculateDeltaHBottom(1, 1);
        assertEquals(-1, deltaHBottom, "The deltaHBottom should be -1");

        // Bottom edge cell should have a deltaHBottom of 0 since it has no bottom
        // neighbor
        deltaHBottom = simulationFlow.calculateDeltaHBottom(1, 2);
        assertEquals(0, deltaHBottom, "The deltaHBottom at the bottom edge should be 0");
    }

    @Test
    void testCalculateDeltaHTop() {
        // Middle left cell should have a difference in water height of 1 compared to
        // its top neighbor
        float deltaHTop = simulationFlow.calculateDeltaHTop(0, 1);
        assertEquals(1, deltaHTop, "The deltaHTop should be 1");

        // Top edge cell should have a deltaHTop of 0 since it has no top neighbor
        deltaHTop = simulationFlow.calculateDeltaHTop(1, 0);
        assertEquals(0, deltaHTop, "The deltaHTop at the top edge should be 0");
    }
}
