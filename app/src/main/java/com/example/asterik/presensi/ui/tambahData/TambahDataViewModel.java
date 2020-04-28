package com.example.asterik.presensi.ui.tambahData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TambahDataViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TambahDataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}