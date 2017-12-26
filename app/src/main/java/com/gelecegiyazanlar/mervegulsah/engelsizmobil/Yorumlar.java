package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Context;

/**
 * Created by merve on 26.12.2017.
 */

public class Yorumlar {
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

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    private String yorum;
    public Yorumlar(String yorum) {
        this.yorum = yorum;
    }
}
