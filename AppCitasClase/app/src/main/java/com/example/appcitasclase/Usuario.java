package com.example.appcitasclase;
public class Usuario {
    private String email;
    private String password;
    private String nombre;

    // Constructor vacío requerido para Firestore
    public Usuario() {
    }

    // Constructor para crear un usuario
    public Usuario(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
