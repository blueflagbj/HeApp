package com.heclient.heapp.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;


public class DashboardViewModel extends ViewModel {

    private Timer timer = null;//设置定时器
    private String xlsPath;

    private MutableLiveData<String> mText;
    private MutableLiveData<ListView> mLv;


    @SuppressLint("HandlerLeak")
    private static Handler MessageHandle =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 100:
                   /* if (PartnerLoginFragment.this.duration > 0){
                        PartnerLoginFragment.this.sendSmsBtn.setTextColor(-1);
                        PartnerLoginFragment.this.sendSmsBtn.setClickable(false);
                        PartnerLoginFragment.this.sendSmsBtn.setText(PartnerLoginFragment.this.duration + PartnerLoginFragment.SECOND_DESC);
                        PartnerLoginFragment.access$2310(PartnerLoginFragment.this);
                        return;
                    }else{
                        PartnerLoginFragment.this.sendSmsTask.cancel();
                        PartnerLoginFragment.this.sendSmsBtn.setClickable(true);
                        PartnerLoginFragment.this.sendSmsBtn.setText("再次发送");
                        return;
                    }*/
                    break;
                case 200:


                    break;
                default:
                    break;
            }
        }
    };


    public DashboardViewModel() {

        mText = new MutableLiveData<>();
        mLv = new MutableLiveData<>();

        xlsPath= Environment.getExternalStorageDirectory().getPath() + "/Pictures";

         mText.setValue("采集数据");

    }

    public LiveData<String> getText() {
        return mText;
    }
    public void setText(String s) {
       this.mText.setValue(s);
    }
    public LiveData<ListView> getLv(){return mLv;}

    private void startTimerTask() {
        GetDBTask getDBTask = new GetDBTask();
        this.timer.schedule(getDBTask, 0, 1000);
        // this.duration = 30;
    }

    private class GetDBTask extends TimerTask {

        private GetDBTask(){

        }
        @Override
        public void run() {
            DashboardViewModel.MessageHandle.sendEmptyMessage(100);
        }
    }
}