package mealmanager;

public class Quantity {
    private final double amount;
    private final MeasuringUnit unit;

    public Quantity(double amount, MeasuringUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public MeasuringUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return (amount + " " + unit);
    }
}
