package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EtkinlikDetay extends AppCompatActivity {

    private String mPost_key=null;
    private DatabaseReference mDatabase;
    private ImageView mEtkinlikDetayImage;
    private TextView mEtkinlikAd;
    private TextView mEtkinlikIcegigi;
    private Button mEtkinlikbtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detay);

        mAuth=FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mPost_key=getIntent().getExtras().getString("etkinlik_id");
        mEtkinlikIcegigi= (TextView) findViewById(R.id.etkinlik_detay_icerigi);
        mEtkinlikDetayImage= (ImageView) findViewById(R.id.etkinlik_detay_image);
        mEtkinlikAd= (TextView) findViewById(R.id.etkinlik_detay_ad);
       // mEtkinlikbtn=(Button)findViewById(R.id.singleRemoveBtn);

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_ead= (String) dataSnapshot.child("etkinlikAdi").getValue();
                String post_eicerigi= (String) dataSnapshot.child("etkinlikİcerigi").getValue();
                String post_eresmi= (String) dataSnapshot.child("etkinlikResmi").getValue();

                mEtkinlikAd.setText(post_ead);
                mEtkinlikIcegigi.setText(post_eicerigi);

                Picasso.with(EtkinlikDetay.this).load(post_eresmi).into(mEtkinlikDetayImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
