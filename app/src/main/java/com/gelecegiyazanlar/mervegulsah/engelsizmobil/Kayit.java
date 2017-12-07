package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.Activity;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
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

public class Kayit extends Activity {
    private EditText edtKullaniciAdi, edtİsim, edtSoyisim, edtTelefon, edtMail, edtSifre;
    private Button btnKayıtOl;
    private ImageButton mImageBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference reference = database.getReference();
        final DatabaseReference membref=reference.child("Kullanıcılar");

        mAuth=mAuth.getInstance();


        edtKullaniciAdi = (EditText) findViewById(R.id.edtKullaniciAdi);
        edtİsim = (EditText) findViewById(R.id.edtİsim);
        edtSoyisim = (EditText) findViewById(R.id.edtSoyisim);
        edtTelefon = (EditText) findViewById(R.id.edtTelefon);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtSifre = (EditText) findViewById(R.id.edtSifre);
        edtSifre.setTypeface(Typeface.DEFAULT);

        mStorage = FirebaseStorage.getInstance().getReference();
        btnKayıtOl = (Button) findViewById(R.id.btnKayıtOl);

        btnKayıtOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//kayit
                final Kullanici k=new Kullanici();
                k.setKullaniciAdi(edtKullaniciAdi.getText().toString());
                k.setIsim(edtİsim.getText().toString());
                k.setSoyisim(edtSoyisim.getText().toString());
                k.setMail(edtMail.getText().toString());
                k.setTelefon(edtTelefon.getText().toString());
                k.setSifre(edtSifre.getText().toString());

                Toast.makeText(Kayit.this,"Kayit Başarılı..",Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog = ProgressDialog.show(Kayit.this,"Lütfen bekleyiniz..","İşlem Yapiliyor",true);


                (mAuth.createUserWithEmailAndPassword(edtKullaniciAdi.getText().toString(), edtSifre.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().getUid();
                                    membref.child(membref.push().getKey()).setValue(k);
                                    SignIn();
                                } else {
                                      Log.e("ERROR", task.getException().toString());
                                     // Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

               membref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren())
                        {
                            String key = data.getKey();
                            Kullanici K = new Kullanici();
                            K.setMail(dataSnapshot.child(key).getValue(Kullanici.class).getMail().toString());
                            String kmail = K.getMail().toString();

                            if(TextUtils.isEmpty(edtMail.getText()))
                            {
                                Toast.makeText(getApplicationContext(),"Lütfen kullanıcı adınızı değiştiriniz.",Toast.LENGTH_SHORT).show();
                                edtMail.setText("");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(TextUtils.isEmpty(edtKullaniciAdi.getText()) || TextUtils.isEmpty(edtİsim.getText()) ||
                        TextUtils.isEmpty(edtSoyisim.getText()) ||TextUtils.isEmpty(edtMail.getText()) ||
                        TextUtils.isEmpty(edtTelefon.getText()) || TextUtils.isEmpty(edtSifre.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Lütfen boş alanları doldurunuz.",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(isValid(edtMail.getText().toString()) == true)
                    {


                        if (!TextUtils.isEmpty(edtSifre.getText())) {

                            final Kullanici kullanici = new Kullanici();
                            kullanici.setKullaniciAdi(edtKullaniciAdi.getText().toString());
                            kullanici.setIsim(edtİsim.getText().toString());
                            kullanici.setSoyisim(edtSoyisim.getText().toString());
                            kullanici.setMail(edtMail.getText().toString());
                            kullanici.setTelefon(edtTelefon.getText().toString());
                            kullanici.setSifre(edtSifre.getText().toString());
                            membref.child(membref.push().getKey()).setValue(kullanici);

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
    public void SignIn(){
        final ProgressDialog progressDialog=ProgressDialog.show(Kayit.this,"Lütfen bekleyiniz..",
                /*processing*/"işlem..",true);
        (mAuth.signInWithEmailAndPassword(edtMail.getText().toString(),edtSifre.getText().toString())).
                addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){
                            Toast.makeText(Kayit.this,"Giriş Başarılı",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(Kayit.this,Anasayfa.class);
                            startActivity(i);

                            } else {
                                progressDialog.dismiss();
                                Intent i=new Intent(Kayit.this,Anasayfa.class);
                                startActivity(i);
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