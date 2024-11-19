package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombre;  // Campo de texto para el nombre del usuario
    private EditText editTextEmail;   // Campo de texto para el email
    private EditText editTextPassword; // Campo de texto para la contraseña
    private Button btnRegister;
    private Button btnVolverInicio; // Botón para volver al inicio

    private FirebaseFirestore db; // Instancia de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar las vistas
        editTextNombre = findViewById(R.id.txt_nombre);
        editTextEmail = findViewById(R.id.txt_email);
        editTextPassword = findViewById(R.id.txt_password); // Campo de contraseña
        btnRegister = findViewById(R.id.btn_register);
        btnVolverInicio = findViewById(R.id.btn_volverInicio); // Inicializamos el botón para volver al inicio

        // Configurar el botón de registro
        btnRegister.setOnClickListener(v -> createAccount());

        // Configurar el botón de volver al inicio
        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, InicioActivity.class); // Redirigir al inicio
            startActivity(intent);
            finish(); // Finaliza la actividad actual para que el usuario no pueda volver con el botón de atrás
        });
    }

    // Método para crear una cuenta de usuario
    private void createAccount() {
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim(); // Obtener la contraseña

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegistroActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Usuario con los valores proporcionados
        Usuario usuario = new Usuario(nombre, email, password); // Pasar también la contraseña

        // Generar un ID único para el nuevo usuario en Firestore
        String userId = db.collection("usuarios").document().getId(); // Generar un ID único automáticamente

        // Guardar los datos del usuario en Firestore con un ID único
        db.collection("usuarios")
                .document(userId) // Usar el ID único generado
                .set(usuario) // Guardar los datos
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                    // Redirigir al usuario al menú principal
                    Intent intent = new Intent(RegistroActivity.this, MenuActivity.class);
                    intent.putExtra("USER_ID", userId);  // Pasar el userId al MenuActivity
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual para evitar que el usuario vuelva atrás
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistroActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
