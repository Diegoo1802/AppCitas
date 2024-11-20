package com.example.appcitasclase;

public class Cita {
    private String id;
    private String fecha;
    private String hora;
    private String tipoMasaje;
    private String estado;

    public Cita() {
    }

    public Cita(String id, String fecha, String hora, String tipoMasaje, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoMasaje = tipoMasaje;
        this.estado = estado;
    }

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
