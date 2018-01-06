package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class YorumKullanici extends AppCompatActivity {

    private RecyclerView mEtkinlikBlog3;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseKatil;
    private DatabaseReference mDatabaseBegen;
    private DatabaseReference mDatabaseYorum;
    private DatabaseReference mDatabaseCurrentKullanici;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private Query mQueryCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu=false;
    private boolean mBegenmeDurumu=false;
    private boolean mYorumDurumu=false;
    private ImageButton mYorumGonder,mYorum;
    private EditText mEdtYorum;
    public String mPost_key2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum_kullanici);
        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent2=new Intent(YorumKullanici.this,Anasayfa.class);
                    loginIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent2);
                }
            }
        };
        mPost_key2=getIntent().getExtras().getString("etkinlik_id2");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        mDatabaseUsers.keepSynced(true);
        mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
        mDatabaseBegen=FirebaseDatabase.getInstance().getReference().child("Begenenler");
        mDatabaseYorum=FirebaseDatabase.getInstance().getReference().child("Yorumlar");
        mDatabaseCurrentKullanici= FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
        mDatabase.keepSynced(true);
        mDatabaseKatil.keepSynced(true);
        mDatabaseBegen.keepSynced(true);
        mDatabaseYorum.keepSynced(true);
        mEtkinlikBlog3= (RecyclerView) findViewById(R.id.yorumlar_list);
        mEtkinlikBlog3.setHasFixedSize(true);
        mEtkinlikBlog3.setLayoutManager(new LinearLayoutManager(this));
        mYorum=(ImageButton)findViewById(R.id.btnYorumYap);
        mYorumGonder=(ImageButton)findViewById(R.id.imageBtnYorum);
        mEdtYorum=(EditText)findViewById(R.id.edtyorum);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Yorumlar, YorumKullanici.EtkinlikViewHolder3> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Yorumlar, YorumKullanici.EtkinlikViewHolder3>(

                Yorumlar.class,
                R.layout.yorum_row,
                YorumKullanici.EtkinlikViewHolder3.class,
                mDatabaseYorum
        ) {
            @Override
            protected void populateViewHolder(YorumKullanici.EtkinlikViewHolder3 viewHolder,  Yorumlar model2, final int position) {
                final String post_key = getRef(position).getKey();

                Kullanici kul = new Kullanici();
                final Yorumlar y = new Yorumlar();
             //  viewHolder.setKullaniciAd(kul.getKullaniciAdi());
                viewHolder.setYorumAciklama(model2.getYorum());
                //   viewHolder.setImage(getApplicationContext(),model.getEtkinlikResmi());


            }
        };
        mEtkinlikBlog3.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EtkinlikViewHolder3 extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mKatil,mBegen,mYorum,mYorumGonder;
        EditText mEdtYorum;
        DatabaseReference mDatabaseKatil,mDatabaseBegen,mDatabaseYorum;
        FirebaseAuth mAuth;




        public EtkinlikViewHolder3(View itemView) {
            super(itemView);
            mView=itemView;

            mBegen=(ImageButton)mView.findViewById(R.id.btnBegen);
            mKatil=(ImageButton)mView.findViewById(R.id.btnKatil);
            mYorum=(ImageButton)mView.findViewById(R.id.btnYorumYap);
            mYorumGonder=(ImageButton)mView.findViewById(R.id.imageBtnYorum);
            mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
            mDatabaseBegen=FirebaseDatabase.getInstance().getReference().child("Begenenler");

            mDatabaseYorum=FirebaseDatabase.getInstance().getReference().child("Yorumlar");
            mAuth=FirebaseAuth.getInstance();
            mEdtYorum=(EditText)mView.findViewById(R.id.edtyorum);
            mBegen=(ImageButton)mView.findViewById(R.id.btnBegen);
            mDatabaseKatil.keepSynced(true);
            mDatabaseBegen.keepSynced(true);
            mDatabaseYorum.keepSynced(true);

        }
      /*  public void setKullaniciAd(String title){
            TextView kul_ad=(TextView)mView.findViewById(R.id.kullanici_ismi);
            kul_ad.setText(title);
        }*/
        public void setYorumAciklama(String aciklama){
            TextView y_aciklama=(TextView)mView.findViewById(R.id.yorum_aciklama);
            y_aciklama.setText(aciklama);
        }


   /*     public void setImage(Context ctx, String image){
            ImageView e_image=(ImageView)mView.findViewById(R.id.etkinlik_image);
            Picasso.with(ctx).load(image).into(e_image);
        }*/



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }


}

