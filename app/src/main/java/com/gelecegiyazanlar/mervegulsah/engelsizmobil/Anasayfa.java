package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.squareup.picasso.Picasso;

public class Anasayfa extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKatil;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        mAuth =FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(Anasayfa.this,Giris.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");

        mDatabase.keepSynced(true);
        mDatabaseKatil.keepSynced(true);

        mEtkinlikBlog= (RecyclerView) findViewById(R.id.etkinlik_list);
        mEtkinlikBlog.setHasFixedSize(true);
        mEtkinlikBlog.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Etkinlik,EtkinlikViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Etkinlik, EtkinlikViewHolder>(

                Etkinlik.class,
                R.layout.etkinlik_row,
                EtkinlikViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(EtkinlikViewHolder viewHolder, Etkinlik model, int position) {
                final String post_key=getRef(position).getKey();

                viewHolder.setEtkinlikAdi(model.getEtkinlikAdi());
                viewHolder.setAciklama(model.getEtkinlikİcerigi());
                viewHolder.setImage(getApplicationContext(),model.getEtkinlikResmi());
                viewHolder.setmKatil(post_key);



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(Anasayfa.this,post_key,Toast.LENGTH_LONG).show();
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


                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                            mDatabaseKatil.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mKatilmaDurumu = false;

                                        } else {
                                            mDatabaseKatil.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Randomvalue");
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

    public static class EtkinlikViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mKatil;
        DatabaseReference mDatabaseKatil;
        FirebaseAuth mAuth;

        public void  setmKatil(final String post_key){

            mDatabaseKatil.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
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

        public EtkinlikViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            mKatil=(ImageButton)mView.findViewById(R.id.btnKatil);
            mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
            mAuth=FirebaseAuth.getInstance();

            mDatabaseKatil.keepSynced(true);

        }
        public void setEtkinlikAdi(String title){
            TextView etkinlik_adi=(TextView)mView.findViewById(R.id.etkinlik_adi);
            etkinlik_adi.setText(title);
        }
        public void setAciklama(String aciklama){
            TextView e_aciklama=(TextView)mView.findViewById(R.id.etkinlik_aciklama);
            e_aciklama.setText(aciklama);
        }
        public void setImage(Context ctx, String image){
            ImageView e_image=(ImageView)mView.findViewById(R.id.etkinlik_image);
            Picasso.with(ctx).load(image).into(e_image);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intocan = new Intent();

        if(item.getItemId()==R.id.action_logout){
            logout();
        }
        if(item.getItemId()== R.id.action_add){
            startActivity(new Intent(Anasayfa.this,Post.class));
        }
        else if(item.getItemId() == R.id.action_profil){
            Intent into = new Intent(Anasayfa.this,Profil.class);

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
        Intent into = new Intent(getApplicationContext(),Giris.class);
        into.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(into);
        finish();
    }
}

