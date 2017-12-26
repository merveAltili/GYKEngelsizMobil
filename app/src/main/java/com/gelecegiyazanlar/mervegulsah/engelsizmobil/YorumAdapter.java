package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by merve on 26.12.2017.
 */

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.MyViewHolder> {
    private ArrayList<Yorumlar> myYorum;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public EditText txtYorumAciklama;

        public MyViewHolder (View itemView){
            super(itemView);

            txtYorumAciklama = (EditText)itemView.findViewById(R.id.yorum_aciklama);


        }

     /*   public void setImage(Context ctx2, String image2) {
            YKullaniciResmi = (ImageView) itemView.findViewById(R.id.kullanici_resmi);
            Picasso.with(ctx2).load(image2).into(YKullaniciResmi);
        }*/



    }

    public YorumAdapter(ArrayList<Yorumlar> myYorum)
    {
        this.myYorum = myYorum;
    }

    @Override
    public YorumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.yorum_row,parent,false);
        return new YorumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(YorumAdapter.MyViewHolder holder, int position) {

        holder.txtYorumAciklama.setText(myYorum.get(position).getYorum());


    }

    @Override
    public int getItemCount() {
        return myYorum.size();
    }



}
