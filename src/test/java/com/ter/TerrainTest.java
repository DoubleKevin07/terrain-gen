package com.ter;

import com.ter.proto.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TerrainTest {

    @Test
    public void testTerrainSize() {
        int width = 50, height = 60;
        Terrain terrain = new Terrain(width, height, 12345, 0.005);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                assertNotNull(terrain.getCell(x, y), "Each cell should be initialized");
            }
        }
    }

    @Test
    public void testHeightValuesInRange() {
        Terrain terrain = new Terrain(100, 100, 12345, 0.005);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                Cell cell = terrain.getCell(x, y);
                assertTrue(cell.getHeight() >= 0.0f && cell.getHeight() <= 1.0f,
                        "Height value should be between 0 and 1");
            }
        }
    }

    @Test
    public void testDefaultWaterAndSedimentValues() {
        Terrain terrain = new Terrain(10, 10, 12345, 0.005);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = terrain.getCell(x, y);
                assertEquals(0.0f, cell.getWater(), "Default water value should be 0");
                assertEquals(0.0f, cell.getSediment(), "Default sediment value should be 0");
            }
        }
    }
}
