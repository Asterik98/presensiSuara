package com.example.asterik.presensi.ui.hapusData;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asterik.presensi.R;

public class HapusDataFragment extends Fragment {

    private HapusDataViewModel mViewModel;

    public static HapusDataFragment newInstance() {
        return new HapusDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hapus, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HapusDataViewModel.class);
        // TODO: Use the ViewModel
    }

}
