package com.Panel;
import com.Panel.UserData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreareCont {
    private static final String FILE_PATH = "utilizatori.dat";

    public boolean creareCont(String nume, String prenume, String email, String parola) {
        if (emailExistente(email)) {
            return false;
        }

        UserData userData = new UserData(nume, prenume, email, parola);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(userData.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean emailExistente(String email) {
        return false; // Implementare fictivă pentru exemplu
    }
}