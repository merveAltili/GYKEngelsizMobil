package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;

import java.util.ArrayList;

public class Etkinlik {

    private String etkinlikAdi;
    private String etkinlikİcerigi;
    private String etkinlikSaati;
    private String etkinlikResmi;
    private String username;

    public String getKullanici_id() {
        return kullanici_id;
    }

    public void setKullanici_id(String kullanici_id) {
        this.kullanici_id = kullanici_id;
    }

    private String kullanici_id;
    Context context;

    public Context getContext() {
        return context;
    }

    public Etkinlik(){


    }
    public Etkinlik(String etkinlikAdi,String etkinlikİcerigi,String etkinlikResmi,String username,String kullanici_id){
        this.etkinlikAdi=etkinlikAdi;
        this.etkinlikİcerigi=etkinlikİcerigi;
        this.etkinlikResmi=etkinlikResmi;
        this.username=username;
        this.kullanici_id=kullanici_id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getEtkinlikResmi() {
        return etkinlikResmi;
    }

    public void setEtkinlikResmi(String etkinlikResmi) {
        this.etkinlikResmi = etkinlikResmi;
    }

    public String getEtkinlikAdi() {
        return etkinlikAdi;
    }

    public void setEtkinlikAdi(String etkinlikAdi) {
        this.etkinlikAdi = etkinlikAdi;
    }

    public String getEtkinlikİcerigi() {
        return etkinlikİcerigi;
    }

    public void setEtkinlikİcerigi(String etkinlikİcerigi) {
        this.etkinlikİcerigi = etkinlikİcerigi;
    }

    public String getEtkinlikSaati() {
        return etkinlikSaati;
    }

    public void setEtkinlikSaati(String etkinlikSaati) {
        this.etkinlikSaati = etkinlikSaati;
    }


}
