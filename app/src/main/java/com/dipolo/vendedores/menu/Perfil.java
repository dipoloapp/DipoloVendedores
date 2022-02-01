package com.dipolo.vendedores.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import com.dipolo.vendedores.Metodos;
import com.dipolo.vendedores.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Perfil extends AppCompatActivity {

    TextView saludo;
    EditText correo,telefono;
    ImageView editarcorreo, guardarcorreo, editartelefono, guardartelefono;
    CardView cv_correo, cv_telefono;
    Metodos metodos = new Metodos();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences DatosUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MenuInferior();

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        correo = findViewById(R.id.user_correo);
        telefono = findViewById(R.id.user_telefono);
        saludo = findViewById(R.id.saludo);

        correo.setText(DatosUsuario.getString("correo", ""));
        telefono.setText(DatosUsuario.getString("telefono", ""));
        saludo.setText("Hola "+DatosUsuario.getString("nombre", "")+"!");

        correo.setFocusable(false);
        telefono.setFocusable(false);
        editarcorreo = findViewById(R.id.editarcorreo);
        guardarcorreo = findViewById(R.id.guardarcorreo);
        editartelefono = findViewById(R.id.editartelefono);
        guardartelefono = findViewById(R.id.guardartelefono);

        cv_correo = findViewById(R.id.cardcorreo);
        cv_telefono = findViewById(R.id.cardtelefono);


        editarcorreo.setOnClickListener(v -> {
            editarcorreo.setVisibility(View.GONE);
            guardarcorreo.setVisibility(View.VISIBLE);
            correo.setFocusableInTouchMode(true);
            cv_correo.setBackgroundResource(R.drawable.bordenoseleccionad);
            cv_correo.setElevation(7);
        });

        guardarcorreo.setOnClickListener(v -> {

            guardarcorreo.setVisibility(View.GONE);
            editarcorreo.setVisibility(View.VISIBLE);
            cv_correo.setBackgroundResource(R.drawable.fondoinputsperfil);
            cv_correo.setElevation(0);

            if (metodos.ComprobarCorreo(correo.getText().toString())) {
                if (!correo.getText().toString().equals(DatosUsuario.getString("correo", ""))){
                    db.collection("Vendedores").document(DatosUsuario.getString("idVendedor", "")).update("correo",correo.getText().toString());
                    DatosUsuario.edit().putString("correo", correo.getText().toString()).apply();
                    Toast.makeText(this, "Correo actualizado con éxito!", Toast.LENGTH_SHORT).show();
                }
                correo.setFocusable(false);
            } else {
                Toast.makeText(this, "Comprueba tu correo", Toast.LENGTH_SHORT).show();
            }

        });


        editartelefono.setOnClickListener(v -> {
            editartelefono.setVisibility(View.GONE);
            guardartelefono.setVisibility(View.VISIBLE);
            telefono.setFocusableInTouchMode(true);
            cv_telefono.setBackgroundResource(R.drawable.bordenoseleccionad);
            cv_telefono.setElevation(7);
        });

        guardartelefono.setOnClickListener(v -> {
            guardartelefono.setVisibility(View.GONE);
            editartelefono.setVisibility(View.VISIBLE);
            cv_telefono.setBackgroundResource(R.drawable.fondoinputsperfil);
            cv_telefono.setElevation(0);

            if (metodos.ComprobarTelefono(telefono.getText().toString())) {
                if (!telefono.getText().toString().equals(DatosUsuario.getString("telefono", ""))){
                    db.collection("Vendedores").document(Objects.requireNonNull(DatosUsuario.getString("idVendedor", ""))).update("telefono",telefono.getText().toString());
                    DatosUsuario.edit().putString("telefono", telefono.getText().toString()).apply();
                    Toast.makeText(this, "Teléfono actualizado con éxito!", Toast.LENGTH_SHORT).show();
                }
                telefono.setFocusable(false);
            } else {
                Toast.makeText(this, "Comprueba tu teléfono, recuerda añadir el +56", Toast.LENGTH_SHORT).show();
            }

        });



    }





    @SuppressLint("NonConstantResourceId")
    public void MenuInferior() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.perfil);
        Drawable drawable2 = bottomNavigationView.getMenu().getItem(3).getIcon();
        drawable2.mutate();
        drawable2.setColorFilter(Color.parseColor("#2a78ff"), PorterDuff.Mode.SRC_IN);
        bottomNavigationView.getMenu().getItem(3).setIcon(drawable2);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Perfil.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    Intent inicio = new Intent(Perfil.this, Inicio.class);
                    startActivity(inicio,bundle);
                    break;
                case R.id.llamadas:
                    Intent llamadas = new Intent(Perfil.this, Llamadas.class);
                    startActivity(llamadas,bundle);
                    break;
                case R.id.biblioteca:
                    Intent biblioteca = new Intent(Perfil.this, Bibliotecas.class);
                    startActivity(biblioteca,bundle);
                    break;
                case R.id.perfil:
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}