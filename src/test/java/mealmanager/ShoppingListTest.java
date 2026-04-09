package mealmanager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingListTest {

    GroceryItem broccoli;
    GroceryItem pasta;
    ShoppingList shoppingList;

    @BeforeEach
    public void setUp() {
        broccoli = new GroceryItem("2000434900004", "Brokkoli", 1, MeasuringUnit.STK, 14.90);
        pasta = new GroceryItem("7035620018930", "Pasta", 500, MeasuringUnit.GRAM, 22.90);
        shoppingList = new ShoppingList();
    }

    @Test
    public void testEmptyConstructor() {
        ShoppingList emptyList = new ShoppingList();
        assertEquals(Map.of(), emptyList.getShoppingList());
    }
    
    @Test
    public void testNotEmptyConstructor() {
        ShoppingList notEmptyList = new ShoppingList(new ArrayList<>(List.of(new Ingredient(broccoli, 1), new Ingredient(broccoli, 0.5), new Ingredient(pasta, 400))));
        assertTrue(notEmptyList.getShoppingList().get(broccoli) == 2);
        assertTrue(notEmptyList.getShoppingList().get(pasta) == 1);
    }

    @Test
    public void testConstructorNullList() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShoppingList(null);
        });
    }

    @Test
    public void testConstructorNullElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            List<Ingredient> ingredients = new ArrayList<>();
            ingredients.add(new Ingredient(broccoli, 1.5));
            ingredients.add(null);
            new ShoppingList(ingredients);
        });
    }

    @Test
    public void testAddIngredientNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.addIngredient(null);
        });
    }

    @Test
    public void testRemoveIngredientNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.removeIngredient(null);
        });
    }

    @Test
    public void testRemoveNonExistentIngredient() {
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.removeIngredient(new Ingredient(broccoli, 1));
        });
    }

    @Test
    public void testRemoveTooMuchOfIngredient() {
        assertThrows(IllegalStateException.class, () -> {
            shoppingList.addIngredient(new Ingredient(broccoli, 1));
            shoppingList.removeIngredient(new Ingredient(broccoli, 1.1));
        });
        assertDoesNotThrow(() -> {
            shoppingList.addIngredient(new Ingredient(broccoli, 1));
            shoppingList.removeIngredient(new Ingredient(broccoli, 1));
        });
    }

    @Test
    public void testRemovePartOfIngredient() {
        shoppingList.addIngredient(new Ingredient(broccoli, 1.5));
        assertEquals(2, shoppingList.getRequiredPackages(broccoli));
        shoppingList.removeIngredient(new Ingredient(broccoli, 0.5));
        assertEquals(1, shoppingList.getRequiredPackages(broccoli));
    }

    @Test
    public void testRemoveIngredient() {
        shoppingList.addIngredient(new Ingredient(broccoli, 1));
        assertEquals(1, shoppingList.getRequiredPackages(broccoli));
        shoppingList.removeIngredient(new Ingredient(broccoli, 1));
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.getRequiredPackages(broccoli);
        });
    }

    @Test
    public void testGetRequiredPackagesNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.getRequiredPackages(null);
        });
    }

    @Test
    public void testGetRequiredPackagesItemNotInList() {
        GroceryItem randomItem = new GroceryItem("1234567890123", "Random item", 99, MeasuringUnit.GRAM, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingList.getRequiredPackages(randomItem);
        });
    }

    @Test
    public void testGetRequiredPackages() {
        shoppingList.addIngredient(new Ingredient(broccoli, 0.5));
        assertEquals(1, shoppingList.getRequiredPackages(broccoli));
        shoppingList.addIngredient(new Ingredient(broccoli, 0.5));
        assertEquals(1, shoppingList.getRequiredPackages(broccoli));
        shoppingList.addIngredient(new Ingredient(broccoli, 0.5));
        assertEquals(2, shoppingList.getRequiredPackages(broccoli));
    }

    @Test
    public void testGetShoppingList() {
        shoppingList.addIngredient(new Ingredient(pasta, 499));
        assertEquals(1, shoppingList.getShoppingList().get(pasta));
        shoppingList.addIngredient(new Ingredient(pasta, 1));
        assertEquals(1, shoppingList.getShoppingList().get(pasta));
        shoppingList.addIngredient(new Ingredient(pasta, 1));
        assertEquals(2, shoppingList.getShoppingList().get(pasta));
    }
}
