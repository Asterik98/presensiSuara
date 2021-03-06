package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asterik.presensi.MainActivity;
import com.example.asterik.presensi.R;

public class HasilPresensiNone extends AppCompatActivity {
    public static String NAMA_TEMP = "NAMA_TEMP";
    String nama;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hasil_none);
        nama=getIntent().getStringExtra(NAMA_TEMP);
    }
    public void back(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
    public void ulang(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), Rekam.class);
        moveWithObjectIntent.putExtra(Rekam.PILIH_NAMA, nama);
        startActivity(moveWithObjectIntent);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
}
