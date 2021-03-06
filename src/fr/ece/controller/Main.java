package fr.ece.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import fr.ece.view.*;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;

    public void start(Stage primaryStage) {

        try {

            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(Main.class.getResource("../view/TracerouteOverview.fxml"));
            loader.setLocation(Main.class.getResource("/fr/ece/view/TracerouteOverview.fxml"));
            rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(Main.class.getResource("application.css").toString());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
