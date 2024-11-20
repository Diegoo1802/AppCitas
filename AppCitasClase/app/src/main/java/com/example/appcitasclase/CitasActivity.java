package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CitasActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCitas;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas_activity);

        linearLayoutCitas = findViewById(R.id.linearLayoutCitas);

        db = FirebaseFirestore.getInstance();

        // Obtener el userId desde el intent
        userId = getIntent().getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Usuario no encontrado. Por favor, vuelve a iniciar sesión.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Button btnVolverMenu = findViewById(R.id.btn_volver_menu);
        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(CitasActivity.this, MenuActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        });

        // Cargar las citas del usuario
        loadUserCitas(userId);
    }

    //Cargar las citas del usuario desde Firestore
    private void loadUserCitas(String userId) {
        db.collection("reservas")
                .document(userId)
                .collection("reservasUsuario")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.isEmpty()) {
                        showNoCitasMessage();
                    } else {
                        for (DocumentSnapshot document : documents) {
                            displayCita(document);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CitasActivity", "Error al cargar citas", e);
                    Toast.makeText(CitasActivity.this, "Error al cargar citas", Toast.LENGTH_SHORT).show();
                });
    }

    // Muestra un mensaje cuando no hay citas
    private void showNoCitasMessage() {
        TextView noCitasMessage = new TextView(this);
        noCitasMessage.setText("No tienes citas programadas.");
        noCitasMessage.setTextSize(18);
        noCitasMessage.setPadding(16, 16, 16, 16);
        linearLayoutCitas.addView(noCitasMessage);
    }

    // Muestra una cita en la pantalla
    private void displayCita(DocumentSnapshot document) {
        String masaje = document.getString("masaje");
        String fecha = document.getString("fecha");
        String hora = document.getString("hora");

        // Crear un TextView para mostrar la cita
        TextView citaView = new TextView(this);
        citaView.setText(String.format("Masaje: %s\nFecha: %s\nHora: %s", masaje, fecha, hora));
        citaView.setTextSize(16);
        citaView.setPadding(16, 16, 16, 16);
        linearLayoutCitas.addView(citaView);

        Button btnEliminar = new Button(this);
        btnEliminar.setText("Eliminar");
        btnEliminar.setOnClickListener(v -> eliminarCita(document.getId()));
        linearLayoutCitas.addView(btnEliminar);
    }

    // Eliminar una cita
    private void eliminarCita(String citaId) {
        db.collection("reservas")
                .document(userId)
                .collection("reservasUsuario")
                .document(citaId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cita eliminada con éxito", Toast.LENGTH_SHORT).show();
                    recreate(); // Recargar la actividad para actualizar las citas
                })
                .addOnFailureListener(e -> {
                    Log.e("CitasActivity", "Error al eliminar cita", e);
                    Toast.makeText(this, "Error al eliminar cita", Toast.LENGTH_SHORT).show();
                });
    }
}
