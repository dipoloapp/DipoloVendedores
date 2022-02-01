/*
 * Copyright (c) 2021. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;

import com.dipolo.vendedores.InicioSesion;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.adapters.EmpresasAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class Inicio extends AppCompatActivity {

    ViewPager vistaEmpresas;
    TabLayout tabLayout;
    TextView saludo;
    SharedPreferences DatosUsuario;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        MenuInferior();

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        usuarioExiste();

        saludo = findViewById(R.id.saludo);
        tabLayout = findViewById(R.id.tabla);
        vistaEmpresas = findViewById(R.id.viewPager);


        saludo.setText("Hola "+DatosUsuario.getString("nombre", "")+"!");

        final EmpresasAdapter adapter = new EmpresasAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        vistaEmpresas.setAdapter(adapter);

        vistaEmpresas.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vistaEmpresas.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    public void MenuInferior() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.inicio);
        Drawable drawable2 = bottomNavigationView.getMenu().getItem(0).getIcon();
        drawable2.mutate();
        drawable2.setColorFilter(Color.parseColor("#2a78ff"), PorterDuff.Mode.SRC_IN);
        bottomNavigationView.getMenu().getItem(0).setIcon(drawable2);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Inicio.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:

                    break;
                case R.id.llamadas:
                    Intent llamadas = new Intent(Inicio.this, Llamadas.class);
                    startActivity(llamadas,bundle);
                    break;
                case R.id.biblioteca:
                    Intent biblioteca = new Intent(Inicio.this, Bibliotecas.class);
                    startActivity(biblioteca,bundle);
                    break;
                case R.id.perfil:
                    Intent perfil = new Intent(Inicio.this, Perfil.class);
                    startActivity(perfil,bundle);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    public void usuarioExiste() {
        if (DatosUsuario.getString("idVendedor", "").equals("")) {
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Inicio.this,
                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            Intent intent = new Intent(Inicio.this, InicioSesion.class);
            startActivity(intent,bundle);
        }
    }


}