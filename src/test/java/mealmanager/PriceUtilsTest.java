package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PriceUtilsTest {
    @Test
    public void testRounding() {
        double val1 = 3.33333;
        double val2 = 1.1150;
        double val3 = 1.1149;

        assertEquals(3.33, PriceUtils.round(val1));
        assertEquals(1.12, PriceUtils.round(val2));
        assertEquals(1.11, PriceUtils.round(val3));
    }
}
