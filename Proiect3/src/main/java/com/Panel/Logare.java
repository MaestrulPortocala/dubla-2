package com.Panel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Logare extends Application {
    private Runnable onLoginSuccess;


    public void start(Stage primaryStage) {
        primaryStage.setTitle("Logare Aplicație");

        // Create the LoginPane and pass the current Logare instance
        LoginPane loginPane = new LoginPane(this, primaryStage);

        StackPane root = new StackPane();
        root.getChildren().add(loginPane);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public void loginSuccessful() {
        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }
}