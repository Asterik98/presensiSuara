package com.example.asterik.presensi.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asterik.presensi.R;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class RekamAddData extends AppCompatActivity {
    public static final String PEGAWAI_DATA= "pegawai_data";
    TextView detik;
    TextView detikTeks;
    ImageView gambarDetik;
    ImageButton mulai;
    ImageButton kirim;
    ImageButton record;
    Integer second;
    Integer indeks=1;
    ProgressBar progressBar;
    ProgressBar progres;
    CountDownTimer timer;
    String name;
    private MediaRecorder recorder;
    private static String fileName;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private String uploadUrl="http://192.168.1.101:80/new/";
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
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
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
        mulai.setVisibility(View.INVISIBLE);
        detik.setVisibility(View.VISIBLE);
        detikTeks.setVisibility(View.VISIBLE);
        gambarDetik.setVisibility(View.VISIBLE);
        progres.setProgressTintList(ColorStateList.valueOf(0xFFFF6D00));
        second=3;
        startRecording(indeks);
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
                    stopRecording();
                    kirim.setVisibility(View.VISIBLE);
                    record.setVisibility(View.GONE);
                    detik.setVisibility(View.GONE);
                    detikTeks.setVisibility(View.GONE);
                    gambarDetik.setVisibility(View.GONE);
                }else{
                    stopRecording();
                    detik.setText(String.valueOf(5-indeks));
                    detikTeks.setText("Lagi");
                }

            }
        }.start();
    }
    public void kirim(View v){
        progressBar.setVisibility(View.VISIBLE);
        showLoading(true);
        uploadAudio();

    }

    private void startRecording(int indeks) {
        fileName=getExternalCacheDir().getAbsolutePath();
        fileName += "/"+ String.valueOf(indeks)+".wav";
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
        Map<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
        params.put("audio", audioToString());
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, uploadUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                showLoading(false);
                Intent moveWithObjectIntent = new Intent(getApplicationContext(), DataTerdaftar.class);
                moveWithObjectIntent.putExtra(DataTerdaftar.PEGAWAI_DATA_TERDAFTAR,name);
                startActivity(moveWithObjectIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonRequest);
    }

    private ArrayList<String> audioToString(){
        ArrayList<String>dataBaru=new ArrayList<>();
        while(indeks>0) {
            File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.asterik.presensi/cache/" + String.valueOf(indeks) + ".wav");
            try {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                dataBaru.add(encoded);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return dataBaru;
    }
    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}