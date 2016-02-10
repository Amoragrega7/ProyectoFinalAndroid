package com.example.shine.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Movie;
import android.location.GpsStatus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;

public class Memory extends AppCompatActivity implements View.OnClickListener {

    private Button boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8,
            boton9, boton10, boton11, boton12 ,boton13, boton14, boton15, boton16;
    private TextView fallos;
    private ArrayList< Pair<Button,Integer> > buttons;
    private ArrayList<Integer> numbers;
    private int nfallos = 0;
    private int control = 0;
    private int whatbutton = -1;
    private Pair<Integer, Integer>  a, b;
    private int finish = 0;
    private boolean lavariablesalvadora = false;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fallos = (TextView) findViewById(R.id.textViewNfallos);
        fallos.setText("0");

        boton1  = (Button) findViewById(R.id.button1);  boton2  = (Button) findViewById(R.id.button2);
        boton3  = (Button) findViewById(R.id.button3);  boton4  = (Button) findViewById(R.id.button4);
        boton5  = (Button) findViewById(R.id.button5);  boton6  = (Button) findViewById(R.id.button6);
        boton7  = (Button) findViewById(R.id.button7);  boton8  = (Button) findViewById(R.id.button8);
        boton9  = (Button) findViewById(R.id.button9);  boton10 = (Button) findViewById(R.id.button10);
        boton11 = (Button) findViewById(R.id.button11); boton12 = (Button) findViewById(R.id.button12);
        boton13 = (Button) findViewById(R.id.button13); boton14 = (Button) findViewById(R.id.button14);
        boton15 = (Button) findViewById(R.id.button15); boton16 = (Button) findViewById(R.id.button16);

        boton1.setOnClickListener(this);  boton2. setOnClickListener(this);
        boton3.setOnClickListener(this);  boton4. setOnClickListener(this);
        boton5.setOnClickListener(this);  boton6. setOnClickListener(this);
        boton7.setOnClickListener(this);  boton8. setOnClickListener(this);
        boton9.setOnClickListener(this);  boton10.setOnClickListener(this);
        boton11.setOnClickListener(this); boton12.setOnClickListener(this);
        boton13.setOnClickListener(this); boton14.setOnClickListener(this);
        boton15.setOnClickListener(this); boton16.setOnClickListener(this);

        buttons = new ArrayList<>(16);
        buttons.add(Pair.create(boton1, -1)); buttons.add(Pair.create(boton2, -1));
        buttons.add(Pair.create(boton3, -1)); buttons.add(Pair.create(boton4, -1));
        buttons.add(Pair.create(boton5, -1)); buttons.add(Pair.create(boton6, -1));
        buttons.add(Pair.create(boton7, -1)); buttons.add(Pair.create(boton8, -1));
        buttons.add(Pair.create(boton9, -1)); buttons.add(Pair.create(boton10,-1));
        buttons.add(Pair.create(boton11,-1)); buttons.add(Pair.create(boton12,-1));
        buttons.add(Pair.create(boton13,-1)); buttons.add(Pair.create(boton14,-1));
        buttons.add(Pair.create(boton15,-1)); buttons.add(Pair.create(boton16,-1));

        numbers = new ArrayList<>(16);
        gestionaRandom();
        a = Pair.create(-1,-1);
        b = Pair.create(-1,-1);
        whatbutton = -1;
        control = 0;
        finish = 0;
        nfallos = 0;
        lavariablesalvadora = false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    private void gestionaRandom() {
        numbers.add(1);numbers.add(1);
        numbers.add(2);numbers.add(2);
        numbers.add(3);numbers.add(3);
        numbers.add(4);numbers.add(4);
        numbers.add(5);numbers.add(5);
        numbers.add(6);numbers.add(6);
        numbers.add(7);numbers.add(7);
        numbers.add(8);numbers.add(8);

        Random r = new Random();
        for (int i = 15; i >= 0; --i) {
            int i1;
            i1 = r.nextInt(i+1 - 0) + 0;
            Integer x = numbers.get(i1);
            numbers.remove(i1);
            buttons.set(i, Pair.create(buttons.get(i).first, x));
            buttons.get(i).first.setVisibility(View.VISIBLE);
            buttons.get(i).first.setBackgroundResource(R.drawable.backicon);
        }
    }

