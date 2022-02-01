package com.dipolo.vendedores.llamadas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dipolo.vendedores.Metodos;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.menu.Llamadas;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NuevoRegistro extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Spinner seleccionaEmpresa;
    SharedPreferences DatosUsuario;

    LinearLayout registroenviado;
    EditText detallellamada;

    Date fechaLlamada;
    ProgressDialog carga;

    Metodos metodos = new Metodos();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_registro);

        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);

        seleccionaEmpresa = findViewById(R.id.spinner);
        detallellamada = findViewById(R.id.detalle_llamada);
        registroenviado = findViewById(R.id.registroenviado);

        cargaInformacion();

    }

    public void cargaInformacion() {

        db.collection("Vendedores").document(DatosUsuario.getString("idVendedor", "")).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();

                ArrayList<String> empresasD = new ArrayList<>();
                empresasD.addAll((ArrayList<String>) documentSnapshot.get("empresasDipolizadas"));
                Collections.sort(empresasD, String.CASE_INSENSITIVE_ORDER);
                empresasD.add(0, "Selecciona la empresa...");


                ArrayAdapter<String> adapter = new ArrayAdapter<>(NuevoRegistro.this,
                        android.R.layout.simple_spinner_item, empresasD);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                seleccionaEmpresa.setAdapter(adapter);

            }
        });

    }


    public void seleccionarFecha(View v) {

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {

            Button fecha = findViewById(R.id.fecha);
            fecha.setText(dayOfMonth + "-" + monthOfYear + "-" + year);

            Calendar fechaRegistro = Calendar.getInstance();
            fechaRegistro.set(year,monthOfYear,dayOfMonth,0,0,0);
            fechaLlamada = fechaRegistro.getTime();

        }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setLocale(Locale.getDefault());
        datePickerDialog.setOkText("Listo");


        // Setting Min Date
        Calendar min_date_c = Calendar.getInstance();
        min_date_c.set(Calendar.DAY_OF_MONTH, min_date_c.get(Calendar.DAY_OF_MONTH)-30);
        datePickerDialog.setMinDate(min_date_c);

        datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");

    }

    public void subirRegistro(View v) {

        if (metodos.ComprobarCamposRegistro(seleccionaEmpresa.getSelectedItem().toString(), fechaLlamada, detallellamada.getText().toString() )) {
            carga = ProgressDialog.show(NuevoRegistro.this, "Espera",
                    "Estamos subiendo tu registro...", true);

            v.setEnabled(false);
            HashMap<String, Object> mapRegistro = new HashMap<>();
            mapRegistro.put("resumen", detallellamada.getText().toString());
            mapRegistro.put("dia", fechaLlamada);
            mapRegistro.put("idVendedor", DatosUsuario.getString("idVendedor", ""));
            String[] seleccionado = seleccionaEmpresa.getSelectedItem().toString().split(" / ");


            db.collection("Empresas").document(seleccionado[1]).collection("HistorialLlamadas").add(mapRegistro).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    carga.dismiss();
                    v.setEnabled(true);
                    registroenviado.setVisibility(View.VISIBLE);
                } else {
                    carga.dismiss();
                    v.setEnabled(false);
                    Toast.makeText(NuevoRegistro.this, "Registro fallido, intenta denuevo", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(NuevoRegistro.this, "Registro fallido, revisa haber llenado todos los datos", Toast.LENGTH_SHORT).show();
        }

    }

    public void listoVolver(View v) {
        Intent intent = new Intent(NuevoRegistro.this, Llamadas.class);
        startActivity(intent);
    }

}