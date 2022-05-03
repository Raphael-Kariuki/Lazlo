package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class sortedTasks extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_tasks);


        viewPager2 = findViewById(R.id.tasks_viewPager2);
        tabLayout = findViewById(R.id.tasks_tabLayout);

        viewPager2.setAdapter(new FragmentAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position){
                        case 0:{
                            tab.setText("School");
                            tab.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_school_24 ,null));
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(getResources().getColor(R.color.black,null));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(2);
                            badgeDrawable.setMaxCharacterCount(2);
                            break;

                        }
                        case 1:{
                            tab.setText("Business");
                            tab.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_business_24 ,null));
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(getResources().getColor(R.color.black,null));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(2);
                            badgeDrawable.setMaxCharacterCount(2);
                            break;

                        }
                        case 2:{
                            tab.setText("Work");
                            tab.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_work_24 ,null));
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(getResources().getColor(R.color.black,null));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(2);
                            badgeDrawable.setMaxCharacterCount(2);
                            break;

                        }
                        default:{
                            tab.setText("Home");
                            tab.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_home_24 ,null));
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(getResources().getColor(R.color.black,null));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(2);
                            badgeDrawable.setMaxCharacterCount(2);
                            break;

                        }
                    }
                });
        tabLayoutMediator.attach();
    }
}