package com.example.shine.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class Ranking extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private SharedPreferences settings;
    private MyCustomAdapter myca;
    private BD bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_ranking, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mRecyclerView);

        mLinearLayout = new LinearLayoutManager(rootView.getContext());

        mRecyclerView.setLayoutManager(mLinearLayout);

        settings = this.getActivity().getSharedPreferences("Usuario", 0);
        myca = new MyCustomAdapter(rellena(),settings.getString("Username",""));
        mRecyclerView.setAdapter(myca);

        setHasOptionsMenu(true);
        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mlogout:
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
            case R.id.mreset:
                confirmarReset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmarReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Estás seguro de que quieres resetear tus puntuaciones?");
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        SharedPreferences.Editor editor = settings.edit();
                        Toast.makeText(getActivity(), "Puntuaciones reseteadas", Toast.LENGTH_LONG).show();
                        editor.putInt("BestPoints", -1);
                        editor.apply();
                        MyTask task = new MyTask();
                        task.execute();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wich) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<InfoUser> rellena() {
        ArrayList<InfoUser>  IU = new ArrayList<>();
        bd = new BD(getContext());
        Cursor c = bd.getAll();
        c.moveToFirst();
        int i = 0;
        while (!c.isAfterLast()) {
            if (c.getInt(2) == -1) IU.add(new InfoUser(c.getString(4), c.getString(0), c.getInt(2)));
            else {
                IU.add(i, new InfoUser(c.getString(4), c.getString(0), c.getInt(2)));
                ++i;
            }
            c.moveToNext();
        }
        return IU;
    }

    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            bd = new BD(getContext());
            ContentValues cv = new ContentValues();
            cv.put("BestPoints", -1);
            bd.setPoints(settings.getString("Username", ""), cv);
            bd.close();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            myca.setData(rellena());
            ((PerfilRanking)getActivity()).onResume();
            super.onPostExecute(s);
        }
    }
}
