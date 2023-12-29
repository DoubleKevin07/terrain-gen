package test.java.com.thirdparty;

import com.thirdparty.OpenSimplex2S;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpenSimplex2STest {

    @Test
    public void testNoise2BasicFunctionality() {
        long seed = 12345;
        double x = 10.0, y = 20.0;
        assertDoesNotThrow(() -> OpenSimplex2S.noise2(seed, x, y));
    }

    @Test
    public void testNoise2Determinism() {
        long seed = 12345;
        double x = 10.0, y = 20.0;
        float result1 = OpenSimplex2S.noise2(seed, x, y);
        float result2 = OpenSimplex2S.noise2(seed, x, y);
        assertEquals(result1, result2, "Noise function should be deterministic");
    }
}
