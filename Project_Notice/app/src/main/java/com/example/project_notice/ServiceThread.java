package com.example.project_notice;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void onPuase(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        while(isRun){
            handler.sendEmptyMessage(0);
            try{
                //스피너를 통해 시간을 결정하여 딜레이를 주는 방법을 생각해봐야겠다.
                Thread.sleep(3000);
                //Thread.sleep(24*60*60*1000);
            }catch (Exception e) {}

            Log.d("쓰레드", "" + isRun);
        }
    }
}
