package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {

    ImageView imgEtkinlik;
    TextView txtPuan;
    RecyclerView recyclerEtkinlik;
    RecyclerView.LayoutManager mLayoutManager;
    LinearLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        imgEtkinlik = (ImageView)findViewById(R.id.imgEtkinlik);
        txtPuan = (TextView)findViewById(R.id.txtPuan);
        recyclerEtkinlik = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        recyclerEtkinlik.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(this);
       llm = new LinearLayoutManager(getApplicationContext());

        recyclerEtkinlik.setLayoutManager(mLayoutManager);

/*
      Etkinlik mEtkinlik1 = new Etkinlik("Etkinlik1 hakkında bilgi","Gulsah Coban");
        Etkinlik mEtkinlik2 = new Etkinlik("Etkinlik2 hakkında bilgi","Merve Altılı");
        ArrayList<Etkinlik> myEtkinlik = new ArrayList<>();
        myEtkinlik.add(mEtkinlik1);
        myEtkinlik.add(mEtkinlik2);
        myEtkinlik.add(mEtkinlik1);
        myEtkinlik.add(mEtkinlik2);
        myEtkinlik.add(mEtkinlik1);
        myEtkinlik.add(mEtkinlik2);

       EtkinlikAdapter eAdapter = new EtkinlikAdapter(myEtkinlik);
      recyclerEtkinlik.setAdapter(eAdapter);*/
    }
}
