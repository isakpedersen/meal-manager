package mealmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;

public class MealManager {
    private final FileHandler fileHandler = new FileHandler();
    private final ImageService imageService = new ImageService();
    private ShoppingList shoppingList = new ShoppingList();

    private List<GroceryItem> availableGroceryItems;
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

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                shoppingList.addIngredient(ingredient);
            }
        }
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
        shoppingList.addIngredient(ingredient);
    }

    public void removeIngredientFromCurrentRecipe(Ingredient ingredient) {
        getCurrentRecipe().removeIngredient(ingredient);
        shoppingList.removeIngredient(ingredient);
    }

    public void createNewRecipe(String name) {
        Recipe newRecipe = new Recipe(name, new ArrayList<>(), 1);
        recipes.add(newRecipe);
        setCurrentRecipe(newRecipe);
    }

    public void deleteCurrentRecipe() {
        for (Ingredient ingredient : getCurrentRecipe().getIngredients()) {
            removeIngredientFromCurrentRecipe(ingredient);
        }
        recipes.remove(getCurrentRecipe());
        setCurrentRecipe(getRecipe(0));
        saveRecipes();
    }

    public Double getCurrentRecipePrice() {
        double price = 0;
        for (Ingredient ingredient : getCurrentIngredients()) {
            price += ingredient.getPrice();
        }
        return MathUtils.round(price);
    }

    public List<GroceryItem> getAvailableGroceryItems() {
        return availableGroceryItems;
    }

    public Image getImage(String ean) {
        return imageService.getImage(ean);
    }

    public Recipe getRecipe(int index) {
        if (index < 0 || index >= recipes.size()) throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        return recipes.get(index);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe recipe) {
        Validators.validateNotNull(recipe, "recipe");
        this.currentRecipe = recipe;
    }

    public Map<GroceryItem, Integer> getShoppingList() {
        return shoppingList.getShoppingList();
    }

    public double getActualPackages(GroceryItem item) {
        return shoppingList.getActualPackages(item);
    }

    public String getActualAmountString(GroceryItem item) {
        return shoppingList.getActualAmountString(item);
    }

    public String getShoppingListPriceString() {
        return String.format("%.2f", shoppingList.getPrice()) + " kr";
    }

    public String getShoppingItemPriceString(GroceryItem item) {
        double price = getShoppingList().get(item) * item.getPrice();
        return String.format("%.2f", MathUtils.round(price)) + " kr";
    }
}
