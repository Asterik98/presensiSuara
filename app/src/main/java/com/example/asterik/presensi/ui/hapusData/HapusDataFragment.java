package com.example.asterik.presensi.ui.hapusData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.KonfirmasiDeletePegawai;
import com.example.asterik.presensi.ui.RekamAddData;
import com.example.asterik.presensi.ui.adapter.listViewHapusAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HapusDataFragment extends Fragment {

    private RecyclerView rvCategory;
    private ProgressBar progressBar;
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    ArrayList<String>list;
    public static ArrayList<String>hapus;
    public static listViewHapusAdapter listHapusAdapter;
    public String name;
    public static TextView jumlahHapus;
    public static ImageButton hapusButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hapus, container, false);
        jumlahHapus=(TextView)root.findViewById(R.id.jumlahHapus);
        hapusButton=(ImageButton)root.findViewById(R.id.hapus);
        firedb = FirebaseDatabase.getInstance();
        daftar = firedb.getReference("Daftar");
        list=new ArrayList<>();
        daftar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    name= postSnapshot.getKey();
                    list.add(name);
                }
                listHapusAdapter.notifyDataSetChanged();
                showLoading(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        hapusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveWithObjectIntent = new Intent(getContext(), KonfirmasiDeletePegawai.class);
                moveWithObjectIntent.putExtra(RekamAddData.PEGAWAI_DATA, hapus);
                startActivity(moveWithObjectIntent);
            }
        });
        listHapusAdapter = new listViewHapusAdapter(getActivity(),list);
        progressBar = root.findViewById(R.id.progressBar);
        showLoading(true);
        rvCategory = root.findViewById(R.id.rv_category);
        rvCategory.setAdapter(listHapusAdapter);
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
    public static void getDataHapus(ArrayList<String>dataHapus){
        if(dataHapus.size()==0) {
            jumlahHapus.setVisibility(View.INVISIBLE);
            hapusButton.setVisibility(View.INVISIBLE);
        }else{
            jumlahHapus.setVisibility(View.VISIBLE);
            hapusButton.setVisibility(View.VISIBLE);
        }
        jumlahHapus.setText(String.valueOf(dataHapus.size()));
        hapus=dataHapus;
    }
}
