package com.example.flashlightauto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        //экран выключен
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //todo
            //MainScreenActivity.setFlashLigthOn();
            Log.e("ScreenReceiver", "Экран выключился");
            wasScreenOn = false;
        }
        //экран включён
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            //todo
            MainScreenActivity.setFlashLightOff();
            Log.e("ScreenReceiver", "Экран включился");
            wasScreenOn = true;
            if (!MainScreenActivity.check_button) {
                Log.e("MainScreenActivity", "check_button: " + MainScreenActivity.check_button);
                MainScreenActivity.check_button = true;
                //MainScreenActivity.setFlashLightOff();

            } else if (MainScreenActivity.check_button) {
                Log.e("MainScreenActivity", "check_button: " + MainScreenActivity.check_button);
                MainScreenActivity.check_button = false;

                MainScreenActivity.setFlashLigthOn();
            }
        }
    }
}

