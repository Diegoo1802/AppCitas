package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;
    private Button btn_logout;
    private TextView txt_bienvenida;

    private FirebaseFirestore db;
    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        db = FirebaseFirestore.getInstance();

        // Inicializamos los elementos de la interfaz
        txt_bienvenida = findViewById(R.id.txt_bienvenida);
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_citas = findViewById(R.id.btn_citas);
        btn_reservas = findViewById(R.id.btn_reservas);
        btn_logout = findViewById(R.id.btn_logout);

        // Recuperar el userId y el userName desde el Intent
        userId = getIntent().getStringExtra("USER_ID");
        userName = getIntent().getStringExtra("USER_NAME");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(MenuActivity.this, "No se encontró el ID de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el nombre de usuario está disponible
        if (userName == null || userName.isEmpty()) {
            // Si no está disponible, buscar el nombre desde Firestore
            fetchUserNameFromFirestore(userId);
        } else {
            // Si el nombre ya está disponible, mostrarlo
            txt_bienvenida.setText("¡Bienvenido, " + userName + "!");
        }

        // Navegar a PerfilActivity pasando el userId
        btn_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_NAME", userName); // Pasar también el nombre
            startActivity(intent);
        });

        // Navegar a CitasActivity pasando el userId
        btn_citas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CitasActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_NAME", userName); // Pasar también el nombre
            startActivity(intent);
        });

        // Navegar a ReservasActivity pasando el userId
        btn_reservas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ReservasActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_NAME", userName); // Pasar también el nombre
            startActivity(intent);
        });

        // Cerrar sesión y redirigir a la pantalla de inicio
        btn_logout.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Método para obtener el nombre del usuario desde Firestore
     */
    private void fetchUserNameFromFirestore(@NonNull String userId) {
        db.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userName = documentSnapshot.getString("nombre");
                        if (userName != null && !userName.isEmpty()) {
                            txt_bienvenida.setText("¡Bienvenido, " + userName + "!");
                        } else {
                            txt_bienvenida.setText("¡Bienvenido!");
                        }
                    } else {
                        txt_bienvenida.setText("¡Bienvenido!");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MenuActivity.this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
                });
    }
}
