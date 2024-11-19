package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;
    private Button btn_logout; // Botón de cierre de sesión
    private TextView txt_bienvenida; // Texto de bienvenida para el usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        // Inicializar los elementos de la interfaz
        txt_bienvenida = findViewById(R.id.txt_bienvenida); // Asegúrate de tener este TextView en el XML
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_citas = findViewById(R.id.btn_citas);
        btn_reservas = findViewById(R.id.btn_reservas);
        btn_logout = findViewById(R.id.btn_logout);

        // Configurar los listeners para los botones
        btn_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        btn_citas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CitasActivity.class);
            startActivity(intent);
        });

        btn_reservas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        // Configurar el listener para el botón de cierre de sesión
        btn_logout.setOnClickListener(v -> {
            // Cerrar sesión del usuario
            Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
            startActivity(intent);
            finish(); // Cerrar la actividad actual
        });

        // Recuperar el nombre del usuario desde el Intent
        String userName = getIntent().getStringExtra("USER_NAME");

        if (userName != null && !userName.isEmpty()) {
            // Mostrar el nombre del usuario en el TextView de bienvenida
            txt_bienvenida.setText("¡Bienvenido, " + userName + "!");
        }
    }
}
