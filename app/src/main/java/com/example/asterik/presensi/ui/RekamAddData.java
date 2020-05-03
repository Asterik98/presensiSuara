package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asterik.presensi.R;

public class RekamAddData extends AppCompatActivity {
    public static final String PEGAWAI_DATA= "pegawai_data";
    TextView detik;
    TextView detikTeks;
    ImageView gambarDetik;
    ImageButton mulai;
    ImageButton kirim;
    ImageButton record;
    Integer second;
    Integer indeks=0;
    ProgressBar progres;
    CountDownTimer timer;
    String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rekam_add_data);
        detik=(TextView)findViewById(R.id.waktu);
        progres=(ProgressBar)findViewById(R.id.progress);
        detikTeks=(TextView)findViewById(R.id.detikTeks);
        gambarDetik=(ImageView)findViewById(R.id.detik);
        mulai=(ImageButton)findViewById(R.id.mulai);
        kirim=(ImageButton)findViewById(R.id.kirim);
        record=(ImageButton)findViewById(R.id.record);
        name=getIntent().getStringExtra(PEGAWAI_DATA);
    }
    public void recordSuara(View view){
        mulai.setVisibility(View.INVISIBLE);
        detik.setVisibility(View.VISIBLE);
        detikTeks.setVisibility(View.VISIBLE);
        gambarDetik.setVisibility(View.VISIBLE);
        progres.setProgressTintList(ColorStateList.valueOf(0xFFFF6D00));
        second=3;
        timer=new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("second",String.valueOf(second));
                detik.setText(String.valueOf(second));
                detikTeks.setText("Detik");
                record.setVisibility(View.GONE);
                second--;
            }

            public void onFinish() {
                indeks++;
                progres.setProgress(indeks*20);
                record.setVisibility(View.VISIBLE);
                if(indeks==5) {
                    kirim.setVisibility(View.VISIBLE);
                    record.setVisibility(View.GONE);
                    detik.setVisibility(View.GONE);
                    detikTeks.setVisibility(View.GONE);
                    gambarDetik.setVisibility(View.GONE);
                }else{
                    detik.setText(String.valueOf(5-indeks));
                    detikTeks.setText("Lagi");
                }

            }
        }.start();
    }
    public void kirim(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), DataTerdaftar.class);
        moveWithObjectIntent.putExtra(DataTerdaftar.PEGAWAI_DATA_TERDAFTAR,name);
        startActivity(moveWithObjectIntent);
    }
}