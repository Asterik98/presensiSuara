package com.example.asterik.presensi.ui.tambahData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.RekamAddData;

public class TambahDataFragment extends Fragment {
    ImageButton next;
    EditText name;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tambah, container, false);
        next=(ImageButton)root.findViewById(R.id.next);
        name=(EditText) root.findViewById(R.id.nama);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()!=0) {
                    String nama=String.valueOf(name.getText());
                    Intent moveWithObjectIntent = new Intent(getContext(), RekamAddData.class);
                    moveWithObjectIntent.putExtra(RekamAddData.PEGAWAI_DATA, nama);
                    startActivity(moveWithObjectIntent);
                }else{
                    name.setError("Isi Nama Terlebih Dahulu");
                }
            }
        });
        return root;
    }

}
