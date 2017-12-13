package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.MyViewHolder> {

    private ArrayList<Kullanici> kullanicilar;

    public KullaniciAdapter(ArrayList<Kullanici> kullanicilar)
    {
        this.kullanicilar = kullanicilar;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageButton imgProfilResmi;
        public TextView txtPuan;

        public MyViewHolder(View itemView){
            super(itemView);
            //txtPuan = (TextView)itemView.findViewById(R.id.txtPuan);
        }
        public void setImage(Context ctx, String image)
        {
            imgProfilResmi = (ImageButton)itemView.findViewById(R.id.imgProfilResmi);
            Picasso.with(ctx).load(image).into(imgProfilResmi);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profil_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtPuan.setText(kullanicilar.get(position).getPuan());
        holder.setImage(kullanicilar.get(position).context,kullanicilar.get(position).getResim());
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }


}
