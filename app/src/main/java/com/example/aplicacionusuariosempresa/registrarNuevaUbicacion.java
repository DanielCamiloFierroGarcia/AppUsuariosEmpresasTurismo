package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacionusuariosempresa.Modelo.Ubicacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registrarNuevaUbicacion extends AppCompatActivity implements View.OnClickListener{

    private EditText longitud; //X = longitud
    private EditText latitud; // Y = latitud
    private EditText nombre;
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  Toast.makeText(registrarNuevaUbicacion.this, "Inicio registro", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_nueva_ubicacion);

        Button buttonSave = findViewById(R.id.save);
        nombre = findViewById(R.id.editTextTextPersonName);
        longitud = findViewById(R.id.editTextX);
        latitud = findViewById(R.id.editTextY);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Registro_ubicaciones");

        auth = FirebaseAuth.getInstance();
        buttonSave.setOnClickListener(view -> {

            TextView text_nombre = findViewById(R.id.textView_nombre);
            TextView text_longitud =  findViewById(R.id.textView_X);
            TextView text_latitud =  findViewById(R.id.textView_Y);

            TextView guardado_nombre =  findViewById(R.id.nombre_guardado);
            TextView guardado_longitud =  findViewById(R.id.x_guardado);
            TextView guardado_latitud = findViewById(R.id.y_guardado);

            if(validarCoord(longitud.getText().toString()) && validarCoord(latitud.getText().toString())){

                guardarInfo(longitud.getText().toString(), latitud.getText().toString(), nombre.getText().toString());
                Toast.makeText(registrarNuevaUbicacion.this, "Se ha guardado", Toast.LENGTH_SHORT).show();

                guardado_nombre.setVisibility(View.VISIBLE);
                guardado_longitud.setVisibility(View.VISIBLE);
                guardado_latitud.setVisibility(View.VISIBLE);

                text_nombre.setText(nombre.getText().toString());
                text_longitud.setText(longitud.getText().toString());
                text_latitud.setText(latitud.getText().toString());

            }else{
                Toast.makeText(registrarNuevaUbicacion.this, "Error en las coordenadas", Toast.LENGTH_SHORT).show();

                guardado_nombre.setVisibility(View.INVISIBLE);
                guardado_longitud.setVisibility(View.INVISIBLE);
                guardado_latitud.setVisibility(View.INVISIBLE);

                text_nombre.setText("");
                text_longitud.setText("");
                text_latitud.setText("");
            }
        });
        Button buttonVer = findViewById(R.id.ver_ubicaciones);

        buttonVer.setOnClickListener(view -> {
            startActivity(new Intent(registrarNuevaUbicacion.this, verUbicaciones.class));
        });
        Button buttonVolver= findViewById(R.id.volverAPerfil);

        buttonVolver.setOnClickListener(view -> {
            startActivity(new Intent(registrarNuevaUbicacion.this, PerfilUsuario.class));
        });
    }


    public void guardarInfo(String longitud, String latitud, String nombre){

        Ubicacion ubicacion =  new Ubicacion(longitud, latitud, nombre);
        if(auth.getCurrentUser() != null){
            myRef.child(auth.getCurrentUser().getUid()).child(ubicacion.getNombre()).setValue(ubicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(registrarNuevaUbicacion.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(registrarNuevaUbicacion.this, "No se pudo guardar",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onClick(View view) {

    }
    public boolean validarCoord(String coord){
        Pattern p = Pattern.compile("[-]?[0-9]+[.]?[0-9]+");
        Matcher m = p.matcher(coord);
        return m.matches();
    }
}