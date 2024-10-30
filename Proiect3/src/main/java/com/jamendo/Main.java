package com.jamendo;

import com.jamendo.JamendoApiClient;
import com.jamendo.JamendoApiException;
import com.jamendo.model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize the client
            JamendoApiClient client = new JamendoApiClient();

            // Search for albums by artist name
            Map<String, String> albumParams = new HashMap<>();
            albumParams.put("namesearch", "Premiers Jets");
            albumParams.put("limit", "5");

            JamendoAlbumsResponse albumsResponse = client.getAlbums(albumParams);
            for (Album album : albumsResponse.getResults()) {
                System.out.println("Album: " + album.getName() + " by " + album.getArtist_name());
            }

            // Search for tracks with specific tags
            Map<String, String> trackParams = new HashMap<>();
            trackParams.put("fuzzytags", "groove rock");
            trackParams.put("speed", "high veryhigh");
            trackParams.put("include", "musicinfo");
            trackParams.put("groupby", "artist_id");

            JamendoTracksResponse tracksResponse = client.getTracks(trackParams);
            for (Track track : tracksResponse.getResults()) {
                System.out.println("Track: " + track.getName() + " by " + track.getArtist_name() + " Audio: " + track.getAudio());
            }

        } catch (JamendoApiException e) {
            e.printStackTrace();
        }
    }
}
