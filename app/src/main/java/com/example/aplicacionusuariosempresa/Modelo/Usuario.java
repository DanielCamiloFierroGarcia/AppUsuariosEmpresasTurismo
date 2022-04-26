package com.example.aplicacionusuariosempresa.Modelo;

public class Usuario {

    public String nombre, correo, direccion;
    public Boolean tipoUsuario;//Sera true si es un usuario no empresa
    public Integer veces;//cantidad de veces que loggea

    public Usuario(){

    }
    public Usuario(String nombre, String correo,String direccion, Boolean tipoUsuario, int veces){
        this.nombre=nombre;
        this.correo=correo;
        this.direccion=direccion;
        this.tipoUsuario=tipoUsuario;
        this.veces=veces;
    }
}
