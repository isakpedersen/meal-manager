package mealmanager;

import java.lang.Math;

public class MathUtils {
    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
