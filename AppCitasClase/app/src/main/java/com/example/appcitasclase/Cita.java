package com.example.appcitasclase;

public class Cita {
    private String id;  // ID único de la cita
    private String fecha;  // Fecha de la cita
    private String hora;  // Hora de la cita
    private String tipoMasaje;  // Tipo de masaje
    private String estado;  // Estado de la cita (confirmada, pendiente, etc.)

    public Cita() {
        // Constructor vacío necesario para Firestore
    }

    // Constructor con parámetros
    public Cita(String id, String fecha, String hora, String tipoMasaje, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoMasaje = tipoMasaje;
        this.estado = estado;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTipoMasaje() {
        return tipoMasaje;
    }

    public void setTipoMasaje(String tipoMasaje) {
        this.tipoMasaje = tipoMasaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
