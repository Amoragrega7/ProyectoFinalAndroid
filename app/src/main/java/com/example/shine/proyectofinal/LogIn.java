package com.example.shine.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LogIn extends Fragment {
    private EditText username;
    private EditText password;

    private Button Logi;

    private BD bd;

    private int nerrores = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_log_in, container, false);

        bd = new BD(this.getActivity().getApplicationContext());
        username = (EditText) rootView.findViewById(R.id.editTextUser);
        password =  (EditText) rootView. findViewById(R.id.editTextPass);

        Logi = (Button) rootView.findViewById(R.id.buttonLog);

        View.OnClickListener lis2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compruebaUser(v);
            }
        };
        Logi.setOnClickListener(lis2);

        return rootView;
    }

    public void compruebaUser(View view) {
        Cursor c = bd.getUsername(String.valueOf(username.getText()));
        if (username.getText().toString().equals("")) {
            Toast.makeText(this.getActivity(), "¡Introduce un username válido!", Toast.LENGTH_SHORT).show();
        }
        else if (!c.moveToFirst()) {
            Toast.makeText(this.getActivity(), "¡El usuario no existe!", Toast.LENGTH_SHORT).show();
        }
        else if (password.getText().toString().equals("")) {
            Toast.makeText(this.getActivity(), "¡Introduce una contraseña!", Toast.LENGTH_SHORT).show();
        }
        else {
            c = bd.getPasswordByUsername(username.getText().toString());
            c.moveToFirst();
            if (c.getString(c.getColumnIndex("Password")).equals(password.getText().toString())) {
                Toast.makeText(this.getActivity(), "¡LogIn satisfactorio!", Toast.LENGTH_SHORT).show();

                SharedPreferences settings = this.getActivity().getSharedPreferences("Usuario", 0);
                SharedPreferences.Editor editor = settings.edit();
                c = bd.getAllinfofromUsername(String.valueOf(username.getText()));
                c.moveToFirst();
                editor.putString("Username", c.getString(c.getColumnIndex("Username")));
                editor.putString("Password", c.getString(c.getColumnIndex("Password")));
                editor.putInt("BestPoints", c.getInt(c.getColumnIndex("BestPoints")));
                editor.putInt("Notimode", c.getInt(c.getColumnIndex("NotificationMode")));
                editor.putString("Avatar", c.getString(c.getColumnIndex("Avatar")));
                editor.apply();

                Intent intent = new Intent(this.getActivity().getApplicationContext(), MenuPrincipal.class);
                startActivity(intent);
                this.getActivity().finish();

            }
            else {
                Toast.makeText(this.getActivity(), "¡Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                ++nerrores;
                if (nerrores == 3) { retry(); nerrores = 0; }
            }
        }
    }

    private void retry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¡Contraseña incorrecta!");
        builder.setPositiveButton("Reintentar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialoglayout, null);
        dialog.setView(dialogLayout);
        dialog.show();
    }
}
