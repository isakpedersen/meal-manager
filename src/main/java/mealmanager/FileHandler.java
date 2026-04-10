package mealmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String GROCERY_FILE = "data/groceryitems.csv";
    private static final String RECIPE_FILE = "data/recipes.txt";

    public List<GroceryItem> loadGroceryItems() throws IOException {
        List<GroceryItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GROCERY_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String ean = parts[0];
                String name = parts[1];
                double packageContent = Double.parseDouble(parts[2]);
                MeasuringUnit measuringUnit = MeasuringUnit.valueOf(parts[3]);
                double price = Double.parseDouble(parts[4]);
                items.add(new GroceryItem(ean, name, packageContent, measuringUnit, price));
            }
        }
        if (items.isEmpty()) {
            items.add(new GroceryItem("0000", "Empty grocery item", 1, MeasuringUnit.GRAM, 1));
        }
        return items;
    }

    public List<Recipe> loadRecipes(List<GroceryItem> availableGroceryItems) throws IOException {
        if (availableGroceryItems == null) throw new IllegalArgumentException("availableGroceryItems cannot be null");
        if (availableGroceryItems.contains(null)) throw new IllegalArgumentException("availableGroceryItems cannot contain null elements");
        List<Recipe> recipes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RECIPE_FILE))) {
            String line;
            Recipe currentRecipe = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (line.startsWith("RECIPE")) {
                    currentRecipe = new Recipe(parts[1], new ArrayList<>(), Integer.parseInt(parts[2]));
                } else if (line.startsWith("INGREDIENT") && currentRecipe != null) {
                    String ean = parts[1];
                    GroceryItem item = null;
                    for (GroceryItem groceryItem : availableGroceryItems) {
                        if (ean.equals(groceryItem.getEan())) {
                            item = groceryItem;
                            break;
                        }
                    }
                    if (item == null) throw new IllegalStateException("invalid ean:" + ean);
                    currentRecipe.addIngredient(new Ingredient(item, Double.parseDouble(parts[2])));
                } else if (line.startsWith("END_RECIPE")) {
                    recipes.add(currentRecipe);
                    currentRecipe = null;
                }
            }
        }
        if (recipes.isEmpty()) {
            recipes.add(new Recipe("Tom oppskrift", new ArrayList<>(), 4));
        }
        return recipes;
    }

    public void saveRecipes(List<Recipe> recipes) throws IOException {
        if (recipes == null) throw new IllegalArgumentException("recipes cannot be null");
        if (recipes.contains(null)) throw new IllegalArgumentException("recipes cannot contain null elements");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECIPE_FILE))) {
            for (Recipe recipe : recipes) {
                writer.write("RECIPE:" + recipe.getName() + ":" + recipe.getServings() + "\n");
                for (Ingredient ingredient : recipe) {
                    writer.write("INGREDIENT:" + ingredient.getGroceryItem().getEan() + ":" + ingredient.getAmount() + "\n");
                }
                writer.write("END_RECIPE\n");
            }
        }
    }
}
