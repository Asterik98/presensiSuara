package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asterik.presensi.MainActivity;
import com.example.asterik.presensi.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HasilPresensiTerdeteksi extends AppCompatActivity {
    public static String HASIL_NAMA = "HASIL_NAMA";
    ArrayList<String> nama=new ArrayList<>();
    TextView nameText;
    TextView tanggalText;
    TextView jamText;
    TextView status;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hasil_terdeteksi);
        nama=getIntent().getStringArrayListExtra(HASIL_NAMA);
        nameText=(TextView)findViewById(R.id.namaDeteksi);
        tanggalText=(TextView)findViewById(R.id.tanggalDeteksi);
        jamText=(TextView)findViewById(R.id.jamDeteksi);
        status=(TextView)findViewById(R.id.statusDeteksi);
        nameText.setText(nama.get(0));
        tanggalText.setText(nama.get(1));
        jamText.setText(nama.get(2));
        status.setText(nama.get(3));
    }
    public void back(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
}
