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
        import android.widget.QuickContactBadge;
        import android.widget.TextView;
        import android.widget.Toast;


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

public class Anasayfa extends AppCompatActivity {
    private RecyclerView mEtkinlikBlog;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        mAuth=FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(Anasayfa.this,GirisKullanici.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };


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
        mEtkinlikBlog= (RecyclerView) findViewById(R.id.etkinlik_list);
        mEtkinlikBlog.setHasFixedSize(true);
        mEtkinlikBlog.setLayoutManager(new LinearLayoutManager(this));
        mYorum=(ImageButton)findViewById(R.id.btnYorumYap);
        mYorumGonder=(ImageButton)findViewById(R.id.imageBtnYorum);
        mEdtYorum=(EditText)findViewById(R.id.edtyorum);
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
            protected void populateViewHolder(EtkinlikViewHolder viewHolder, final Etkinlik model, int position) {
                final String post_key=getRef(position).getKey();

                Kullanici kul=new Kullanici();

                viewHolder.setEtkinlikAdi(model.getEtkinlikAdi());
                viewHolder.setAciklama(model.getEtkinlikİcerigi());
                viewHolder.setImage(getApplicationContext(),model.getEtkinlikResmi());
                viewHolder.setUserName(model.getUsername());
                viewHolder.setmKatil(post_key);
                viewHolder.setUserName(model.getUsername());
                viewHolder.setImage2(getApplicationContext(),model.getDernekResmi());
                viewHolder.setmBegen(post_key);
                viewHolder.setmYorum(post_key);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(Anasayfa.this,post_key,Toast.LENGTH_LONG).show();

                        Intent etkinlikDetay=new Intent(Anasayfa.this, EtkinlikDetay.class);
                        etkinlikDetay.putExtra("etkinlik_id",post_key);
                        startActivity(etkinlikDetay);
                    }
                });
                viewHolder.mBegen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBegenmeDurumu=true;
                        mDatabaseBegen.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mBegenmeDurumu) {
                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mDatabaseBegen.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mBegenmeDurumu = false;

                                    } else {

                                        mDatabaseBegen.child(post_key).child(mAuth.getCurrentUser().getUid()).child("etkinlikAdi").setValue(model.getEtkinlikAdi());
                                        mDatabaseBegen.child(post_key).child(mAuth.getCurrentUser().getUid()).child("etkinlikResmi").setValue(model.getEtkinlikResmi());
                                        mDatabaseBegen.child(post_key).child(mAuth.getCurrentUser().getUid()).child("etkinlikİcerigi").setValue(model.getEtkinlikİcerigi());
                                        mBegenmeDurumu = false;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.mYorum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

        EditText edt=(EditText)findViewById(R.id.edtyorum);
        ImageButton img=(ImageButton)findViewById(R.id.imageBtnYorum);
                        edt.setVisibility(View.VISIBLE);
                        img.setVisibility(View.VISIBLE);

                    }
                });

viewHolder.mYorumGonder.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mYorumDurumu=true;

        mDatabaseYorum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EditText edt=(EditText)findViewById(R.id.edtyorum);
                String edtyrm = edt.getText().toString().trim();
                Yorumlar y=new Yorumlar();
                y.setYorum(edtyrm);
                if (mYorumDurumu) {
                   if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(post_key)){
                       // mDatabaseYorum.child(mAuth.getCurrentUser().getUid()).child(post_key).removeValue();
                        mYorumDurumu = false;

                    } else {
                       mDatabaseYorum.child(mAuth.getCurrentUser().getUid()).child(post_key).child("yorum").setValue(y.getYorum());

                       mYorumDurumu = false;

                   }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

                                    } else {

                                        mDatabaseKatil.child(mAuth.getCurrentUser().getUid()).child(post_key).child("etkinlikAdi").setValue(model.getEtkinlikAdi());
                                        mDatabaseKatil.child(mAuth.getCurrentUser().getUid()).child(post_key).child("etkinlikResmi").setValue(model.getEtkinlikResmi());
                                        mDatabaseKatil.child(mAuth.getCurrentUser().getUid()).child(post_key).child("etkinlikİcerigi").setValue(model.getEtkinlikİcerigi());
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
        ImageButton mKatil,mBegen,mYorum,mYorumGonder;
        EditText mEdtYorum;
        DatabaseReference mDatabaseKatil,mDatabaseBegen,mDatabaseYorum;
        FirebaseAuth mAuth;

        public void  setmYorum(final String kul_id){

            mDatabaseYorum.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(kul_id)){
                        mYorum.setImageResource(R.drawable.ic_chat_ici_bos24dp);

                    }else{

                        mYorum.setImageResource(R.drawable.ic_chat_ici_bos24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


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
        public void setmBegen(final String kul_id2){
            mDatabaseBegen.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(kul_id2).hasChild(mAuth.getCurrentUser().getUid())){
                        mBegen.setImageResource(R.drawable.ic_kalp_ici_dolu_24dp);

                    }else{

                        mBegen.setImageResource(R.drawable.ic_kalp_ici_bos_24dp);
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
            mBegen=(ImageButton)mView.findViewById(R.id.btnBegen);
            mKatil=(ImageButton)mView.findViewById(R.id.btnKatil);
            mYorum=(ImageButton)mView.findViewById(R.id.btnYorumYap);
            mYorumGonder=(ImageButton)mView.findViewById(R.id.imageBtnYorum);
            mDatabaseKatil=FirebaseDatabase.getInstance().getReference().child("Katilan");
            mDatabaseYorum=FirebaseDatabase.getInstance().getReference().child("Yorumlar");
            mDatabaseBegen=FirebaseDatabase.getInstance().getReference().child("Begenenler");
            mAuth=FirebaseAuth.getInstance();
            mEdtYorum=(EditText)mView.findViewById(R.id.edtyorum);
            mBegen=(ImageButton)mView.findViewById(R.id.btnBegen);
            mDatabaseKatil.keepSynced(true);
            mDatabaseBegen.keepSynced(true);
            mDatabaseYorum.keepSynced(true);

        }
        public void setEtkinlikAdi(String title){
            TextView etkinlik_adi=(TextView)mView.findViewById(R.id.etkinlik_adi);
            etkinlik_adi.setText(title);
        }
        public void setAciklama(String aciklama){
            TextView e_aciklama=(TextView)mView.findViewById(R.id.etkinlik_aciklama);
            e_aciklama.setText(aciklama);
        }
        public void setUserName(String dernekadii){
            TextView dernekadi=(TextView)mView.findViewById(R.id.post_username);
            dernekadi.setText(dernekadii);
        }
        public void setImage(Context ctx, String image){
            ImageView e_image=(ImageView)mView.findViewById(R.id.etkinlik_image);
            Picasso.with(ctx).load(image).into(e_image);
        }
        public void setImage2(Context ctx2, String image2){
            ImageView e_image2=(CircleImageView)mView.findViewById(R.id.btndernekprofil1);
            Picasso.with(ctx2).load(image2).into(e_image2);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intocan = new Intent();

        if(item.getItemId()==R.id.action_logout2){
            logout();
        }

        if(item.getItemId() == R.id.action_profil2){

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
        Intent into = new Intent(getApplicationContext(),GirisKullanici.class);
        into.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(into);
        finish();
    }
}