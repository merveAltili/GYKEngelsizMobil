package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KayitActivity extends AppCompatActivity {
    EditText edtKullaniciAdi,edtİsim,edtSoyisim,edtTelefon,edtMail,edtSifre;
    RadioButton rdoDernek,rdoGönüllü;
    Button btnKayıtOl;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);


        mProgress=new ProgressDialog(this);
        edtKullaniciAdi = (EditText)findViewById(R.id.edtKullaniciAdi);
        edtİsim = (EditText)findViewById(R.id.edtİsim);
        edtSoyisim = (EditText)findViewById(R.id.edtSoyisim);
        edtTelefon = (EditText)findViewById(R.id.edtTelefon);
        edtMail = (EditText)findViewById(R.id.edtMail);
        edtSifre = (EditText)findViewById(R.id.edtSifre);

        edtSifre.setTypeface(Typeface.DEFAULT);
        rdoDernek = (RadioButton)findViewById(R.id.rdoDernek);
        rdoGönüllü = (RadioButton)findViewById(R.id.rdoGönüllü);

        btnKayıtOl = (Button)findViewById(R.id.btnKayıtOl);
        btnKayıtOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String kullaniciAdi = edtKullaniciAdi.getText().toString().trim() ;
                String isim = edtİsim.getText().toString().trim();
                String soyisim = edtSoyisim.getText().toString().trim();
                String mail = edtMail.getText().toString().trim();
                String telefon = edtTelefon.getText().toString().trim();
                String sifre = edtSifre.getText().toString().trim();




                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Kullanıcılar");
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

                    if(rdoDernek.isChecked() == false && rdoGönüllü.isChecked() == false)
                    {
                        Toast.makeText(getApplicationContext(),"Lütfen Dernek/Gönüllü seçimini yapınız.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (!sifre.equals("")) {

                                Kullanici kullanici = new Kullanici();
                                kullanici.setKullaniciAdi(kullaniciAdi);
                                kullanici.setIsim(isim);
                                kullanici.setSoyisim(soyisim);
                                kullanici.setMail(mail);
                                kullanici.setTelefon(telefon);
                                kullanici.setSifre(sifre);
                                if(rdoDernek.isChecked())
                                {
                                    kullanici.setDernek_gönüllü("Dernek");
                                }
                                else
                                {
                                    kullanici.setDernek_gönüllü("Gönüllü");
                                }


                            reference.child(reference.push().getKey()).setValue(kullanici);
                                Intent i = new Intent(getApplicationContext(), Giris.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        else {
                            Toast.makeText(getApplicationContext(), "Lütfen şifreleri aynı giriniz.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
