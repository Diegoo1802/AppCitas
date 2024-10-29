package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {

    // Definimos las credenciales predefinidas
    private final String CORRECT_USERNAME = "usuario123";
    private final String CORRECT_PASSWORD = "contraseña123";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btn_inicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_activity);

        // Enlazamos las vistas con sus ID en el layout
        editTextUsername = findViewById(R.id.txt_nombre);
        editTextPassword = findViewById(R.id.txt_pass);
        btn_inicios = findViewById(R.id.btn_inicios);

        // Configuramos el botón de inicio de sesión
        btn_inicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    // Método para validar las credenciales de inicio de sesión
    private void validateLogin() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Comprobamos si las credenciales coinciden con las predefinidas
        if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
            // Inicio de sesión exitoso
            Toast.makeText(InicioActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            // Navegamos a la siguiente pantalla
            Intent intent = new Intent(InicioActivity.this, InicioActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual para que el usuario no regrese con el botón de retroceso
        } else {
            // Credenciales incorrectas
            Toast.makeText(InicioActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
