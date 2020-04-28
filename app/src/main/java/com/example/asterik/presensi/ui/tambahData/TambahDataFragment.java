package com.example.asterik.presensi.ui.tambahData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.asterik.presensi.R;

public class TambahDataFragment extends Fragment {

    private TambahDataViewModel tambahDataViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tambahDataViewModel =
                ViewModelProviders.of(this).get(TambahDataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tambah, container, false);
        return root;
    }
}
