/*
 * Copyright (c) 2022. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SinConexion extends AppCompatActivity {

    Metodos metodos = new Metodos();
    LottieAnimationView sincon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_conexion);
        sincon = findViewById(R.id.sincon);
    }

    public void Reintentar (View view) {
        sincon.playAnimation();
        if (metodos.revisarConexionInternetBoolean(SinConexion.this)) {
            finish();
        }
    }

}