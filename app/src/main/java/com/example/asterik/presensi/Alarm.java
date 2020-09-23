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
import java.util.Locale;

import static com.loopj.android.http.AsyncHttpClient.log;


public class Alarm extends BroadcastReceiver{
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    private DatabaseReference testIsi;
    public static final String TYPE_REPEATING = "Daily Remainder";
    public static final String EXTRA_TYPE = "type";
    private final int ID_REPEATING = 101;
    boolean flag=false;
    private ArrayList<String>daftarNama=new ArrayList<>();
    String fixDate;
    Locale indo=new Locale("id");
    SimpleDateFormat simpledateformat=new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat simpledateformat2=new SimpleDateFormat("HH");
    SimpleDateFormat simpledateformat3=new SimpleDateFormat("EEEE",indo);
    Calendar calendar=Calendar.getInstance();
    Calendar calendar2=Calendar.getInstance();
    public Alarm() {
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        testIsi = firedb.getReference("Daftar");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            flag=true;
        }else {
            String jam=simpledateformat2.format(calendar2.getTime());
            log.d("jam",jam);
            if(jam.equals("23") || jam.equals("00")||jam.equals("01")){
                flag=true;
            }
        }
        setFirebase();
    }
    public void setFirebase(){
        final String hari=simpledateformat3.format(calendar2.getTime());
        getName(new MyCallback() {
            @Override
            public void onCallback(ArrayList<String> value) {
                if(flag==true) {
                    daftarNama = value;
                    fixDate = simpledateformat.format(calendar.getTime());
                    for (int i = 0; i < daftarNama.size(); i++) {
                        if(hari.equals("Minggu")==false) {
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Jam").setValue("-");
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Status").setValue("Tidak Masuk");
                        }else{
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Jam").setValue("-");
                            daftar.child(String.valueOf(daftarNama.get(i))).child(fixDate).child("Status").setValue("Libur");
                        }
                    }
                    flag=false;
                    Log.d("flag","b");
                }
            }
        });
    }
    public void setRepeatingAlarm(Context context, String type) {
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 05);
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
