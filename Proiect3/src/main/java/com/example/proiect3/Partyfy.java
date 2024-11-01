package com.example.proiect3;

import com.Panel.Logare;
import com.jamendo.JamendoApiClient;
import com.jamendo.JamendoApiException;
import com.jamendo.model.JamendoTracksResponse;
import com.jamendo.model.Track;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class Partyfy extends Application {
    private PlaylistManager playlistManager = new PlaylistManager(); 
    private Playlist myPlaylist = new Playlist("My Playlist"); 

    private boolean isMenuVisible = false; 
    private VBox menu; 
    private StackPane contentArea; 


    public void start(Stage primaryStage) {
        
        playlistManager.addPlaylist(myPlaylist);

        root = new BorderPane(); 
        root.setBackground(new Background(new BackgroundFill(Color.rgb(40, 40, 40), CornerRadii.EMPTY, Insets.EMPTY)));

        
        menu = createSideMenu();
        menu.setTranslateX(-200);

        
        contentArea = createContentArea();
        root.setCenter(contentArea);

    
        root.setTop(createTopBar(root));
        Image sigla = new Image("C://Users//lacat//IdeaProjects//Proiect3//src//main//java//com//example//proiect3//Imagini//sigla.png");

        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.getIcons().add(sigla);
        primaryStage.setTitle("Partyfy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    
    private HBox createTopBar(BorderPane root) {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER); 

        
        Button hamburgerButton = new Button("☰");
        hamburgerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18;");
        hamburgerButton.setOnAction(e -> toggleMenu(root)); 

        
        HBox searchBar = createSearchBar();
        HBox.setHgrow(searchBar, Priority.ALWAYS);

        
        topBar.getChildren().addAll(hamburgerButton, searchBar);

        return topBar;
    }

    
    private void toggleMenu(BorderPane root) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), menu);
        if (isMenuVisible) {
            slide.setToX(-menu.getWidth()); 
            root.setLeft(null); 
        } else {
            root.setLeft(menu); 
            slide.setToX(0); 
        }
        isMenuVisible = !isMenuVisible;
        slide.play();
    }

    
    private HBox createSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setAlignment(Pos.CENTER); 
        searchBar.setPadding(new Insets(5));

        
        TextField searchField = new TextField();
        searchField.setPromptText("Search for music...");
        searchField.setStyle(
                "-fx-background-color: black; " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: gray; " +
                        "-fx-border-color: transparent; " + 
                        "-fx-background-radius: 15; " +
                        "-fx-padding: 10 10 10 30;" 
        );

        
        Button searchButton = new Button("🔍");
        searchButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18;");
        searchButton.setOnAction(e -> performSearch(searchField.getText()));

        searchBar.getChildren().addAll(searchField, searchButton);
        return searchBar;
    }

    
    private VBox createSideMenu() {
        VBox menu = new VBox();
        menu.setPadding(new Insets(10));
        menu.setSpacing(15);

        Button homeButton = createStyledButton("Home");
        homeButton.setOnAction(e-> home());
        Button playlistsButton = createStyledButton("Playlists");

        
        playlistsButton.setOnAction(e -> showPlaylists());
        Button settingsButton = createStyledButton("Change Account");
        Logare schimbaContul= new Logare();
        Stage anotherStage=new Stage();
        settingsButton.setOnAction(e->  {
            schimbaContul.start(anotherStage);
            Stage currentStage = (Stage) settingsButton.getScene().getWindow();
            currentStage.close(); 
        });
        menu.getChildren().addAll(homeButton, playlistsButton, settingsButton);
        menu.setBackground(new Background(new BackgroundFill(Color.rgb(30, 30, 30), CornerRadii.EMPTY, Insets.EMPTY)));

        return menu;
    }
    private BorderPane root; 

    private void home() {
        contentArea.getChildren().clear(); 
        contentArea = createContentArea(); 
        root.setCenter(contentArea); 
    }


    private void showPlaylists() {
        contentArea.getChildren().clear(); 

        VBox playlistsBox = new VBox(10);
        playlistsBox.setPadding(new Insets(10));

      
        List<Playlist> allPlaylists = playlistManager.getAllPlaylists(); 

        if (allPlaylists.isEmpty()) {
            Label noPlaylistsLabel = new Label("You have no playlists yet.");
            noPlaylistsLabel.setStyle("-fx-text-fill: white;");
            playlistsBox.getChildren().add(noPlaylistsLabel);
        } else {
            for (Playlist pl : allPlaylists) {
                Label playlistLabel = new Label(pl.getName()); 
                playlistLabel.setStyle("-fx-text-fill: white;");

                VBox tracksBox = new VBox(5); 
                for (Track track : pl.getTracks()) {
                    Label trackLabel = new Label(track.getName() + " by " + track.getArtist_name());
                    trackLabel.setStyle("-fx-text-fill: white;");

                    Button playButton = new Button("Play");
                    playButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                    playButton.setOnAction(e -> {
                        showMP3Player(track.getAudio(), track); 
                    });

                    HBox trackBox = new HBox(10, trackLabel, playButton);
                    trackBox.setAlignment(Pos.CENTER_LEFT);
                    tracksBox.getChildren().add(trackBox);
                }

                VBox playlistContainer = new VBox(10, playlistLabel, tracksBox);
                playlistsBox.getChildren().add(playlistContainer); 
            }
        }

        contentArea.getChildren().add(playlistsBox); 
    }

    
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: white;");
        return button;
    }

    
    private StackPane createContentArea() {
        Image logoImage = new Image("C://Users//lacat//IdeaProjects//Proiect3//src//main//java//com//example//proiect3//Imagini//sigla1.png");
        ImageView logoImageView = new ImageView(logoImage);

        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        StackPane contentArea = new StackPane();
        contentArea.getChildren().add(logoImageView);
        contentArea.setStyle("-fx-background-color: black;");

        return contentArea;
    }

    
    private void performSearch(String searchTerm) {
        contentArea.getChildren().clear(); 

        if (searchTerm.trim().isEmpty()) {
            Label errorLabel = new Label("Please enter a search term.");
            errorLabel.setStyle("-fx-text-fill: red;");
            contentArea.getChildren().add(errorLabel);
            return;
        }

        try {
            JamendoApiClient client = new JamendoApiClient();
            Map<String, String> trackParams = new HashMap<>();
            trackParams.put("namesearch", searchTerm);
            trackParams.put("limit", "10");

            JamendoTracksResponse tracksResponse = client.getTracks(trackParams);
            VBox resultsBox = new VBox(10);
            resultsBox.setPadding(new Insets(10));

            for (Track track : tracksResponse.getResults()) {
                Label trackLabel = new Label("Track: " + track.getName() + " by " + track.getArtist_name());
                trackLabel.setStyle("-fx-text-fill: white;");

                String audioUrl = track.getAudio(); 

                Button playButton = new Button("Play");
                Button playlistButton = new Button("Add to playlist");
                playButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                playlistButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

                
                playButton.setOnAction(e -> {
                    showMP3Player(audioUrl, track); 
                });

                
                playlistButton.setOnAction(e -> {
                    myPlaylist.addTrack(track); 
                    System.out.println("Track " + track.getName() + " has been added to the playlist");
                });

                HBox trackBox = new HBox(10, trackLabel, playButton, playlistButton);
                trackBox.setAlignment(Pos.CENTER_LEFT);
                resultsBox.getChildren().add(trackBox);
            }

            contentArea.getChildren().add(resultsBox); 
        } catch (JamendoApiException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error while searching for tracks. Please try again.");
            errorLabel.setStyle("-fx-text-fill: red;");
            contentArea.getChildren().add(errorLabel);
        }
    }

    
    private void showMP3Player(String audioUrl, Track track) {
        try {
            Stage mp3PlayerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml")); 
            Pane mp3PlayerPane = loader.load();

            
            Controller controller = loader.getController();
            controller.setSongUrls(new ArrayList<>(Collections.singletonList(audioUrl)), new ArrayList<>(Collections.singletonList(track)));



            Scene scene = new Scene(mp3PlayerPane);
            mp3PlayerStage.setTitle("MP3 Player");
            mp3PlayerStage.setScene(scene);

            
            mp3PlayerStage.setOnCloseRequest(e -> {
                controller.pauseMedia();
                controller.cancelTimer(); 
            });

            mp3PlayerStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
