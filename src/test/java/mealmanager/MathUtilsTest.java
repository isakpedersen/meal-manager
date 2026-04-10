package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MathUtilsTest {
    @Test
    public void testRounding() {
        double val1 = 3.33333;
        double val2 = 11.115;
        double val3 = 11.1149;
        double val4 = 1.5;

        assertEquals(3.33, MathUtils.round(val1));
        assertEquals(11.12, MathUtils.round(val2));
        assertEquals(11.11, MathUtils.round(val3));
        assertEquals(1.50, MathUtils.round(val4));
    }
}
