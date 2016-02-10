package com.example.shine.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class Songlist extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclermp);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mLinearLayout = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<String> x = null;
        if(bundle != null){
            x = bundle.getStringArrayList("tosend1");
            Log.v("bundle", x.toString());
        }
        else {
            Log.v("bundle", "bundle is null");
        }
        MyCustomAdapter2 myca = new MyCustomAdapter2(x);
        mRecyclerView.setAdapter(myca);
    }
}
