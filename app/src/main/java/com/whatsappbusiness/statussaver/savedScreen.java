package com.whatsappbusiness.statussaver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.whatsappbusiness.statussaver.Modals.imagesModals;
import com.whatsappbusiness.statussaver.adaptors.recyclerAdaptor;
import com.whatsappbusiness.statussaver.databinding.ActivitySavedScreenBinding;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class savedScreen extends AppCompatActivity {

    ActivitySavedScreenBinding binding;
    ImageView imageView;
    String file_path;
    VideoView videoPlayer;
    MediaController controller;
    Toolbar toolbar;
    Boolean success = false;
    File folder = null;
    int fileIndex;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        file_path = getIntent().getStringExtra("path");
        fileIndex = getIntent().getIntExtra("index",0);
        imageView = binding.imageView;
        videoPlayer = binding.videoView;
        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        if (!file_path.contains(".mp4")){
            imageView.setVisibility(View.VISIBLE);
            videoPlayer.setVisibility(View.GONE);
            Glide.with(this).load(file_path).fitCenter().into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
            videoPlayer.setVisibility(View.VISIBLE);
            setUpVideo();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_item:
                        deleteFile(fileIndex);
                        break;
                    case R.id.repost_item:
                        if (file_path.contains(".mp4")){
                            repostVideo();
                        }else{
                            repostImages();
                        }
                        break;
                    case R.id.share_item:
                        if (file_path.contains(".mp4")){
                            shareVideo();
                        }else{
                            shareImage();
                        }
                        break;
                }
                return true;
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void repostVideo() {
        success = createDirectory("temp_directory");
        if (success) {
            if (Environment.isExternalStorageManager()) {
                rePost(file_path);
            } else {
                Toast.makeText(savedScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void repostImages() {
        success = createDirectory("temp_directory");
        if (success) {
            if (Environment.isExternalStorageManager()) {
                rePostImage(file_path);
            } else {
                Toast.makeText(savedScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void shareVideo() {
        success = createDirectory("temp_directory");
        if (success) {
            if (Environment.isExternalStorageManager()) {
                shareNow(file_path);
            } else {
                Toast.makeText(savedScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void shareImage() {
        success = createDirectory("temp_directory");
        if (success) {
            if (Environment.isExternalStorageManager()) {
                shareNowImage(file_path);
            } else {
                Toast.makeText(savedScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }

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


    public void shareNowImage(String path) {

        try {
            File file = new File(path);
            File file2 = new File(folder, "Image.jpg");
            FileUtils.copyFile(file, file2);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(this, "com.whatsappbusiness.statussaver.provider", file2);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setType("images/*");
            startActivity(Intent.createChooser(intent, "share with"));

        } catch (IOException e) {
            e.printStackTrace();
        }


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


    private void setUpVideo() {
        imageView.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.VISIBLE);

        controller = new MediaController(this);

//        videoPlayer.setVideoURI(Uri.parse(file_path));
        videoPlayer.setVideoPath(file_path);
        videoPlayer.setMediaController(controller);
        videoPlayer.start();

    }

    private void deleteFile(int fileIndex) {
        File directory=null;
        if (Constraints.SELECTED == 1) {
            directory = getDir("StatusSaverWhatsApp", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 2) {
            directory = getDir("StatusSaverBusiness", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 3) {
            directory = getDir("StatusSaverGB", Context.MODE_PRIVATE);
        }

        directory.listFiles()[fileIndex].delete();
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        finish();


    }


    private void rePostImage(String path) {

        try {
            File file = new File(path);
            File file2 = new File(folder, "tempImage.jpg");
            FileUtils.copyFile(file, file2);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            if (Constraints.SELECTED == 1) {
                intent.setPackage("com.whatsapp");
            } else if (Constraints.SELECTED == 2) {
                intent.setPackage("com.whatsapp.w4b");
            } else if (Constraints.SELECTED == 3) {
                intent.setPackage("com.gbwhatsapp");
            }
            Uri uri = FileProvider.getUriForFile(this, "com.whatsappbusiness.statussaver.provider", file2);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setType("images/*");
            startActivity(Intent.createChooser(intent, "share with"));

        } catch (IOException e) {
            e.printStackTrace();
        }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.saved_menu, menu);
        return true;
    }

}