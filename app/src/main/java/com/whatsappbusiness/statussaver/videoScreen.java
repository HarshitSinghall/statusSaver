package com.whatsappbusiness.statussaver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.whatsappbusiness.statussaver.databinding.ActivityVideoScreenBinding;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class videoScreen extends AppCompatActivity {

    ActivityVideoScreenBinding binding;
    MediaController controller;
    VideoView videoPlayer;
    File folder = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoPlayer = binding.videoPlayer;
        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);
        String path = getIntent().getStringExtra("path");

//        String path = UriUtils.getPathFromUri(videoScreen.this, Uri.parse(video_path));

        controller = new MediaController(this);

        videoPlayer.setVideoURI(Uri.parse(path));
        videoPlayer.start();

        videoPlayer.setMediaController(controller);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Boolean success = false;
                switch (item.getItemId()) {
                    case R.id.repost_item:
                        success = createDirectory("temp_directory");
                        if (success) {
                            if (Environment.isExternalStorageManager()) {
                                rePost(path);
                            } else {
                                Toast.makeText(videoScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(getpermission);
                            }
                        }
                        break;
                    case R.id.save_item:
                        if (Constraints.SELECTED == 1) {
                            success = createDirectory("StatusSaverWhatsApp");
                        } else if (Constraints.SELECTED == 2) {
                            success = createDirectory("StatusSaverBusiness");
                        } else if (Constraints.SELECTED == 3) {
                            success = createDirectory("StatusSaverGB");
                        }
                        if (success) {
                            if (Environment.isExternalStorageManager()) {
                                saveVideo(path);
                            } else {
                                Toast.makeText(videoScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(getpermission);
                            }

                        }

                        break;
                    case R.id.share_item:
                        success = createDirectory("temp_directory");
                        if (success) {
                            if (Environment.isExternalStorageManager()) {
                                shareNow(path);
                            } else {
                                Toast.makeText(videoScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(getpermission);
                            }
                        }

                        break;
                }
                return true;
            }
        });

    }

    private void rePost(String path) {

        try {
            File file = new File(path);
            File file2 = new File(folder, "Video.mp4");
            FileUtils.copyFile(file, file2);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            if (Constraints.SELECTED == 1) {
                intent.setPackage(Constraints.WHATSAPP_PACKAGE);
            } else if (Constraints.SELECTED == 2) {
                intent.setPackage(Constraints.WHATSAPP_BUSINESS_PACKAGE);
            } else if (Constraints.SELECTED == 3) {
                intent.setPackage(Constraints.WHATSAPP_GB_PACKAGE);
            }
            Uri uri = FileProvider.getUriForFile(this, "com.whatsappbusiness.statussaver.provider", file2);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setType("video/*");
            startActivity(Intent.createChooser(intent, "share with"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveVideo(String video_path) {

        try {

            File file = new File(video_path);
            String fname = file.getName().substring(file.getName().length() - 10);
            File file2 = new File(folder, fname);

            FileUtils.copyFile(file, file2);

            Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private Boolean createDirectory(String name) {
        folder = getDir(name, Context.MODE_PRIVATE);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (controller.isShowing()) {
            controller.hide();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_menu, menu);
        return true;
    }

    public void shareNow(String path) {

        try {
            File file = new File(path);
            File file2 = new File(folder, "Video.mp4");
            FileUtils.copyFile(file, file2);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(this, "com.whatsappbusiness.statussaver.provider", file2);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setType("video/*");
            startActivity(Intent.createChooser(intent, "share with"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
