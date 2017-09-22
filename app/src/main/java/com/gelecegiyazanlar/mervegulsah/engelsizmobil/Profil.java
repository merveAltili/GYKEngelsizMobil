package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKatil;
    private DatabaseReference mDatabaseCurrentKullanici;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private Query mQueryCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu=false;
    RecyclerView recyclerEtkinlik,recyclerProfil,recyclerKullaniciAdi;
    RecyclerView.LayoutManager LayoutManagerEtkinlik, LayoutManagerProfil,LayoutManagerKullaniciAdi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        mAuth =FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(Profil.this,Giris.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        recyclerKullaniciAdi= (RecyclerView) findViewById(R.id.recyclerKullaniciAdi);
        recyclerEtkinlik = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        recyclerProfil = (RecyclerView) findViewById(R.id.recyclerProfil);
        LayoutManagerKullaniciAdi=new LinearLayoutManager(this);
        LayoutManagerEtkinlik = new LinearLayoutManager(this);
        LayoutManagerProfil = new LinearLayoutManager(this);

        recyclerEtkinlik.setLayoutManager(LayoutManagerEtkinlik);
        recyclerProfil.setLayoutManager(LayoutManagerProfil);
        recyclerKullaniciAdi.setLayoutManager(LayoutManagerKullaniciAdi);

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
        /*
      Bundle bundle = getIntent().getExtras();
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
