/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.constructores;

import com.google.firebase.firestore.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Llamada {

    public String resumen;
    public Object dia;


    public Llamada() {

    }

    public Llamada(String resumen, Object dia) {
        this.resumen = resumen;
        this.dia = dia;
    }

    public Object getDiaRegistro() {
        return dia;
    }


    public String getResumen() {
        return resumen;
    }
}
