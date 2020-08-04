package com.example.flashlightauto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.getIntent;
import static android.widget.Toast.LENGTH_SHORT;

public class CheckScreenService extends Service {

    boolean check_service;

    public int onStartCommand(Intent intent, int flags, int startId) {

        check_service = getIntent().getBooleanExtra("1",true);

        serviceTask();
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented"); }

    void serviceTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //while(true){}
//todo ScreenCheck

            }
        }).start();
    }
}
