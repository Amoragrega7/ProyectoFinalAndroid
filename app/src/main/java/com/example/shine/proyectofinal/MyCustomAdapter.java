package com.example.shine.proyectofinal;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.AdapterViewHolder>{

    private ArrayList<InfoUser> users;
    private String actualuser;
    private BD bd;

    MyCustomAdapter(ArrayList<InfoUser> IU, String s){
        users = IU;
        actualuser = s;
    }

//notifydatachange
    @Override
    public MyCustomAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Instancia un layout XML en la correspondiente vista.
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //Inflamos en la vista el layout para cada elemento
        View view = inflater.inflate(R.layout.listlayout, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.AdapterViewHolder adapterViewholder, int position) {
        String s = users.get(position).getAvatar();
        if (s.equals("-1")) adapterViewholder.icon.setImageResource(R.drawable.ludi);
        else adapterViewholder.icon.setImageURI(Uri.parse(s));
        adapterViewholder.username.setText(users.get(position).getUsername());
        int x = users.get(position).getPoints();
        if (x == -1) adapterViewholder.puntos.setText("N/A");
        else adapterViewholder.puntos.setText(""+x);
        if (actualuser.equals(users.get(position).getUsername())) {
            adapterViewholder.username.setTypeface(null, Typeface.BOLD);
            adapterViewholder.puntos.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        //Debemos retornar el tamaño de todos los elementos contenidos en el viewholder
        //Por defecto es return 0 --> No se mostrará nada.
        return users.size();
    }



    //Definimos una clase viewholder que funciona como adapter para
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        /*
        *  Mantener una referencia a los elementos de nuestro ListView mientras el usuario realiza
        *  scrolling en nuestra aplicación. Así que cada vez que obtenemos la vista de un item,
        *  evitamos las frecuentes llamadas a findViewById, la cuál se realizaría únicamente la primera vez y el resto
        *  llamaríamos a la referencia en el ViewHolder, ahorrándonos procesamiento.
        */
        public ImageView icon;
        public TextView username;
        public TextView puntos;
        public View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.icon = (ImageView) itemView.findViewById(R.id.imageView);
            this.username = (TextView) itemView.findViewById(R.id.username);
            this.puntos = (TextView) itemView.findViewById(R.id.puntos);
        }
    }
/*
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/

    public void setData(ArrayList<InfoUser> newData){
        this.users = newData;
        notifyDataSetChanged();
    }
}
