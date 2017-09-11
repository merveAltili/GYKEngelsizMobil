package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.net.Uri;
import android.net.wifi.ScanResult;

public class Etkinlik {

    String etkinlikAdi;
    String etkinlikİcerigi;
    String etkinlikSaati;
    String etkinlikResmi;


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
