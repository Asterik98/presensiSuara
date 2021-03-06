package com.example.asterik.presensi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.Pegawai;
import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.adapter.listViewHapusAdapter;
import com.example.asterik.presensi.ui.adapter.listViewPilihAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class pilihNama extends AppCompatActivity {
    private RecyclerView rvCategory;
    private ProgressBar progressBar;
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    ArrayList<Pegawai> list;
    public static listViewPilihAdapter listViewPilihAdapter;
    public String name;
    public String waktu;
    Pegawai pegawai;
    SimpleDateFormat simpledateformat=new SimpleDateFormat("dd-MM-yyyy");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pilihnama);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        list=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        final String tanggal=simpledateformat.format(calendar.getTime());
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    pegawai=new Pegawai(null,null,null);
                    name= postSnapshot.getKey();
                    waktu = postSnapshot.child(tanggal).child("Jam").getValue(String.class);
                    pegawai.setName(name);
                    pegawai.setJam(waktu);
                    list.add(pegawai);
                }
                listViewPilihAdapter.notifyDataSetChanged();
                showLoading(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listViewPilihAdapter = new listViewPilihAdapter(getApplicationContext(),list);
        progressBar = findViewById(R.id.progressBar);
        showLoading(true);
        rvCategory = findViewById(R.id.rv_category);
        rvCategory.setAdapter(listViewPilihAdapter);
        rvCategory.addItemDecoration(new DividerItemDecoration(rvCategory.getContext(), DividerItemDecoration.VERTICAL));
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(  new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Pegawai pegawai = list.get(position);
                String nama=pegawai.getName();
                if(pegawai.getJam().equals("-")==true) {
                    Intent moveWithObjectIntent = new Intent(getApplicationContext(), Rekam.class);
                    moveWithObjectIntent.putExtra(Rekam.PILIH_NAMA, nama);
                    startActivity(moveWithObjectIntent);
                    showLoading(false);
                }else{
                    Toast.makeText(getApplicationContext(), nama+" telah presensi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
