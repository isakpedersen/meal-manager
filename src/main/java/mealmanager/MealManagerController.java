package mealmanager;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MealManagerController {
    private static final int MAX_SUGGESTIONS = 5;
    private static final int MAX_GROCERY_ROWS = 8;
    private static final Collator collator = Collator.getInstance(Locale.of("no", "NO"));

    private MealManager mealManager = new MealManager();

    @FXML private ComboBox<Recipe> recipeBox;
    
    @FXML private Label currentRecipeLabel;
    
    @FXML private ListView<Ingredient> ingredientList;
    
    @FXML private Label totalPriceLabel;

    @FXML private TextField ingredientSearchField;
    @FXML private ListView<GroceryItem> ingredientSearchList;
    private Scroller ingredientDropdownScroller;
    private String currentQuery = "";

    @FXML private TextField amountField;
    
    @FXML private ComboBox<MeasuringUnit> ingredientUnitBox;

    @FXML private TableView<GroceryItem> shoppingListTable;
    @FXML private TableColumn<GroceryItem, String> shoppingItemNameColumn;
    @FXML private TableColumn<GroceryItem, Integer> shoppingItemPackagesColumn;
    @FXML private TableColumn<GroceryItem, Double> shoppingItemActualPackagesColumn;

    @FXML private TableView<GroceryItem> groceryItemTable;
    @FXML private TableColumn<GroceryItem, String> eanColumn;
    @FXML private TableColumn<GroceryItem, String> nameColumn;
    @FXML private TableColumn<GroceryItem, Quantity> quantityColumn;
    @FXML private TableColumn<GroceryItem, String> priceColumn;
    @FXML private TableColumn<GroceryItem, String> unitPriceColumn;

    @FXML private ImageView productImage;

    @FXML
    public void initialize() {
        ingredientDropdownScroller = new Scroller(ingredientSearchList, MAX_SUGGESTIONS);
        updateRecipe();
        updateRecipes();
    
        ingredientList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                Ingredient selected = ingredientList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    mealManager.removeIngredientFromCurrentRecipe(selected);
                    updateRecipe();
                }
            }
        });

        // Initialize shopping list table
        shoppingItemNameColumn.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getName()));
        shoppingItemPackagesColumn.setCellValueFactory(row -> new SimpleObjectProperty<>(mealManager.getShoppingList().get(row.getValue())));
        shoppingItemActualPackagesColumn.setCellValueFactory(row -> new SimpleObjectProperty<>(mealManager.getActualPackages(row.getValue())));

        // Initialize grocery item table
        eanColumn.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getEan()));
        nameColumn.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getName()));
        quantityColumn.setCellValueFactory(row -> new SimpleObjectProperty<>(row.getValue().getQuantity()));
        priceColumn.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getPriceString()));
        unitPriceColumn.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getUnitPriceString()));
        groceryItemTable.getItems().setAll(mealManager.getAvailableGroceryItems());

        // Sort table by name column upon initial launch
        FXCollections.sort(groceryItemTable.getItems(), Comparator.comparing(GroceryItem::getName, collator));
        // Sort using collator when clicking on column header
        nameColumn.setComparator((a, b) -> collator.compare(a,b));

        groceryItemTable.getSelectionModel().select(groceryItemTable.getItems().get(0));
        updateImage();

        // Update product image when selecting grocery items 
        groceryItemTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null && newVal != oldVal) updateImage();
            }
        );

        // Configure listener for ingredient search box
        ingredientSearchField.textProperty().addListener((obs, oldVal, query) -> {
            currentQuery = query;
            ingredientSearchField.getStyleClass().remove("ingredient-search-confirmed");
            
            if (query.isBlank()) {
                clearSearch();
                return;
            };

            GroceryItem lastSelected = getSelectedSuggestion();

            // Load and filter dropdown list
            List<GroceryItem> filteredSearchList = mealManager.getAvailableGroceryItems().stream()
            .filter(groceryItem -> {
                return groceryItem.toString().toLowerCase().contains(query.toLowerCase());
            })
            .sorted((a, b) -> {
                // Keep lastSelected at the top
                if (a.equals(lastSelected)) return -1;
                if (b.equals(lastSelected)) return 1;
                
                // Then show items that starts with query
                boolean aStartsWithQuery = a.toString().toLowerCase().startsWith(query.toLowerCase());
                boolean bStartsWithQuery = b.toString().toLowerCase().startsWith(query.toLowerCase());
                if (aStartsWithQuery && !bStartsWithQuery) return -1;
                if (!aStartsWithQuery && bStartsWithQuery) return 1;
                
                // Sort alphabetically if both or none of the items starts with query
                return a.toString().compareTo(b.toString());
                
            })
            .toList();
            ingredientSearchList.getItems().setAll(filteredSearchList);
            ingredientSearchList.setPrefHeight(filteredSearchList.size() * 42.0 + 2.0);
            
            if (ingredientSearchList.getItems().isEmpty()) {
                clearSearch();
                return;
            }

            if (!filteredSearchList.contains(lastSelected)) {
                setSelectedSuggestionIndex(0);
            } else {
                setSelectedSuggestionIndex(filteredSearchList.indexOf(lastSelected));
            }

            ingredientSearchList.setVisible(true);

            ingredientDropdownScroller.refresh();
        });

        // Highlight query text in suggestions in bold
        ingredientSearchList.setCellFactory(listView -> new ListCell<GroceryItem>() {
            @Override
            protected void updateItem(GroceryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String itemString = item.toString();
                    int matchStart = itemString.toLowerCase().indexOf(currentQuery.toLowerCase());
                    int matchEnd = matchStart + currentQuery.length();

                    Text before = new Text(itemString.substring(0, matchStart));
                    Text match = new Text(itemString.substring(matchStart, matchEnd));
                    Text after = new Text(itemString.substring(matchEnd));
                    match.setStyle("-fx-font-weight: bold");
                    
                    // Pack text in label to preserve css styling
                    Label label = new Label();
                    label.setGraphic(new TextFlow(before, match, after));
                    setGraphic(label);
                    setText(null);
                }
            }
        });

        // Keyboard triggers
        ingredientSearchField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                ingredientDropdownScroller.scrollDown();
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                ingredientDropdownScroller.scrollUp();
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                ingredientSearchField.setText(ingredientSearchList.getSelectionModel().getSelectedItem().toString());
                ingredientSearchField.getStyleClass().add("ingredient-search-confirmed");
                ingredientSearchList.setVisible(false);
                handleIngredientSelection();
                amountField.requestFocus();
            }
        });

        // Mouse triggers

        // Run after UI is initialized:
        Platform.runLater(() -> {
            AnchorPane.setTopAnchor(ingredientSearchList, ingredientSearchField.getHeight());
            ingredientSearchList.setPrefWidth(ingredientSearchField.getWidth());
            ingredientSearchList.setMaxHeight(ingredientDropdownScroller.getMaxSuggestions() * 42.0 + 2.0);
            groceryItemTable.setMaxHeight(MAX_GROCERY_ROWS * 42.0 + 40.0);
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
        GroceryItem selected = getSelectedSuggestion();
        MeasuringUnit unit = selected.getMeasuringUnit();
        ingredientUnitBox.setValue(unit);
        ingredientUnitBox.getItems().setAll(unit.getConvertibleUnits());
        // select corresponding grocery item in grocery table
        groceryItemTable.getSelectionModel().select(selected);
        groceryItemTable.scrollTo(selected);
    }

    private void updateRecipe() {
        currentRecipeLabel.setText("Valgt oppskrift: " + mealManager.getCurrentRecipe().toString());
        ingredientList.getItems().setAll(mealManager.getCurrentIngredients());
        totalPriceLabel.setText(mealManager.getCurrentRecipePrice().toString());
        mealManager.saveRecipes();
        ingredientList.scrollTo(ingredientList.getItems().size() - 1);
        ingredientList.getSelectionModel().select(ingredientList.getItems().size() - 1);
        updateShoppingList();
    }

    private void updateRecipes() {
        recipeBox.getItems().setAll(mealManager.getRecipes());
        mealManager.saveRecipes();
    }

    private void updateShoppingList() {
        shoppingListTable.getItems().setAll(mealManager.getShoppingList().keySet());
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

    private GroceryItem getSelectedSuggestion() {
        return ingredientSearchList.getSelectionModel().getSelectedItem();
    }

    private void setSelectedSuggestionIndex(int index) {
        if (index < 0 || index >= ingredientSearchList.getItems().size()) throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        ingredientSearchList.getSelectionModel().select(index);
    }

    private void clearSearch() {
        ingredientSearchList.setVisible(false);
        ingredientDropdownScroller.reset();
        ingredientSearchList.getSelectionModel().clearSelection();
    }
}
