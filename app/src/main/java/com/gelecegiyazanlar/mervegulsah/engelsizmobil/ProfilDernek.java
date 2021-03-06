package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfilDernek extends AppCompatActivity {

    private StorageReference mStorageRef;
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers2;
    private DatabaseReference mDatabaseDernek2;
    private DatabaseReference mDatabaseKullanici2;
    private DatabaseReference mDatabaseKatil;
    private FirebaseUser mCurrentKullanici;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private Query mQueryCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu = false;
    private ImageButton ivUser;
    private static final int GALLERY_INTENT = 2;
    private String CurrentImgPath = "-";
    private EditText txtKullaniciAdi3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_dernek);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent lIntent = new Intent(ProfilDernek.this, GirisKullanici.class);
                    lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(lIntent);
                }
            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("DernekEtkinlik").child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);
        mDatabaseUsers2=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        mDatabaseUsers2.keepSynced(true);
        mDatabaseDernek2=FirebaseDatabase.getInstance().getReference().child("Dernekler");
        mDatabaseDernek2.keepSynced(true);
        mDatabaseKatil = FirebaseDatabase.getInstance().getReference().child("Katilan");

        //mDatabaseCurrentKullanici= FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");

        txtKullaniciAdi3= (EditText) findViewById(R.id.txtKullaniciAdi3);
        mDatabaseKatil.keepSynced(true);
        mEtkinlikBlog = (RecyclerView) findViewById(R.id.etkinlikler_list);
        mEtkinlikBlog.setHasFixedSize(true);
        mEtkinlikBlog.setLayoutManager(new LinearLayoutManager(this));

        mStorageRef = FirebaseStorage.getInstance().getReference();
        ivUser = (ImageButton) findViewById(R.id.imgProfilResmi2);

        mDatabaseKullanici2=FirebaseDatabase.getInstance().getReference().child("Dernekler").child(mAuth.getCurrentUser().getUid()) ;


        final StorageReference filepath = mStorageRef.child("Profil resimleri").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ivUser.getContext()).load(uri.toString()).into(ivUser);

            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }


        });


        mDatabaseDernek2.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dernek d=dataSnapshot.getValue(Dernek.class);
                //  mDatabaseDernek.child(mAuth.getCurrentUser().getUid()).child("dernekProfil").setValue(FirebaseStorage.getInstance().getReference().child("Profil resimleri").getDownloadUrl().toString());
                txtKullaniciAdi3.setText(d.getDernekAdi()+" Derneği");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Etkinlik,ProfilDernek.EtkinlikViewHolder4> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Etkinlik, ProfilDernek.EtkinlikViewHolder4>(

                Etkinlik.class,
                R.layout.profil_row,
                ProfilDernek.EtkinlikViewHolder4.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ProfilDernek.EtkinlikViewHolder4 viewHolder, final Etkinlik model, int position) {
                final String post_key=getRef(position).getKey();

                Kullanici kul=new Kullanici();


                viewHolder.setEtkinlikAdi(model.getEtkinlikAdi());
                viewHolder.setAciklama(model.getEtkinlikİcerigi());
                viewHolder.setImage(getApplicationContext(),model.getEtkinlikResmi());
                viewHolder.setmKatil(post_key);
                viewHolder.setUserName(model.getUsername());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(Anasayfa.this,post_key,Toast.LENGTH_LONG).show();

                        Intent etkinlikDetay=new Intent(ProfilDernek.this, EtkinlikDetay.class);
                        etkinlikDetay.putExtra("etkinlik_id",post_key);
                        startActivity(etkinlikDetay);
                    }
                });

                viewHolder.mKatil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mKatilmaDurumu=true;

                        mDatabaseKatil.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (mKatilmaDurumu) {


                                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(post_key)) {
                                        mDatabaseKatil.child(mAuth.getCurrentUser().getUid()).child(post_key).removeValue();
                                        mKatilmaDurumu = false;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                });
            }
        };
        mEtkinlikBlog.setAdapter(firebaseRecyclerAdapter);
    }



    public static class EtkinlikViewHolder4 extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mKatil;
        DatabaseReference mDatabaseKatil;
        FirebaseAuth mAuth;

        public void  setmKatil(final String kul_id){

            mDatabaseKatil.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(kul_id)){
                        mKatil.setImageResource(R.drawable.ic_tik_kirmizi_24dp);

                    }else{

                        mKatil.setImageResource(R.drawable.ic_tik_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public EtkinlikViewHolder4(View itemView) {
            super(itemView);
            mView=itemView;
            mKatil=(ImageButton)mView.findViewById(R.id.btnKatil3);
            mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
            mAuth=FirebaseAuth.getInstance();

            mDatabaseKatil.keepSynced(true);

        }
        public void setEtkinlikAdi(String title){
            TextView etkinlik_adi=(TextView)mView.findViewById(R.id.etkinlik_adip);
            etkinlik_adi.setText(title);
        }
        public void setAciklama(String aciklama){
            TextView e_aciklama=(TextView)mView.findViewById(R.id.etkinlik_aciklamap);
            e_aciklama.setText(aciklama);
        }
        public void setUserName(String username){
            TextView e_username=(TextView)mView.findViewById(R.id.post_usernamep);
            e_username.setText(username);
        }
        public void setImage(Context ctx, String image){
            ImageView e_image=(ImageView)mView.findViewById(R.id.etkinlik_imagep);
            Picasso.with(ctx).load(image).into(e_image);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intocan = new Intent();

        if (item.getItemId() == R.id.action_logout2) {
            logout();
        }

        if (item.getItemId() == R.id.action_profil2) {

            Intent into = new Intent(ProfilDernek.this, ProfilDernek.class);

         /* Bundle bundle = getIntent().getExtras();
            Kullanici kullanici = new Kullanici();
            kullanici.setKullaniciAdi(bundle.getString("Kullanici_Adi"));
            kullanici.setSifre(bundle.getString("Sifre"));
            kullanici.setResim(bundle.getString("Resim"));
            into.putExtra("Kullanici_Adi",kullanici.getIsim());
            into.putExtra("Sifre",kullanici.getSifre());
            into.putExtra("Resim",kullanici.getResim());*/
            startActivity(into);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        Intent into = new Intent(getApplicationContext(), GirisKullanici.class);
        into.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(into);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            final Uri uri = data.getData();

            final StorageReference filepath = mStorageRef.child("Profil resimleri").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            filepath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost2=mDatabaseDernek2.push();
                            mDatabaseDernek2.child(mAuth.getCurrentUser().getUid()).child("dernekResmi").setValue(downloadUrl.toString());
                            CurrentImgPath = downloadUrl.toString();
                            Picasso.with(ivUser.getContext()).load(CurrentImgPath).into(ivUser);

                            Toast.makeText(ProfilDernek.this, "Yükleme Tamamlandı", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    }