    private int setImagen(int x) {
        switch (x) {
            case 0:
                return R.drawable.onepiecelogo;
            case 1:
                return R.drawable.fairytaillogo;
            case 2:
                return R.drawable.bleachlogo;
            case 3:
                return R.drawable.katekyologo;
            case 4:
                return R.drawable.narutologo;
            case 5:
                return R.drawable.dragonballlogo;
            case 6:
                return R.drawable.fmalogo;
            case 7:
                return R.drawable.souleaterlogo;
            default:
                return R.drawable.backicon;
        }
    }

    private void gestionaBoton(int x) {
        if (control < 2)   {
            if (whatbutton != x) {
                whatbutton = x;
                MyTask task = new MyTask();
                task.execute(x);
                task.onCancelled();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                gestionaBoton(1);
                break;
            case R.id.button2:
                gestionaBoton(2);
                break;
            case R.id.button3:
                gestionaBoton(3);
                break;
            case R.id.button4:
                gestionaBoton(4);
                break;
            case R.id.button5:
                gestionaBoton(5);
                break;
            case R.id.button6:
                gestionaBoton(6);
                break;
            case R.id.button7:
                gestionaBoton(7);
                break;
            case R.id.button8:
                gestionaBoton(8);
                break;
            case R.id.button9:
                gestionaBoton(9);
                break;
            case R.id.button10:
                gestionaBoton(10);
                break;
            case R.id.button11:
                gestionaBoton(11);
                break;
            case R.id.button12:
                gestionaBoton(12);
                break;
            case R.id.button13:
                gestionaBoton(13);
                break;
            case R.id.button14:
                gestionaBoton(14);
                break;
            case R.id.button15:
                gestionaBoton(15);
                break;
            case R.id.button16:
                gestionaBoton(16);
                break;
        }
    }

    private void restart() {
        finish = 0;
        nfallos = 0;
        gestionaRandom();
        fallos.setText("" + nfallos);
        lavariablesalvadora = false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mrestart:
                restart();
                return true;
            case R.id.mlogout:
                settings = getSharedPreferences("Usuario", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.putInt("BestPoints", -1);
                editor.putInt("Notimode", 1);
                editor.putString("Avatar", "-1");
                editor.apply();
                Intent intent = new Intent(this, SigninLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(Integer... param) {
            if(lavariablesalvadora) {
                while (lavariablesalvadora) ;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String aux) {
            if (a.first.intValue() != -1 && b.first.intValue() != -1 && a.first.intValue() == b.first.intValue()) {
                buttons.get(a.second).first.setVisibility(View.INVISIBLE);
                buttons.get(b.second).first.setVisibility(View.INVISIBLE);
                ++finish;
                if (finish == 8) finPartida();
            }
            else if (a.first.intValue() != -1 && b.first.intValue() != -1 && a.first.intValue() != b.first.intValue()) {
                buttons.get(a.second).first.setBackgroundResource(R.drawable.backicon);
                buttons.get(b.second).first.setBackgroundResource(R.drawable.backicon);
                ++nfallos;
                fallos.setText("" + nfallos);
            }
            if (a.first.intValue() != -1) a = Pair.create(-1,-1);
            if (b.first.intValue() != -1) b = Pair.create(-1,-1);
            control = 0;
            whatbutton = -1;
        }

        @Override
        protected void onPreExecute() {
            int aux = whatbutton-1;
            ++control;
            int n = buttons.get(aux).second.intValue();
            if (a.first.intValue() == -1) a = Pair.create(n,aux);
            else if (b.first.intValue() == -1) b = Pair.create(n, aux);
            buttons.get(aux).first.setBackgroundResource(setImagen(n % 8));
            lavariablesalvadora = !lavariablesalvadora;
            super.onPreExecute();
        }
    }

    private void cierra() {
        this.finish();
    }

    private void finPartida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Partida Finalizada");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        gestionaPuntos();
                        cierra();
                    }
                });
        builder.setNegativeButton("Nueva Partida",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        gestionaPuntos();
                        restart();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void gestionaPuntos() {
        settings = getSharedPreferences("Usuario", 0);
        int p = settings.getInt("BestPoints",-1);
        if (nfallos < p || p == -1) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BestPoints", nfallos);
            editor.apply();
            MyTask2 task = new MyTask2();
            task.execute();
        }
    }

    private class MyTask2 extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            String username = settings.getString("Username","");
            BD bd = new BD(getApplicationContext());
            ContentValues dataToInsert = new ContentValues();
            dataToInsert.put("BestPoints", nfallos);
            bd.setPoints(username, dataToInsert);
            bd.close();
            return null;
        }
    }
}