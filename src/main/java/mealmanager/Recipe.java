package mealmanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients;
    private int servings;

    public Recipe(String name, List<Ingredient> ingredients, int servings) {
        this.name = name;
        if (ingredients != null) {
            this.ingredients = new ArrayList<>(ingredients);
        } else {
            this.ingredients = new ArrayList<>();
        }
        this.servings = servings;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getServings() {
        return servings;
    }

    @Override
    public String toString() {
        return getName();
    }

}

