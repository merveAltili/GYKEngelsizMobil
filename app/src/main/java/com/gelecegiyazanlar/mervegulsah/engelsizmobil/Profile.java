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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ImageView imgProfil;
    TextView txtPuan;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfil = (ImageView)findViewById(R.id.imgProfil);
        txtPuan = (TextView)findViewById(R.id.txtPuan);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Etkinlik e1 = new Etkinlik();
        e1.setEtkinlikAdi("Yardım");
        e1.setEtkinlikİcerigi("Yardım1");
        e1.setEtkinlikSaati("20:00");
        Etkinlik e2 = new Etkinlik();
        e2.setEtkinlikAdi("Yardim2");
        e2.setEtkinlikİcerigi("Yardım2");
        e2.setEtkinlikSaati("08:00");

        final ArrayList<Etkinlik> myEtkinlik = new ArrayList<>();
        //myEtkinlik.add(e1);
        //myEtkinlik.add(e2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("Etkinlik");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String key = data.getKey();
                    Etkinlik etkinlik = new Etkinlik();
                    etkinlik.setEtkinlikSaati("08:00");
                    etkinlik.setEtkinlikAdi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikAdi());
                    etkinlik.setEtkinlikİcerigi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikİcerigi());
                    myEtkinlik.add(etkinlik);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        EtkinlikAdapter etkinlikAdapter = new EtkinlikAdapter(myEtkinlik);
        mRecyclerView.setAdapter(etkinlikAdapter);
    }
}
