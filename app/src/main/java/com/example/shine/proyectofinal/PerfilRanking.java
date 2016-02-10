package com.example.shine.proyectofinal;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;

public class PerfilRanking extends AppCompatActivity {

    PagerAdapter mPagerAdapter;
    @Override
    protected void onResume() {
        super.onResume();
        if (!(mPagerAdapter == null)) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilranking);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int perfil = bundle.getInt("who");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), PerfilRanking.this);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(perfil);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(Color.DKGRAY, Color.BLACK);
        tabLayout.setupWithViewPager(viewPager);
    }


}

