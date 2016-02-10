package com.example.shine.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignIn extends Fragment {

    private EditText username;
    private EditText password;
    private EditText password2;

    private Button Sign;

    private BD bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        bd = new BD(this.getActivity().getApplicationContext());
        username = (EditText)rootView.findViewById(R.id.editTextUser);
        password =  (EditText) rootView.findViewById(R.id.editTextPass);
        password2 =  (EditText) rootView.findViewById(R.id.editTextPass2);

        Sign = (Button) rootView.findViewById(R.id.buttonSign);

        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(v);
            }
        };
        Sign.setOnClickListener(lis);

        return rootView;
    }

    public void addUser(View view) {
        Cursor c = bd.getUsername(String.valueOf(username.getText()));
        if (username.getText().toString().equals("")) {
            Toast.makeText(this.getActivity(), "¡Introduce un username válido!", Toast.LENGTH_SHORT).show();
        }
        else if (c.moveToFirst()) {
            Toast.makeText(this.getActivity(), "¡El usuario ya existe!", Toast.LENGTH_SHORT).show();
        }
        else if (password.getText().toString().equals("")) {
            Toast.makeText(this.getActivity(), "¡Introduce contraseña!", Toast.LENGTH_SHORT).show();
        }
        else if (password2.getText().toString().equals("")) {
            Toast.makeText(this.getActivity(), "¡Repite la contraseña!", Toast.LENGTH_SHORT).show();
        }
        else if (!password2.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(this.getActivity(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues valuesToStore = new ContentValues();
            valuesToStore.put("Username", String.valueOf(username.getText()));
            valuesToStore.put("Password", String.valueOf(password.getText()));
            valuesToStore.put("BestPoints", -1);
            valuesToStore.put("NotificationMode", 1);
            valuesToStore.put("Avatar",-1);
            bd.createUser(valuesToStore, "BD");

            Toast.makeText(this.getActivity(), "¡Usuario creado!", Toast.LENGTH_SHORT).show();
            username.setText("");
            password.setText("");
            password2.setText("");
        }
    }
}