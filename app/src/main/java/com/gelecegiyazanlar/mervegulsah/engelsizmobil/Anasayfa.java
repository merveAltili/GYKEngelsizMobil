package com.gelecegiyazanlar.mervegulsah.engelsizmobil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Anasayfa extends AppCompatActivity {
 Button btnProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
 btnProfil= (Button) findViewById(R.id.btnProfil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Anasayfa.this,Profil.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add){
            startActivity(new Intent(Anasayfa.this,Post.class));
        }
        else if(item.getItemId() == R.id.action_profil){
            startActivity(new Intent(Anasayfa.this,Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
