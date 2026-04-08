package mealmanager;

public class Validators {

    public static void validateEan(String ean) {
        if (ean == null) throw new IllegalArgumentException("EAN cannot be null");
        if (!ean.matches("[0-9]{8}|[0-9]{13}")) throw new IllegalArgumentException("EAN must only consist of digits (8 or 13)");
    }

    public static void validateDoublePositive(double value, String fieldName) {
    if (value <= 0) throw new IllegalArgumentException(fieldName + " cannot be negative or zero");
    }

    public static void validateNotNull(Object object, String fieldName) {
        if (object == null) throw new IllegalArgumentException(fieldName + " cannot be null");
    }

    public static void validateString(String string, String fieldName) {
        if (string == null || string.isBlank()) throw new IllegalArgumentException(fieldName + " cannot be null or blank");
    }
}
