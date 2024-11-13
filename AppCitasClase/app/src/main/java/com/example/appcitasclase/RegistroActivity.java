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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnRegister;
    private Button btnVolverInicio;  // Nuevo botón para volver al inicio

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar las vistas
        editTextEmail = findViewById(R.id.txt_email);
        editTextPassword = findViewById(R.id.txt_password);
        btnRegister = findViewById(R.id.btn_register);
        btnVolverInicio = findViewById(R.id.btn_volverInicio);  // Referencia al nuevo botón

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
                // Redirigir al usuario a la pantalla de inicio
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

        // Verificar que los campos no estén vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegistroActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar el formato del email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegistroActivity.this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que la contraseña tenga al menos 6 caracteres
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
                            // Registro exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                            // Redirigir al usuario a la pantalla de menú principal
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
}
