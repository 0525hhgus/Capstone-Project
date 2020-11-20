package org.techtown.mysummaryapp.ui.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RepoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RepoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Repo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}