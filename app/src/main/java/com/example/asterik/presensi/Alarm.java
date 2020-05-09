package com.example.asterik.presensi;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Alarm extends BroadcastReceiver implements LifecycleOwner {
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    public static final String TYPE_REPEATING = "Daily Remainder";
    public static final String EXTRA_TYPE = "type";
    private final int ID_REPEATING = 101;
    private ArrayList<String>daftarNama=new ArrayList<>();
    String fixDate;
    SimpleDateFormat simpledateformat=new SimpleDateFormat("dd-MM-yyyy");
    public Alarm() {
        Calendar calendar=Calendar.getInstance();
        fixDate=simpledateformat.format(calendar.getTime());
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    String name= postSnapshot.getKey();
                    daftarNama.add(name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }


    public void setRepeatingAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            for(int i=0;i<daftarNama.size();i++) {
                daftar.child(String.valueOf(daftarNama.indexOf(i))).push().setValue(fixDate);
                daftar.child(String.valueOf(daftarNama.indexOf(i))).child(fixDate).child("Jam").setValue("-");
                daftar.child(String.valueOf(daftarNama.indexOf(i))).child(fixDate).child("Status").setValue("Tidak Masuk");
            }
        }
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }
}
