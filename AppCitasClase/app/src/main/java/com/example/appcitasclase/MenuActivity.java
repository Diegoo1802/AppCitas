package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;
    private Button btn_logout; // Botón de cierre de sesión

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Si no hay usuario autenticado, redirigir a la pantalla de inicio de sesión
            Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
            startActivity(intent);
            finish(); // Cerrar esta actividad
        }

        // Inicializar los botones
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_citas = findViewById(R.id.btn_citas);
        btn_reservas = findViewById(R.id.btn_reservas);
        btn_logout = findViewById(R.id.btn_logout); // Suponiendo que agregarás un botón para cerrar sesión

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
    }
}
