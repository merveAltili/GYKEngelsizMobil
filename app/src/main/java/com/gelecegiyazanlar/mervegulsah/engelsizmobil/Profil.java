package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {

    RecyclerView recyclerEtkinlik,recyclerProfil;
    RecyclerView.LayoutManager LayoutManagerEtkinlik, LayoutManagerProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        recyclerEtkinlik = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        recyclerProfil = (RecyclerView) findViewById(R.id.recyclerProfil);
        LayoutManagerEtkinlik = new LinearLayoutManager(this);
        LayoutManagerProfil = new LinearLayoutManager(this);

        recyclerEtkinlik.setLayoutManager(LayoutManagerEtkinlik);
        recyclerProfil.setLayoutManager(LayoutManagerProfil);

        final ArrayList<Etkinlik> myEtkinlik = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("Etkinlik");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String key = data.getKey();
                    Etkinlik etkinlik = new Etkinlik();
                    etkinlik.setEtkinlikSaati(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikSaati());
                    etkinlik.setEtkinlikAdi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikAdi());
                    etkinlik.setEtkinlikİcerigi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikİcerigi());
                    etkinlik.setEtkinlikResmi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikResmi());
                    etkinlik.setContext(getApplicationContext());
                    myEtkinlik.add(etkinlik);
                    EtkinlikAdapter adapter = new EtkinlikAdapter(myEtkinlik);
                    recyclerEtkinlik.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final ArrayList<Kullanici> kullanicilar = new ArrayList<>();
       /* Bundle bundle = getIntent().getExtras();
        Kullanici kullanici = new Kullanici();
        kullanici.setKullaniciAdi(bundle.getString("Kullanici_Adi"));
        kullanici.setSifre(bundle.getString("Sifre"));
        kullanici.setResim(bundle.getString("Resim"));
        kullanici.setContext(getApplicationContext());
        kullanicilar.add(kullanici);*/

        KullaniciAdapter kAdapter = new KullaniciAdapter(kullanicilar);
        recyclerProfil.setAdapter(kAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
