package com.gelecegiyazanlar.mervegulsah.engelsizmobil;


import android.widget.ImageView;

public class Etkinlik {
    ImageView etkinlikResmi;
    String etkinlikAdi;
    String etkinlikİcerik;

    public Etkinlik(String etkinlikAdi,String etkinlikİcerik)
    {
        this.etkinlikAdi = etkinlikAdi;
        this.etkinlikİcerik = etkinlikİcerik;
    }
    public ImageView getEtkinlikResmi() {
        return etkinlikResmi;
    }

    public void setEtkinlikResmi(ImageView etkinlikResmi) {
        this.etkinlikResmi = etkinlikResmi;
    }

    public String getEtkinlikAdi() {
        return etkinlikAdi;
    }

    public void setEtkinlikAdi(String etkinlikAdi) {
        this.etkinlikAdi = etkinlikAdi;
    }

    public String getEtkinlikİcerik() {
        return etkinlikİcerik;
    }

    public void setEtkinlikİcerik(String etkinlikİcerik) {
        this.etkinlikİcerik = etkinlikİcerik;
    }
}
