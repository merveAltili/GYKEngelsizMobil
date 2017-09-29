package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnGiris extends AppCompatActivity {

    Button btnDernek,btnKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_giris);
        btnKullanici= (Button) findViewById(R.id.btnKullanici);
        btnDernek= (Button) findViewById(R.id.btnDernek);

        btnDernek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OnGiris.this,Giris.class);
                startActivity(i);
            }
        });

        btnKullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OnGiris.this,GirisKullanici.class);
                startActivity(i);
            }
        });
    }

}
