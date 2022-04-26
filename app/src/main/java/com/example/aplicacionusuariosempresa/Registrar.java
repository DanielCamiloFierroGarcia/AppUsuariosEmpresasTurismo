package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aplicacionusuariosempresa.Modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registrar extends AppCompatActivity implements View.OnClickListener{

    private ImageView imagen;
    private FirebaseAuth mAuth;
    private EditText editNombre, editEdad, editCorreo, editDireccion, editClave;
    private ProgressBar barraProrgreso;
    private Button botonRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();

        editNombre = (EditText) findViewById(R.id.Nombre);
        editCorreo = (EditText) findViewById(R.id.Correo2);
        editDireccion = (EditText) findViewById(R.id.Genero);
        editClave = (EditText) findViewById(R.id.Clave);
        botonRegistro = (Button) findViewById(R.id.Registrar);
        botonRegistro.setOnClickListener(this);

        imagen = (ImageView) findViewById(R.id.ImagenVolver);
        imagen.setOnClickListener(this);

        barraProrgreso = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ImagenVolver:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.Registrar:
                registrarUsuario();
                break;
        }
    }

    private void registrarUsuario() {
        String email = editCorreo.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String direccion = editDireccion.getText().toString().trim();
        String genero = editDireccion.getText().toString().trim();
        String contrasena = editClave.getText().toString().trim();
        Boolean tipoUser= false;

        if(nombre.isEmpty()){
            editNombre.setError("Se requiere nombre completo");
            editNombre.requestFocus();
            return;
        }

        if(direccion.isEmpty()){
            editDireccion.setError("Se requiere nombre completo");
            editDireccion.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editCorreo.setError("Se requiere nombre completo");
            editCorreo.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Porfavor digite un correo valido");
            editCorreo.requestFocus();
            return;
        }
        if(contrasena.isEmpty()){
            editClave.setError("Se requiere nombre completo");
            editClave.requestFocus();
            return;
        }

        if(contrasena.length()<6){
            editClave.setError("Digite una clave de mas de 6 digitos");
            editClave.requestFocus();
            return;
        }

        barraProrgreso.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Usuario user = new Usuario(nombre, email,direccion, tipoUser,0);

                            FirebaseDatabase.getInstance().getReference("Usuarios")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Registrar.this, "Usuario se registro", Toast.LENGTH_LONG).show();
                                        barraProrgreso.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(Registrar.this, "Se fallo el registro", Toast.LENGTH_SHORT).show();
                                        barraProrgreso.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(Registrar.this, "Se fallo el registro, intente otra vez", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}