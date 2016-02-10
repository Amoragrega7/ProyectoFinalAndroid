package com.example.shine.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class Perfil extends Fragment implements View.OnClickListener {

    private TextView username, bestpoints;
    private ImageView avat;
    private Button ChangeA, Localiz, LogOut;
    private SharedPreferences settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_perfil, container, false);

        username = (TextView) rootView.findViewById(R.id.textViewUsername);
        bestpoints = (TextView) rootView.findViewById(R.id.textViewPoints);
        avat = (ImageView) rootView.findViewById(R.id.imageViewAvatar);

        ChangeA = (Button) rootView.findViewById(R.id.buttonCambiarA);
        Localiz = (Button) rootView.findViewById(R.id.buttonLoca);
        LogOut = (Button) rootView.findViewById(R.id.buttonLogOut);

        ChangeA.setOnClickListener(this);
        Localiz.setOnClickListener(this);
        LogOut.setOnClickListener(this);

        setHasOptionsMenu(true);

        settings = this.getActivity().getSharedPreferences("Usuario", 0);
        username.setText(settings.getString("Username", ""));
        int x = settings.getInt("BestPoints", -1);
        if (x == -1) bestpoints.setText("N/A");
        else bestpoints.setText(String.valueOf(x));

        setAvatar();

        return rootView;
    }

    /*public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mlogout:
                SharedPreferences settings = this.getActivity().getSharedPreferences("Usuario", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.putInt("BestPoints", -1);
                editor.putInt("Notimode", 1);
                editor.putString("Avatar", "-1");
                editor.apply();
                Intent intent = new Intent(this.getContext(), SigninLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void setAvatar() {
        String a = settings.getString("Avatar","-1");
        if (!a.equals("-1") && !a.equals("null"))  {
            avat.setImageURI(Uri.parse(a));
        }
        else {
            avat.setImageResource(R.drawable.ludi);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCambiarA:
                Intent getImageAsContent = new Intent(Intent.ACTION_GET_CONTENT, null);
                getImageAsContent.setType("image/*");
                startActivityForResult(getImageAsContent, 1);
                break;
            case R.id.buttonLoca:
                startActivity(new Intent (this.getContext(), GPSActivity.class));
                break;
            case R.id.buttonLogOut:
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.putInt("BestPoints", -1);
                editor.putInt("Notimode", 1);
                editor.putString("Avatar", "-1");
                editor.apply();
                Intent intent = new Intent(getContext(), SigninLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            data.getData();
            Uri selectedImage = data.getData();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Avatar", selectedImage.toString());
            editor.apply();
            MyTask task = new MyTask();
            task.execute();
            setAvatar();
        }
    }

    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            BD bd = new BD(getContext());
            ContentValues cv = new ContentValues();
            cv.put("Avatar", settings.getString("Avatar","-1").toString());
            bd.changeAvatar(settings.getString("Username", ""), cv);
            bd.close();
            return null;
        }
    }
}
