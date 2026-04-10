package mealmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Recipe implements Iterable<Ingredient> {
    private String name;
    private List<Ingredient> ingredients;
    private int servings;

    public Recipe(String name, List<Ingredient> ingredients, int servings) {
        Validators.validateString(name, "name");
        Validators.validateIntPositive(servings, "servings");
        if (ingredients != null && ingredients.contains(null)) throw new IllegalArgumentException("ingredients cannot contain null elements");

        this.name = name;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        this.servings = servings;
    }

    public void addIngredient(Ingredient ingredient) {
        Validators.validateNotNull(ingredient, "ingredient");
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validators.validateString(name, "name");
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        Validators.validateIntPositive(servings, "servings");
        this.servings = servings;
    }

    public double getPrice() {
        double price = 0;
        for (Ingredient ingredient : getIngredients()) {
            price += ingredient.getPrice();
        }
        return MathUtils.round(price);
    }

    public double getPricePerServing() {
        return MathUtils.round(getPrice() / getServings());
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return ingredients.iterator();
    }

    @Override
    public String toString() {
        return getName();
    }

}

