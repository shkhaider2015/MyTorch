package com.example.mytorch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_CODE = 2;
    private Button btnFlash;
    private boolean hasCameraFlash = false;
    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFlash = findViewById(R.id.switch_on);
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                askPermission(Manifest.permission.CAMERA, CAMERA_CODE);
            }
        });






    }

    private void flasLight()
    {
        if (hasCameraFlash)
        {
            if (isFlashOn)
            {
                btnFlash.setText("ON");
                flashLightOff();
                isFlashOn = false;

            }
            else
            {
                btnFlash.setText("OFF");
                flashLightOn();
                isFlashOn = true;
            }

        }
        else
        {
            Toast.makeText(this, "No Flash Avalable", Toast.LENGTH_SHORT).show();

        }

    }

    private void flashLightOn()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraID = Objects.requireNonNull(cameraManager).getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                cameraManager.setTorchMode(cameraID, true);
            }
            else
            {
                Log.d(TAG, "flashLightOn: Not Prepare for this device");
            }
        }catch (CameraAccessException e)
        {
            Log.e(TAG, "flashLightOn: ", e);
        }
    }
    private void flashLightOff()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraID = Objects.requireNonNull(cameraManager).getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                cameraManager.setTorchMode(cameraID, false);
            }
            else
            {
                Log.d(TAG, "flashLightOn: Not Prepare for this device");
                
            }
        }catch (CameraAccessException e)
        {
            Log.e(TAG, "flashLightOn: ", e);
        }

    }

    private void askPermission(String permission, int requestcode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestcode);
        }
        else
        {
            flashLightOn();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                Toast.makeText(this, "Camera permission Granted", Toast.LENGTH_SHORT).show();
                flasLight();
            }
            else
            {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else
        {
            return;
        }
    }
}
