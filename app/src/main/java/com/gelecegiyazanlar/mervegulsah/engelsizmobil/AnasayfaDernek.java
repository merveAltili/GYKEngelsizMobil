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

public class AnasayfaDernek extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKatil;
    private DatabaseReference mDatabaseCurrentKullanici;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    public ProgressDialog mProgress;
    private Query mQueryCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mKatilmaDurumu= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa_dernek);
        mAuth =FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(AnasayfaDernek.this,Giris.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Etkinlik");
        mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
        mDatabaseCurrentKullanici= FirebaseDatabase.getInstance().getReference().child("Etkinlik");

        // String currentkullanciId=mAuth.getCurrentUser().getUid();
        // mQueryCurrentUser=mDatabaseCurrentKullanici.orderByChild("uid").equalTo(currentkullanciId);

        mDatabase.keepSynced(true);
        mDatabaseKatil.keepSynced(true);

        mEtkinlikBlog= (RecyclerView) findViewById(R.id.etkinlik_list2);
        mEtkinlikBlog.setHasFixedSize(true);
        mEtkinlikBlog.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Etkinlik,AnasayfaDernek.EtkinlikViewHolder2> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Etkinlik, AnasayfaDernek.EtkinlikViewHolder2>(

                Etkinlik.class,
                R.layout.etkinlik_row2,
                AnasayfaDernek.EtkinlikViewHolder2.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AnasayfaDernek.EtkinlikViewHolder2 viewHolder, Etkinlik model, int position) {
                final String post_key2=getRef(position).getKey();

                viewHolder.setEtkinlikAdi(model.getEtkinlikAdi());
                viewHolder.setAciklama(model.getEtkinlikİcerigi());
                viewHolder.setImage(getApplicationContext(),model.getEtkinlikResmi());
                viewHolder.setmKatil(post_key2);
                viewHolder.setUserName(model.getUsername());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(Anasayfa.this,post_key,Toast.LENGTH_LONG).show();

                        Intent etkinlikDetay=new Intent(AnasayfaDernek.this, EtkinlikDetay.class);
                        etkinlikDetay.putExtra("etkinlik_id",post_key2);
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


                                    if (dataSnapshot.child(post_key2).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mDatabaseKatil.child(post_key2).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mKatilmaDurumu = false;

                                    } else {
                                        mDatabaseKatil.child(post_key2).child(mAuth.getCurrentUser().getUid()).setValue("Randomvalue");
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

    public static class EtkinlikViewHolder2 extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mKatil;
        DatabaseReference mDatabaseKatil;
        FirebaseAuth mAuth;

        public void  setmKatil(final String post_key2){

            mDatabaseKatil.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key2).hasChild(mAuth.getCurrentUser().getUid())){
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

        public EtkinlikViewHolder2(View itemView) {
            super(itemView);
            mView=itemView;
            mKatil=(ImageButton)mView.findViewById(R.id.btnKatil2);
            mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
            mAuth=FirebaseAuth.getInstance();

            mDatabaseKatil.keepSynced(true);

        }
        public void setEtkinlikAdi(String title){
            TextView etkinlik_adi=(TextView)mView.findViewById(R.id.etkinlik_adi2);
            etkinlik_adi.setText(title);
        }
        public void setAciklama(String aciklama){
            TextView e_aciklama=(TextView)mView.findViewById(R.id.etkinlik_aciklama2);
            e_aciklama.setText(aciklama);
        }
        public void setUserName(String username){
            TextView e_username=(TextView)mView.findViewById(R.id.post_username2);
            e_username.setText(username);
        }
        public void setImage(Context ctx, String image){
            ImageView e_image=(ImageView)mView.findViewById(R.id.etkinlik_image2);
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
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(AnasayfaDernek.this, Post.class));
        }
        if(item.getItemId() == R.id.action_profil){
            Intent into = new Intent(AnasayfaDernek.this,Profil.class);

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
