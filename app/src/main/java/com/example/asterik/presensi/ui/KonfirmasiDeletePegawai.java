package com.example.asterik.presensi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asterik.presensi.MainActivity;
import com.example.asterik.presensi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class KonfirmasiDeletePegawai extends AppCompatActivity {
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    public static final String PEGAWAI_DATA= "pegawai_data";
    public static ArrayList<String> nama;
    private String uploadUrl="http://18.220.9.243:5000/hapus";
    TextView teksKonfirmasi;
    String namaYangDihapus;
    ImageButton hapus;
    ImageButton cancel;
    ImageButton back;
    ProgressBar progres;
    String[]name;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_konfirmasi_hapus);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        nama=getIntent().getStringArrayListExtra(PEGAWAI_DATA);
        teksKonfirmasi=(TextView)findViewById(R.id.textKonfirmasi);
        hapus=(ImageButton) findViewById(R.id.hapus);
        cancel=(ImageButton) findViewById(R.id.cancel);
        back=(ImageButton) findViewById(R.id.back);
        progres=(ProgressBar)findViewById(R.id.progress);
        name= new String[nama.size()];
        for(int i=0;i<nama.size();i++){
            String elemenNama=nama.get(i);
            name[i]=elemenNama;
            if(namaYangDihapus==null) {
                namaYangDihapus = elemenNama;
            }else{
                namaYangDihapus = namaYangDihapus+", "+elemenNama;
            }

        }
        teksKonfirmasi.setText("Apakah anda akan menghapus data "+namaYangDihapus+" ?");
    }
    public void hapus(View v){
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    String name=postSnapshot.getKey();
                    if(name.equals(nama.get(i))){
                        daftar.child(name).removeValue();
                        i++;
                        if(i==nama.size()){
                            break;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        requestDelete();
    }
    private void requestDelete() {
        showLoading(true);
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("data", name);
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, uploadUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    Log.d("hasil",obj.getString("hasil"));
                    showLoading(false);
                    teksKonfirmasi.setText("Sukses\nDihapus");
                    teksKonfirmasi.setTextSize(50);
                    teksKonfirmasi.setGravity(Gravity.CENTER_HORIZONTAL);
                    hapus.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }
    public void cancel(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
    public void back(View v){
        Intent moveWithObjectIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveWithObjectIntent);
    }
    private void showLoading(Boolean state) {
        if (state) {
            progres.setVisibility(View.VISIBLE);
        } else {
            progres.setVisibility(View.GONE);
        }
    }
}
