package mealmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class MealManager {
    private final FileHandler fileHandler = new FileHandler();
    private final ImageService imageService = new ImageService();

    // temporary recipes
    //private Recipe testRecipe = new Recipe("Carb recipe", new ArrayList<>(List.of(new Ingredient(availableGroceryItems.get(0), 200.), new Ingredient(rice,100.))), 1);
    //private Recipe testRecipe2 = new Recipe("Veggie recipe", new ArrayList<>(List.of(new Ingredient(broccoli, 200.), new Ingredient(zucchini,100.))), 1);
    
    private List<GroceryItem> availableGroceryItems;
    //private List<Recipe> recipes = new ArrayList<>(List.of(testRecipe, testRecipe2));
    private List<Recipe> recipes;
    private Recipe currentRecipe;

    public MealManager() {
        try {
            availableGroceryItems = new ArrayList<>(fileHandler.loadGroceryItems());
            recipes = new ArrayList<>(fileHandler.loadRecipes(availableGroceryItems));
        } catch (IOException e) {
            return;
        }
        setCurrentRecipe(recipes.get(0));
    }

    public void saveRecipes() {
        try {
            fileHandler.saveRecipes(recipes);
        } catch (IOException e) {
            return;
        }
    }

    public List<Ingredient> getCurrentIngredients() {
        return getCurrentRecipe().getIngredients();
    }
    
    public void addIngredientToCurrentRecipe(Ingredient ingredient) {
        getCurrentRecipe().addIngredient(ingredient);
    }

    public void removeIngredientFromCurrentRecipe(Ingredient ingredient) {
        getCurrentRecipe().removeIngredient(ingredient);
    }

    public void createNewRecipe(String name) {
        Recipe newRecipe = new Recipe(name, new ArrayList<>(), 1);
        recipes.add(newRecipe);
        setCurrentRecipe(newRecipe);
    }

    public void deleteCurrentRecipe() {
        recipes.remove(getCurrentRecipe());
        setCurrentRecipe(getRecipe(0));
        saveRecipes();
    }

    public Double getCurrentRecipePrice() {
        double price = 0;
        for (Ingredient ingredient : getCurrentIngredients()) {
            price += ingredient.getPrice();
        }
        return PriceUtils.round(price);
    }

    public List<GroceryItem> getAvailableGroceryItems() {
        return availableGroceryItems;
    }

    public Image getImage(String ean) {
        return imageService.getImage(ean);
    }

    public Recipe getRecipe(int index) {
        if (index < 0 || index >= recipes.size()) throw new IndexOutOfBoundsException("Invalid index");
        return recipes.get(index);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe recipe) {
        this.currentRecipe = recipe;
    }
}
