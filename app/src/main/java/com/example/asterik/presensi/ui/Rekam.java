package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asterik.presensi.R;

public class Rekam extends AppCompatActivity {
    TextView detik;
    Integer second;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rekam);
        detik=(TextView)findViewById(R.id.waktu);
    }
    public void recordSuara(View view){
        second=3;
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                detik.setTextSize(70);
                detik.setText("0"+second+":00");
                second--;
            }

            public void onFinish() {
                Intent moveWithObjectIntent = new Intent(getApplicationContext(), HasilPresensiTerdeteksi.class);
                startActivity(moveWithObjectIntent);
            }
        }.start();
    }
}
