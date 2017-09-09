package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Post extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALERY_REQUEST=1;
    private EditText mEtkinlikAd;
    private EditText mAciklama;
    private Button mBtnEtkinlik;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private  Uri mImageUri =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorage= FirebaseStorage.getInstance().getReference();
        mEtkinlikAd=(EditText)findViewById(R.id.edtEtkinlikAd) ;
        mAciklama=(EditText)findViewById(R.id.edtAciklama);
        mBtnEtkinlik=(Button)findViewById(R.id.btnEtkinlikOlustur);
        mSelectImage= (ImageButton) findViewById(R.id.imageSelect);
        mProgress=new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALERY_REQUEST);

            }
        });
        mBtnEtkinlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }
    private void startPosting(){
        mProgress.setMessage("Etkinlik oluşturuluyor ...");
        mProgress.show();
        String etkinlik_Ad=mEtkinlikAd.getText().toString().trim();
        String aciklama=mAciklama.getText().toString().trim();

        if(!TextUtils.isEmpty(etkinlik_Ad) && !TextUtils.isEmpty(aciklama)&& mImageUri!=null)
        {
            StorageReference filepath=mStorage.child("Etkinlik_resimleri").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mProgress.dismiss();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==GALERY_REQUEST && resultCode== RESULT_OK)
    {
       mImageUri=data.getData();
        mSelectImage.setImageURI(mImageUri);
    }
    }
}
