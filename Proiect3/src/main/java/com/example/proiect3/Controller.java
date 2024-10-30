package com.example.proiect3;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import com.jamendo.JamendoApiClient;
import com.jamendo.JamendoApiException;
import com.jamendo.model.JamendoTracksResponse;
import com.jamendo.model.Track;

public class Controller implements Initializable {
    public Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    private Media media;
    private MediaPlayer mediaPlayer;

    private ArrayList<String> songUrls; // List of song URLs from the API
    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songUrls = new ArrayList<>(); // Initialize the list of song URLs
        songNumber = 0; // Start from the first song

        // Populate the speed box
        for (int speed : speeds) {
            speedBox.getItems().add(speed + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        songProgressBar.setStyle("-fx-accent: #00FF00;");
    }

    private Track currentTrack; // Add this to store the current track information

    private void loadMedia(String audioUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop previous media player
        }

        media = new Media(audioUrl);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnError(() -> {
            if (mediaPlayer.getError() != null) {
                System.err.println("Media Error: " + mediaPlayer.getError().getMessage());
            } else {
                System.err.println("Unknown media error.");
            }
        });

        mediaPlayer.setOnReady(() -> {
            // Set the song label to show the song name when the media is ready
            if (currentTrack != null) {
                songLabel.setText(currentTrack.getName()); // Use currentTrack here
            } else {
                songLabel.setText(new File(audioUrl).getName()); // Fallback if track is null
            }
            mediaPlayer.play(); // Automatically play when ready
            beginTimer();
        });

        mediaPlayer.setOnEndOfMedia(() -> nextMedia()); // Automatically play the next track
    }



    public void playMedia() {
        if (!songUrls.isEmpty() && mediaPlayer == null) {
            loadMedia(songUrls.get(songNumber)); // Load media from the song URL
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        } else if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pauseMedia() {
        if (mediaPlayer != null) {
            cancelTimer(); // Stop the progress tracking timer
            mediaPlayer.pause(); // Pause the media player
        }
    }


    public void resetMedia() {
        if (mediaPlayer != null) {
            songProgressBar.setProgress(0);
            mediaPlayer.seek(Duration.seconds(0));
        }
    }

    public void previousMedia() {
        if (songNumber > 0) {
            songNumber--;
        } else {
            songNumber = songUrls.size() - 1; // Loop to the last song
        }
        playMedia();
    }

    public void nextMedia() {
        if (songNumber < songUrls.size() - 1) {
            songNumber++;
        } else {
            songNumber = 0; // Loop to the first song
        }
        playMedia();
    }

    public void changeSpeed(ActionEvent event) {
        if (mediaPlayer != null && speedBox.getValue() != null) {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().replace("%", "")) * 0.01);
        }
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                if (mediaPlayer != null && mediaPlayer.getCurrentTime() != null && media.getDuration() != null) {
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = media.getDuration().toSeconds();
                    songProgressBar.setProgress(current / end);

                    if (current / end >= 1) {
                        cancelTimer();
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null; // Set to null to avoid repeated calls to cancel
        }
    }

    // Method to set song URLs from the API
    public void setSongUrls(ArrayList<String> urls, ArrayList<Track> tracks) {
        this.songUrls = urls;
        if (!songUrls.isEmpty() && !tracks.isEmpty()) {
            songNumber = 0; // Reset to the first song
            currentTrack = tracks.get(0); // Set the current track
            songLabel.setText(currentTrack.getName()); // Display the first song's name
            playMedia(); // Automatically play the first song
        }
    }


}