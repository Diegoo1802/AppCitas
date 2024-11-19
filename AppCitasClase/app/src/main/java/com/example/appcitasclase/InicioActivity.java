package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        editTextUsername = findViewById(R.id.txt_usuario);
        editTextPassword = findViewById(R.id.txt_pass);
        btn_inicios = findViewById(R.id.btn_inicios);
        btn_registro = findViewById(R.id.btn_registro);

        // Configurar el botón de registro
        btn_registro.setOnClickListener(v -> {
            // Redirigir a la actividad de registro
            Intent intent = new Intent(InicioActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // Configurar el botón de inicio de sesión
        btn_inicios.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(InicioActivity.this, "Por favor, ingresa el correo y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar Firestore para obtener los datos del usuario
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // El usuario existe, ahora verificamos la contraseña
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Usuario usuario = document.toObject(Usuario.class);
                                if (usuario != null && usuario.getPassword().equals(password)) {
                                    // Si la contraseña es correcta, iniciar sesión
                                    Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
                                    intent.putExtra("USER_NAME", usuario.getNombre()); // Pasamos el nombre del usuario
                                    startActivity(intent);
                                    finish(); // Finaliza la actividad actual
                                } else {
                                    Toast.makeText(InicioActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(InicioActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InicioActivity.this, "Error al verificar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
