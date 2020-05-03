package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.asterik.presensi.MainActivity;
import com.example.asterik.presensi.R;

import java.util.ArrayList;

public class KonfirmasiDeletePegawai extends AppCompatActivity {
    public static final String PEGAWAI_DATA= "pegawai_data";
    public static ArrayList<String> nama;
    TextView teksKonfirmasi;
    String namaYangDihapus;
    ImageButton hapus;
    ImageButton cancel;
    private Fragment fragment;
    ImageButton back;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_konfirmasi_hapus);
        nama=getIntent().getStringArrayListExtra(PEGAWAI_DATA);
        teksKonfirmasi=(TextView)findViewById(R.id.textKonfirmasi);
        hapus=(ImageButton) findViewById(R.id.hapus);
        cancel=(ImageButton) findViewById(R.id.cancel);
        back=(ImageButton) findViewById(R.id.back);
        for(int i=0;i<nama.size();i++){
            String elemenNama=nama.get(i);
            if(namaYangDihapus==null) {
                namaYangDihapus = elemenNama;
            }else{
                namaYangDihapus = namaYangDihapus+", "+elemenNama;
            }

        }
        teksKonfirmasi.setText("Apakah anda akan menghapus data "+namaYangDihapus+" ?");
    }
    public void hapus(View v){
        teksKonfirmasi.setText("Sukses\nDihapus");
        teksKonfirmasi.setTextSize(50);
        teksKonfirmasi.setGravity(Gravity.CENTER_HORIZONTAL);
        hapus.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        back.setVisibility(View.VISIBLE);
    }
    public void cancel(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
    public void back(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
}
