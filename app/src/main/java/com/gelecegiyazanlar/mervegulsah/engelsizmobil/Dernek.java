package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Context;

/**
 * Created by merve on 28.09.2017.
 */

public class Dernek {
    String dernekAdi;
    String sifre;

    public String getEtkinlik_id() {
        return etkinlik_id;
    }

    public void setEtkinlik_id(String etkinlik_id) {
        this.etkinlik_id = etkinlik_id;
    }

    String etkinlik_id;
    Context context;
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public String getDernekAdi() {
        return dernekAdi;
    }

    public void setDernekAdi(String dernekAdi) {
        this.dernekAdi = dernekAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

}
