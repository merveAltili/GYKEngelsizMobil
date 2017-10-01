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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference mDatabaseKatil;
    private DatabaseReference mDatabaseCurrentKullanici;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    public ProgressDialog mProgress;
    private Query mQueryCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu=false;
    private  String userId;


    RecyclerView recyclerEtkinlik,recyclerProfil,recyclerKullaniciAdi;
    EditText edtKullaniciAdi2;
    EditText txtKullaniciAdi;
    RecyclerView.LayoutManager LayoutManagerEtkinlik, LayoutManagerProfil,LayoutManagerKullaniciAdi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);




        edtKullaniciAdi2= (EditText) findViewById(R.id.giriskullaniciad2);
        txtKullaniciAdi= (EditText) findViewById(R.id.txtKullaniciAdi);
        //DatabaseReference mchild=databaseReference.child("kullaniciAdi");

         //      txtKullaniciAdi.setText(edtKullaniciAdi2.getText().toString() );


        mAuth =FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(Profil.this,GirisKullanici.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        recyclerKullaniciAdi= (RecyclerView) findViewById(R.id.recyclerKullaniciAdi2);
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

                 //   kullanici.setKullaniciAdi(dataSnapshot.child(key).getValue(Kullanici.class).getKullaniciAdi());
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




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
