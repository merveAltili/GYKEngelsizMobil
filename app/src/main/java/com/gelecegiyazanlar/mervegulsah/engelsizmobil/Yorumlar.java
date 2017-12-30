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
        return yorumc;
    }

    public void setYorum(String yoruma) {
        this.yorumc = yoruma;
    }

    private String yorumc;
    public Yorumlar(String yorum) {
        this.yorumc = yorum;
    }
}
