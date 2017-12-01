package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.health.PidHealthStats;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.zzbjp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profil extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu = false;
    private static final int GALLERY_INTENT = 2;

    private DatabaseReference dbref;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKullanici;
    private FirebaseUser mCurrentKullanici;

    private String CurrentImgPath = "-";
    private ImageView imgResim = null;
    private ImageView profilresm;
    private Etkinlik e = new Etkinlik();
    private String user_name;
    private Uri imageuri;
    private  StorageReference mStorageRef;
    Button btnResimEkle;
    TextView txtetkinlikAdi, txtetkinlikresmi, txtetkinlikicerigi;
    ImageButton imgProfilResmi;
    RecyclerView recyclerEtkinlik, recyclerProfil, recyclerKullaniciAdi;
    EditText edtKullaniciAdi2;
    TextView TxtKullaniciAdi;
    RecyclerView.LayoutManager LayoutManagerEtkinlik, LayoutManagerProfil, LayoutManagerKullaniciAdi;
    private String mPost_key=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        IDyazdir();

        mAuth = FirebaseAuth.getInstance();
        mCurrentKullanici = mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseKullanici=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mkatildiklarimRef = firebaseDatabase.getReference();
       // mPost_key=getIntent().getExtras().getString("kul_id");
        final ArrayList<Etkinlik> myEtkinlik = new ArrayList<>();
    /*    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();*/




        btnResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_INTENT);
            }
        });
        final DatabaseReference newPost=mDatabase.getRef();

      /* mDatabaseKullanici.child(mPost_key).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String post_ead= (String) dataSnapshot.child("isim").getValue();
               TxtKullaniciAdi.setText(post_ead);
               TxtKullaniciAdi.setEnabled(true);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/

        FirebaseDatabase.getInstance().getReference().child("Katilan").child(mCurrentKullanici.getUid())
.equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            try {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Etkinlik e = ds.getValue(Etkinlik.class);

                                    Log.d("Profil", "etkinlik adi: " + e.getEtkinlikAdi());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(Profil.this, GirisKullanici.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        LayoutManagerKullaniciAdi = new LinearLayoutManager(this);
        LayoutManagerEtkinlik = new LinearLayoutManager(this);
        LayoutManagerProfil = new LinearLayoutManager(this);
        recyclerEtkinlik.setLayoutManager(LayoutManagerEtkinlik);
        recyclerProfil.setLayoutManager(LayoutManagerProfil);
        recyclerKullaniciAdi.setLayoutManager(LayoutManagerKullaniciAdi);
       /* myReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {

                    Kullanici k=new Kullanici();
                    k=dataSnapshot.getValue(Kullanici.class);

                    String key = data.getKey();
                    Etkinlik etkinlik = new Etkinlik();
                    etkinlik.setEtkinlikSaati(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikSaati());
                    etkinlik.setEtkinlikAdi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikAdi());
                    etkinlik.setEtkinlikİcerigi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikİcerigi());
                    etkinlik.setEtkinlikResmi(dataSnapshot.child(key).getValue(Etkinlik.class).getEtkinlikResmi());
                    etkinlik.setContext(getApplicationContext());
                    myEtkinlik.add(etkinlik);
                    EtkinlikAdapter adapter = new EtkinlikAdapter(myEtkinlik);
                    recyclerEtkinlik.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout2){
            logout();
        }
        else if(item.getItemId() == R.id.action_profil2){
            Intent into = new Intent(Profil.this,Profil.class);

            startActivity(into);
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        mAuth.signOut();
        Intent into = new Intent(getApplicationContext(),GirisKullanici.class);
        into.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(into);
        finish();
    }
    private void IDyazdir(){
        btnResimEkle = (Button) findViewById(R.id.btnresimekle);
        txtetkinlikAdi = (TextView) findViewById(R.id.txtEtkinlikAdi);
        txtetkinlikresmi = (TextView) findViewById(R.id.txtEtkinlikImage);
        txtetkinlikicerigi = (TextView) findViewById(R.id.txtEtkinlikİcerigi);
        edtKullaniciAdi2 = (EditText) findViewById(R.id.giriskullaniciad2);
        TxtKullaniciAdi = (TextView) findViewById(R.id.txtKullaniciAdi);
        imgProfilResmi = (ImageButton) findViewById(R.id.imgProfilResmi);
        profilresm=(ImageView)findViewById(R.id.profilresm);
        recyclerKullaniciAdi= (RecyclerView) findViewById(R.id.recyclerKullaniciAdi2);
        recyclerEtkinlik = (RecyclerView)findViewById(R.id.recyclerEtkinlik);
        recyclerProfil = (RecyclerView) findViewById(R.id.recyclerProfil);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==GALLERY_INTENT && resultCode==RESULT_OK) {
            final  Uri uri=data.getData();
            mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference filepath=mStorageRef.child(" Profil resimleri").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        CurrentImgPath=downloadUrl.toString();
                        Picasso.with(profilresm.getContext()).load(downloadUrl.toString()).into(profilresm);
                        Toast.makeText(Profil.this,"Yükleme Tamamlandı",Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
}



