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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorage;


    private Button btnKayit,btnResmEkle;
    private String userName;
    private String userPassword;
    private StorageReference mStorageRef;
    private static final int GALLERY_INTENT = 2;
    private FirebaseAuth mAuth;
    private ImageButton ivProfil;
    private String CurrentImgPath="-";
    private Uri imageUri;

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
        userName=edtMail.getText().toString();
        userPassword=edtSifre.getText().toString();

        ivProfil=(ImageButton) findViewById(R.id.imgResim1);
        mStorage = FirebaseStorage.getInstance().getReference();
        btnKayıtOl = (Button) findViewById(R.id.btnKayıtOl);


        btnKayıtOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//kayit


final Kullanici k2=new Kullanici();

                Toast.makeText(Kayit.this, "Kayit Başarılı..", Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog = ProgressDialog.show(Kayit.this, "Lütfen bekleyiniz..", "İşlem Yapiliyor", true);
                (mAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtSifre.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {

                                    k2.setKid(mAuth.getCurrentUser().getUid());
                                    SignIn();
                                } else {
                                    Log.e("ERROR", task.getException().toString());
                                    // Toast.makeText(Kayit.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        });
        }

        });

        ivProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }
    public void SignIn(){
        final ProgressDialog progressDialog=ProgressDialog.show(Kayit.this,"Lütfen bekleyiniz..",
                /*processing*/"işlem yapılıyor ...",true);
        (mAuth.signInWithEmailAndPassword(edtMail.getText().toString(),edtSifre.getText().toString())).
                addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

        if(task.isSuccessful()){

        if(imageUri!=null){
            mStorageRef = FirebaseStorage.getInstance().getReference();
final StorageReference filepath=mStorageRef.child("Profile")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        filepath.putFile(imageUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
@Override
public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot){

@SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
    final Kullanici k = new Kullanici();
    k.setKullaniciAdi(edtKullaniciAdi.getText().toString());
    k.setIsim(edtİsim.getText().toString());
    k.setSoyisim(edtSoyisim.getText().toString());
    k.setMail(edtMail.getText().toString());
    k.setTelefon(edtTelefon.getText().toString());
    k.setSifre(edtSifre.getText().toString());
    k.setKid(mAuth.getCurrentUser().getUid());
    k.setResim(downloadUrl.toString());

    final DatabaseReference membref=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");

    membref.child(mAuth.getCurrentUser().getUid()).setValue(k);


        Picasso.with(ivProfil.getContext()).load(downloadUrl.toString()).into(ivProfil);


        progressDialog.dismiss();

        Toast.makeText(Kayit.this,"Giriş Başarılı ",Toast.LENGTH_LONG).show();
        Intent i=new Intent(Kayit.this,Anasayfa.class);
        startActivity(i);
        }
        });
        }else{
        progressDialog.dismiss();
        Intent i=new Intent(Kayit.this,Anasayfa.class);
        startActivity(i);
        }


        }else{
        Log.e("ERROR",task.getException().toString());
        Toast.makeText(Kayit.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode== RESULT_OK ){
            imageUri = data.getData();

        }
    }
}


