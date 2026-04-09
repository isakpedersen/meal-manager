package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IngredientTest {

    @Test
    public void testConstructor() {
        GroceryItem item = new GroceryItem("0000000000001", "Unnamed", 400, MeasuringUnit.GRAM, 34.90);
        Ingredient ingredient = new Ingredient(item, 300);
        assertEquals(300.0, ingredient.getAmount());
        assertEquals(item, ingredient.getGroceryItem());
    }

    @Test
    public void testPrice() {
        GroceryItem item = new GroceryItem("0000000000002", "Pasta", 500, MeasuringUnit.GRAM, 30);
        Ingredient ingredient = new Ingredient(item, 200);
        assertEquals(12.0, ingredient.getPrice());
    }

    @Test
    public void testToString() {
        GroceryItem item = new GroceryItem("0000000000002", "Pasta", 500, MeasuringUnit.GRAM, 30);
        Ingredient ingredient = new Ingredient(item, 200);
        assertEquals("200.0 g Pasta — 12.00 kr", ingredient.toString());
    }
}
