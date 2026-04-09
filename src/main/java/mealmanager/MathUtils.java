package mealmanager;

import java.lang.Math;

public class MathUtils {
    public static double round(double value) {
        return round(value, 2);
    }

    public static double round(double value, int digits) {
        double factor = Math.pow(10.0, digits);
        return Math.round(value * factor) / factor;
    }
}
