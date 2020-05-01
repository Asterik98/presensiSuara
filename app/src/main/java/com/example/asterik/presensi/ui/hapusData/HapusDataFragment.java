package com.example.asterik.presensi.ui.hapusData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.asterik.presensi.R;
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
    public static listViewHapusAdapter listHapusAdapter;
    public String name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hapus, container, false);
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


}
