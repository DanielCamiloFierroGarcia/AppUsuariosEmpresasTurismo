package com.example.aplicacionusuariosempresa.Modelo;

public class Ubicacion {

    private String longitud;
    private String latitud;
    private String nombre;
    private String tipo;

    public Ubicacion(){

    }
    public Ubicacion(String longitud, String latitud, String nombre){
        this.longitud = longitud;
        this.latitud = latitud;
        this.nombre = nombre;
    }
    public Ubicacion(String longitud, String latitud, String nombre, String tipo){
        this.longitud = longitud;
        this.latitud = latitud;
        this.nombre = nombre;
        this.tipo = tipo;
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
    public String getTipo(){return tipo;    }

    public void setTipo(String tipo) {this.tipo = tipo;}

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
