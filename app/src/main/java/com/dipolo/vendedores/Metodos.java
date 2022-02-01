/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class Metodos {


    public static String logoCurrentEmpresa;
    public static Uri pathContrato = null;
    public static Uri pathComprobante = null;
    public static String urlContrato;
    public static String urlComprobante;




    public boolean ComprobarCamposRegistro(String spinner, Date fecha, String detalle) {
        return !spinner.equals("Selecciona la empresa...") && spinner != null && fecha != null && detalle.length() > 0;
    }


    public boolean ComprobarCamposDipolizar(String correo, String telefono, String rut, String plan, Activity activity) {

        if (correo.contains("@") && correo.contains(".")) {
            if (telefono.contains("+56") && telefono.length() == 12) {
                if (rut.contains(".") && rut.contains("-") && rut.length() >= 11) {
                    if (!plan.equals("Selecciona el plan...")) {
                        if (Metodos.pathContrato != null && Metodos.pathComprobante != null) {
                            return true;
                        } else {
                            Toast.makeText(activity, "Recuerda subir el comprobante de pago y el contrato firmado", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(activity, "Selecciona el plan de la empresa", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(activity, "Revisa el RUT de la empresa, recuerda añadirle puntos y guión", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(activity, "Revisa el correo teléfono de la empresa, recuerda que debe llevar +56", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(activity, "Revisa el correo electrónico de la empresa", Toast.LENGTH_SHORT).show();
            return false;
        }

    }



    public boolean ComprobarCorreo(String correo) {
        return correo.contains("@") && correo.contains(".");
    }

    public boolean ComprobarTelefono(String telefono) {
        return telefono.contains("+56") && telefono.length() == 12;
    }


    public String formatearFecha(Object object) {

        Timestamp time = (Timestamp) object;
        Date date = new Date(Long.parseLong(String.valueOf(Objects.requireNonNull(time).getSeconds())) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        return sdf.format(date);
    }



    public boolean revisarConexionInternetBoolean (Activity context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            try {
                Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal==0);
                if (!reachable) {
                    noConectado(context);
                }
                return reachable;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        return true;

    }

    public void noConectado (Context context) {

        if (!(context instanceof SinConexion)) {
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(context,
                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            Intent intent = new Intent(context, SinConexion.class);
            context.startActivity(intent,bundle);
        }

    }

}
