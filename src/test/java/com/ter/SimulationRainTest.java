package com.ter;

import com.ter.proto.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationRainTest {
    private Terrain terrain;
    private Simulation simulation;
    private float rainRate;
    private float globalRateParameter;
    private float deltaTime;

    @BeforeEach
    void setUp() {
        // Initialize your terrain with a fixed size, for example 10x10
        int width = 10;
        int height = 10;
        terrain = new Terrain(width, height, 12345, 0.05); // Assuming Terrain has a constructor like this

        // Initialize other variables
        rainRate = 0.1f; // Example rain rate
        globalRateParameter = 0.1f; // Example global rate parameter
        deltaTime = 1.0f; // Example delta time

        // Create an instance of SimulationRain
        simulation = new SimulationRain(terrain, rainRate, globalRateParameter);
    }

    @Test
    void testSimulateRainIncreasesWaterLevel() {
        // Run the simulation
        simulation.simulate(deltaTime);

        // Check that water level has increased by the expected amount
        for (int x = 0; x < terrain.getWidth(); x++) {
            for (int y = 0; y < terrain.getHeight(); y++) {
                Cell cell = terrain.getCell(x, y);

                System.out.println(cell.getWater());
                assertEquals(rainRate * globalRateParameter * deltaTime, cell.getWater(),
                        "The water level should be increased by the rain rate after simulation.");
            }
        }
    }
}
