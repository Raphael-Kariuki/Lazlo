package com.example.lazlo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public MyAdapter(@NonNull FragmentManager fragmentManager, int totalTabs){
        super(fragmentManager);
        this.totalTabs = totalTabs;
    }
    public MyAdapter(@NonNull FragmentManager fragmentManager){
        super(fragmentManager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Home home = new Home();
                return home;
            case 1:
                School school = new School();
                return school;
            case 2:
                Work work = new Work();
                return work;
            case 3:
                Bussiness bussiness = new Bussiness();
                return bussiness;
        }

        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
