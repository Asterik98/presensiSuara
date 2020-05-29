package com.example.asterik.presensi.ui.tambahData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.asterik.presensi.Pegawai;
import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.RekamAddData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TambahDataFragment extends Fragment {
    private FirebaseDatabase firedb;
    private DatabaseReference daftar;
    private FirebaseAuth mAuth;
    ImageButton next;
    EditText name;
    boolean checkNama;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tambah, container, false);
        firedb = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        daftar = firedb.getReference("Daftar");
        next=(ImageButton)root.findViewById(R.id.next);
        name=(EditText) root.findViewById(R.id.nama);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()!=0) {
                    final String nama=String.valueOf(name.getText());
                    checkNama=false;
                    daftar.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                                String name=postSnapshot.getKey();
                                if(nama.equals(name)){
                                    checkNama=true;
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    if(checkNama==true) {
                        name.setError("Nama telah dipakai, silahkan pilih nama lain");
                    }else {
                        Intent moveWithObjectIntent = new Intent(getContext(), RekamAddData.class);
                        moveWithObjectIntent.putExtra(RekamAddData.PEGAWAI_DATA, nama);
                        startActivity(moveWithObjectIntent);
                    }
                }else{
                    name.setError("Isi Nama Terlebih Dahulu");
                }
            }
        });
        return root;
    }
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
