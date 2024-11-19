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
        // Crear un MaterialDatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar Fecha")
                .build();

        // Mostrar el date picker
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        // Añadir listener cuando se elija una fecha
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Convertir el Long timestamp a fecha y asignar a fechaSeleccionada
            fechaSeleccionada = datePicker.getHeaderText();
            // Actualizar el resumen
            actualizarResumen();
        });
    }

    // Método para seleccionar la hora
    private void selectTime() {
        // Crear un MaterialTimePicker
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Seleccionar Hora")
                .setHour(8)
                .setMinute(0)
                .build();

        // Mostrar el time picker
        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");

        // Añadir listener cuando se elija la hora
        timePicker.addOnPositiveButtonClickListener(view -> {
            // Obtener la hora y los minutos seleccionados
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Validar que la hora esté entre las 8:00 y las 22:00
            if (hour < 8 || hour > 21) {
                Toast.makeText(this, "Solo puedes seleccionar horas entre las 8:00 y las 22:00", Toast.LENGTH_SHORT).show();
                return;
            }

            // Formatear la hora seleccionada
            horaSeleccionada = String.format("%02d:%02d", hour, minute);
            // Actualizar el resumen
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

        // Crear una referencia a la colección de reservas
        db.collection("reservas")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Reserva", "Reserva guardada con éxito");
                    Toast.makeText(this, "Reserva confirmada con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a la página de citas
                    // Intent intent = new Intent(ReservasActivity.this, CitasActivity.class);
                    // startActivity(intent);
                    finish();  // Cerrar la pantalla de reservas
                })
                .addOnFailureListener(e -> {
                    Log.e("Reserva", "Error al guardar la reserva", e);
                    Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                });
    }
}
