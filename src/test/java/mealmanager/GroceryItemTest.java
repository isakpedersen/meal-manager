package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GroceryItemTest {

    @Test
    public void testPrice() {
        GroceryItem item = new GroceryItem(1, "Pasta", 500, MeasuringUnit.GRAM, 10);
        assertEquals(10, item.getPrice());
    }

    @Test
    public void testUnitPrice() {
        GroceryItem item = new GroceryItem(1, "Pasta", 500, MeasuringUnit.GRAM, 10);
        assertEquals(0.02, item.getUnitPrice());
    }
}