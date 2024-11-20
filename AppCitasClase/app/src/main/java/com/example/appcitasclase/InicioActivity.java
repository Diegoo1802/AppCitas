package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Importante para los logs
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InicioActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btn_inicios;
    private Button btn_registro;

    private FirebaseFirestore db; // Instancia de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        db = FirebaseFirestore.getInstance();

        editTextUsername = findViewById(R.id.txt_usuario);
        editTextPassword = findViewById(R.id.txt_pass);
        btn_inicios = findViewById(R.id.btn_inicios);
        btn_registro = findViewById(R.id.btn_registro);

        btn_registro.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        btn_inicios.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(InicioActivity.this, "Por favor, ingresa el correo y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        email = email.toLowerCase();

        // Buscar el usuario por email en la colección de Firestore
        db.collection("usuarios")
                .whereEqualTo("email", email) // Aseguramos que buscamos por el email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Recorremos los resultados para verificar la contraseña
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Convertimos el documento a un objeto Usuario
                                Usuario usuario = document.toObject(Usuario.class);

                                if (usuario != null) {
                                    Log.d("InicioActivity", "Usuario encontrado: " + usuario.getEmail()); // Log de depuración

                                    // Comparar la contraseña
                                    if (usuario.getPassword().equals(password)) {
                                        // Si las contraseñas coinciden, iniciar sesión
                                        Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
                                        intent.putExtra("USER_NAME", usuario.getNombre()); // Pasamos el nombre del usuario
                                        intent.putExtra("USER_ID", document.getId()); // Pasamos el ID del documento (ID de usuario)
                                        startActivity(intent);
                                        finish(); // Finaliza la actividad actual
                                        return;
                                    } else {
                                        Toast.makeText(InicioActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(InicioActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error al consultar Firestore
                        Log.d("InicioActivity", "Error al consultar Firestore: " + task.getException().getMessage()); // Log de depuración
                        Toast.makeText(InicioActivity.this, "Error al verificar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
