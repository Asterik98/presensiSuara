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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asterik.presensi.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class Rekam extends AppCompatActivity {
    TextView detik;
    Integer second;
    private static String fileName;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private String uploadUrl="http://192.168.1.101:80/";
    private MediaRecorder recorder;
    private MediaStore.Audio audio;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rekam);
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
        second=3;
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                onRecord(true);
                detik.setTextSize(80);
                detik.setText(String.valueOf(second));
                second--;
            }

            public void onFinish() {
                onRecord(false);
                uploadAudio();
                Intent moveWithObjectIntent = new Intent(getApplicationContext(), HasilPresensiNone.class);
                startActivity(moveWithObjectIntent);
            }
        }.start();

    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioChannels(1);
        recorder.setAudioEncodingBitRate(128000);
        recorder.setAudioSamplingRate(48000);
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

    private void onRecord(boolean start) {
        if (start) {
            startRecording();

        } else {
            stopRecording();
        }
    }

    private void uploadAudio() {
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "test");
                params.put("audio", audioToString());

                return params;
            }
        };
        queue.add(stringRequest);
    }

    private String audioToString(){
        File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.asterik.presensi/cache/test.wav");
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            String encoded = Base64.encodeToString(bytes, 0);
            return encoded;
        }catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
