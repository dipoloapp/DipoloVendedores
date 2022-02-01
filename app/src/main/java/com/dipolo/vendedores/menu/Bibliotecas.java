package com.dipolo.vendedores.menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dipolo.vendedores.R;
import com.dipolo.vendedores.adapters.BibliotecaAdapter;
import com.dipolo.vendedores.constructores.Biblioteca;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Bibliotecas extends AppCompatActivity implements
        BibliotecaAdapter.OnBibliotecaSelectedListener {

    BibliotecaAdapter mAdapter;
    private RecyclerView BibliotecaRecycler;
    private Query mQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);
        MenuInferior();

        BibliotecaRecycler = findViewById(R.id.recyclerview);

        QueryFiltros();
        initRecyclerView();

    }


    public void QueryFiltros() {

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("General").document("sistema").collection("Biblioteca");

    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("", "No query, not initializing RecyclerView");
        }

        // Show a snackbar on errors
        mAdapter = new BibliotecaAdapter(mQuery, this, Bibliotecas.this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    BibliotecaRecycler.setVisibility(View.GONE);

                } else {
                    BibliotecaRecycler.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        BibliotecaRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        BibliotecaRecycler.setAdapter(mAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }




    @Override
    public void onBibliotecaSelectedListener(String idDocument, Biblioteca biblioteca) {



    }



    public void MenuInferior() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.biblioteca);
        Drawable drawable2 = bottomNavigationView.getMenu().getItem(2).getIcon();
        drawable2.mutate();
        drawable2.setColorFilter(Color.parseColor("#2a78ff"), PorterDuff.Mode.SRC_IN);
        bottomNavigationView.getMenu().getItem(2).setIcon(drawable2);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Bibliotecas.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    Intent inicio = new Intent(Bibliotecas.this, Inicio.class);
                    startActivity(inicio,bundle);
                    break;
                case R.id.llamadas:
                    Intent llamadas = new Intent(Bibliotecas.this, Llamadas.class);
                    startActivity(llamadas,bundle);
                    break;
                case R.id.biblioteca:

                    break;
                case R.id.perfil:
                    Intent perfil = new Intent(Bibliotecas.this, Perfil.class);
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