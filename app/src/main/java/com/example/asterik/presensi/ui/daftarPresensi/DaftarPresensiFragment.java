package com.example.asterik.presensi.ui.daftarPresensi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.Pegawai;
import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.DatePickerFragment;
import com.example.asterik.presensi.ui.adapter.listViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.getInstance;

public class DaftarPresensiFragment extends Fragment {
    private RecyclerView rvCategory;
    private ProgressBar progressBar;
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    ArrayList<Pegawai>list;
    public static listViewAdapter listDaftarAdapter;
    Pegawai pegawai;
    public String name;
    public String waktu;
    public String status;
    Integer index=0;
    ImageButton pilihTanggal;
    TextView tanggal;
    TextView tahun;
    Locale indo=new Locale("id");
    SimpleDateFormat simpledateformat=new SimpleDateFormat("EEEE, MMMM d",indo);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daftar, container, false);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        list=new ArrayList<>();
        pilihTanggal=(ImageButton)root.findViewById(R.id.tanggal);
        tanggal=(TextView) root.findViewById(R.id.teksTanggal);
        tahun=(TextView) root.findViewById(R.id.teksTahun);
        Calendar calendar=Calendar.getInstance();
        String fixDate=simpledateformat.format(calendar.getTime());
        tanggal.setText(fixDate);
        pilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    pegawai=new Pegawai(null,null,null);
                    name= postSnapshot.getKey();
                    waktu = postSnapshot.child("29-3-2020").child("Jam").getValue(String.class);
                    status = postSnapshot.child("29-3-2020").child("Status").getValue(String.class);
                    pegawai.setName(name);
                    pegawai.setStatus(status);
                    pegawai.setJam(waktu);
                    list.add(pegawai);
                    index++;
                }
                listDaftarAdapter.notifyDataSetChanged();
                showLoading(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        listDaftarAdapter = new listViewAdapter(getActivity(),list);
        progressBar = root.findViewById(R.id.progressBar);
        showLoading(true);
        rvCategory = root.findViewById(R.id.rv_category);
        rvCategory.setAdapter(listDaftarAdapter);
        rvCategory.addItemDecoration(new DividerItemDecoration(rvCategory.getContext(), DividerItemDecoration.VERTICAL));
        rvCategory.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return root;
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, final int year, final int monthOfYear,
                              final int dayOfMonth) {
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            String fixDate=simpledateformat.format(calendar.getTime());
            tanggal.setText(fixDate);
            tahun.setText(String.valueOf(year));
            list.clear();
            daftar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                        Log.d("index",index.toString());
                        String infoDate=String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(year);
                        pegawai=new Pegawai(null,null,null);
                        name= postSnapshot.getKey();
                        waktu = postSnapshot.child(infoDate).child("Jam").getValue(String.class);
                        status = postSnapshot.child(infoDate).child("Status").getValue(String.class);
                        pegawai.setName(name);
                        pegawai.setStatus(status);
                        pegawai.setJam(waktu);
                        list.add(pegawai);
                        index++;
                    }
                    listDaftarAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            listDaftarAdapter = new listViewAdapter(getActivity(),list);
            rvCategory.setAdapter(listDaftarAdapter);
        }
    };
}
