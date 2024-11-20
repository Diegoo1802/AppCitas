package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNombre, txtEmail, txtContraseña;
    private ImageView imgPerfil;
    private Button btnBorrarCuenta, btnVolverMenu;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);

        db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario desde el Intent
        userId = getIntent().getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Error: No se pasó el ID del usuario", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        txtNombre = findViewById(R.id.txt_nombre);
        txtEmail = findViewById(R.id.txt_email);
        txtContraseña = findViewById(R.id.txt_contraseña);
        imgPerfil = findViewById(R.id.img_perfil);
        btnBorrarCuenta = findViewById(R.id.btn_borrar_cuenta);
        btnVolverMenu = findViewById(R.id.btn_volver_menu);

        // Cargar los datos del usuario desde Firestore
        loadUserProfile();

        btnBorrarCuenta.setOnClickListener(v -> {
            deleteUserAccount();
        });

        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MenuActivity.class);
            intent.putExtra("USER_ID", userId); // Pasar el ID del usuario al menú
            startActivity(intent);
            finish();
        });
    }

    // Método para cargar el perfil del usuario desde Firestore
    private void loadUserProfile() {
        db.collection("usuarios")
                .document(userId)  // Obtener el documento del usuario usando su ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Usuario usuario = document.toObject(Usuario.class);

                            if (usuario != null) {
                                txtNombre.setText("Nombre: " + usuario.getNombre());
                                txtEmail.setText("Email: " + usuario.getEmail());
                                txtContraseña.setText("Contraseña: " + usuario.getPassword());

                                // Mostrar una imagen aleatoria de perfil
                                int randomImage = getRandomProfileImage();
                                imgPerfil.setImageResource(randomImage);
                            }
                        } else {
                            Toast.makeText(PerfilActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error al cargar los datos del perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteUserAccount() {
        db.collection("usuarios")
                .document(userId)  // Obtener el documento del usuario usando su ID
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PerfilActivity.this, "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PerfilActivity.this, InicioActivity.class));  // Redirigir al login
                    finish();  // Finalizar la actividad actual
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PerfilActivity.this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                });
    }

    private int getRandomProfileImage() {
        Random random = new Random();
        int randomIndex = random.nextInt(3);

        switch (randomIndex) {
            case 0:
                return R.drawable.concha;
            case 1:
                return R.drawable.edu;
            case 2:
                return R.drawable.pepe;
            default:
                return R.drawable.pepe;
        }
    }
}
