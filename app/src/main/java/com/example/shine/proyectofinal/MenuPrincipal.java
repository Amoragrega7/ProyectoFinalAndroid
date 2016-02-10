package com.example.shine.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.URL;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {

    private Button botonMemo, botonPerf, botonRank, botonMedia, botonCalcu;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botonMemo   = (Button) findViewById(R.id.buttonMemory);
        botonPerf   = (Button) findViewById(R.id.buttonPerfil);
        botonRank   = (Button) findViewById(R.id.buttonRanking);
        botonMedia  = (Button) findViewById(R.id.buttonMediaPlayer);
        botonCalcu  = (Button) findViewById(R.id.buttonCalculadora);
        back        = (LinearLayout) findViewById(R.id.back);

        botonMemo. setOnClickListener(this);
        botonPerf. setOnClickListener(this);
        botonRank. setOnClickListener(this);
        botonMedia.setOnClickListener(this);
        botonCalcu.setOnClickListener(this);

        downloadImage(back);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mlogout:
                SharedPreferences settings = getSharedPreferences("Usuario", 0);
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

    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.buttonMemory:
                intent = new Intent(getApplicationContext(), Memory.class);
                startActivity(intent);
                break;
            case R.id.buttonPerfil:
                intent = new Intent(getApplicationContext(), PerfilRanking.class);
                bundle = new Bundle();
                bundle.putInt("who",0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonRanking:
                intent = new Intent(getApplicationContext(), PerfilRanking.class);
                bundle = new Bundle();
                bundle.putInt("who",1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonMediaPlayer:
                intent = new Intent(getApplicationContext(), MediaPlay.class);
                startActivity(intent);
                break;
            case R.id.buttonCalculadora:
                SharedPreferences firstTime = getSharedPreferences("FirstTime", 0);
                SharedPreferences.Editor editor = firstTime.edit();
                editor.putBoolean("Control", true);
                editor.apply();
                intent = new Intent(getApplicationContext(), Calculadora.class);
                startActivity(intent);
                break;
        }
    }

    private Handler mhHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 100 && msg.obj != null) {
                BitmapDrawable ob = new BitmapDrawable(getResources(), (Bitmap)msg.obj);
                back.setBackground(ob);
            }
        }
    };


    public void downloadImage(View v) {
        new Thread(new Runnable() {
            private Bitmap loadImageFromNetwork(String url) {
                try {
                    Bitmap bitmap = BitmapFactory
                            .decodeStream((InputStream) new URL(url)
                                    .getContent());
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void run() {
                final Bitmap bitmap = loadImageFromNetwork("https://scontent.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/12688374_10208473699424720_3727512736559555692_n.jpg?oh=7c80580de3c4180a36894b5802983768&oe=5737B63D");
                Message msg = new Message();
                msg.what = 100;
                msg.obj = bitmap;
                mhHandler.sendMessage(msg);
            }
        }).start();
    }
}
