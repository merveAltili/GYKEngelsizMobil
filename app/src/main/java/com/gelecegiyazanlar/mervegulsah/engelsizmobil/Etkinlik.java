package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;

public class Etkinlik {

    private String etkinlikAdi;
    private String etkinlikİcerigi;
    private String etkinlikSaati;
    private String etkinlikResmi;
    private String username;
    Context context;

    public Context getContext() {
        return context;
    }

    public Etkinlik(){


    }
    public Etkinlik(String etkinlikAdi,String etkinlikİcerigi,String etkinlikResmi,String username){
        this.etkinlikAdi=etkinlikAdi;
        this.etkinlikİcerigi=etkinlikİcerigi;
        this.etkinlikResmi=etkinlikResmi;
        this.username=username;
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
