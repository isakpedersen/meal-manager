package mealmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RecipeTest {
    @Test
    public void testContainsGroceryItem() {
        GroceryItem pasta = new GroceryItem(1, "Pasta", 500,MeasuringUnit.GRAM, 10);
        GroceryItem rice = new GroceryItem(2, "rice", 1000, MeasuringUnit.GRAM, 15);
        Ingredient ingredient1 = new Ingredient(pasta, 100);
        Ingredient ingredient2 = new Ingredient(rice, 100);

        Recipe recipe = new Recipe("Carb recipe", new ArrayList<>(List.of(ingredient1, ingredient2)), 1);
        //assertTrue(recipe.containsGroceryItem(pasta));
    }
}
