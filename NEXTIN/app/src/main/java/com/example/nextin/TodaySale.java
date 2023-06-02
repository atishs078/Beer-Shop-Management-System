package com.example.nextin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TodaySale extends AppCompatActivity {
    CardView tuborg, kingfisher, crstrong, crmild, budwizer, tuborggreen, Payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_sale);
        tuborg = findViewById(R.id.tuborg);
        kingfisher = findViewById(R.id.kingfisher);
        crstrong = findViewById(R.id.crstrong);
        crmild = findViewById(R.id.crmild);
        budwizer = findViewById(R.id.budwizer);
        tuborggreen = findViewById(R.id.tuborggreen);
        Payment = findViewById(R.id.dash);
        tuborg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "Tuborg";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        kingfisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "KingFisher";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        crstrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "Carlsberg Strong";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        crmild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "Carlsberg Mild";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        budwizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "Budwizer";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        tuborggreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodaySale.this, TodaysDataInsertion.class);
                String name = "Tuborg Green";
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.nextin.Payment.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}