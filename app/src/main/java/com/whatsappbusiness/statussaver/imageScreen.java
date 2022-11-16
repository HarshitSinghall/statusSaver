package com.whatsappbusiness.statussaver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.whatsappbusiness.statussaver.databinding.ActivityImageScreenBinding;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class imageScreen extends AppCompatActivity {

    ActivityImageScreenBinding binding;
    String imagePath;
    ImageView imageView;
    Toolbar toolbar;
    File folder = null;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = binding.image;
        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        imagePath = getIntent().getStringExtra("path");

        Glide.with(this).load(imagePath).fitCenter().into(imageView);


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
                                rePost(imagePath);
                            } else {
                                Toast.makeText(imageScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
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
                                saveImg(imagePath);
                            } else {
                                Toast.makeText(imageScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
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
                                shareNow(imagePath);
                            } else {
                                Toast.makeText(imageScreen.this, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
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


    private Boolean createDirectory(String name) {
        folder = getDir(name, Context.MODE_PRIVATE);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_menu, menu);
        return true;
    }


    private void saveImg(String path) {

        try {

            File file = new File(path);
            String fname = file.getName().substring(file.getName().length() - 10);
            File file2 = new File(folder, fname);

            FileUtils.copyFile(file, file2);

            Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void shareNow(String path) {

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


    private void rePost(String path) {

        try {
            File file = new File(path);
            File file2 = new File(folder, "Image.jpg");
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
            intent.setType("images/*");
            startActivity(Intent.createChooser(intent, "share with"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}