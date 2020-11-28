package org.techtown.gwangjubus.ui.busstop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusstopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BusstopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}