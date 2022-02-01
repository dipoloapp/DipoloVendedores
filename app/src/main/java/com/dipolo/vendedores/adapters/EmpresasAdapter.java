/*
 * Copyright (c) 2020. Todos los Derechos Reservados.
 * Código escrito y desarrollado por Ricardo Morgado.
 */

package com.dipolo.vendedores.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dipolo.vendedores.empresas.AsignadasFragment;
import com.dipolo.vendedores.empresas.DipolizadasFragment;

public class EmpresasAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public EmpresasAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AsignadasFragment encurso = new AsignadasFragment();
                return encurso;
            case 1:
                DipolizadasFragment anteriores = new DipolizadasFragment();
                return anteriores;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}