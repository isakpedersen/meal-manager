package mealmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingList {
    private Map<GroceryItem, Double> requiredAmounts = new HashMap<>();

    public ShoppingList() {}

    public ShoppingList(List<Ingredient> ingredients) {
        Validators.validateNotNull(ingredients, "ingredients");

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
        }
    }

    public void addIngredient(Ingredient ingredient) {
        Validators.validateNotNull(ingredient, "ingredient");
        requiredAmounts.merge(ingredient.getGroceryItem(), ingredient.getAmount(), (a, b) -> MathUtils.round(a + b, 3));
    }

    public void removeIngredient(Ingredient ingredient) {
        Validators.validateNotNull(ingredient, "ingredient");
        GroceryItem item = ingredient.getGroceryItem();
        if (!requiredAmounts.containsKey(item)) {
            throw new IllegalArgumentException("Item not in shopping list: " + item);
        }
        if (ingredient.getAmount() > requiredAmounts.get(item)) {
            throw new IllegalStateException("Cannot remove " + ingredient.getAmount() + " of "
                + item.getName() + ", only " + requiredAmounts.get(item) + " is available");
        }

        requiredAmounts.put(item, MathUtils.round(requiredAmounts.get(item) - ingredient.getAmount(), 3));
        if (requiredAmounts.get(item) == 0) {
            requiredAmounts.remove(item);
        }
    }

    private double getRequiredAmount(GroceryItem item) {
        Validators.validateNotNull(item, "item");
        if (!requiredAmounts.containsKey(item)) throw new IllegalArgumentException("Item not in shopping list: " + item);
        return requiredAmounts.get(item);
    }

    public int getRequiredPackages(GroceryItem item) {
        Double requiredAmount = getRequiredAmount(item);
        double packageContent = item.getPackageContent();
        int requiredPackages = 1;
        while (requiredAmount / (requiredPackages * packageContent) > 1) {
            requiredPackages++;
        }
        return requiredPackages;
    }

    public double getActualPackages(GroceryItem item) {
        return MathUtils.round(getRequiredAmount(item) / item.getPackageContent(), 3);
    }

    public String getActualAmountString(GroceryItem item) {
        return getRequiredAmount(item) + " " + item.getMeasuringUnit();
    }

    public Map<GroceryItem, Integer> getShoppingList() {
        Map<GroceryItem, Integer> shoppingList = new HashMap<>();
        for (GroceryItem item : requiredAmounts.keySet()) {
            int requiredPackages = getRequiredPackages(item);
            shoppingList.put(item, requiredPackages);
       }
       return shoppingList;
    }

    public double getPrice() {
        double price = 0;
        for (GroceryItem item : getShoppingList().keySet()) {
            price += getShoppingList().get(item) * item.getPrice();
        }
        return MathUtils.round(price);
    }
}
