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

    private static final int RC_SIGN_IN=1;
    private ImageButton Mimggoogle;
    private static final String TAG="Giris";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mData;
    private String imgProfilResmi;
    TextView kayit;
    EditText giriskullaniciad,girissifre;
    Button  girisbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        mAuth = FirebaseAuth.getInstance();

        mData = FirebaseDatabase.getInstance().getReference().child("kullaniciAdi");
        mData.keepSynced(true);

        mProgress = new ProgressDialog(this);
        giriskullaniciad = (EditText) findViewById(R.id.giriskullaniciad);
        girissifre = (EditText) findViewById(R.id.girissifre);
        girissifre.setTypeface(Typeface.DEFAULT);
        girisbutton = (Button) findViewById(R.id.girisbutton);
        //  imgfacebook= (ImageButton) findViewById(R.id.imgfacebook);
        // imgtwitter= (ImageButton) findViewById(R.id.imgtwitter);
        Mimggoogle = (ImageButton) findViewById(R.id.imggoogle);
        kayit = (TextView) findViewById(R.id.edtkayit);
        girisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kullaniciAdi = giriskullaniciad.getText().toString();
                final String sifre = girissifre.getText().toString();
                if (!kullaniciAdi.equals("") || !sifre.equals("")) {
                    mProgress.setMessage("Giriş Yapılıyor...");
                    mProgress.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Kullanıcılar");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                Kullanici kullanici = new Kullanici();
                                kullanici.setKullaniciAdi(dataSnapshot.child(key).getValue(Kullanici.class).getKullaniciAdi());
                                kullanici.setSifre(dataSnapshot.child(key).getValue(Kullanici.class).getSifre());
                                kullanici.setTelefon(dataSnapshot.child(key).getValue(Kullanici.class).getTelefon());
                                kullanici.setIsim(dataSnapshot.child(key).getValue(Kullanici.class).getIsim());
                                kullanici.setMail(dataSnapshot.child(key).getValue(Kullanici.class).getMail());
                                kullanici.setSoyisim(dataSnapshot.child(key).getValue(Kullanici.class).getSoyisim());
                                kullanici.setDernek_gönüllü(dataSnapshot.child(key).getValue(Kullanici.class).getDernek_gönüllü());
                                kullanici.setResim(dataSnapshot.child(key).getValue(Kullanici.class).getResim());
                                imgProfilResmi = kullanici.getResim();
                                if (kullaniciAdi.equals(kullanici.getKullaniciAdi()) && sifre.equals(kullanici.getSifre()))
                                {
                                    if(kullanici.getDernek_gönüllü().equals("Gönüllü"))
                                    {
                                        mProgress.dismiss();
                                        Intent intocan = new Intent(getApplicationContext(),Anasayfa.class);
                                        //intocan.putExtra("Kullanıcı Adı",kullanici.getKullaniciAdi());
                                        //intocan.putExtra("Şifre",kullanici.getSifre());
                                       // intocan.putExtra("Resim",kullanici.getResim());
                                       // intocan.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intocan);
                                        //finish();
                                    }
                                    else
                                    {
                                        mProgress.dismiss();
                                        Intent intocan = new Intent(getApplicationContext(),Anasayfa.class);
                                        //intocan.putExtra("Kullanici_Adi",kullanici.getKullaniciAdi());
                                        //intocan.putExtra("Sifre",kullanici.getSifre());
                                        //intocan.putExtra("Resim",kullanici.getResim());
                                       // intocan.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intocan);

                                        //finish();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    if (kullaniciAdi.equals("")) {
                        Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adınızı giriniz.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lütfen şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Mimggoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Giris.this,Kayit.class));
            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mProgress.setMessage("Giriş Yapılıyor ...");
            mProgress.show();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
              Intent intocan = new Intent(getApplicationContext(),Anasayfa.class);
                startActivity(intocan);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                mProgress.dismiss();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(Giris.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Giris.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        mProgress.show();
                        // ...
                    }
                });
    }



}
