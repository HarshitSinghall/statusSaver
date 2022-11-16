package com.whatsappbusiness.statussaver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whatsappbusiness.statussaver.databinding.ActivityPermissionBinding;

public class permissionActivity extends AppCompatActivity {

    ActivityPermissionBinding binding;
    boolean started = false;
    Button permissionBtn;
    Boolean present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        permissionBtn = binding.permissionBtn;

        present = getSharedPreferences("MY_PREF", MODE_PRIVATE).getBoolean("onBoarding", false);

        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onResume() {
        super.onResume();
        if (Environment.isExternalStorageManager()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            if (present) {
                startActivity(new Intent(this, MainActivity.class));
            }else{
                startActivity(new Intent(this, onBoardingActivity.class));
            }
        }

    }
}