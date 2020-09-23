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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.Pegawai;
import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.DatePickerFragment;
import com.example.asterik.presensi.ui.adapter.listViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;
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
    ImageButton pilihTanggal;
    TextView tanggal;
    TextView tahun;
    Locale indo=new Locale("id");
    SimpleDateFormat simpledateformat=new SimpleDateFormat("EEEE, MMMM d",indo);
    SimpleDateFormat simpledateformat2=new SimpleDateFormat("dd-MM-yyyy");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daftar, container, false);
        firedb = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        daftar = firedb.getReference("Daftar");
        list=new ArrayList<>();
        pilihTanggal=(ImageButton)root.findViewById(R.id.tanggal);
        tanggal=(TextView) root.findViewById(R.id.teksTanggal);
        tahun=(TextView) root.findViewById(R.id.teksTahun);
        Calendar calendar=Calendar.getInstance();
        String fixDate=simpledateformat.format(calendar.getTime());
        final String dateNow=simpledateformat2.format(calendar.getTime());
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
                    waktu = postSnapshot.child(dateNow).child("Jam").getValue(String.class);
                    status = postSnapshot.child(dateNow).child("Status").getValue(String.class);
                    pegawai.setName(name);
                    pegawai.setStatus(status);
                    pegawai.setJam(waktu);
                    list.add(pegawai);
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
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, final int year, final int monthOfYear,
                              final int dayOfMonth) {
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            String fixDate=simpledateformat.format(calendar.getTime());
            view.setMaxDate(System.currentTimeMillis());
            tanggal.setText(fixDate);
            tahun.setText(String.valueOf(year));
            list.clear();
            final String infoDate;
            if (monthOfYear+1<10) {
                if(dayOfMonth<10){
                    infoDate = "0" + (dayOfMonth) + "-" +"0"+ (monthOfYear + 1) + "-" + year;
                }else {
                    infoDate = dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year;
                }
            }else{
                if(dayOfMonth<10){
                    infoDate = "0" + dayOfMonth + "-" +(monthOfYear + 1) + "-" + year;
                }else {
                    infoDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                }
            }

            daftar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                        pegawai=new Pegawai(null,null,null);
                        name= postSnapshot.getKey();
                        waktu = postSnapshot.child(infoDate).child("Jam").getValue(String.class);
                        status = postSnapshot.child(infoDate).child("Status").getValue(String.class);
                        pegawai.setName(name);
                        pegawai.setStatus(status);
                        pegawai.setJam(waktu);
                        list.add(pegawai);
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
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(),"Sign In Terlebih Dahulu",Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.navigation_login);
        }
    }
}
