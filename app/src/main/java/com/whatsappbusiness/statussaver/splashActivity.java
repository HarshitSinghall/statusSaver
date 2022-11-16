package com.whatsappbusiness.statussaver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class splashActivity extends AppCompatActivity {

    Boolean present;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        present = getSharedPreferences("MY_PREF",MODE_PRIVATE).getBoolean("onBoarding", false);


        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(new Intent(this, permissionActivity.class));
                finish();
            }else{
                if(present){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                                startActivity(new Intent(splashActivity.this, MainActivity.class));
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    startActivity(new Intent(splashActivity.this, onBoardingActivity.class));
                    finish();
                }
            }

        }else{
            Toast.makeText(this, "This App is valid for 11+ Android Devices", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


}