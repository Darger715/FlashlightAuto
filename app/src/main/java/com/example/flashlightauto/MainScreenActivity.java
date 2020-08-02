package com.example.flashlightauto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
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
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

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
    }







    private void showCameraAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_text)
                .setPositiveButton(R.string.exit_message, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void setFlashLigthOn() {

        new Thread(new Runnable() {
            @Override
            public void run() {
           //   if (camera != null) {
           //       parameters = camera.getParameters();

           //       if (parameters != null) {
           //           List supportedFlashModes = parameters.getSupportedFlashModes();


           //           if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
           //               parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
           //           }

           //           try {
           //               camera.setPreviewTexture(new SurfaceTexture(0));
           //           } catch (IOException e) {
           //               e.printStackTrace();
           //           }
           //       }
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            camera.setParameters(parameters);
            }

        }).start();
    }


    private void setFlashLightOff() {
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
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(parameters);
                            camera.stopPreview();
                        }
                    }
                }
            }
        }).start();
    }
}

