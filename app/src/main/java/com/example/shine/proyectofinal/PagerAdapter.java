package com.example.shine.proyectofinal;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


public class PagerAdapter extends FragmentStatePagerAdapter {


    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Perfil", "Ranking" };
    private Context context;
    Fragment tab = null;

    //creadora
    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //crea las tabas, siempre tiene que retornar con el numero de tabs que queremos mostrar
    @Override
    public int getCount() { return PAGE_COUNT; }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //Lanza el fragment asociado con el numero de tab
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                tab = new Perfil();
                break;
            case 1:
                tab = new Ranking();
                break;
        }
        return tab;
    }

    //pone el nombre en cada tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
