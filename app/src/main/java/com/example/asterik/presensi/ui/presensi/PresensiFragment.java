package com.example.asterik.presensi.ui.presensi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.asterik.presensi.R;
import com.example.asterik.presensi.ui.Rekam;

public class PresensiFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_presensi, container, false);
        ImageButton rekam = (ImageButton) root.findViewById(R.id.mulai);
        rekam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveWithObjectIntent = new Intent(getContext(), Rekam.class);
                startActivity(moveWithObjectIntent);
            }
        });
        return root;
    }
}
