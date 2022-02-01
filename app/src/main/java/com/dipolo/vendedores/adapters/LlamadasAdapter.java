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

import com.dipolo.vendedores.Metodos;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.constructores.Llamada;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class LlamadasAdapter extends FirestoreNormalAdapter<LlamadasAdapter.ViewHolder> {

    private final OnLlamadasSelectedListener mListener;

    public LlamadasAdapter(Query query, OnLlamadasSelectedListener listener) {
        super(query);

        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_llamada, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnLlamadasSelectedListener {

        void onLlamadasSelectedListener(String idDocument, Llamada llamada);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nombre,fecha;
        CardView item;


        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrempresa);
            fecha = itemView.findViewById(R.id.fecha);
            item = itemView.findViewById(R.id.item);

        }

        @SuppressLint("SetTextI18n,SimpleDateFormat")
        void bind(final DocumentSnapshot snapshot,
                  final OnLlamadasSelectedListener listener) {

            final Llamada llamada = snapshot.toObject(Llamada.class);
            Metodos metodos = new Metodos();

            String[] path = snapshot.getReference().getPath().split("/");
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Empresas").document(path[1]).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    nombre.setText(task.getResult().get("datosEmpresa.nombre")+"");
                }
            });


            fecha.setText(metodos.formatearFecha(llamada.getDiaRegistro()));



            item.setOnClickListener(v -> {
                if (listener != null) {

                    listener.onLlamadasSelectedListener(snapshot.getId(), llamada);

                }
            });

        }

    }


}
