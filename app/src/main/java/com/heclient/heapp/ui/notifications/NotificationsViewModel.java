package com.heclient.heapp.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("数据处理");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void setText(String s) {
        this.mText.setValue(s);
    }
}