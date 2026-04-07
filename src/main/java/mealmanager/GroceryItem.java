package mealmanager;

public class GroceryItem {
    private final String ean;
    private final String name;
    private final Quantity quantity;
    
    private double price;

    public GroceryItem(String ean, String name, double packageContent, MeasuringUnit measuringUnit, double price) {
        this.ean = ean;
        this.name = name;
        this.quantity = new Quantity(packageContent, measuringUnit);
        this.price = price;
    }

    public double getUnitPrice() {
        return PriceUtils.round(getPrice() / getPackageContent());
    }

    public String getEan() {
        return ean;
    }

    public String getName() {
        return name;
    }

    public Quantity getQuantity() {
        return quantity;
    }
    
    public double getPackageContent() {
        return quantity.getAmount();
    }
    
    public MeasuringUnit getMeasuringUnit() {
        return quantity.getUnit();
    }
    
    public double getPrice() {
        return PriceUtils.round(price);
    }

    @Override
    public String toString() {
        return getName();
    }
}
