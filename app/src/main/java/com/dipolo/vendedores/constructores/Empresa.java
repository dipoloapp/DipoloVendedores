/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.constructores;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;


@IgnoreExtraProperties
public class Empresa {

    public HashMap<String, Object> datosEmpresa;
    public HashMap<String, Object> datosPlan;
    public HashMap<String, Object> tutoriales;
    public String logo;

    public Empresa() {

    }

    public Empresa(HashMap<String, Object> datosEmpresa,HashMap<String, Object> datosPlan,HashMap<String, Object> tutoriales, String logo) {
        this.datosEmpresa = datosEmpresa;
        this.datosPlan = datosPlan;
        this.tutoriales = tutoriales;
        this.logo = logo;
    }

    public HashMap<String, Object> getDatosPlan() {
        return datosPlan;
    }

    public HashMap<String, Object> getDatosEmpresa() {
        return datosEmpresa;
    }

    public HashMap<String, Object> getTutoriales() {
        return tutoriales;
    }

    public String getLogo() {
        return logo;
    }

}
