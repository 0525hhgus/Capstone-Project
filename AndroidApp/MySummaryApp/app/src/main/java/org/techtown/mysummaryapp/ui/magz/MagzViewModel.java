package org.techtown.mysummaryapp.ui.magz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MagzViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MagzViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is magz fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}