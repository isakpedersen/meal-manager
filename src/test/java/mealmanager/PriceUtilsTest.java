package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PriceUtilsTest {
    @Test
    public void testRounding() {
        double val1 = 3.33333;
        double val2 = 11.115;
        double val3 = 1.1149;
        double val4 = 1.5;

        assertEquals(3.33, PriceUtils.round(val1));
        assertEquals(11.12, PriceUtils.round(val2));
        assertEquals(1.11, PriceUtils.round(val3));
        assertEquals(1.50, PriceUtils.round(val4));
    }
}
