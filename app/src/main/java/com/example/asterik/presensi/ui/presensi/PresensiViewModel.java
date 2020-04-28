package com.example.asterik.presensi.ui.presensi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PresensiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PresensiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}