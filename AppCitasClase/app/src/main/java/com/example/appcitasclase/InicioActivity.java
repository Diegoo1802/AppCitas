package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
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

public class InicioActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btn_inicios;
    private Button btn_registro;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario ya está autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Si el usuario ya está autenticado, redirigir al menú
            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual para evitar que el usuario regrese a la pantalla de inicio de sesión
        }

        // Inicializar las vistas
        editTextUsername = findViewById(R.id.txt_usuario);
        editTextPassword = findViewById(R.id.txt_pass);
        btn_inicios = findViewById(R.id.btn_inicios);
        btn_registro = findViewById(R.id.btn_registro);

        // Configurar el botón de inicio de sesión
        btn_inicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });

        // Configurar el botón de registro
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad de registro
                Intent intent = new Intent(InicioActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para validar el inicio de sesión
    private void validateLogin() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Verificar si el correo y la contraseña no están vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(InicioActivity.this, "Por favor, ingresa el correo y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Intentar iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Usuario autenticado correctamente
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InicioActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Redirigir a la pantalla MenuActivity
                            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual para evitar que el usuario regrese a la pantalla de inicio
                        } else {
                            // Si la autenticación falla, mostrar un mensaje de error
                            Toast.makeText(InicioActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
