package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecipeTest {

    private Ingredient pasta;
    private Ingredient rice;

    @BeforeEach
    public void setUp() {
        GroceryItem pasta = new GroceryItem("0000000000001", "Pasta", 500,MeasuringUnit.GRAM, 10);
        GroceryItem rice = new GroceryItem("0000000000002", "Rice", 1000, MeasuringUnit.GRAM, 15);
        this.pasta = new Ingredient(pasta, 100);
        this.rice = new Ingredient(rice, 100);
    }

    @Test
    public void testGetIngredients() {
        Recipe recipe = new Recipe("Carb recipe", new ArrayList<>(List.of(pasta, rice)), 1);
        assertEquals(List.of(pasta, rice), recipe.getIngredients());
    }

    @Test
    public void testAddIngredient() {
        Recipe recipe = new Recipe("Recipe", new ArrayList<>(List.of(pasta)), 1);
        assertEquals(List.of(pasta), recipe.getIngredients());
        recipe.addIngredient(rice);
        assertEquals(List.of(pasta, rice), recipe.getIngredients());
    }

    @Test
    public void testRemoveIngredient() {
        Recipe recipe = new Recipe("Recipe", new ArrayList<>(List.of(pasta)), 1);
        assertEquals(List.of(pasta), recipe.getIngredients());
        recipe.removeIngredient(pasta);;
        assertEquals(List.of(), recipe.getIngredients());
    }

    @Test
    public void testToString() {
        Recipe recipe = new Recipe("Recipe 2", null, 1);
        assertEquals("Recipe 2", recipe.toString());
    }
}
