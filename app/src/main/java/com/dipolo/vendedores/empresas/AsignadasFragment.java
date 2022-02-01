/*
 * Copyright (c) 2021. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.empresas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dipolo.vendedores.R;
import com.dipolo.vendedores.adapters.EmpresaAdapter;
import com.dipolo.vendedores.constructores.Empresa;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class AsignadasFragment extends Fragment implements
        EmpresaAdapter.OnEmpresaSelectedListener {

    EmpresaAdapter mAdapter;
    private RecyclerView EmpresasRecycler;
    private Query mQuery;
    SharedPreferences DatosUsuario;


    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;

    public AsignadasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.asignadas_vista, container, false);
        DatosUsuario = getContext().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        EmpresasRecycler = view.findViewById(R.id.recyclerview);


        QueryFiltros();
        initRecyclerView();


        return view;
    }

    public void QueryFiltros() {

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Empresas")
                .whereEqualTo("datosEmpresa.idVendedor", DatosUsuario.getString("idVendedor",""))
                .whereEqualTo("datosEmpresa.usuario", null);

    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("", "No query, not initializing RecyclerView");
        }

        // Show a snackbar on errors
        mAdapter = new EmpresaAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    EmpresasRecycler.setVisibility(View.GONE);

                } else {
                    EmpresasRecycler.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(container.findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        EmpresasRecycler.setLayoutManager(layoutManager);
        EmpresasRecycler.setAdapter(mAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }


    @Override
    public void onEmpresaSelectedListener(String idEmpresa, Empresa empresa) {
        Intent intent = new Intent(inflater.getContext(), InteriorEmpresa.class);
        intent.putExtra("idEmpresa", idEmpresa);
        intent.putExtra("nombre", empresa.getDatosEmpresa().get("nombre")+"");
        intent.putExtra("telefono", empresa.getDatosEmpresa().get("telefonoContacto")+"");
        intent.putExtra("correo", empresa.getDatosEmpresa().get("mailContacto")+"");
        intent.putExtra("origen", empresa.getDatosEmpresa().get("origen")+"");
        startActivity(intent);
    }

}
