package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ReservasActivity extends AppCompatActivity {

    private Spinner spinnerMasajes;
    private Button btnFecha, btnHora, btnConfirmar, btnVolverMenu;
    private TextView txtResumen;
    private String fechaSeleccionada, horaSeleccionada, masajeSeleccionado;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_activity);

        // Inicializar elementos de la vista
        spinnerMasajes = findViewById(R.id.spinner_masajes);
        btnFecha = findViewById(R.id.btn_fecha);
        btnHora = findViewById(R.id.btn_hora);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        btnVolverMenu = findViewById(R.id.btn_volver_menu);
        txtResumen = findViewById(R.id.txt_resumen);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el userId desde el intent
        userId = getIntent().getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Usuario no encontrado. Por favor, vuelve a iniciar sesión.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar el Spinner de masajes
        setupSpinner();

        // Configurar botones
        btnFecha.setOnClickListener(v -> selectDate());
        btnHora.setOnClickListener(v -> selectTime());
        btnConfirmar.setOnClickListener(v -> confirmarReserva(userId));

        // Configurar botón "Volver al menú"
        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ReservasActivity.this, MenuActivity.class);
            intent.putExtra("USER_ID", userId); // Pasar el USER_ID al menú
            startActivity(intent);
            finish();
        });
    }

    private void setupSpinner() {
        String[] masajes = {"Relajante", "Descontracturante", "Piedras calientes", "Aromaterapia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, masajes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMasajes.setAdapter(adapter);
    }

    private void selectDate() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar Fecha")
                .build();

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            fechaSeleccionada = datePicker.getHeaderText();
            actualizarResumen();
        });
    }

    private void selectTime() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Seleccionar Hora")
                .setHour(8)
                .setMinute(0)
                .build();

        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
        timePicker.addOnPositiveButtonClickListener(view -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (hour < 8 || hour > 21) {
                Toast.makeText(this, "Solo puedes seleccionar horas entre las 8:00 y las 22:00", Toast.LENGTH_SHORT).show();
                return;
            }
            horaSeleccionada = String.format("%02d:%02d", hour, minute);
            actualizarResumen();
        });
    }

    private void actualizarResumen() {
        masajeSeleccionado = spinnerMasajes.getSelectedItem().toString();
        txtResumen.setText(String.format("Masaje: %s\nFecha: %s\nHora: %s", masajeSeleccionado, fechaSeleccionada, horaSeleccionada));
    }

    private void confirmarReserva(String userId) {
        if (fechaSeleccionada == null || horaSeleccionada == null || masajeSeleccionado == null) {
            Toast.makeText(this, "Por favor, selecciona todos los detalles de la reserva.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> reserva = new HashMap<>();
        reserva.put("masaje", masajeSeleccionado);
        reserva.put("fecha", fechaSeleccionada);
        reserva.put("hora", horaSeleccionada);
        reserva.put("usuario", userId);

        db.collection("reservas")
                .document(userId)
                .collection("reservasUsuario")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Reserva confirmada con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("ReservasActivity", "Error al guardar la reserva", e);
                    Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                });
    }
}
