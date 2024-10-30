package com.Panel;


public class UserData {
    private String nume;
    private String prenume;
    private String email;
    private String parola;

    public UserData(String nume, String prenume, String email, String parola) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }
    public String getPrenume() {
        return prenume;
    }
    public String getEmail() {
        return email;
    }
    public String getParola() {
        return parola;
    }

    public String toString() {
        return nume + "," + prenume + "," + email + "," + parola;
    }
}