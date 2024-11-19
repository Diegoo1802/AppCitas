import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcitasclase.CitasActivity;
import com.example.appcitasclase.ReservasActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;
    private Button btn_logout; // Botón de cierre de sesión
    private TextView txt_bienvenida; // Texto de bienvenida para el usuario
    private FirebaseFirestore db; // Instancia de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        // Inicializamos Firestore
        db = FirebaseFirestore.getInstance();

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
            // Verificar si el usuario está registrado antes de acceder a la actividad de Citas
            verificarUsuarioYAcceder("CitasActivity");
        });

        btn_reservas.setOnClickListener(v -> {
            // Verificar si el usuario está registrado antes de acceder a la actividad de Reservas
            verificarUsuarioYAcceder("ReservasActivity");
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

    // Método para verificar si el usuario está registrado en Firestore antes de acceder a una actividad
    private void verificarUsuarioYAcceder(String activityName) {
        // Supongamos que tienes un campo 'userId' que almacena el ID del usuario
        String userId = "someUserId";  // Aquí es donde debes obtener el ID de usuario de alguna manera

        if (userId != null && !userId.isEmpty()) {
            // Verificar si el usuario existe en Firestore
            db.collection("usuarios").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Si el usuario existe, podemos permitir el acceso
                            if (activityName.equals("CitasActivity")) {
                                Intent intent = new Intent(MenuActivity.this, CitasActivity.class);
                                startActivity(intent);
                            } else if (activityName.equals("ReservasActivity")) {
                                Intent intent = new Intent(MenuActivity.this, ReservasActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // Si no existe el usuario, muestra un mensaje de error
                            Toast.makeText(MenuActivity.this, "Usuario no encontrado. Por favor, regístrate.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MenuActivity.this, "Error al verificar el usuario", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(MenuActivity.this, "Por favor, inicia sesión primero.", Toast.LENGTH_SHORT).show();
        }
    }
}
