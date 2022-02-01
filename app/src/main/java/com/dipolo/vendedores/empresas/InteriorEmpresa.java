package com.dipolo.vendedores.empresas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.dipolo.vendedores.R;
import com.dipolo.vendedores.menu.Inicio;
import com.google.firebase.firestore.FirebaseFirestore;

public class InteriorEmpresa extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView nombre, telefono, correo, origen;
    CardView cardtel, cardcorr;
    String idEmpresa;
    Bundle extras;
    SharedPreferences DatosUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_empresa);

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        nombre = findViewById(R.id.nombre_empresa);
        telefono = findViewById(R.id.telefono_empresa);
        correo = findViewById(R.id.correo_empresa);
        origen = findViewById(R.id.origen_empresa);
        cardtel = findViewById(R.id.card_llamar);
        cardcorr = findViewById(R.id.card_correo);

        extras = getIntent().getExtras();

        cargaInformacion();
    }

    public void cargaInformacion() {

        if (extras != null) {

            idEmpresa = extras.getString("idEmpresa");
            nombre.setText(extras.getString("nombre"));
            origen.setText("Viene desde "+extras.getString("origen"));

            if (!extras.getString("telefono").equals("")) {
                telefono.setText(extras.getString("telefono"));
            }else {
                cardtel.setVisibility(View.GONE);
            }

            if (!extras.getString("correo").equals("")) {
                correo.setText(extras.getString("correo"));
            }else {
                cardcorr.setVisibility(View.GONE);
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


    public void dipolizarEmpresa(View v) {
        Intent intent = new Intent(InteriorEmpresa.this, FormularioDipolizar.class);
        intent.putExtra("idEmpresa", idEmpresa);
        if (!extras.getString("telefono").equals("")) {
            intent.putExtra("telefono", extras.getString("telefono"));
        }
        if (!extras.getString("correo").equals("")) {
            intent.putExtra("correo", extras.getString("correo"));
        }
        intent.putExtra("nombre", extras.getString("nombre"));
        startActivity(intent);
    }

    public void volver(View v) {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(InteriorEmpresa.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        Intent intent = new Intent(InteriorEmpresa.this, Inicio.class);
        startActivity(intent,bundle);
    }


}