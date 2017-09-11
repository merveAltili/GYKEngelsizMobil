package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.gelecegiyazanlar.mervegulsah.engelsizmobil.R.id.edtKullaniciAdi;

public class Post extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALERY_REQUEST=1;
    private EditText mEtkinlikAd;
    private EditText mAciklama;
    private Button mBtnEtkinlik;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private  Uri mImageUri = null;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mEtkinlikAd=(EditText)findViewById(R.id.edtEtkinlikAd) ;
        mAciklama=(EditText)findViewById(R.id.edtAciklama);
        mBtnEtkinlik=(Button)findViewById(R.id.btnEtkinlikOlustur);
        mSelectImage= (ImageButton) findViewById(R.id.imageSelect);
        mProgress=new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALERY_REQUEST);

                    }
                });
                mBtnEtkinlik.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPosting();
                    }
                });
            }

            private void startPosting() {

                final String etkinlikAd = mEtkinlikAd.getText().toString().trim();
                final String aciklama = mAciklama.getText().toString().trim();

                if (!TextUtils.isEmpty(etkinlikAd) && !TextUtils.isEmpty(aciklama) && mImageUri != null) {
                    mProgress.setMessage("Etkinlik oluşturuluyor ...");
                    mProgress.show();
                    StorageReference filepath = mStorage.child("Etkinlik resimleri").child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                          DatabaseReference newPost=database.getReference("Etkinlik");
                            Etkinlik etkinlik = new Etkinlik();
                            etkinlik.setEtkinlikAdi(etkinlikAd);
                            etkinlik.setEtkinlikİcerigi(aciklama);
                            etkinlik.setEtkinlikResmi(downloadUrl.toString());
                            newPost.child(newPost.push().getKey()).setValue(etkinlik);
                            mProgress.dismiss();
                            startActivity(new Intent(Post.this,Anasayfa.class));

                        }
                    });
                }

            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == GALERY_REQUEST && resultCode == RESULT_OK) {
                    mImageUri = data.getData();
                    mSelectImage.setImageURI(mImageUri);
                }
            }
        }


