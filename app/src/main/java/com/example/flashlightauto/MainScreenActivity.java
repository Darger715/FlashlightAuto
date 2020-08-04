package com.example.flashlightauto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.security.Policy;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    Button button_on_off;
    //true = on, false = off
    boolean check_button;
    Camera.Parameters parameters;
    private Camera camera;
    boolean check_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button_on_off = findViewById(R.id.mainScreenActivity_button_flashlight);
        button_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to off
                if (check_button) {
                    button_on_off.setBackgroundResource(R.drawable.flashlight_off);
                    check_button = false;
                    setFlashLightOff();
                }

                //to on
                else {
                    button_on_off.setBackgroundResource(R.drawable.flashlight_on);
                    check_button = true;
                    setFlashLigthOn();
                }
            }
        });

        //запуск Сервиса
        (findViewById(R.id.mainScreenActivity_button_turnOnService)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = check_service;
                startService(new Intent(MainScreenActivity.this,CheckScreenService.class));
            check_service = true;
            if(flag != check_service)
            {
                Intent toCheckScreenService =  new Intent(MainScreenActivity.this,CheckScreenService.class);
                toCheckScreenService.putExtra("1",check_service);
                //startActivity(toCheckScreenService);

            }
            }
        });

        //выключение Сервиса
        (findViewById(R.id.mainScreenActivity_button_turnOffService)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = check_service;
                stopService(new Intent(MainScreenActivity.this,CheckScreenService.class));
                check_service = false;
                if(flag != check_service)
                {
                    Intent toCheckScreenService =  new Intent(MainScreenActivity.this,CheckScreenService.class);
                    toCheckScreenService.putExtra("1",check_service);
                    //startActivity(toCheckScreenService);
            }}
        });
        boolean isCameraFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        camera = Camera.open();
    }


    private void setFlashLigthOn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (camera != null) {

                    parameters = camera.getParameters();

                    if (parameters != null) {
                        List supportedFlashModes = parameters.getSupportedFlashModes();

                        if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                        } else camera = null;

                        if (camera != null) {
                            camera.setParameters(parameters);
                            camera.startPreview();
                            try {
                                camera.setPreviewTexture(new SurfaceTexture(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }


    private void setFlashLightOff() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (camera != null) {

                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                }
            }
        }).start();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}

class BootBroadcast extends BroadcastReceiver{
    @Override
public void onReceive(Context context, Intent intent){
        context.startService(new Intent(context, CheckScreenService.class));
    }
}