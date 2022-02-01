/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dipolo.vendedores.R;
import com.dipolo.vendedores.constructores.Empresa;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class EmpresaAdapter extends FirestoreNormalAdapter<EmpresaAdapter.ViewHolder> {

    private final OnEmpresaSelectedListener mListener;

    public EmpresaAdapter(Query query, OnEmpresaSelectedListener listener) {
        super(query);

        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_empresa, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnEmpresaSelectedListener {

        void onEmpresaSelectedListener(String idEmpresa, Empresa empresa);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nombre, telefono;
        CardView item;


        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrempresa);
            telefono = itemView.findViewById(R.id.telefonocontacto);
            item = itemView.findViewById(R.id.item);

        }

        @SuppressLint("SetTextI18n,SimpleDateFormat")
        void bind(final DocumentSnapshot snapshot,
                  final OnEmpresaSelectedListener listener) {

            final Empresa empresa = snapshot.toObject(Empresa.class);

            nombre.setText(empresa.getDatosEmpresa().get("nombre").toString());
            telefono.setText(empresa.getDatosEmpresa().get("telefonoContacto").toString());



            item.setOnClickListener(v -> {
                if (listener != null) {

                    listener.onEmpresaSelectedListener(snapshot.getId(), empresa);

                }
            });

        }

    }


}
