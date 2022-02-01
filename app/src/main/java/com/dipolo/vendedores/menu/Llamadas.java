package com.dipolo.vendedores.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dipolo.vendedores.R;
import com.dipolo.vendedores.adapters.LlamadasAdapter;
import com.dipolo.vendedores.constructores.Llamada;
import com.dipolo.vendedores.llamadas.NuevoRegistro;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Llamadas extends AppCompatActivity implements
        LlamadasAdapter.OnLlamadasSelectedListener {

    LlamadasAdapter mAdapter;
    private RecyclerView LlamadasRecycler;
    private Query mQuery;
    SharedPreferences DatosUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamadas);
        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        LlamadasRecycler = findViewById(R.id.recyclerview);

        QueryFiltros();
        initRecyclerView();
        MenuInferior();

    }


    public void QueryFiltros() {

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collectionGroup("HistorialLlamadas")
                .whereEqualTo("idVendedor", DatosUsuario.getString("idVendedor", ""));

    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("", "No query, not initializing RecyclerView");
        }

        // Show a snackbar on errors
        mAdapter = new LlamadasAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    LlamadasRecycler.setVisibility(View.GONE);

                } else {
                    LlamadasRecycler.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LlamadasRecycler.setLayoutManager(layoutManager);
        LlamadasRecycler.setAdapter(mAdapter);

    }

    public void nuevoRegistro(View v) {
        Intent intent = new Intent(Llamadas.this, NuevoRegistro.class);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onLlamadasSelectedListener(String idDocument, Llamada llamada) {
        /*Intent intent = new Intent(Llamadas.this, InteriorLlamada.class);
        intent.putExtra("id", idDocument);
        intent.putExtra("resumen", llamada.getResumen());
        intent.putExtra("diaRegistro", llamada.getDiaRegistro().toString()+"");
        startActivity(intent);*/

        new AlertDialog.Builder(this)
                .setTitle("Resumen de llamada")
                .setMessage(llamada.getResumen())
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
        
    }

    public void MenuInferior() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.llamadas);
        Drawable drawable2 = bottomNavigationView.getMenu().getItem(1).getIcon();
        drawable2.mutate();
        drawable2.setColorFilter(Color.parseColor("#2a78ff"), PorterDuff.Mode.SRC_IN);
        bottomNavigationView.getMenu().getItem(1).setIcon(drawable2);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Llamadas.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    Intent inicio = new Intent(Llamadas.this, Inicio.class);
                    startActivity(inicio,bundle);
                    break;
                case R.id.llamadas:
                    break;
                case R.id.biblioteca:
                    Intent biblioteca = new Intent(Llamadas.this, Bibliotecas.class);
                    startActivity(biblioteca,bundle);
                    break;
                case R.id.perfil:
                    Intent perfil = new Intent(Llamadas.this, Perfil.class);
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
}