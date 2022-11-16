package com.whatsappbusiness.statussaver.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabItem;
import com.squareup.picasso.Picasso;
import com.whatsappbusiness.statussaver.Constraints;
import com.whatsappbusiness.statussaver.MainActivity;
import com.whatsappbusiness.statussaver.Modals.*;
import com.whatsappbusiness.statussaver.R;
import com.whatsappbusiness.statussaver.fragments.savedFragment;
import com.whatsappbusiness.statussaver.imageScreen;
import com.whatsappbusiness.statussaver.savedScreen;
import com.whatsappbusiness.statussaver.videoScreen;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class recyclerAdaptor extends RecyclerView.Adapter<recyclerAdaptor.ViewHolder> {

    ArrayList<imagesModals> imagesList;
    Context context;
    int FORMAT;
    File folder = null;

    public recyclerAdaptor(ArrayList<imagesModals> imagesList, Context context, int format) {
        this.imagesList = imagesList;
        this.context = context;
        this.FORMAT = format;
    }

    @NonNull
    @Override
    public recyclerAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull recyclerAdaptor.ViewHolder holder, int position) {

        imagesModals item = imagesList.get(position);

//        Picasso.get().load(item.getPath()).fit().into(holder.imageView);

        Glide.with(context).load(item.getPath()).centerCrop().into(holder.imageView);

        holder.playImage.setVisibility(View.GONE);

        if (item.getName().contains(".mp4")){
            holder.playImage.setVisibility(View.VISIBLE);
        }else{
            holder.playImage.setVisibility(View.GONE);
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (FORMAT){
                    case 1:
                        //Image clicked
                        context.startActivity(new Intent(context, imageScreen.class).putExtra("path",item.getPath()));
                        break;
                    case 2:
                        //Video Clicked
                        context.startActivity(new Intent(context, videoScreen.class).putExtra("path",item.getPath()));
                        break;
                    case 3:
                        //Saved Clicked
                        context.startActivity(new Intent(context, savedScreen.class).putExtra("path",item.getPath()).putExtra("index",item.getIndex()));
                        break;
                }
            }
        });

        if (FORMAT == 3){
            holder.downloadText.setText("Delete");
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {

                if (FORMAT == 3){

                    deleteFile(item.getIndex());

                }else{
                    Boolean success = false;
                    if (Constraints.SELECTED == 1) {
                        success = createDirectory("StatusSaverWhatsApp");
                    } else if (Constraints.SELECTED == 2) {
                        success = createDirectory("StatusSaverBusiness");
                    } else if (Constraints.SELECTED == 3) {
                        success= createDirectory("StatusSaverGB");
                    }
                    if (item.getName().contains(".mp4")){
                        if (success){
                            if (Environment.isExternalStorageManager()){
                                saveVideo(item.getPath());
                            }else{
                                Toast.makeText(context, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                context.startActivity(getpermission);
                            }
                        }
                    }else{
                        if (success){
                            if (Environment.isExternalStorageManager()) {
                                saveImg(item.getPath());
                            } else {
                                Toast.makeText(context, "Please Allow Permission!!", Toast.LENGTH_SHORT).show();
                                Intent getpermission = new Intent();
                                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                               context.startActivity(getpermission);
                            }
                        }
                    }
                }

            }
        });


    }


    private void saveVideo(String video_path) {

        try {

            File file = new File(video_path);
            String fname = file.getName().substring(file.getName().length() - 10);
            File file2 = new File(folder, fname);

            FileUtils.copyFile(file, file2);

            Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, playImage;
        TextView downloadText;
        CardView downloadBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.statusImage);
            playImage = itemView.findViewById(R.id.play_img);
            downloadBtn = itemView.findViewById(R.id.downloadBtn);
            downloadText = itemView.findViewById(R.id.downloadText);
        }
    }

    private Boolean createDirectory(String name) {
        folder = context.getDir(name, Context.MODE_PRIVATE);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;
    }



    private void saveImg(String path) {

        try {

            File file = new File(path);
            String fname = file.getName().substring(file.getName().length() - 10);
            File file2 = new File(folder, fname);

            FileUtils.copyFile(file, file2);

            Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteFile(int fileIndex) {
        File directory=null;
        if (Constraints.SELECTED == 1) {
            directory = context.getDir("StatusSaverWhatsApp", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 2) {
            directory = context.getDir("StatusSaverBusiness", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 3) {
            directory = context.getDir("StatusSaverGB", Context.MODE_PRIVATE);
        }

        directory.listFiles()[fileIndex].delete();
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();

        savedFragment.setup(context);

    }


}
