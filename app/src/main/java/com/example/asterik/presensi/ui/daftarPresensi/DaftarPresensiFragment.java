package com.example.asterik.presensi.ui.daftarPresensi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.listViewAdapter;
import com.example.asterik.presensi.ui.Pegawai;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DaftarPresensiFragment extends Fragment {
    private RecyclerView rvCategory;
    private ProgressBar progressBar;
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    private Query kueri;
    ArrayList<Pegawai>list;
    public static listViewAdapter listDaftarAdapter;
    Pegawai pegawai;
    String name;
    String waktu;
    String status;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daftar, container, false);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("presensisuara-4623c");
        kueri=daftar.child("daftar").child("29-03-2019");
        list=new ArrayList<>();

        kueri.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    pegawai=new Pegawai(null,null,null);
                    name= postSnapshot.getKey().toString();
                    waktu= postSnapshot.child(name).child("Jam").getValue(String.class).toString();
                    status=postSnapshot.child(name).child("Status").getValue(String.class).toString();
                    pegawai.setName(name);
                    pegawai.setJam(waktu);
                    pegawai.setStatus(status);
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
}
