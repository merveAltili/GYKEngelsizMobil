package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;

public class Etkinlik {

    String etkinlikAdi;
    String etkinlikİcerigi;
    String etkinlikSaati;
    String etkinlikResmi;
    Context context;

    public Context getContext() {
        return context;
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
