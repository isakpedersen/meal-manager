package mealmanager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum MeasuringUnit {
    GRAM("g"), KG("kg"), ML("ml"), DL("dl"), LITER("l"), STK("stk");

    private final String abbreviation;
    private Map<MeasuringUnit, Double> conversionMap = new LinkedHashMap<>();

    MeasuringUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    static {
        GRAM.conversionMap.put(KG, 0.001);
        KG.conversionMap.put(GRAM, 1000.0);
        ML.conversionMap.putAll(Map.of(DL, 0.01, LITER, 0.001));
        DL.conversionMap.putAll(Map.of(ML, 100.0, LITER, 0.1));
        LITER.conversionMap.putAll(Map.of(ML, 1000.0, DL, 100.0));

        // adds all units to its own conversionMap with a factor 1.
        for (MeasuringUnit unit : MeasuringUnit.values()) {
            unit.conversionMap.put(unit, 1.0);
        }
    }

    public double getConversionFactor(MeasuringUnit toUnit) {
        if (this == toUnit) return 1.0;
        if (!this.conversionMap.containsKey(toUnit)) throw new IllegalStateException("Cannot convert to unit:" + toUnit);
        return this.conversionMap.get(toUnit);
    }

    public List<MeasuringUnit> getConvertibleUnits() {
        List<MeasuringUnit> units = new ArrayList<>();
        for (MeasuringUnit unit : MeasuringUnit.values()) {
            if (this.conversionMap.containsKey(unit)) {
                units.add(unit);
            }
        }
        return units;
    } 

    @Override
    public String toString() {
        return abbreviation;
    }
}
