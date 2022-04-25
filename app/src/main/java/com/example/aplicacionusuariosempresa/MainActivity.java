package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacionusuariosempresa.Modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView registrar;
    private EditText editCorreo, editContrasena;
    private Button logIn;
    private String idUsuario;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressBar barraProgreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database

        registrar = (TextView) findViewById(R.id.registrar);
        registrar.setOnClickListener(this);

        logIn = (Button) findViewById(R.id.login);
        logIn.setOnClickListener(this);

        editCorreo = (EditText) findViewById(R.id.correo);
        editContrasena = (EditText) findViewById(R.id.contrase);

        barraProgreso = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registrar:
                startActivity(new Intent(this, Registrar.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editCorreo.getText().toString().trim();
        String password = editContrasena.getText().toString().trim();

        if(email.isEmpty()){
            editCorreo.setError("Se requiere correo");
            editCorreo.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Porfavor ingrese un correo valido");
            editCorreo.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editContrasena.setError("Se requiere contraseña");
            editContrasena.requestFocus();
            return;
        }
        if(password.length()<6){
            editContrasena.setError("La contraseña es de minimo 6 caracteres");
            editContrasena.requestFocus();
        }
        barraProgreso.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //verificar correo
                    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                    idUsuario = usuario.getUid();

                    if(usuario.isEmailVerified()){
                        reference.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Usuario perfilUsuario = snapshot.getValue(Usuario.class);
                                perfilUsuario.veces++;

                                FirebaseDatabase.getInstance().getReference("Usuarios")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(perfilUsuario);/*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, "Cambios hechos", Toast.LENGTH_LONG).show();

                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Se fallo el cambio", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });*/

                                if(perfilUsuario.tipoUsuario == false){
                                    //redireccionar al perfil
                                    startActivity(new Intent(MainActivity.this, PerfilUsuario.class));

                                    Toast.makeText(MainActivity.this, "FELICIDADES ESTAS ADENTRO", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "No eres un usuario valido de esta app", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Un errorsito", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else{
                        usuario.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Verifica tu correo", Toast.LENGTH_LONG).show();
                    }

                    barraProgreso.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(MainActivity.this, "Error en el login a la app, revisa tus credenciales", Toast.LENGTH_LONG).show();
                    barraProgreso.setVisibility(View.GONE);
                }
            }
        });
    }
}