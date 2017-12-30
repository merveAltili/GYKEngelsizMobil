package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class YorumKullanici extends AppCompatActivity {
    private StorageReference mStorageRef;
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseEtkinlik;
    private DatabaseReference mDatabaseKullanici2;
    private DatabaseReference mDatabaseKatil;
    private DatabaseReference mDatabaseYorumYap;
    private boolean mYorumDurumu=false;
    private FirebaseAuth mAuth;
    private ImageButton btnYorum;
    private ProgressDialog mProgress;
    private EditText yorumAciklama;
    private String mPost_key2=null;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum_kullanici);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent lIntent = new Intent(YorumKullanici.this, Anasayfa.class);
                    lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(lIntent);
                }
            }
        };
        mPost_key2=getIntent().getExtras().getString("etkinlik_id2");
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Yorumlar").child(mPost_key2).child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        mDatabaseUsers.keepSynced(true);
        mDatabaseEtkinlik = FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseEtkinlik.keepSynced(true);


        //mDatabaseCurrentKullanici= FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");

        //   txtKullaniciAdi2 = (EditText) findViewById(R.id.txtKullaniciAdi2);
        mDatabaseKatil.keepSynced(true);
        mDatabaseYorumYap.keepSynced(true);
        mEtkinlikBlog = (RecyclerView) findViewById(R.id.yorumlar_list);
        mEtkinlikBlog.setHasFixedSize(true);
        mEtkinlikBlog.setLayoutManager(new LinearLayoutManager(this));
btnYorum=(ImageButton)findViewById(R.id.imageBtnYorum);
        mStorageRef = FirebaseStorage.getInstance().getReference();
       // yorumAciklama = (EditText) findViewById(R.id.edtyorum);

        mDatabaseKullanici2 = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        final DatabaseReference newPost = mDatabase.push();

    }
   @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Yorumlar,YorumKullanici.EtkinlikViewHolder4> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Yorumlar, YorumKullanici.EtkinlikViewHolder4>(

                Yorumlar.class,
                R.layout.yorum_row,
                YorumKullanici.EtkinlikViewHolder4.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(YorumKullanici.EtkinlikViewHolder4 viewHolder, final Yorumlar model, int position) {
                final String post_key3 = getRef(position).getKey();

            //    viewHolder.setmYorumYap(post_key3);
                viewHolder.setYorumAciklama(model.getYorum());



            }
        };
        mEtkinlikBlog.setAdapter(firebaseRecyclerAdapter);
    }

public static class EtkinlikViewHolder4 extends RecyclerView.ViewHolder{

    View mView;
    ImageButton mYorumBegen;
    DatabaseReference mDatabaseBegen,mDatabase;
    FirebaseAuth mAuth;
/*
    public void  setmYorumYap(final String kul_id){

        mDatabaseYorumYap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(kul_id)){
                 //   mYorumYap.setImageResource(R.drawable.ic_kalp_ici_dolu_24dp);

                }else{

                 //   mYorumYap.setImageResource(R.drawable.ic_kalp_ici_bos_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/


    public EtkinlikViewHolder4(View itemView) {
        super(itemView);
        mView=itemView;
        mYorumBegen=(ImageButton)mView.findViewById(R.id.btnYorumBegen);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Yorumlar");

        mDatabaseBegen=FirebaseDatabase.getInstance().getReference().child("YorumBegen");
        mDatabaseBegen.keepSynced(true);
mDatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();

        CircleImageView profileImage;

    }
    public void setYorumAciklama(String title){
        TextView yorumAciklama=(TextView) mView.findViewById(R.id.yorum_aciklama);
        yorumAciklama.setText(title);
    }
    /*
   public void setUserName(String username){
        TextView e_username=(TextView)mView.findViewById(R.id.kullanici_ismi);
        e_username.setText(username);
    }
    public void setImage(Context ctx, String image){
        ImageView e_image=(ImageView) mView.findViewById(R.id.kullanici_resmi);
        Picasso.with(ctx).load(image).into(e_image);
    }*/

}
  /*
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

            Intent into = new Intent(YorumKullanici.this, YorumKullanici.class);

         /* Bundle bundle = getIntent().getExtras();
            Kullanici kullanici = new Kullanici();
            kullanici.setKullaniciAdi(bundle.getString("Kullanici_Adi"));
            kullanici.setSifre(bundle.getString("Sifre"));
            kullanici.setResim(bundle.getString("Resim"));
            into.putExtra("Kullanici_Adi",kullanici.getIsim());
            into.putExtra("Sifre",kullanici.getSifre());
            into.putExtra("Resim",kullanici.getResim());*/
  /*
            startActivity(into);
        }
        return super.onOptionsItemSelected(item);
    }
*/

    private void logout() {
        mAuth.signOut();
        Intent into = new Intent(getApplicationContext(), GirisKullanici.class);
        into.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(into);
        finish();
    }



}
