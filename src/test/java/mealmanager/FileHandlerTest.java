package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FileHandlerTest {
    @Test
    public void testSaveAndLoadRecipes() throws IOException {
        FileHandler fileHandler = new FileHandler();
        List<GroceryItem> availableGroceryItems = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>();
        
        availableGroceryItems = fileHandler.loadGroceryItems();
        recipes = fileHandler.loadRecipes(availableGroceryItems);

        Recipe newRecipe = new Recipe("New recipe", new ArrayList<>(List.of(new Ingredient(availableGroceryItems.get(0), 1))), 1);

        recipes.add(newRecipe);
        fileHandler.saveRecipes(recipes);

        recipes = fileHandler.loadRecipes(availableGroceryItems);
        assertEquals(newRecipe.getName(), recipes.getLast().getName());


        recipes.remove(recipes.getLast());

        fileHandler.saveRecipes(recipes);
    }
}
