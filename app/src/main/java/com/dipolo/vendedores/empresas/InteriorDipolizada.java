package com.dipolo.vendedores.empresas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dipolo.vendedores.Metodos;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.adapters.LlamadasAdapter;
import com.dipolo.vendedores.constructores.Llamada;
import com.dipolo.vendedores.llamadas.NuevoRegistro;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class InteriorDipolizada extends AppCompatActivity implements
        LlamadasAdapter.OnLlamadasSelectedListener {

    Bundle extras;
    SharedPreferences DatosUsuario;
    String idEmpresa;
    TextView tx_plan, tx_nombre;
    ImageView logo;
    LinearLayout onboarding;

    LlamadasAdapter mAdapter;
    private RecyclerView LlamadasRecycler;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_dipolizada);

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        extras = getIntent().getExtras();

        LlamadasRecycler = findViewById(R.id.recyclerview);

        tx_plan = findViewById(R.id.plan_empresa);
        tx_nombre = findViewById(R.id.nombre_empresa);
        logo = findViewById(R.id.logo_empresa);
        onboarding = findViewById(R.id.onboardingalert);


        cargaInformacion();
        cargaDB();

    }


    public void cargaInformacion() {
    Log.d("tes","");
        if (extras != null) {

            idEmpresa = extras.getString("idEmpresa");

            tx_plan.setText("Plan "+extras.getString("plan"));
            tx_nombre.setText(extras.getString("nombre"));

            Glide.with(InteriorDipolizada.this)
                    .load(Metodos.logoCurrentEmpresa)
                    .into(logo);

            if (!extras.getBoolean("onboarding")) {
                //Si no ha completado el onboarding
                onboarding.setVisibility(View.VISIBLE);
            }

        }


    }



    public void llamarEmpresa(View v) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + extras.getString("telefono")));
            startActivity(intent);

        }else{

            ActivityCompat.requestPermissions(this, new
                    String[]{android.Manifest.permission.CALL_PHONE}, 0);

            Toast.makeText(this, "Aún no has dado permisos de llamada, activalos y vuelve a intentarlo", Toast.LENGTH_SHORT).show();
        }


    }

    public void enviarCorreo(View v) {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ extras.getString("correo")});
        email.putExtra(Intent.EXTRA_SUBJECT, "Hola soy "+DatosUsuario.getString("nombre", "")+" de Dipolo!");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));

    }



    private void cargaDB() {

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collectionGroup("HistorialLlamadas")
                .whereEqualTo("idVendedor", DatosUsuario.getString("idVendedor", ""));

        // Show a snackbar on errors
        mAdapter = new LlamadasAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    LlamadasRecycler.setVisibility(View.GONE);
                    ConstraintLayout nada = findViewById(R.id.nadaencarrito);
                    nada.setVisibility(View.VISIBLE);

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
        Intent intent = new Intent(InteriorDipolizada.this, NuevoRegistro.class);
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
        new AlertDialog.Builder(this)
                .setTitle("Resumen de llamada")
                .setMessage(llamada.getResumen())
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    public void volver(View v) {
        finish();
    }

}