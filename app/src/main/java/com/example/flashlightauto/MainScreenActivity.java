package com.example.flashlightauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {


    Button button_on_off;
    //true = on, false = off
    public static boolean check_button;
    public static Camera.Parameters parameters;
    public static Camera camera;
    private BroadcastReceiver mReceiver = null;
    boolean check_flashlight;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button_on_off = findViewById(R.id.mainScreenActivity_button_flashlight);

        // initialize receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

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

        boolean isCameraFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        camera = Camera.open();
    }

    //receiver
    @Override
    protected void onPause() {
        // when the screen is about to turn off
        if (ScreenReceiver.wasScreenOn) {
            // this is the case when onPause() is called by the system due to a screen state change
            Log.e("MainScreenActivity", "SCREEN TURNED OFF");
            //MainScreenActivity.setFlashLigthOn();
            check_button = false;

        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // only when screen turns on
        button_on_off.setBackgroundResource(R.drawable.flashlight_off);
        if (!ScreenReceiver.wasScreenOn) {
            // this is when onResume() is called due to a screen state change
            Log.e("MainScreenActivity", "SCREEN TURNED ON");
            MainScreenActivity.setFlashLightOff();
            check_button = true;
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }


    public static void setFlashLigthOn() {
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


    public static void setFlashLightOff() {
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

