package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kayit extends AppCompatActivity {
    EditText edtKullaniciAdi, edtİsim, edtSoyisim, edtTelefon, edtMail, edtSifre;
    Button btnKayıtOl;
    int number;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseKullanici;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mData;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference dtRef=database.getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentKullanici=mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(Kayit.this, GirisKullanici.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mData = FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseKullanici = FirebaseDatabase.getInstance().getReference().child("kullaniciAdi");

        mProgress = new ProgressDialog(this);
        edtKullaniciAdi = (EditText) findViewById(R.id.edtKullaniciAdi);
        edtİsim = (EditText) findViewById(R.id.edtİsim);
        edtSoyisim = (EditText) findViewById(R.id.edtSoyisim);
        edtTelefon = (EditText) findViewById(R.id.edtTelefon);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtSifre = (EditText) findViewById(R.id.edtSifre);
        edtSifre.setTypeface(Typeface.DEFAULT);

        mStorage = FirebaseStorage.getInstance().getReference();

        Random random = new Random();
        number = (random.nextInt(100) + 1);

        btnKayıtOl = (Button) findViewById(R.id.btnKayıtOl);
        btnKayıtOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//kayit
                final String kullaniciAdi = edtKullaniciAdi.getText().toString().trim() ;
                final String isim = edtİsim.getText().toString().trim();
                final String soyisim = edtSoyisim.getText().toString().trim();
                final String mail = edtMail.getText().toString().trim();
                final String telefon = edtTelefon.getText().toString().trim();
                final String sifre = edtSifre.getText().toString().trim();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference("Kullanıcılar");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren())
                        {
                            String key = data.getKey();
                            Kullanici KullaniciAdi = new Kullanici();
                            KullaniciAdi.setKullaniciAdi(dataSnapshot.child(key).getValue(Kullanici.class).getKullaniciAdi().toString());
                            String kAdi = KullaniciAdi.getKullaniciAdi().toString();

                            if(kullaniciAdi.equals(kAdi))
                            {
                                Toast.makeText(getApplicationContext(),"Lütfen kullanıcı adınızı değiştiriniz.",Toast.LENGTH_SHORT).show();
                                edtKullaniciAdi.setText("");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(kullaniciAdi.equals("") || isim.equals("") || soyisim.equals("") || mail.equals("") || telefon.equals("") || sifre.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Lütfen boş alanları doldurunuz.",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(isValid(mail) == true)
                    {


                            if (!sifre.equals("")) {

                                final Kullanici kullanici = new Kullanici();
                                kullanici.setKullaniciAdi(kullaniciAdi);
                                kullanici.setIsim(isim);
                                kullanici.setSoyisim(soyisim);
                                kullanici.setMail(mail);
                                kullanici.setTelefon(telefon);
                                kullanici.setSifre(sifre);
                                reference.child(reference.push().getKey()).setValue(kullanici);

                                Intent i = new Intent(getApplicationContext(), GirisKullanici.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Lütfen şifreleri aynı giriniz.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    else
                    {
                        Toast.makeText(getApplicationContext(),"Lütfen mail adresinizi doğru giriniz.",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    public static boolean isValid(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        else{
            return false;
        }
    }


}