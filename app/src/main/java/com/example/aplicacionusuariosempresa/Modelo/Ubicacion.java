package com.example.aplicacionusuariosempresa.Modelo;

public class Ubicacion {

    private String longitud;
    private String latitud;
    private String nombre;

    public Ubicacion(){

    }
    public Ubicacion(String longitud, String latitud, String nombre){
        this.longitud = longitud;
        this.latitud = latitud;
        this.nombre = nombre;
    }
    public String getLongitud() {
        return longitud;
    }
    public String getLatitud() {
        return latitud;
    }
    public String getNombre() {
        return nombre;
    }

    public void setLongitud(String x) {
        this.longitud = x;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(String y) {
        this.latitud = y;
    }
}
