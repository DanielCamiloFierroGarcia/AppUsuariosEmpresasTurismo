package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aplicacionusuariosempresa.Adaptadores.AdaptadorUbicacion;
import com.example.aplicacionusuariosempresa.Modelo.Ubicacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerUbicaciones extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth auth;
    private RecyclerView mRecycler;
    private ArrayList<Ubicacion> ubicacionesList = new ArrayList<>();
    AdaptadorUbicacion adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ubicaciones);

        mRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        auth = FirebaseAuth.getInstance();

        getUbicacionesFirebase();

        Button buttonVolver = findViewById(R.id.volverUbicaciones);
        buttonVolver.setOnClickListener(view -> {
            startActivity(new Intent(VerUbicaciones.this, registrarNuevaUbicacion.class));
        });
        Button buttonIrAMapa = findViewById(R.id.irAMapa);
        buttonIrAMapa.setOnClickListener(view -> {
            startActivity(new Intent(VerUbicaciones.this, VerUbicacionEnMapa.class));
        });
    }

    @Override
    public void onClick(View view) {

    }
    public void getUbicacionesFirebase(){
        myRef.child("Registro_ubicaciones").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ubicacionesList.clear();

                    for (DataSnapshot ds: snapshot.getChildren()) {

                        String nombre = ds.child("nombre").getValue().toString();
                        String longitud = ds.child("longitud").getValue().toString();
                        String latitud = ds.child("latitud").getValue().toString();
                        ubicacionesList.add(new Ubicacion(longitud, latitud, nombre));
                    }
                    adapter = new AdaptadorUbicacion(ubicacionesList, R.layout.ubicacion_view);
                    mRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}