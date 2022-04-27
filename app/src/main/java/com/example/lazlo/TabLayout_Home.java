package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class TabLayout_Home extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_home);

        //obtain the tab layout and viewpager and toolbar
        tabLayout = (TabLayout) findViewById(R.id._tabLayout);
        viewPager = (ViewPager) findViewById(R.id._viewPager);
        toolbar = (Toolbar) findViewById(R.id.tasksToolbar);

        //add tabs

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Work"));
        tabLayout.addTab(tabLayout.newTab().setText("School"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //set toolbar
        toolbar.setTitle("All Tasks");
        setSupportActionBar(toolbar);

        final MyAdapter adapter = new MyAdapter(this.getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
}