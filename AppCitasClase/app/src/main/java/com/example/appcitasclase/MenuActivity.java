package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;
    private Button btn_logout; // Botón de cierre de sesión
    private TextView txt_bienvenida; // Texto de bienvenida para el usuario

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        // Inicializar FirebaseAuth y DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Verificar si el usuario está autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Si no hay usuario autenticado, redirigir a la pantalla de inicio de sesión
            Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
            startActivity(intent);
            finish(); // Cerrar esta actividad
            return;
        }

        // Inicializar los elementos de la interfaz
        txt_bienvenida = findViewById(R.id.txt_bienvenida); // Asegúrate de tener este TextView en el XML
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_citas = findViewById(R.id.btn_citas);
        btn_reservas = findViewById(R.id.btn_reservas);
        btn_logout = findViewById(R.id.btn_logout);

        // Configurar los listeners para los botones
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btn_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CitasActivity.class);
                startActivity(intent);
            }
        });

        btn_reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ReservasActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el listener para el botón de cierre de sesión
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión del usuario
                mAuth.signOut();
                Toast.makeText(MenuActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                // Redirigir al inicio de sesión
                Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
                startActivity(intent);
                finish(); // Cerrar la actividad actual
            }
        });

        // Recuperar los datos del usuario de Firebase Realtime Database
        if (user != null) {
            retrieveUserData(user.getUid());
        }
    }

    // Método para recuperar los datos del usuario desde Firebase Database
    private void retrieveUserData(String userId) {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    txt_bienvenida.setText("¡Bienvenido, " + nombre + "!");
                } else {
                    Toast.makeText(MenuActivity.this, "Datos no encontrados", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MenuActivity.this, "Error al recuperar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
