package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static com.gelecegiyazanlar.mervegulsah.engelsizmobil.R.id.edtKullaniciAdi;
import static com.gelecegiyazanlar.mervegulsah.engelsizmobil.R.id.post_username;

public class Post extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALERY_REQUEST=1;
    private EditText mEtkinlikAd;
    private EditText mAciklama;
    private TextView mKullaniciAdi;
    private Button mBtnEtkinlik;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private  Uri mImageUri = null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentKullanici;
    private DatabaseReference mDatabaseKullanici;
    private DatabaseReference mDataba;
    Dernek dernek2 = new Dernek();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mAuth=FirebaseAuth.getInstance();
        mCurrentKullanici=mAuth.getCurrentUser();

        mDataba=FirebaseDatabase.getInstance().getReference().child("Dernekler");
        mDataba.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key2 = data.getKey();


                    dernek2.setDernekAdi(dataSnapshot.child(key2).getValue(Dernek.class).getDernekAdi());

                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabaseKullanici=FirebaseDatabase.getInstance().getReference().child("Dernekler").child(mAuth.getCurrentUser().getUid()) ;

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mEtkinlikAd=(EditText)findViewById(R.id.edtEtkinlikAd) ;
        mAciklama=(EditText)findViewById(R.id.edtAciklama);
        mKullaniciAdi=(TextView)findViewById(R.id.post_username);
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

               // final String kullaniciAdi =mKullaniciAdi.getText().toString().trim() ;

                if (!TextUtils.isEmpty(etkinlikAd) && !TextUtils.isEmpty(aciklama) && mImageUri != null) {
                    mProgress.setMessage("Etkinlik oluşturuluyor ...");
                    mProgress.show();
                    StorageReference filepath = mStorage.child("Etkinlik resimleri").child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            final DatabaseReference newPost=mDatabase.push();

                            final DatabaseReference newpost2=mDatabaseKullanici.push();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();



                            mDatabaseKullanici.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                        newPost.child("etkinlikAdi").setValue(etkinlikAd);
                                        newPost.child("etkinlikİcerigi").setValue(aciklama);
                                        newPost.child("etkinlikResmi").setValue(downloadUrl.toString());
                                        newPost.child("uid").setValue(mCurrentKullanici.getUid());
                                        newPost.child("username").setValue(dataSnapshot.child("dernekAdi").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent i=new Intent(Post.this, Anasayfa.class);
                                                    startActivity(i);
                                                    i.putExtra("Dernek Adi",dernek2.getDernekAdi());
                                                    i.putExtra("Sifre",dernek2.getSifre());
                                                  }
                                            }
                                        });


                                    }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                           //DatabaseReference newPost=database.getReference("Etkinlik");
                           //Etkinlik etkinlik = new Etkinlik();
                           //etkinlik.setEtkinlikAdi(etkinlikAd);
                           //etkinlik.setEtkinlikİcerigi(aciklama);
                           //etkinlik.setEtkinlikResmi(downloadUrl.toString());
                           //newPost.child(newPost.push().getKey()).setValue(etkinlik);

                            mProgress.dismiss();


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


