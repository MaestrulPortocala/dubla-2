package com.Panel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginManager {
    private static LoginManager instance;

    private LoginManager() {}

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public boolean autentificare(String email, String parola) {
        try (BufferedReader reader = new BufferedReader(new FileReader("utilizatori.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String emailExist = data[2];
                    String parolaExist = data[3];

                    if (email.equals(emailExist) && parola.equals(parolaExist)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}