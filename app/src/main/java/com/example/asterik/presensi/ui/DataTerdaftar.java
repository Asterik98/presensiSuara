package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asterik.presensi.MainActivity;
import com.example.asterik.presensi.R;

public class DataTerdaftar extends AppCompatActivity {
    public static String PEGAWAI_DATA_TERDAFTAR="pegawai terdaftar";
    String name;
    TextView nama;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_terdaftar);
        nama=(TextView)findViewById(R.id.nama);
        name=getIntent().getStringExtra(PEGAWAI_DATA_TERDAFTAR);
        nama.setText(name.toUpperCase());
    }
    public void back(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
}
