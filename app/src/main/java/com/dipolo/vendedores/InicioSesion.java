package com.dipolo.vendedores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.dipolo.vendedores.menu.Inicio;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class InicioSesion extends AppCompatActivity {

    EditText rut, pass;
    ProgressDialog progress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences DatosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        rut = findViewById(R.id.user_rut);
        pass = findViewById(R.id.user_password);


    }

    public void iniciarSesion(View view) {
        view.setEnabled(false);
        progress = ProgressDialog.show(InicioSesion.this, "Espera..",
                "Estamos buscando tu usuario", true);

        Query query = db.collection("Vendedores")
                .whereEqualTo("rut", rut.getText().toString())
                .whereEqualTo("clave", pass.getText().toString())
                .limit(1);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult().getDocuments().size() > 0){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(InicioSesion.this,
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        SharedPreferences.Editor editor = DatosUsuario.edit();
                        editor.putString("nombre", documentSnapshot.getString("nombre"));
                        editor.putString("correo", documentSnapshot.getString("correo"));
                        editor.putString("telefono", documentSnapshot.getString("telefono"));
                        editor.putString("rut", documentSnapshot.getString("rut"));
                        editor.putString("idVendedor", documentSnapshot.getId());
                        editor.apply();
                        progress.dismiss();

                        Intent intent = new Intent(InicioSesion.this, Inicio.class);
                        startActivity(intent,bundle);

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(this, "El usuario no existe o tus accesos estan incorrectos", Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                }
            }
        });




    }




}