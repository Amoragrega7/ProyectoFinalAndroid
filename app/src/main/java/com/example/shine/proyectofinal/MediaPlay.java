package com.example.shine.proyectofinal;

//import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class MediaPlay extends AppCompatActivity implements View.OnClickListener {

    private Button botonPlay, botonStop, botonAnterior, botonSiguiente, botonRaw;
    private TextView textSongname;
    private boolean moderaw = false;
    private MediaPlayerService mService;
    private boolean bound;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mService = binder.getService();
            bound = true;
            mService.setMediaPlay(MediaPlay.this);
            textSongname.setText(mService.setSong());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textSongname = (TextView) findViewById(R.id.textViewSong);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botonPlay = (Button) findViewById(R.id.buttonPlay);
        botonStop = (Button) findViewById(R.id.buttonStop);
        botonAnterior = (Button) findViewById(R.id.buttonAnterior);
        botonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        botonRaw = (Button) findViewById(R.id.button);

        botonPlay.setOnClickListener(this);
        botonStop.setOnClickListener(this);
        botonAnterior.setOnClickListener(this);
        botonSiguiente.setOnClickListener(this);
        botonRaw.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu4, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent (MediaPlay.this, MediaPlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        stop();
        if (moderaw)  {
            botonRaw.setText("Play raw memory audio");
            moderaw = false;
        }
        super.onStop();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    private void play() {
        mService.play();
        botonPlay.setBackgroundResource(R.drawable.pause);
    }

    private void pause() {
        mService.pause();
        botonPlay.setBackgroundResource(R.drawable.play);
    }

    private void stop() {
        mService.stop();
        botonPlay.setBackgroundResource(R.drawable.play);
    }

    public void next(String s) {
        textSongname.setText(s);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                if (!moderaw) {
                    if (mService.playing()) pause();
                    else play();
                }
                else  {
                    botonRaw.setText("Play raw memory audio");
                    moderaw = false;
                    stop();
                    play();
                }
                break;
            case R.id.buttonStop:
                stop();
                if (moderaw) {
                    botonRaw.setText("Play raw memory audio");
                    moderaw = false;
                }
                break;
            case R.id.buttonSiguiente:
                textSongname.setText(mService.siguiente());
                if (moderaw) {
                    botonRaw.setText("Play raw memory audio");
                    moderaw = false;
                }
                break;
            case R.id.buttonAnterior:
                textSongname.setText(mService.anterior());
                if (moderaw) {
                    botonRaw.setText("Play raw memory audio");
                    moderaw = false;
                }
                break;
            case R.id.button:
                if (!moderaw) {
                    stop();
                    mService.Raw();
                    mService.playRaw();
                    moderaw = true;
                    botonPlay.setBackgroundResource(R.drawable.play);
                    botonRaw.setText("Pause raw memory audio");
                }
                else {
                    if (mService.playing()) {
                        mService.pauseRaw();
                        botonRaw.setText("Play raw memory audio");
                    }
                    else {
                        mService.playRaw();
                        botonRaw.setText("Pause raw memory audio");
                    }
                }
                break;
        }
    }

    private void songlist() {
        ArrayList<String> lc = new ArrayList<String>(Arrays.asList(mService.getSongList()));
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("tosend1",lc);
        Intent intent = new Intent(getApplicationContext(), Songlist.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mlist:
                songlist();
                return true;
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
}
