package org.techtown.mysummaryapp.ui.judm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JudmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JudmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is judm fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}