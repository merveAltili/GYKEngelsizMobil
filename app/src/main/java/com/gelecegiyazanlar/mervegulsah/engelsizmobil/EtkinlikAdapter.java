package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EtkinlikAdapter extends RecyclerView.Adapter<EtkinlikAdapter.EtkinlikViewHolder> {

    private ArrayList<Etkinlik> myEtkinlik;

    public class EtkinlikViewHolder extends RecyclerView.ViewHolder {

        //public CardView cv;
        public ImageView imgEtkinlik;
        public TextView txtEtkinlikAdi,txtEtkinlikİcerigi;
        public EtkinlikViewHolder(View itemView)
        {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.cv);
            imgEtkinlik = (ImageView)itemView.findViewById(R.id.imgEtkinlik);
            txtEtkinlikAdi = (TextView) itemView.findViewById(R.id.txtEtkinlikAdi);
            txtEtkinlikİcerigi = (TextView)itemView.findViewById(R.id.txtEtkinlikİcerigi);
        }

    }

    public EtkinlikAdapter(ArrayList<Etkinlik> myEtkinlik)
    {
        this.myEtkinlik = myEtkinlik;
    }

    @Override
    public EtkinlikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new EtkinlikViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EtkinlikViewHolder holder, int position) {

        //holder.imgEtkinlik.setImageResource(myEtkinlik.get(position).getEtkinlikResmi().getId());
        holder.txtEtkinlikAdi.setText(myEtkinlik.get(position).getEtkinlikAdi());
        holder.txtEtkinlikİcerigi.setText(myEtkinlik.get(position).getEtkinlikİcerik());



    }


    @Override
    public int getItemCount() {
        return myEtkinlik.size();
    }
}
