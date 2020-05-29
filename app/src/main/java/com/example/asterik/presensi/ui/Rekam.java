package com.example.asterik.presensi.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asterik.presensi.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import static java.lang.Integer.parseInt;

public class Rekam extends AppCompatActivity {
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    TextView detik;
    TextView info;
    Integer second;
    public static String PILIH_NAMA = "PILIH_NAMA";
    private static String fileName;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private String uploadUrl="http://192.168.1.102:80/";
    private MediaRecorder recorder;
    private String hasil;
    private ProgressBar progressBar;
    ImageButton rekam;
    String nama;
    SimpleDateFormat simpledateformat=new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat simpledateformat2=new SimpleDateFormat("HH.mm");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rekam);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        nama=getIntent().getStringExtra(PILIH_NAMA);
        rekam=(ImageButton)findViewById(R.id.record);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        info=(TextView) findViewById(R.id.infoRekam);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        fileName=getExternalCacheDir().getAbsolutePath();
        fileName += "/test.wav";
        detik=(TextView)findViewById(R.id.waktu);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }
    public void recordSuara(View view){
        rekam.setVisibility(view.INVISIBLE);
        second=3;
        startRecording();
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                detik.setTextSize(80);
                detik.setText(String.valueOf(second));
                second--;
            }

            public void onFinish() {
                info.setVisibility(View.INVISIBLE);
                stopRecording();
                uploadAudio();


            }
        }.start();

    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        recorder.setAudioChannels(1);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(fileName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("a", "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void uploadAudio() {
        showLoading(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("audio", audioToString());
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, uploadUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    hasil=obj.getString("prediction");
                    showLoading(false);
                    Log.d("Nama",nama);
                    if(hasil.equals(nama)==false) {
                        Intent moveWithObjectIntent = new Intent(getApplicationContext(), HasilPresensiNone.class);
                        moveWithObjectIntent.putExtra(HasilPresensiNone.NAMA_TEMP, nama);
                        startActivity(moveWithObjectIntent);
                    }else if(hasil.equals(nama)==true){
                        Calendar calendar=Calendar.getInstance();
                        String tanggal=simpledateformat.format(calendar.getTime());
                        String waktu=simpledateformat2.format(calendar.getTime());
                        String jam=waktu.substring(0,2);
                        String menit=waktu.substring(3,5);
                        String status=null;
                        if(parseInt(jam)>=8){
                            if(parseInt(jam)==8 && parseInt(menit)<=30){
                                status="Tepat";
                            }else if(parseInt(jam)==8 && parseInt(menit)>30){
                                status="Terlambat";
                            }if(parseInt(jam)>=9){
                                if(parseInt(jam)<=16){
                                    if(parseInt(jam)<16){
                                        status="Terlambat";
                                    }else if(parseInt(jam)==16 &&parseInt(menit)<=30){
                                        status="Terlambat";
                                    }else{
                                        status="Tidak Masuk";
                                    }
                                }else{
                                    status="Tidak Masuk";
                                }
                            }
                        }else if(parseInt(jam)<8){
                            status="Tepat";
                        }
                        ArrayList<String>dataPresensi=new ArrayList<>();
                        dataPresensi.add(hasil);
                        dataPresensi.add(tanggal);
                        dataPresensi.add(waktu);
                        dataPresensi.add(status);
                        daftar.child(hasil).child(tanggal).child("Jam").setValue(waktu);
                        daftar.child(hasil).child(tanggal).child("Status").setValue(status);
                        Intent moveWithObjectIntent = new Intent(getApplicationContext(), HasilPresensiTerdeteksi.class);
                        moveWithObjectIntent.putExtra(HasilPresensiTerdeteksi.HASIL_NAMA,dataPresensi);
                        startActivity(moveWithObjectIntent);
                    }
                }
                catch (JSONException e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonRequest);
    }

    private String audioToString(){
        File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.asterik.presensi/cache/test.wav");
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encoded;
        }catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
