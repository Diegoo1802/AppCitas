package com.example.appcitasclase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ReservasActivity extends AppCompatActivity {

    private Spinner spinnerMasajes;
    private Button btnFecha, btnHora, btnConfirmar;
    private TextView txtResumen;
    private String fechaSeleccionada, horaSeleccionada, masajeSeleccionado;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_activity);

        // Inicializamos los elementos de la vista
        spinnerMasajes = findViewById(R.id.spinner_masajes);
        btnFecha = findViewById(R.id.btn_fecha);
        btnHora = findViewById(R.id.btn_hora);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        txtResumen = findViewById(R.id.txt_resumen);

        // Inicializamos FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Verificar que el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Si no hay usuario autenticado, redirigir a la pantalla de login o mostrar mensaje
            Toast.makeText(this, "Por favor, inicia sesión", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad si el usuario no está autenticado
        } else {
            Log.d("Reserva", "Usuario autenticado: " + currentUser.getUid());
        }

        // Configuramos el Spinner de masajes
        setupSpinner();

        // Configuramos los listeners para los botones
        btnFecha.setOnClickListener(v -> selectDate());
        btnHora.setOnClickListener(v -> selectTime());

        // El OnClickListener para el botón de confirmar
        btnConfirmar.setOnClickListener(v -> confirmarReserva());
    }

    // Configura el Spinner de masajes
    private void setupSpinner() {
        String[] masajes = {"Relajante", "Descontracturante", "Piedras calientes", "Aromaterapia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, masajes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMasajes.setAdapter(adapter);
    }

    // Método para seleccionar la fecha
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

    // Método para seleccionar la hora
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

    // Actualiza el resumen con los datos seleccionados
    private void actualizarResumen() {
        masajeSeleccionado = spinnerMasajes.getSelectedItem().toString();
        txtResumen.setText(String.format("Masaje: %s\nFecha: %s\nHora: %s", masajeSeleccionado, fechaSeleccionada, horaSeleccionada));
    }

    // Método para confirmar la reserva
    public void confirmarReserva() {
        // Verificar que se hayan seleccionado todos los campos
        if (fechaSeleccionada == null || horaSeleccionada == null || masajeSeleccionado == null) {
            Toast.makeText(this, "Por favor, selecciona todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No debería llegar a este punto si el usuario está autenticado, pero se incluye como control
            Toast.makeText(this, "Por favor, inicia sesión", Toast.LENGTH_SHORT).show();
            return;  // No continuar si no hay usuario autenticado
        }

        String userId = currentUser.getUid();
        Log.d("Reserva", "Usuario autenticado: " + userId);

        // Crear un HashMap con los datos de la reserva
        HashMap<String, Object> reserva = new HashMap<>();
        reserva.put("masaje", masajeSeleccionado);
        reserva.put("fecha", fechaSeleccionada);
        reserva.put("hora", horaSeleccionada);
        reserva.put("usuario", userId);

        // Guardar la reserva en Firestore
        db.collection("reservas")
                .document(userId)  // Usamos el UID del usuario para almacenar las reservas bajo su nombre
                .collection("reservasUsuario")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Reserva", "Reserva guardada con éxito");
                    Toast.makeText(this, "Reserva confirmada con éxito", Toast.LENGTH_SHORT).show();
                    finish();  // Cerrar la pantalla de reservas
                })
                .addOnFailureListener(e -> {
                    Log.e("Reserva", "Error al guardar la reserva", e);
                    Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                });
    }
}
