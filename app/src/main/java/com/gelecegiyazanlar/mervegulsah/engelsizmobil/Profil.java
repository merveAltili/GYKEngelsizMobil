package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {

    ImageView imgProfil;
    TextView txtPuan;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        imgProfil = (ImageView)findViewById(R.id.imgProfil);
        txtPuan = (TextView)findViewById(R.id.txtPuan);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

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
                    mRecyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
