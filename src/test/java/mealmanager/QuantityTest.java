package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class QuantityTest {
    @Test
    public void testConstructor() {
        Quantity quantity = new Quantity(100, MeasuringUnit.GRAM);
        assertEquals(100.0, quantity.getAmount());
        assertEquals(MeasuringUnit.GRAM, quantity.getUnit());
    }

    @Test
    public void testToString() {
        Quantity quantity = new Quantity(100, MeasuringUnit.GRAM);
        assertEquals("100.0 g", quantity.toString());
    }
}
