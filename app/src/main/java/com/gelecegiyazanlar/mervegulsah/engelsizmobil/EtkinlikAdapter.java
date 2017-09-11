package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EtkinlikAdapter extends RecyclerView.Adapter<EtkinlikAdapter.MyViewHolder> {

    private ArrayList<Etkinlik> myEtkinlik;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgEtkinlik;
        public TextView txtEtkinlikAdi,txtEtkinlikİceriği,txtEtkinlikSaati,txtEtkinlikImge;

        public MyViewHolder (View itemView){
            super(itemView);
            imgEtkinlik = (ImageView)itemView.findViewById(R.id.imgEtkinlik);
            txtEtkinlikAdi = (TextView)itemView.findViewById(R.id.txtEtkinlikAdi);
            txtEtkinlikİceriği = (TextView)itemView.findViewById(R.id.txtEtkinlikİcerigi);
            txtEtkinlikSaati = (TextView)itemView.findViewById(R.id.txtEtkinlikSaati);
            txtEtkinlikImge=(TextView)itemView.findViewById(R.id.txtEtkinlikImage);

        }
    }

    public EtkinlikAdapter(ArrayList<Etkinlik> myEtkinlik)
    {
        this.myEtkinlik = myEtkinlik;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtEtkinlikAdi.setText(myEtkinlik.get(position).getEtkinlikAdi());
        holder.txtEtkinlikİceriği.setText(myEtkinlik.get(position).getEtkinlikİcerigi());
        holder.txtEtkinlikSaati.setText(myEtkinlik.get(position).getEtkinlikSaati());
        holder.txtEtkinlikImge.setText(myEtkinlik.get(position).getEtkinlikImage());
    }

    @Override
    public int getItemCount() {
        return myEtkinlik.size();
    }


}
