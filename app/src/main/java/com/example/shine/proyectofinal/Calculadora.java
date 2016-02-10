package com.example.shine.proyectofinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Calculadora extends AppCompatActivity implements View.OnClickListener {

    private TextView operaciones;
    private Button boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9, boton0;
    private Button botonB, botonS, botonR, botonM, botonD, botonP, botonI;
    private double num = 0;
    private double numaux = 0;
    private double ans = 0;
    private boolean cambiaSigno =false;
    private int op = 0;
    private boolean moderesult = false;
    private boolean modeans = false;
    private int noti = 0;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences settings;
    private SharedPreferences firstTime;
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        operaciones = (TextView) findViewById(R.id.textView);
        operaciones.setText("0");

        boton1 = (Button) findViewById(R.id.button1);
        boton2 = (Button) findViewById(R.id.button2);
        boton3 = (Button) findViewById(R.id.button3);
        boton4 = (Button) findViewById(R.id.button4);
        boton5 = (Button) findViewById(R.id.button5);
        boton6 = (Button) findViewById(R.id.button6);
        boton7 = (Button) findViewById(R.id.button7);
        boton8 = (Button) findViewById(R.id.button8);
        boton9 = (Button) findViewById(R.id.button9);
        boton0 = (Button) findViewById(R.id.button0);
        botonB = (Button) findViewById(R.id.buttonB);
        botonS = (Button) findViewById(R.id.buttonSum);
        botonR = (Button) findViewById(R.id.buttonRes);
        botonM = (Button) findViewById(R.id.buttonMul);
        botonD = (Button) findViewById(R.id.buttonDiv);
        botonP = (Button) findViewById(R.id.buttonP);
        botonI = (Button) findViewById(R.id.buttonIgu);

        boton1.setOnClickListener(this);
        boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);
        boton4.setOnClickListener(this);
        boton5.setOnClickListener(this);
        boton6.setOnClickListener(this);
        boton7.setOnClickListener(this);
        boton8.setOnClickListener(this);
        boton9.setOnClickListener(this);
        boton0.setOnClickListener(this);
        botonS.setOnClickListener(this);
        botonR.setOnClickListener(this);
        botonM.setOnClickListener(this);
        botonD.setOnClickListener(this);
        botonP.setOnClickListener(this);
        inicializabotonIgual();
        inicializabotonBorrar();

        settings = getSharedPreferences("Usuario", 0);
        noti = settings.getInt("Notimode", 1);

        firstTime = getSharedPreferences("FirstTime", 0);
        if (firstTime.getBoolean("Control", true)) {
            showNotification(noti, "Tip: Mantener pulsada tecla '=' activa el modo Ans " +
                    "y la tecla '<-' el modo CE");
            SharedPreferences.Editor editor = firstTime.edit();
            editor.putBoolean("Control", false);
            editor.apply();
        }
    }

    private void inicializabotonIgual() {
        View.OnTouchListener lisT = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    botonI.setText("=");
                    if (!modeans) {
                        if (!moderesult) {
                            if (op == 4 && num == 0) {
                                showNotification(noti, "¡División entre 0!");
                            }
                            else  {
                                opera();
                                moderesult = true;
                                op = 0;
                                operaciones.setText(escribe(-1));
                            }
                        }
                        else if (op == 0) operaciones.setText(escribe(-1));
                        else {
                            opera();
                            moderesult = true;
                            op = 0;
                            operaciones.setText(escribe(-1));
                        }
                        ans = num;
                    }
                    else modeans = false;
                }
                return false;
            }
        };
        botonI.setOnTouchListener(lisT);

        View.OnLongClickListener lis = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                modeans = true;
                botonI.setText("Ans");
                num = ans;
                operaciones.setText(escribe(-1));
                return false;
            }
        };
        botonI.setOnLongClickListener(lis);
    }

    private void inicializabotonBorrar() {
        View.OnTouchListener lisT = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    botonB.setText("<-");
                    if (moderesult) {
                        num = 0;
                        moderesult = false;
                        operaciones.setText("0");
                    }
                    else { borra(); moderesult = false; }
                }
                return false;
            }
        };
        botonB.setOnTouchListener(lisT);

        View.OnLongClickListener lis = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                botonB.setText("CE");
                num = 0;
                numaux = 0;
                op = 0;
                operaciones.setText("0");
                return false;
            }
        };
        botonB.setOnLongClickListener(lis);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu5, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("tosave0", noti);
        outstate.putString("tosave1", operaciones.getText().toString());
        outstate.putDouble("tosave2", num);
        outstate.putDouble("tosave3", numaux);
        outstate.putDouble("tosave4", ans);
        outstate.putInt("tosave5", op);
        outstate.putBoolean("tosave6", cambiaSigno);
        outstate.putBoolean("tosave7",moderesult);
        outstate.putBoolean("tosave8", modeans);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        noti = savedInstanceState.getInt("tosave0");
        operaciones.setText(savedInstanceState.getString("tosave1"));
        num = savedInstanceState.getDouble("tosave2");
        numaux = savedInstanceState.getDouble("tosave3");
        ans = savedInstanceState.getDouble("tosave4");
        op = savedInstanceState.getInt("tosave5");
        cambiaSigno = savedInstanceState.getBoolean("tosave6");
        moderesult = savedInstanceState.getBoolean("tosave7");
        modeans = savedInstanceState.getBoolean("tosave8");
    }

    private String escribe(int x) {
        String s = operaciones.getText().toString();
        if (s.equals("+") || s.equals("-") || s.equals("x") || s.equals("÷")) s = "0";
        if (x == -1) { //Resultado
            s = String.valueOf(num);
            if (s.charAt(s.length()-1) == '0' && s.charAt(s.length()-2) == '.') {
                s = s.substring(0,s.length()-2);
            }
        }
        else if (x == -2) { //Decimal
            s = String.valueOf(num);
            s = s.substring(0,s.length()-1);
        }
        else { //Numero
            if (s.equals("0")) s = "";
            s = s + x;
            num = Double.valueOf(s);
        }
        if (cambiaSigno) {// Negativo
            s = "-" + s;
            num = Double.valueOf(s);
            cambiaSigno = false;
        }
        return s;
    }

    private void borra() {
        String s = operaciones.getText().toString();
        if (!s.equals("0")) {
            if (s.length() == 1) {
                s = "0";
                operaciones.setText(s);
                num = 0;
            }
            else {
                s = s.substring(0, s.length() - 1);
                operaciones.setText(s);
                if (s.length() > 0 && s.charAt(s.length() - 1) == '.') s = s + "0";
                num = Double.valueOf(s);
            }
        }
    }

    private void opera() {
        switch (op) {
            case 1:
                num = numaux + num;
                break;
            case 2:
                num = numaux - num;
                break;
            case 3:
                num = numaux * num;
                break;
            case 4:
                num = numaux / num;
                break;
        }
    }

    private void gestionaModeResult() {
        num = 0;
        moderesult = false;
        operaciones.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(1));
                break;
            case R.id.button2:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(2));
                break;
            case R.id.button3:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(3));
                break;
            case R.id.button4:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(4));
                break;
            case R.id.button5:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(5));
                break;
            case R.id.button6:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(6));
                break;
            case R.id.button7:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(7));
                break;
            case R.id.button8:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(8));
                break;
            case R.id.button9:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(9));
                break;
            case R.id.button0:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(0));
                break;
            case R.id.buttonSum:
                op = 1;
                numaux = num;
                num = 0;
                operaciones.setText("+");
                break;
            case R.id.buttonRes:
                if (num == 0) cambiaSigno = true;
                else {
                    op = 2;
                    numaux = num;
                    num = 0;
                    operaciones.setText("-");
                }
                break;
            case R.id.buttonMul:
                op = 3;
                numaux = num;
                num = 0;
                operaciones.setText("x");
                break;
            case R.id.buttonDiv:
                op = 4;
                numaux = num;
                num = 0;
                operaciones.setText("÷");
                break;
            case R.id.buttonP:
                if (moderesult) gestionaModeResult();
                operaciones.setText(escribe(-2));
                break;
        }
    }

    private void setNotiMode(int n) {
        noti = n;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Notimode", n);
        editor.apply();
        MyTask task = new MyTask();
        task.execute(n);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnoti:
                return true;
            case R.id.smsnack:
                setNotiMode(1);
                return true;
            case R.id.smtoast:
                setNotiMode(2);
                return true;
            case R.id.smestado:
                setNotiMode(3);
                return true;
            case R.id.mllamar:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+operaciones.getText()));
                startActivity(intent);
                return true;
            case R.id.mnavegador: //Easter egg: ¡Ojo a los comentarios de esta gran app!
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=air.android.BarbaraCaesareanBirth&hl=es"));
                startActivity(intent2);
                return true;
            case R.id.mlogout:
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.putInt("BestPoints", -1);
                editor.putInt("Notimode", 1);
                editor.putString("Avatar", "-1");
                editor.apply();
                Intent intent3 = new Intent(this, SigninLogin.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNotification(int x, String text) {
        switch (x) {
            case 1:
                Snackbar snack = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
                snack.show();
                break;
            case 2:
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;
            case 3:
                int mId = 1;
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.noti)
                                .setContentTitle("Información")
                                .setContentText(text);
                Intent resultIntent = new Intent();
                android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(Calculadora.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(mId, mBuilder.build());
                break;
        }
    }

    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            bd = new BD(getApplicationContext());
            ContentValues cv = new ContentValues();
            cv.put("NotificationMode", param[0]);
            bd.setNotimode(settings.getString("Username",""),cv);
            bd.close();
            return null;
        }
    }
}