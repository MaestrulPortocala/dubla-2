package com.Panel;

import com.example.proiect3.Partyfy;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.Panel.CreareCont;

import javafx.stage.Stage;

//import static jdk.internal.net.http.common.Utils.close;

public class LoginPane extends VBox {
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button createAccountButton;

    public LoginPane(Logare logare, Stage stage) {
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: black;");

        Text title = new Text("Log In");
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 24px;"); // Mărime font
        getChildren().add(title);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");
        getChildren().add(emailField);

        passwordField = new PasswordField();
        passwordField.setPromptText("Parola");
        passwordField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");
        getChildren().add(passwordField);

        loginButton = new Button("Log In");
        loginButton.setOnAction(e -> handleLogin(logare, stage));
        styleButton(loginButton);
        getChildren().add(loginButton);

        createAccountButton = new Button("Creare Cont");
        createAccountButton.setOnAction(e -> openCreateAccountWindow());
        styleButton(createAccountButton);
        getChildren().add(createAccountButton);
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;"); // Butoane albastre
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;")); // Efect hover
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;")); // Efect hover
    }

    private void handleLogin(Logare logare, Stage stage) {
        String email = emailField.getText();
        String password = passwordField.getText();

        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager.autentificare(email, password)) {
            System.out.println("Logare reusita!");
            logare.loginSuccessful(); // Apelează această metodă când autentificarea are succes
            launchPartyfy(); // Lansează aplicația Partyfy
            stage.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Email sau parola incorecta.");
            alert.showAndWait();
        }
    }

    private void launchPartyfy() {
        // Creează instanța aplicației Partyfy
        Partyfy partyfyApp = new Partyfy();
        try {
            // Deschide aplicația Partyfy
            Stage stage = new Stage();
            partyfyApp.start(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCreateAccountWindow() {
        Stage createAccountStage = new Stage();
        createAccountStage.setTitle("Creare Cont");

        VBox createAccountPane = new VBox(10);
        createAccountPane.setPadding(new Insets(20));
        createAccountPane.setStyle("-fx-background-color: black;");
        createAccountPane.setAlignment(Pos.CENTER); // Aliniază la centru

        TextField nameField = new TextField();
        nameField.setPromptText("Nume");
        nameField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");
        TextField surnameField = new TextField();
        surnameField.setPromptText("Prenume");
        surnameField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Parola");
        passwordField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555; -fx-border-radius: 5; -fx-background-radius: 5;");

        Button createButton = new Button("Creare Cont");
        createButton.setOnAction(e -> {
            String nume = nameField.getText();
            String prenume = surnameField.getText();
            String email = emailField.getText();
            String parola = passwordField.getText();

            CreareCont creareCont = new CreareCont();
            if (creareCont.creareCont(nume, prenume, email, parola)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cont creat cu succes!");
                alert.showAndWait();
                createAccountStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Email-ul este deja folosit.");
                alert.showAndWait();
            }
        });

        styleButton(createButton);

        createAccountPane.getChildren().addAll(nameField, surnameField, emailField, passwordField, createButton);
        Scene createAccountScene = new Scene(createAccountPane, 300, 250);
        createAccountScene.setFill(Color.BLACK);

        createAccountStage.setScene(createAccountScene);
        createAccountStage.show();
    }
}