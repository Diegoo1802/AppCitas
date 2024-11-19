package com.example.appcitasclase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CitasActivity extends AppCompatActivity {

    private LinearLayout citasLayout;
    private Button btnVolverMenu;
    private FirebaseFirestore db;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas_activity);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario desde el Intent
        userId = getIntent().getStringExtra("USER_ID");

        // Inicializar vistas
        citasLayout = findViewById(R.id.layout_citas);
        btnVolverMenu = findViewById(R.id.btn_volver_menu);

        // Cargar las citas
        loadCitas();

        // Configurar el botón para volver al menú
        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(CitasActivity.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Finalizamos esta actividad
        });
    }

    // Método para cargar las citas del usuario desde Firestore
    private void loadCitas() {
        db.collection("citas")
                .whereEqualTo("userId", userId) // Filtrar por el ID del usuario
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cita cita = document.toObject(Cita.class);
                            cita.setId(document.getId());  // Asignar el ID del documento de Firestore

                            // Crear un TextView para cada cita y añadir un botón para eliminarla
                            addCitaToLayout(cita);
                        }
                    } else {
                        Toast.makeText(CitasActivity.this, "Error al cargar las citas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para añadir una cita al layout y mostrar botones de eliminar
    private void addCitaToLayout(Cita cita) {
        // Crear un contenedor para cada cita
        LinearLayout citaItemLayout = new LinearLayout(this);
        citaItemLayout.setOrientation(LinearLayout.VERTICAL);
        citaItemLayout.setPadding(16, 16, 16, 16);

        // Crear un TextView con la información de la cita
        TextView txtCita = new TextView(this);
        txtCita.setText("Fecha: " + cita.getFecha() + "\nHora: " + cita.getHora() + "\nTipo: " + cita.getTipoMasaje());
        txtCita.setTextSize(16);

        // Crear el botón para eliminar la cita
        Button btnEliminarCita = new Button(this);
        btnEliminarCita.setText("Eliminar Cita");
        btnEliminarCita.setOnClickListener(v -> {
            eliminarCita(cita);
        });

        // Añadir los elementos al layout de la cita
        citaItemLayout.addView(txtCita);
        citaItemLayout.addView(btnEliminarCita);

        // Añadir la cita al layout principal
        citasLayout.addView(citaItemLayout);
    }

    // Método para eliminar una cita de Firestore
    private void eliminarCita(Cita cita) {
        db.collection("citas").document(cita.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CitasActivity.this, "Cita eliminada correctamente", Toast.LENGTH_SHORT).show();
                    // Volver a cargar las citas después de eliminar
                    citasLayout.removeAllViews();  // Limpiar las vistas actuales
                    loadCitas();  // Recargar las citas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CitasActivity.this, "Error al eliminar la cita", Toast.LENGTH_SHORT).show();
                });
    }
}
