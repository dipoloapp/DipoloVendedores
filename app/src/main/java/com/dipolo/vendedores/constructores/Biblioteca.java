/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.constructores;

import com.google.firebase.firestore.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Biblioteca {

    public String nombre;
    public String url;
    public String img;


    public Biblioteca() {

    }

    public Biblioteca(String nombre, String url, String img) {
        this.nombre = nombre;
        this.url = url;
        this.img = img;
    }


    public String getImg() {
        return img;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }
}
