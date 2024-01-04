package com.ter;

import com.ter.proto.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class SimulationFlowTest {
    private Terrain terrain;
    private SimulationFlow simulationFlow;

    @BeforeEach
    void setUp() {
        // Create a mock terrain with known values for height and water
        // For simplicity, let's assume terrain is 3x3
        terrain = new Terrain(3, 3, 1, 1);
        simulationFlow = new SimulationFlow(terrain, 0.1f, 0.1f); // Assuming a global rate parameter Kr

        // Initialize the cells with known values
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                // Let's assume all cells have the same terrain height but vary in water height
                terrain.setCell(x, y, Cell.newBuilder().setHeight(10).setWater(Math.max(x, y)).addAllFlux(
                        Arrays.asList(
                                /* left= */0.0f,
                                /* right= */ 0.0f,
                                /* top= */ 0.0f,
                                /* bottom= */ 0.0f))
                        .build());
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

    @Test
    void testCalculateOutflowFluxMiddleLeft() {
        // Test outflow flux from middle left cell.
        List<Float> expectedOutput = Arrays.asList(0.0f, 0.0f, 9.81f, 0.0f); // Flow to lower cell, which is above.

        List<Float> result = simulationFlow.calculateOutflowFlux(0, 1, 1.0f);

        assertEquals(result, expectedOutput);
    }

    @Test
    void testCalculateOutflowFluxMiddleRight() {
        // Test outflow flux from middle right cell.
        List<Float> expectedOutput = Arrays.asList(9.81f, 0.0f, 0.0f, 0.0f); // Flow to left cell.

        List<Float> result = simulationFlow.calculateOutflowFlux(2, 1, 1.0f);

        assertEquals(result, expectedOutput);
    }

    @Test
    void testCalculateOutflowFluxMiddleBottom() {
        // Test outflow flux from middle bottom cell.
        List<Float> expectedOutput = Arrays.asList(0.0f, 0.0f, 9.81f, 0.0f);

        List<Float> result = simulationFlow.calculateOutflowFlux(1, 2, 1.0f);

        assertEquals(result, expectedOutput);
    }

    @Test
    void testCalculateOutflowFluxMiddleTop() {
        // Test outflow flux from middle top cell.
        List<Float> expectedOutput = Arrays.asList(9.81f, 0.0f, 0.0f, 0.0f);

        List<Float> result = simulationFlow.calculateOutflowFlux(1, 0, 1.0f);

        assertEquals(result, expectedOutput);
    }

    @Test
    void testCalculateOutflowFluxMiddle() {
        // Test outflow flux from middle cell.
        // This flux should be 0.
        //
        // Assumptions:
        // - We only have outward flux when water is moving OUT OF the cell.
        //
        // - Since we are in the middle of the terrain, we have water flowing into the
        // cell from the top and right. The cell to the left and above is at the same
        // level, so there is no outward flux.
        List<Float> expectedOutput = Arrays.asList(0.0f, 0.0f, 0.0f, 0.0f);

        List<Float> result = simulationFlow.calculateOutflowFlux(1, 1, 1.0f);

        assertEquals(result, expectedOutput);
    }

    @Test
    void testCalculateLocalTiltAngleMiddle() {
        // TODO: Make this test more helpful.
        double expectedAngle = 35.2643897;

        double result = simulationFlow.calculateLocalTiltAngle(1, 1);
        double diff = Math.abs(expectedAngle - result);

        assertTrue(diff < 0.1, "Difference should be within 0.01.");
    }

    @Test
    void testCalculateVelocityVectorIsZeroIfFluxAllZero() {
        // By default, all cells have 0 flux.
        List<Float> result = simulationFlow.calculateVelocityVector(1, 1);

        assertEquals(result, Arrays.asList(0.0f, 0.0f));
    }

    @Test
    void testCalculateVelocityVectorForNonzeroFlux() {
        // Add flux going to the right and flux going down for the middle cell and neighbor cells.
        terrain.setCell(1, 1, Cell.newBuilder().addAllFlux(
                Arrays.asList(
                        /* left= */ 0.0f,
                        /* right= */ 10.0f,
                        /* top= */ 0.0f,
                        /* bottom= */ 10.0f))
                .build()); // Middle cell
        terrain.setCell(1, 0, Cell.newBuilder().addAllFlux(
                Arrays.asList(
                        /* left= */ 0.0f,
                        /* right= */ 0.0f,
                        /* top= */ 0.0f,
                        /* bottom= */ 10.0f))
                .build()); // Middle cell top neighbor
        terrain.setCell(0, 1, Cell.newBuilder().addAllFlux(
                Arrays.asList(
                        /* left= */ 0.0f,
                        /* right= */ 10.0f,
                        /* top= */ 0.0f,
                        /* bottom= */ 0.0f))
                .build()); // Middle cell left neighbor
        // (By default, other cells have 0 flux.)

        List<Float> result = simulationFlow.calculateVelocityVector(1, 1);

        assertEquals(result, Arrays.asList(10.0f, 10.0f));
    }
}
