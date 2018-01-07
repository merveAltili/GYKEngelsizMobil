package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Context;

/**
 * Created by merve on 26.12.2017.
 */

public class Yorumlar {
    private String yorum;

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    private String resim;
    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    private String kullaniciAdi;
    Context context;

    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public Yorumlar(){


    }
    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yoruma) {
        this.yorum = yoruma;
    }


    public Yorumlar(String yorum,String kullaniciAdi,String resim) {
        this.yorum = yorum;
        this.kullaniciAdi=kullaniciAdi;
        this.resim=resim;
    }
}
