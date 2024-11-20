package com.example.appcitasclase;

public class Reserva {
    private String usuario;
    private String masaje;
    private String fecha;
    private String hora;

    public Reserva() {

    }

    public Reserva(String usuario, String masaje, String fecha, String hora) {
        this.usuario = usuario;
        this.masaje = masaje;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMasaje() {
        return masaje;
    }

    public void setMasaje(String masaje) {
        this.masaje = masaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
