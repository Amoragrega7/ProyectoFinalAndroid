package com.example.shine.proyectofinal;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;


public class MediaPlayerService extends Service {

    private final IBinder mBinder = new MediaPlayerBinder();
    private MediaPlayer mediaPlayer;

    private File song;
    private int songnumber = 0;
    private int maxsongs = 0;
    private boolean modeplay = false;
    private boolean walla = false;//TODO: aacabaaaarr!!
    private String sdPath;
    private String[] sl;
    private MediaPlay mediaPlay;

    public MediaPlayerService(){}

    public void setMediaPlay(MediaPlay mp) {
        this.mediaPlay = mp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        sl = new File(sdPath + "/Music/").list();
        maxsongs = sl.length;
    }

    public String[] getSongList() {
        return sl;
    }

    private String cargaSong() {
        song = new File(sdPath + "/Music/" + sl[songnumber]);
        String s = song.getName().toString();
        return s.substring(0, s.length() - 4);
    }

    public String setSong() {
        String s = cargaSong();
        try {
            mediaPlayer.setDataSource(song.getAbsolutePath());
            mediaPlayer.prepare();
            MediaPlayer.OnCompletionListener li = new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (maxsongs-1 > songnumber) ++songnumber;
                    else songnumber = 0;
                    mediaPlayer.reset();
                    mediaPlay.next(setSong());
                }
            };
            mediaPlayer.setOnCompletionListener(li);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (modeplay) mediaPlayer.start();
        return s;
    }

    public boolean play() {
        mediaPlayer.start();
        modeplay = true;
        return true;
    }

    public boolean pause() {
        mediaPlayer.pause();
        modeplay = false;
        return true;
    }

    public boolean stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        modeplay = false;
        setSong();
        return true;
    }

    public String siguiente() {
        if (maxsongs-1 > songnumber) ++songnumber;
        else songnumber = 0;
        mediaPlayer.stop();
        mediaPlayer.reset();
        return setSong();
    }

    public String anterior() {
        if (0 < songnumber) --songnumber;
        else songnumber = maxsongs-1;
        mediaPlayer.stop();
        mediaPlayer.reset();
        return setSong();
    }

    public boolean playing() {
        return mediaPlayer.isPlaying();
    }

    public boolean Raw() {
        mediaPlayer = MediaPlayer.create(this, R.raw.wallapop);
        return true;
    }

    public boolean playRaw() {
        mediaPlayer.start();
        return true;
    }

    public boolean pauseRaw() {
        mediaPlayer.pause();
        return true;
    }

    public class MediaPlayerBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}


