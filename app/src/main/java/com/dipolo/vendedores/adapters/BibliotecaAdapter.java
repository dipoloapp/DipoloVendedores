/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.constructores.Biblioteca;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class BibliotecaAdapter extends FirestoreNormalAdapter<BibliotecaAdapter.ViewHolder> {

    private final OnBibliotecaSelectedListener mListener;
    Activity activity;

    public BibliotecaAdapter(Query query, OnBibliotecaSelectedListener listener, Activity act) {
        super(query);

        mListener = listener;
        activity = act;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_biblioteca, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener, activity);
    }

    public interface OnBibliotecaSelectedListener {

        void onBibliotecaSelectedListener(String idDocument, Biblioteca biblioteca);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        Context context;
        final TextView nombre;
        final ImageView img,compartir;
        CardView item;


        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombredocumento);
            img = itemView.findViewById(R.id.imgprev);
            compartir = itemView.findViewById(R.id.compartir);
            item = itemView.findViewById(R.id.item);

        }

        @SuppressLint("SetTextI18n,SimpleDateFormat")
        void bind(final DocumentSnapshot snapshot,
                  final OnBibliotecaSelectedListener listener, Activity activity) {

            final Biblioteca biblioteca = snapshot.toObject(Biblioteca.class);

            Log.d("as", biblioteca.getNombre());

            nombre.setText(biblioteca.getNombre());
            Glide.with(img.getContext())
                    .load(biblioteca.getImg())
                    .into(img);

            compartir.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, biblioteca.getNombre()+" : "+biblioteca.getUrl());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                activity.startActivity(shareIntent);
            });


            item.setOnClickListener(v -> {
                if (listener != null) {

                    //listener.onBibliotecaSelectedListener(snapshot.getId(), biblioteca);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(biblioteca.getUrl()));
                    activity.startActivity(i);

                }
            });

        }

    }


}
