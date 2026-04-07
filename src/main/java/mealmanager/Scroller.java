package mealmanager;

import javafx.scene.control.ListView;

public class Scroller {
    private final ListView<GroceryItem> list;
    private final int maxSuggestions;
    private int topIndex = -1;
    private int bottomIndex = -1;

    public Scroller(ListView<GroceryItem> list, int maxSuggestions) {
        this.list = list;
        this.maxSuggestions = maxSuggestions;
    }


    public void scrollDown() {
        if (list.getSelectionModel().getSelectedIndex() < list.getItems().size() - 1) {
            list.getSelectionModel().selectNext();
            if (list.getSelectionModel().getSelectedIndex() > bottomIndex) {
                list.scrollTo(list.getSelectionModel().getSelectedIndex() - maxSuggestions + 1);
                topIndex++;
                bottomIndex++;
            }
        }
    }

    public void scrollUp() {
        if (list.getSelectionModel().getSelectedIndex() > 0) {
            list.getSelectionModel().selectPrevious();
            if (list.getSelectionModel().getSelectedIndex() < topIndex) {
                list.scrollTo(list.getSelectionModel().getSelectedIndex());
                topIndex--;
                bottomIndex--;
            }
        }
    }

    public void scrollTo(int index) {

    }

    // initializes index of top and bottom item in the visible segment of the list
    public void refresh() {
        if (topIndex == -1) { topIndex = 0; }
        bottomIndex = Math.min(list.getItems().size(), maxSuggestions) - 1;
    }

    public void reset() {
        topIndex = -1;
        bottomIndex = -1;
    }

    public int getMaxSuggestions() {
        return maxSuggestions;
    }
}
