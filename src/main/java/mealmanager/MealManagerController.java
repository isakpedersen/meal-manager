package mealmanager;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class MealManagerController {
    private MealManager mealManager = new MealManager();

    @FXML private ComboBox<Recipe> recipeBox;
    
    @FXML private Label currentRecipeLabel;
    
    @FXML private ListView<Ingredient> ingredientList;
    
    @FXML private Label totalPriceLabel;

    @FXML private TextField ingredientSearchField;
    @FXML private ListView<GroceryItem> ingredientSearchList;

    @FXML private TextField amountField;
    
    @FXML private ComboBox<MeasuringUnit> ingredientUnitBox;

    @FXML private TableView<GroceryItem> groceryItemTable;
    @FXML private TableColumn<GroceryItem, String> eanColumn;
    @FXML private TableColumn<GroceryItem, String> nameColumn;
    @FXML private TableColumn<GroceryItem, Quantity> quantityColumn;
    @FXML private TableColumn<GroceryItem, Double> priceColumn;
    @FXML private TableColumn<GroceryItem, Double> unitPriceColumn;

    @FXML private ImageView productImage;

    @FXML
    public void initialize() {
        updateRecipe();
        updateRecipes();
        updateImage();
    
        ingredientList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                Ingredient selected = ingredientList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    mealManager.removeIngredientFromCurrentRecipe(selected);
                    updateRecipe();
                }
            }
        });

        // Initialize GroceryItemTable
        // TO DO
        eanColumn.setCellValueFactory(row -> 
            new SimpleStringProperty(row.getValue().getEan())
        );
        nameColumn.setCellValueFactory(row ->
            new SimpleStringProperty(row.getValue().getName())
        );
        quantityColumn.setCellValueFactory(row ->
            new SimpleObjectProperty<>(row.getValue().getQuantity())
        );
        priceColumn.setCellValueFactory(row ->
            new SimpleObjectProperty<>(row.getValue().getPrice())
        );
        unitPriceColumn.setCellValueFactory(row ->
            new SimpleObjectProperty<>(row.getValue().getUnitPrice())
        );

        groceryItemTable.getItems().setAll(mealManager.getAvailableGroceryItems());

        // Add image updating listener to rows in table
        groceryItemTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null && newVal != oldVal) updateImage();
            }
        );

        // Configures listener for ingredient search box
        ingredientSearchField.textProperty().addListener((obs, oldVal, query) -> {
            ingredientSearchField.getStyleClass().remove("ingredient-search-confirmed");
            if (query.isBlank()) {
                ingredientSearchList.setVisible(false);
                return;
            };

            // Loads and filters dropdown list
            List<GroceryItem> filteredGroceryItems = mealManager.getAvailableGroceryItems().stream()
            .filter(groceryItem -> {
                return groceryItem.toString().toLowerCase().contains(query.toLowerCase());
            })
            .toList();
            ingredientSearchList.getItems().setAll(filteredGroceryItems);
            ingredientSearchList.setPrefHeight(filteredGroceryItems.size() * 42.0 + 2.0);
            
            if (!ingredientSearchList.getItems().isEmpty()) {
                ingredientSearchList.setVisible(true);
            } else {
                ingredientSearchList.setVisible(false);
            }
            
            // Selection of items in list
            GroceryItem selectedItem = ingredientSearchList.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                selectedItem = ingredientSearchList.getItems().get(0);
                ingredientSearchList.getSelectionModel().select(selectedItem);
            }
        });

        // mouse trigger (clicking item in list)
        ingredientSearchField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                if (ingredientSearchList.getSelectionModel().getSelectedIndex() < ingredientSearchList.getItems().size() - 1) {
                    ingredientSearchList.getSelectionModel().selectNext();
                    ingredientSearchList.scrollTo(ingredientSearchList.getSelectionModel().getSelectedIndex() - 4);
                }
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                if (ingredientSearchList.getSelectionModel().getSelectedIndex() > 0) {
                    ingredientSearchList.getSelectionModel().selectPrevious();
                    ingredientSearchList.scrollTo(ingredientSearchList.getSelectionModel().getSelectedIndex());
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                ingredientSearchField.setText(ingredientSearchList.getSelectionModel().getSelectedItem().toString());
                ingredientSearchField.getStyleClass().add("ingredient-search-confirmed");
                ingredientSearchList.setVisible(false);
                handleIngredientSelection();
                amountField.requestFocus();
            }
        });

        // Runs after UI is initialized:
        Platform.runLater(() -> {
            AnchorPane.setTopAnchor(ingredientSearchList, ingredientSearchField.getHeight());
            ingredientSearchList.setPrefWidth(ingredientSearchField.getWidth());
            ingredientSearchList.setMaxHeight(5 * 42.0 + 2.0);
        });
    }

    @FXML
    private void handleRecipeSelection() {
        mealManager.setCurrentRecipe(recipeBox.getValue());
        updateRecipe();
    }

    @FXML
    private void createNewRecipe() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ny oppskrift");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Navn på oppskrift:");
        dialog.showAndWait().ifPresent(name -> {
            mealManager.createNewRecipe(name);
        });

        updateRecipe();
        updateRecipes();
    }

    @FXML
    private void deleteCurrentRecipe() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Slett oppskrift");
        alert.setHeaderText("Vil du slette " + mealManager.getCurrentRecipe() + " ?");
        alert.setGraphic(null);
        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                mealManager.deleteCurrentRecipe();
            } 
        });
        updateRecipe();
        updateRecipes();
    }

    
    @FXML
    private void addIngredientToCurrentRecipe() {
        GroceryItem selectedItem = ingredientSearchList.getSelectionModel().getSelectedItem();
        Double amountInCorrectUnit = Double.parseDouble(amountField.getText()) * ingredientUnitBox.getValue().getConversionFactor(selectedItem.getMeasuringUnit());
        Ingredient ingredient = new Ingredient(selectedItem, amountInCorrectUnit);
        
        mealManager.addIngredientToCurrentRecipe(ingredient);
        updateRecipe();
    }
    
    @FXML
    private void handleUnitSelection() {
        
    }
    
    private void handleIngredientSelection() {
        GroceryItem selected = ingredientSearchList.getSelectionModel().getSelectedItem();
        MeasuringUnit unit = selected.getMeasuringUnit();
        ingredientUnitBox.setValue(unit);
        ingredientUnitBox.getItems().setAll(unit.getConvertibleUnits());
    }

    private void updateRecipe() {
        currentRecipeLabel.setText("Valgt oppskrift: " + mealManager.getCurrentRecipe().toString());
        ingredientList.getItems().setAll(mealManager.getCurrentIngredients());
        totalPriceLabel.setText(mealManager.getCurrentRecipePrice().toString());
        mealManager.saveRecipes();
        ingredientList.scrollTo(ingredientList.getItems().size() - 1);
        ingredientList.getSelectionModel().select(ingredientList.getItems().size() - 1);
    }

    private void updateRecipes() {
        recipeBox.getItems().setAll(mealManager.getRecipes());
        mealManager.saveRecipes();
    }

    private void updateImage() {
        GroceryItem selectedItem = groceryItemTable.getSelectionModel().getSelectedItem();
        String ean;
        if (selectedItem != null) {
            ean = selectedItem.getEan();
        } else {
            ean = "Undefined";
        }
        Image image = mealManager.getImage(ean);
        productImage.setImage(image);
    }
}
