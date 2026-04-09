package mealmanager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MealManagerTest {

    private MealManager mealManager;
    private GroceryItem pasta;
    private GroceryItem rice;
    private Ingredient pastaIngredient;
    private Ingredient riceIngredient;

    @BeforeEach
    public void setUp() {
        mealManager = new MealManager();
        pasta = new GroceryItem("0000000000001", "Pasta", 500,MeasuringUnit.GRAM, 10);
        rice = new GroceryItem("0000000000002", "Rice", 1000, MeasuringUnit.GRAM, 15);
        pastaIngredient = new Ingredient(pasta, 100);
        riceIngredient = new Ingredient(rice, 100);
    }

    @Test
    public void testModifyIngredientsInCurrentRecipe() {
        mealManager.createNewRecipe("Recipe");
        mealManager.addIngredientToCurrentRecipe(pastaIngredient);
        mealManager.addIngredientToCurrentRecipe(riceIngredient);
        assertEquals(List.of(pastaIngredient, riceIngredient), mealManager.getCurrentIngredients());
        
        mealManager.removeIngredientFromCurrentRecipe(pastaIngredient);
        assertEquals(List.of(riceIngredient), mealManager.getCurrentIngredients());
    }

    @Test
    public void testCreateNewRecipe() {
        int size = mealManager.getRecipes().size();
        mealManager.createNewRecipe("Recipe");
        assertEquals("Recipe", mealManager.getRecipe(size).toString());
        assertEquals("Recipe", mealManager.getCurrentRecipe().toString());
    }

    @Test
    public void testDeleteCurrentRecipe() {
        List<Recipe> recipesBefore = new ArrayList<>(mealManager.getRecipes());
        mealManager.createNewRecipe("Recipe 2");
        mealManager.deleteCurrentRecipe();
        assertEquals(recipesBefore, mealManager.getRecipes());
    }

    @Test
    public void testGetCurrentRecipePrice() {
        mealManager.createNewRecipe("Recipe");
        mealManager.addIngredientToCurrentRecipe(pastaIngredient);
        mealManager.addIngredientToCurrentRecipe(riceIngredient);
        assertEquals(3.5, mealManager.getCurrentRecipePrice());
    }

    @Test
    public void testGetImageValidation() {}

    // Requires recipes.txt to contain at least 1 recipe
    @Test
    public void testGetRecipeValidation() {
        int size = mealManager.getRecipes().size();
        assertThrows(IndexOutOfBoundsException.class, () -> mealManager.getRecipe(-1));
        assertDoesNotThrow(() -> mealManager.getRecipe(0));
        assertDoesNotThrow(() -> mealManager.getRecipe(size-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mealManager.getRecipe(size));
    }

    @Test
    public void testGetCurrentRecipe() {
        mealManager.createNewRecipe("Recipe");
        assertEquals("Recipe", mealManager.getCurrentRecipe().toString());
    }

    @Test
    public void testSetCurrentRecipe() {
        int size = mealManager.getRecipes().size();
        mealManager.createNewRecipe("Recipe");
        Recipe recipe = mealManager.getRecipe(size);
        mealManager.createNewRecipe("Recipe 2");
        Recipe recipe2 = mealManager.getRecipe(size + 1);
        assertEquals(recipe2, mealManager.getCurrentRecipe());
        mealManager.setCurrentRecipe(recipe);
        assertEquals(recipe, mealManager.getCurrentRecipe());
    }
}
