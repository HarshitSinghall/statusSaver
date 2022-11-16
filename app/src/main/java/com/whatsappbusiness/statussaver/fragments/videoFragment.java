package com.whatsappbusiness.statussaver.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.whatsappbusiness.statussaver.Constraints;
import com.whatsappbusiness.statussaver.Modals.imagesModals;
import com.whatsappbusiness.statussaver.R;
import com.whatsappbusiness.statussaver.UriUtils;
import com.whatsappbusiness.statussaver.adaptors.recyclerAdaptor;
import com.whatsappbusiness.statussaver.databinding.FragmentImagesBinding;
import com.whatsappbusiness.statussaver.databinding.FragmentVideoBinding;

import java.util.ArrayList;
import java.util.Objects;


public class videoFragment extends Fragment {

    FragmentVideoBinding binding;
    RecyclerView recyclerView;
    MaterialButton whatsappBtn;
    ProgressDialog dialog;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public videoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        recyclerView = binding.imageRecycler;
        whatsappBtn = binding.whatsAppBtn;

        dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        switch (Constraints.SELECTED) {
            case 1:
                if (isPackageInstalled(Constraints.WHATSAPP_PACKAGE, getContext().getPackageManager())) {
                    if (checkPermission("PATH_WHATSAPP")) {
                        SharedPreferences sp = getActivity().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE);
                        String path = sp.getString("PATH_WHATSAPP", "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                imagesForWhatsApp(path);
                            }
                        }).start();
                    } else {
                        permissionForWhatsApp(Constraints.WHATSAPP_PATH);
                    }
                }else{
                    whatsappBtn.setText("Install WhatsApp");
                    whatsappBtn.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    whatsappBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
                            startActivity(intent);
                        }
                    });
                }
                break;
            case 2:
                if (isPackageInstalled(Constraints.WHATSAPP_BUSINESS_PACKAGE, getContext().getPackageManager())) {
                    if (checkPermission("PATH_WHATSAPP_BUSINESS")) {
                        SharedPreferences sp = getActivity().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE);
                        String path = sp.getString("PATH_WHATSAPP_BUSINESS", "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                imagesForWhatsApp(path);
                            }
                        }).start();
                    } else {
                        permissionForWhatsApp(Constraints.WHATSAPP_BUSINESS_PATH);
                    }
                }else{
                    whatsappBtn.setText("Install WhatsApp Business");
                    whatsappBtn.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    whatsappBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp.w4b"));
                            startActivity(intent);
                        }
                    });
                }
                break;
            case 3:
                if (isPackageInstalled(Constraints.WHATSAPP_GB_PACKAGE,getContext().getPackageManager())){
                    if (checkPermission("PATH_WHATSAPP_GB")) {
                        SharedPreferences sp = getActivity().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE);
                        String path = sp.getString("PATH_WHATSAPP_GB", "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                imagesForWhatsApp(path);
                            }
                        }).start();
                    } else {
                        permissionForWhatsApp(Constraints.WHATSAPP_GB_PATH);
                    }
                }else{
                    whatsappBtn.setText("Install GBWhatsApp");
                    whatsappBtn.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    whatsappBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gbwhatsapp.download/gbwhatsapp.html#/pc"));
                            startActivity(intent);
                        }
                    });
                }
                break;
        }


        return v;
    }

    private boolean checkPermission(String string) {
        SharedPreferences sp = getActivity().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE);
        String uriPath = sp.getString(string, "");
        if (uriPath != null) {
            if (uriPath.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void imagesForWhatsApp(String treeUri) {
        if (treeUri != null) {

            getActivity().getContentResolver().takePersistableUriPermission(Uri.parse(treeUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);

            DocumentFile fileList = DocumentFile.fromTreeUri(getContext(), Uri.parse(treeUri));

            ArrayList<imagesModals> imageList = new ArrayList<>();

            assert fileList != null;
            for (DocumentFile file : fileList.listFiles()) {
                if (!Objects.requireNonNull(file.getName()).endsWith(".nomedia")) {
                    String p = file.getUri().toString();
                    String path = UriUtils.getPathFromUri(getContext(), Uri.parse(p));
                    imagesModals item = new imagesModals(file.getName(), path);
                    if (item.getPath().contains(".mp4")){
                        imageList.add(item);
                    }
                }
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setupRecyclerView(imageList);
                }
            });

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void permissionForWhatsApp(String PATH) {
        StorageManager storageManager = (StorageManager) getActivity().getSystemService(Context.STORAGE_SERVICE);
        Intent intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/tree/");
        scheme += "%3A" + PATH;
        uri = Uri.parse(scheme);
        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent, 100);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri treeUri = data.getData();

            SharedPreferences sp = requireActivity().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE);

            if (String.valueOf(treeUri).contains(Constraints.WHATSAPP_BUSINESS_PACKAGE)) {
                sp.edit().putString("PATH_WHATSAPP_BUSINESS", String.valueOf(treeUri)).apply();
            } else if (String.valueOf(treeUri).contains(Constraints.WHATSAPP_GB_PACKAGE)) {
                sp.edit().putString("PATH_WHATSAPP_GB", String.valueOf(treeUri)).apply();
            } else if (String.valueOf(treeUri).contains(Constraints.WHATSAPP_PACKAGE)) {
                sp.edit().putString("PATH_WHATSAPP", String.valueOf(treeUri)).apply();
            } else {
                sp.edit().putString("PATH_WHATSAPP", String.valueOf(treeUri)).apply();
            }


            if (treeUri != null) {

                getActivity().getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                DocumentFile fileList = DocumentFile.fromTreeUri(getContext(), treeUri);

                ArrayList<imagesModals> imageList = new ArrayList<>();

                assert fileList != null;
                for (DocumentFile file : fileList.listFiles()) {
                    if (!Objects.requireNonNull(file.getName()).endsWith(".nomedia")) {
                        String p = file.getUri().toString();
                        String path = UriUtils.getPathFromUri(getContext(), Uri.parse(p));
                        imagesModals item = new imagesModals(file.getName(), path);
                        if (item.getPath().contains(".mp4")){
                            imageList.add(item);
                        }
                    }
                }

                setupRecyclerView(imageList);

            }

        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void setupRecyclerView(ArrayList<imagesModals> imageList) {
        recyclerAdaptor adaptor = new recyclerAdaptor(imageList, getContext(), 2);
        recyclerView.setAdapter(adaptor);
        dialog.dismiss();
    }





}