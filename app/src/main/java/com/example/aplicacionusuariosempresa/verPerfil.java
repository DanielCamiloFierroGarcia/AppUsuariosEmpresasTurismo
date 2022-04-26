package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacionusuariosempresa.Modelo.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class verPerfil extends AppCompatActivity {

    private Button salirSesion, editarPerfil, registrar_ubicacion, irPerfil, guardarCambios;
    private TextView mostrarNombre, mostrarCorreo, mostrarGenero, mostrarEdad, mostrarVeces;
    private FirebaseUser usuarios;
    private DatabaseReference reference;
    private String idUsuario;
    private ImageView imagenPerfil;

    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        guardarCambios = (Button) findViewById(R.id.guardarCambios);
        salirSesion = (Button) findViewById(R.id.cerrarSesion);
        salirSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(verPerfil.this, MainActivity.class));
            }
        });

        //para obtener los datos desde firebase
        usuarios = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagenPerfilRef = storageReference.child("Usuarios/"+usuarios.getUid()+"/profile.jpg");
        imagenPerfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imagenPerfil);
            }
        });
        idUsuario = usuarios.getUid();

        final TextView textViewNombre = (TextView) findViewById(R.id.editar_nombre);
        final TextView textViewDireccion = (TextView) findViewById(R.id.editar_direccion);

        reference.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfilUsuario = snapshot.getValue(Usuario.class);

                if(perfilUsuario != null){
                    String nombreCompleto = perfilUsuario.nombre;
                    String correo = perfilUsuario.correo;
                    String veces = perfilUsuario.veces.toString();
                    String direccion = perfilUsuario.direccion;

                    textViewNombre.setText(nombreCompleto);
                    textViewDireccion.setText(direccion);

                    guardarCambios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nombre2 = textViewNombre.getText().toString();
                            String direccion2= textViewDireccion.getText().toString();

                            Usuario user = new Usuario(nombre2, correo, direccion2, false, perfilUsuario.veces);
                            FirebaseDatabase.getInstance().getReference("Usuarios")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                            startActivity(new Intent(verPerfil.this, PerfilUsuario.class));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(verPerfil.this, "Un error ocurrio", Toast.LENGTH_LONG).show();
            }
        });
        //para la imagen de perfil
        imagenPerfil = (ImageView) findViewById(R.id.foto_empresa);
        /*irPerfil = (Button) findViewById(R.id.irPerfil);
        irPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_editar_perfil);
            }
        });*/
        editarPerfil = (Button) findViewById(R.id.subirFoto);
        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiar imagen de perfil
                //cambiar ahora para modificar tod0 el perfil
                Intent abrirGaleriaFotos = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(abrirGaleriaFotos, 1000);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imagenUri = data.getData();

                //imagenPerfil.setImageURI(imagenUri);

                //para guardar en firebase
                subirImagenFirebase(imagenUri);
            }
        }
    }

    private void subirImagenFirebase(Uri imagenUri) {
        //logica para subir a firebase storage
        StorageReference referenciaArchivo = storageReference.child("Usuarios/"+usuarios.getUid()+"/profile.jpg");
        referenciaArchivo.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(verPerfil.this, "Imagen subida", Toast.LENGTH_LONG).show();
                referenciaArchivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imagenPerfil);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(verPerfil.this, "Error al subir", Toast.LENGTH_LONG).show();
            }
        });

    }
}