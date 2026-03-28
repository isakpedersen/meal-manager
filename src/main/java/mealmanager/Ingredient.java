package mealmanager;

public class Ingredient {
    private GroceryItem item;
    private Quantity quantity;

    public Ingredient(GroceryItem item, double amount) {
        this.item = item;
        this.quantity = new Quantity(amount, item.getMeasuringUnit());
    }

    public double getPrice() {
        return PriceUtils.round(item.getUnitPrice() * getAmount());
    }

    public GroceryItem getGroceryItem() {
        return item;
    }

    public double getAmount() {
        return quantity.getAmount();
    }

    @Override
    public String toString() {
        return getAmount() + " " + item.getMeasuringUnit().toString() + " " + getGroceryItem().getName() + " — " + String.format("%.2f", getPrice()) + " kr";
    }
}
