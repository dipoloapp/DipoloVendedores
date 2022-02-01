package com.dipolo.vendedores.empresas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dipolo.vendedores.Metodos;
import com.dipolo.vendedores.R;
import com.dipolo.vendedores.enviarmail.Mail;
import com.dipolo.vendedores.menu.Inicio;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FormularioDipolizar extends AppCompatActivity  {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Bundle extras;
    SharedPreferences DatosUsuario;
    String idEmpresa;

    ProgressDialog carga, dialog;

    EditText telefono, correo, rut;
    Spinner planEmpresa;
    TextView nombre;

    LinearLayout solicitudenviada;
    boolean enviada = false;

    Uri contrato, comprobante;


    Metodos metodos = new Metodos();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_dipolizar);
        //new SendMail().execute("");


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);



        DatosUsuario = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        extras = getIntent().getExtras();


        nombre = findViewById(R.id.nombre_empresa);
        telefono = findViewById(R.id.empresa_telefono);
        correo = findViewById(R.id.empresa_correo);
        rut = findViewById(R.id.empresa_rut);
        solicitudenviada = findViewById(R.id.solicitudenviada);
        planEmpresa = findViewById(R.id.spinner);


        cargaInformacion();

    }



    public void cargaInformacion() {

        if (extras != null) {

            idEmpresa = extras.getString("idEmpresa");
            nombre.setText(extras.getString("nombre"));

            if (!extras.getString("telefono").equals("")) {
                telefono.setText(extras.getString("telefono"));
            }

            if (!extras.getString("correo").equals("")) {
                correo.setText(extras.getString("correo"));
            }

        }


        String[] arraySpinner = new String[] {
                "Selecciona el plan...", "Plan Básico", "Plan Medio", "Plan Full"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planEmpresa.setAdapter(adapter);


    }



    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> obtenerComprobante = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Button button = findViewById(R.id.button_comprobante);
                    Metodos.pathComprobante = result.getData().getData();

                    setearNombreArchivo(button, result.getData().getData());


                }
            });

    ActivityResultLauncher<Intent> obtenerContrato = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Button button = findViewById(R.id.button_contrato);
                    Metodos.pathContrato = result.getData().getData();
                    setearNombreArchivo(button, result.getData().getData());

                }
            });



    public void subirContrato(View v) {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        galleryIntent.setType("application/pdf");
        obtenerContrato.launch(galleryIntent);

    }

    public void subirComPago(View v) {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        galleryIntent.setType("image/*");
        obtenerComprobante.launch(galleryIntent);


    }


    public void enviarFormulario(View v) {
        carga = ProgressDialog.show(FormularioDipolizar.this, "Espera",
                "Estamos enviando tu solicitud...", true);

        if (metodos.ComprobarCamposDipolizar(correo.getText().toString(), telefono.getText().toString(), rut.getText().toString(), planEmpresa.getSelectedItem().toString(),FormularioDipolizar.this)) {

            subirContrato();

        } else {
            carga.dismiss();
        }
    }

    public void listoVolver(View v) {
        Intent intent = new Intent(FormularioDipolizar.this, Inicio.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (enviada) {
            Intent intent = new Intent(FormularioDipolizar.this, Inicio.class);
            startActivity(intent);
        } else {
            finish();
        }
    }


    public void subirContrato() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Here we are uploading the pdf in firebase storage with the name of current time
        final StorageReference filepath = storageReference.child(nombre.getText().toString().trim()+"-contrato"+ "." + "pdf");

        filepath.putFile(Metodos.pathContrato).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filepath.getDownloadUrl();
        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                Metodos.urlContrato = uri.toString();
                subirComprobante();

            } else {
                Toast.makeText(FormularioDipolizar.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void subirComprobante() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Here we are uploading the pdf in firebase storage with the name of current time
        final StorageReference filepath = storageReference.child(nombre.getText().toString().trim()+"-comprobantePago"+"." + "jpg");

        filepath.putFile(Metodos.pathComprobante).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filepath.getDownloadUrl();
        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
            if (task.isSuccessful()) {
                carga.dismiss();
                Uri uri = task.getResult();
                Metodos.urlComprobante = uri.toString();

                solicitudenviada.setVisibility(View.VISIBLE);
                enviada = true;

                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //ENVIAR CORREO
                new SendMail().execute("");

            } else {
                carga.dismiss();
                Toast.makeText(FormularioDipolizar.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FormularioDipolizar.this, "Espere...", "Terminando de enviar registro...", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail("richi23012@gmail.com", "23012002Aa?");

            String[] toArr = {"richi23012@gmail.com", "richi230120@gmail.com"};
            m.setTo(toArr);
            m.setFrom("richi23012@gmail.com");
            m.setSubject("Nueva solicitud de Dipolización");
            m.setBody("Hola Dipolo! \n\n"+DatosUsuario.getString("nombre","")+" ha solicitado Dipolizar esta empresa. \nTe hemos adjuntado los links del contrato firmado y del comprobante de pago. \n\n\nLos datos de la contratación son:\n\n-Nombre empresa: "+ nombre.getText().toString()+"\n-RUT: "+rut.getText().toString()+"\n-Teléfono: "+telefono.getText().toString()+"\n-Plan seleccionado: "+planEmpresa.getSelectedItem().toString()+"\n\nLink contrato: "+Metodos.urlContrato+"\nLink comprobante: "+Metodos.urlComprobante+"\n\nSi todo esta correcto puedes levantar esta solicitud al mail: soporte@dipolo.cl\nSi hay un problema con los datos subidos por el vendedor, enviale un mail con los detalles a: "+DatosUsuario.getString("correo","")+"\n\n"+"Un saludo desde el equipo de Dipolo!");

            try {
                if(m.send()) {
                    Log.e("MailApp", "Email was sent successfully.");
                } else {
                    Log.e("MailApp", "Email was not sent.");
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }


    public void setearNombreArchivo(Button button, Uri uri){
        Cursor returnCursor =
                getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        button.setText(returnCursor.getString(nameIndex));
        returnCursor.close();
    }

}