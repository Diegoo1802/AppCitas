package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnRegister;
    private Button btnVolverInicio;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        db = FirebaseFirestore.getInstance();

        editTextNombre = findViewById(R.id.txt_nombre);
        editTextEmail = findViewById(R.id.txt_email);
        editTextPassword = findViewById(R.id.txt_password);
        btnRegister = findViewById(R.id.btn_register);
        btnVolverInicio = findViewById(R.id.btn_volverInicio);

        btnRegister.setOnClickListener(v -> createAccount());

        btnVolverInicio.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegistroActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reiniciar el nombre para que sea parte del ID
        String nombreFormateado = nombre.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");

        // Obtenemos el número total de usuarios para hacer el ID
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalUsuarios = queryDocumentSnapshots.size(); // Contar los usuarios existentes
                    String userId = nombreFormateado + "_" + (totalUsuarios + 1); // Creamos el ID único

                    // Crear el objeto del usuario correctamente, asignando nombre, email y password
                    Usuario usuario = new Usuario(email, password, nombre); // Orden correcto

                    // Guardar el usuario en Firestore
                    db.collection("usuarios")
                            .document(userId)
                            .set(usuario)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                // Volver al menú principal
                                Intent intent = new Intent(RegistroActivity.this, MenuActivity.class);
                                intent.putExtra("USER_ID", userId);
                                intent.putExtra("USER_NAME", nombre);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegistroActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistroActivity.this, "Error al verificar usuarios existentes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
