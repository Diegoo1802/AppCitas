package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextNombre;  // Campo de texto para el nombre del usuario
    private Button btnRegister;
    private Button btnVolverInicio;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        // Inicializar FirebaseAuth y DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializar las vistas
        editTextEmail = findViewById(R.id.txt_email);
        editTextPassword = findViewById(R.id.txt_password);
        editTextNombre = findViewById(R.id.txt_nombre);  // Referencia al campo de texto del nombre
        btnRegister = findViewById(R.id.btn_register);
        btnVolverInicio = findViewById(R.id.btn_volverInicio);

        // Configurar el botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        // Configurar el botón de volver al inicio
        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();  // Finaliza la actividad actual para evitar volver atrás
            }
        });
    }

    // Método para crear una cuenta de usuario
    private void createAccount() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();  // Obtener el nombre

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre)) {
            Toast.makeText(RegistroActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato del email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegistroActivity.this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que la contraseña tenga al menos 6 caracteres
        if (password.length() < 6) {
            Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Si el registro es exitoso, guardar los datos del usuario en la base de datos
                                saveUserData(user.getUid(), nombre, email);
                            }

                            // Redirigir al usuario a la pantalla de menú principal
                            Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish(); // Finaliza la actividad actual para que el usuario no pueda regresar

                        } else {
                            // Si falla el registro, mostrar un mensaje de error
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error al registrar usuario";
                            Toast.makeText(RegistroActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para guardar los datos del usuario en Firebase Realtime Database
    private void saveUserData(String userId, String nombre, String email) {
        Usuario usuario = new Usuario(nombre, email); // Crear un objeto Usuario con los datos del formulario
        databaseReference.child(userId).setValue(usuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistroActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistroActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
