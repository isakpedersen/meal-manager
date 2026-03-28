package mealmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static mealmanager.MeasuringUnit.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MeasuringUnitTest {
    @Test
    public void testGetConvertibleUnits() {
        assertEquals(new ArrayList<>(List.of(GRAM, KG)), GRAM.getConvertibleUnits());
        assertEquals(new ArrayList<>(List.of(GRAM, KG)), KG.getConvertibleUnits());

        assertEquals(new ArrayList<>(List.of(ML, DL, LITER)), DL.getConvertibleUnits());
        assertEquals(new ArrayList<>(List.of(ML, DL, LITER)), ML.getConvertibleUnits());
        assertEquals(new ArrayList<>(List.of(ML, DL, LITER)), LITER.getConvertibleUnits());

        assertEquals(new ArrayList<>(List.of(STK)), STK.getConvertibleUnits());

    }

    @Test
    public void testGetConversionFactor() {
        assertEquals(0.001, GRAM.getConversionFactor(KG));
        assertEquals(1000.0, KG.getConversionFactor(GRAM));

        assertEquals(0.001, ML.getConversionFactor(LITER));
        assertEquals(1000.0, LITER.getConversionFactor(ML));
        assertEquals(100.0, DL.getConversionFactor(ML));
    }

    @Test
    public void testToString() {
        assertEquals("g", MeasuringUnit.GRAM.toString());
        assertEquals("l", MeasuringUnit.LITER.toString());
    }
}
