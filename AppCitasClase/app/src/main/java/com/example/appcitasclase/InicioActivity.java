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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Enlazamos las vistas con sus ID en el layout
        editTextUsername = findViewById(R.id.txt_nombre);
        editTextPassword = findViewById(R.id.txt_pass);
        btn_inicios = findViewById(R.id.btn_inicios);

        // Configuramos el botón de inicio de sesión
        btn_inicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    // Método para validar el inicio de sesión con Firebase
    private void validateLogin() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(InicioActivity.this, "Por favor, ingresa el correo y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InicioActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Navegamos a la siguiente pantalla (MenuActivity)
                            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual para que el usuario no regrese con el botón de retroceso
                        } else {
                            // Si el inicio de sesión falla, muestra un mensaje al usuario
                            Toast.makeText(InicioActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
