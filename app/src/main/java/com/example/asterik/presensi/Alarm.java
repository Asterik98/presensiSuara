package com.example.asterik.presensi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Alarm extends BroadcastReceiver{
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    public static final String TYPE_REPEATING = "Daily Remainder";
    public static final String EXTRA_TYPE = "type";
    private final int ID_REPEATING = 101;
    boolean flag=false;
    private ArrayList<String>daftarNama=new ArrayList<>();
    String fixDate;
    SimpleDateFormat simpledateformat=new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat simpledateformat2=new SimpleDateFormat("HH.mm");
    Calendar calendar=Calendar.getInstance();
    public Alarm() {
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar2=Calendar.getInstance();
        String jam=simpledateformat2.format(calendar2.getTime());
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d("jam",jam);
            if(jam.equals("00.00")){
                flag=true;
                Log.d("flag","a");
            }
            getName(new MyCallback() {
                @Override
                public void onCallback(ArrayList<String> value) {
                    if(flag==true) {
                        daftarNama = value;
                        fixDate = simpledateformat.format(calendar.getTime());
                        for (int i = 0; i < daftarNama.size(); i++) {
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Jam").setValue("-");
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Status").setValue("Tidak Masuk");
                        }
                        flag=false;
                        Log.d("flag","b");
                    }
                }
            });
        }else{
            Log.d("jam",jam);
            if(jam.equals("00.00")){
                flag=true;
                Log.d("flag","a");
            }
            getName(new MyCallback() {
                @Override
                public void onCallback(ArrayList<String> value) {
                    if(flag==true) {
                        daftarNama = value;
                        fixDate = simpledateformat.format(calendar.getTime());
                        for (int i = 0; i < daftarNama.size(); i++) {
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Jam").setValue("-");
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Status").setValue("Tidak Masuk");
                        }
                        flag=false;
                        Log.d("flag","b");
                    }
                }
            });
        }

    }

    public void setRepeatingAlarm(Context context, String type) {
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        ComponentName receiver = new ComponentName(context, Alarm.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public interface MyCallback {
        void onCallback(ArrayList<String> value);
    }
    private void getName(final MyCallback myCallback){
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    String name= postSnapshot.getKey();
                    daftarNama.add(name);
                }
                myCallback.onCallback(daftarNama);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
