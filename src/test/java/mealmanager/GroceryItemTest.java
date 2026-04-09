package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GroceryItemTest {

    @Test
    public void testConstructor() {
        GroceryItem item = new GroceryItem("0000000000001", "Unnamed", 330, MeasuringUnit.DL, 11);
        assertEquals(330.0, item.getPackageContent());
        assertEquals(MeasuringUnit.DL, item.getMeasuringUnit());
    }
    
    @Test
    public void testPrice() {
        GroceryItem item = new GroceryItem("0000000000001", "Unnamed", 500, MeasuringUnit.GRAM, 10);
        assertEquals(10, item.getPrice());
    }

    @Test
    public void testUnitPrice() {
        GroceryItem item = new GroceryItem("0000000000001", "Unnamed", 500, MeasuringUnit.GRAM, 10);
        assertEquals(0.02, item.getUnitPrice(true));
    }
    
    @Test public void testUnitPriceRounding() {
        GroceryItem item = new GroceryItem("0000000000001", "Unnamed", 3.0, MeasuringUnit.KG, 10);
        assertEquals(3.33, item.getUnitPrice(true));
    }
}