package main.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ForecastTemplate extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ForecastTemplate.class.getResource("Template.fxml"));
        Parent layout = fxmlLoader.load();

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("main/view/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Holiday Weather Forecast");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
