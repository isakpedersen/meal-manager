package mealmanager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import atlantafx.base.theme.PrimerLight;

public class MealManagerApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter_18pt-Regular.ttf"), 13);
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter_18pt-Bold.ttf"), 13);
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter_18pt-Italic.ttf"), 13);

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("App.fxml")));
        primaryStage.setTitle("Meal Manager");
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
