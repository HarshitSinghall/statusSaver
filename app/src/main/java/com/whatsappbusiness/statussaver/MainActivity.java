package com.whatsappbusiness.statussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.whatsappbusiness.statussaver.adaptors.myPagerAdaptor;
import com.whatsappbusiness.statussaver.Constraints.*;
import com.whatsappbusiness.statussaver.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActivityMainBinding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    String[] list = {"Images", "Videos", "Saved"};
    String appLink;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolBar;
        drawer = binding.drawer;
        navigationView = binding.navigationView;
        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();

        toolbar.setTitle("WhatsApp");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        appLink = "https://play.google.com/store/apps/details?id="+getPackageName();

        SetUp();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.drawer_whatsapp:
                        Constraints.SELECTED = 1;
                        toolbar.setTitle("WhatsApp");
                        break;
                    case R.id.drawer_whatsapp_business:
                        Constraints.SELECTED = 2;
                        toolbar.setTitle("WhatsApp Business");
                        break;
                    case R.id.drawer_whatsapp_gb:
                        Constraints.SELECTED = 3;
                        toolbar.setTitle("GBWhatsApp");

                        break;
                    case R.id.drawer_rate:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(appLink));
                        startActivity(intent);
                        navigationView.setCheckedItem(R.id.drawer_whatsapp);
                        break;
                    case R.id.drawer_share:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/*");
                        i.putExtra(Intent.EXTRA_TEXT,"Download One of the Best StatusSaver App with Support of WhatsApp, Business WhatsApp, GB WhatsApp in one app:\n"+appLink);
                        startActivity(Intent.createChooser(i,"Share Via."));
                        navigationView.setCheckedItem(R.id.drawer_whatsapp);
                        break;


                }
                drawer.close();
                SetUp();
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SetUp(){
        myPagerAdaptor pagerAdaptor = new myPagerAdaptor(this);
        viewPager.setAdapter(pagerAdaptor);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(list[position])).attach();
    }

}