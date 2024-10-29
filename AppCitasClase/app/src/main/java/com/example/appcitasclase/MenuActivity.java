package com.example.appcitasclase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btn_perfil;
    private Button btn_citas;
    private Button btn_reservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        btn_perfil = findViewById(R.id.btn_perfil);
        btn_citas = findViewById(R.id.btn_citas);
        btn_reservas = findViewById(R.id.btn_reservas);



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
    }
}
