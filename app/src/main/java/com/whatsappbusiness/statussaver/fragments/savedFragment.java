package com.whatsappbusiness.statussaver.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whatsappbusiness.statussaver.Constraints;
import com.whatsappbusiness.statussaver.Modals.imagesModals;
import com.whatsappbusiness.statussaver.R;
import com.whatsappbusiness.statussaver.adaptors.recyclerAdaptor;
import com.whatsappbusiness.statussaver.databinding.ActivitySavedScreenBinding;
import com.whatsappbusiness.statussaver.databinding.FragmentImagesBinding;
import com.whatsappbusiness.statussaver.databinding.FragmentSavedBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class savedFragment extends Fragment {


    FragmentSavedBinding binding;
    RecyclerView recyclerView;
    public static ArrayList<imagesModals> savedList = new ArrayList<>();
    public static recyclerAdaptor mAdaptor;

    public savedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View v = binding.getRoot();


        recyclerView = binding.recyclerView;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mAdaptor = new recyclerAdaptor(savedList, getContext(), 3);
        recyclerView.setAdapter(mAdaptor);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        setup(getContext());

    }

    public static void setup(Context context) {
        File directory = null;
        if (Constraints.SELECTED == 1) {
            directory = context.getDir("StatusSaverWhatsApp", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 2) {
            directory = context.getDir("StatusSaverBusiness", Context.MODE_PRIVATE);
        } else if (Constraints.SELECTED == 3) {
            directory = context.getDir("StatusSaverGB", Context.MODE_PRIVATE);
        }
        if (directory.exists()) {
            savedList.clear();
            if (Objects.requireNonNull(directory.listFiles()).length > 0) {
                for (int i = 0; i < directory.listFiles().length; i++) {
                    File file = directory.listFiles()[i];
                    String fileName = file.getName();
                    String filePath = file.getPath();
                    imagesModals item = new imagesModals(fileName, filePath, i);
                    savedList.add(item);
                    mAdaptor.notifyDataSetChanged();
                }
            }

        }

    }
}
