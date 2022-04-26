package com.example.aplicacionusuariosempresa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerfilUsuario extends AppCompatActivity {

    private Button irPerfil, registrar_ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);



        registrar_ubicacion = (Button) findViewById(R.id.agregar_ruta);
        registrar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PerfilUsuario.this, registrarNuevaUbicacion.class));


            }
        });

        irPerfil = (Button) findViewById(R.id.irPerfil);
        irPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerfilUsuario.this, VerPerfil.class));
            }
        });

    }

}