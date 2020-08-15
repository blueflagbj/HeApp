package com.heclient.heapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mTextUser;
    private MutableLiveData<String> mTextPSW;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("操作登陆");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<String> getmTextUser() {
        return mTextUser;
    }

    public void setmTextUser(MutableLiveData<String> mTextUser) {
        this.mTextUser = mTextUser;
    }

    public MutableLiveData<String> getmTextPSW() {
        return mTextPSW;
    }

    public void setmTextPSW(MutableLiveData<String> mTextPSW) {
        this.mTextPSW = mTextPSW;
    }
}