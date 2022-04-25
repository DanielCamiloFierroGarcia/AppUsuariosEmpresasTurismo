package com.example.aplicacionusuariosempresa.Modelo;

public class Usuario {

    public String nombre, edad, correo, genero;
    public Boolean tipoUsuario;//Sera true si es un usuario no empresa
    public Integer veces;//cantidad de veces que loggea

    public Usuario(){

    }
    public Usuario(String nombre, String edad, String correo, String genero, Boolean tipoUsuario, int veces){
        this.nombre=nombre;
        this.edad=edad;
        this.correo=correo;
        this.genero=genero;
        this.tipoUsuario=tipoUsuario;
        this.veces=veces;
    }
}
