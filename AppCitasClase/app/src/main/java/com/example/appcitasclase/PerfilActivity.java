package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Random;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNombre, txtEmail, txtContraseña;
    private ImageView imgPerfil;
    private Button btnModificar, btnVolverMenu;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario desde el Intent
        userId = getIntent().getStringExtra("USER_ID");

        // Inicializar vistas
        txtNombre = findViewById(R.id.txt_nombre);
        txtEmail = findViewById(R.id.txt_email);
        txtContraseña = findViewById(R.id.txt_contraseña);
        imgPerfil = findViewById(R.id.img_perfil);
        btnModificar = findViewById(R.id.btn_modificar);
        btnVolverMenu = findViewById(R.id.btn_volver_menu);

        // Cargar los datos del usuario desde Firestore
        loadUserProfile();

        // Configurar el botón de modificar perfil
        btnModificar.setOnClickListener(v -> {
            // Aquí podrías redirigir a una actividad para modificar el perfil (como cambiar la contraseña o la foto)
            Intent intent = new Intent(PerfilActivity.this, PerfilActivity.class);
            intent.putExtra("USER_ID", userId); // Pasar el ID del usuario
            startActivity(intent);
        });

        // Configurar el botón para volver al menú
        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual
        });
    }

    // Método para cargar el perfil del usuario desde Firestore
    private void loadUserProfile() {
        db.collection("usuarios")
                .document(userId) // Obtener el documento del usuario usando su ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obtener los datos del usuario
                            Usuario usuario = document.toObject(Usuario.class);

                            // Mostrar la información del usuario
                            if (usuario != null) {
                                txtNombre.setText("Nombre: " + usuario.getNombre());
                                txtEmail.setText("Email: " + usuario.getEmail());
                                txtContraseña.setText("Contraseña: " + usuario.getPassword());

                                // Generar y mostrar una imagen aleatoria de perfil
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
