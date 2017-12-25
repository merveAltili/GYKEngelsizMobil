package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Giris extends AppCompatActivity {

    private static final String TAG="Giris";

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mData;
    private DatabaseReference mDatabaseKullanici;
    EditText giriskullaniciad2,girissifre2;
    Button  girisbutton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        mAuth = FirebaseAuth.getInstance();

        mData = FirebaseDatabase.getInstance().getReference().child("Dernekler");
        mData.keepSynced(true);
        mProgress = new ProgressDialog(this);
        giriskullaniciad2 = (EditText) findViewById(R.id.giriskullaniciad);
        girissifre2 = (EditText) findViewById(R.id.girissifre);
        girissifre2.setTypeface(Typeface.DEFAULT);
        girisbutton2 = (Button) findViewById(R.id.girisbutton);
        mDatabaseKullanici=FirebaseDatabase.getInstance().getReference().child("Dernekler");
        final DatabaseReference newpost=mData.push();
        final DatabaseReference newpost2=mDatabaseKullanici.push();

        //  imgfacebook= (ImageButton) findViewById(R.id.imgfacebook);
        // imgtwitter= (ImageButton) findViewById(R.id.imgtwitter);
        //  Mimggoogle = (ImageButton) findViewById(R.id.imggoogle);
        //   kayit = (TextView) findViewById(R.id.edtkayit);
        girisbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String dernekAdi = giriskullaniciad2.getText().toString();
                final String sifre = girissifre2.getText().toString();

                if (!dernekAdi.equals("") || !sifre.equals("")) {
                    mProgress.setMessage("Giriş Yapılıyor...");
                    mProgress.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Dernekler");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String key = data.getKey();

                                Dernek dernek = new Dernek();

                                dernek.setDernekAdi(dataSnapshot.child(key).getValue(Dernek.class).getDernekAdi());
                                dernek.setSifre(dataSnapshot.child(key).getValue(Dernek.class).getSifre());
                                if(!((dernekAdi.equals(dernek.getDernekAdi())) && (sifre.equals(dernek.getSifre()))))
                                {
                                    mProgress.dismiss();

                                }

                                if (dernekAdi.equals(dernek.getDernekAdi()) && sifre.equals(dernek.getSifre()))
                                {
                                    //   d.setDernekAdi(dataSnapshot.child(key).getValue(Dernek.class).getDernekAdi());
                                    //  mDatabaseKullanici.child( mDatabaseKullanici.push().getKey()).setValue(d);
                                    mProgress.dismiss();

                                    Intent intocan = new Intent(Giris.this,AnasayfaDernek.class);


                                    intocan.putExtra("Dernek Adi",dernek.getDernekAdi());
                                    intocan.putExtra("Sifre",dernek.getSifre());

                                    mDatabaseKullanici.child(mAuth.getCurrentUser().getUid()).child("uid").setValue(mAuth.getCurrentUser().getUid());
                                    mDatabaseKullanici.child(mAuth.getCurrentUser().getUid()).child("dernekAdi").setValue(dataSnapshot.child(key).getValue(Dernek.class).getDernekAdi());
                                    mDatabaseKullanici.child(mAuth.getCurrentUser().getUid()).child("sifre").setValue(dataSnapshot.child(key).getValue(Dernek.class).getSifre());
                                    intocan.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intocan);
                                    finish();


                                }



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }

                else {
                    if (dernekAdi.equals("") || sifre.equals("")) {
                        Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adınızı giriniz.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lütfen şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }





}