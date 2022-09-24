package com.example.finalproject.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.finalproject.Adapter.TabPagerAdapter;
import com.example.finalproject.Data.User;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * Question Book Activity
 * @author Mengru.Ji
 */
public class QuestionBookActivity extends AppCompatActivity {

    /**
     * Tablayout for Tabitem
     */
    private TabLayout tabLayout;
    /**
     * View Pager for Tablayout
     */
    private ViewPager viewPager;
    /**
     * Adapter for ViewPager
     */
    private TabPagerAdapter tabAdapter;
    private TabItem tab_wrong;
    private TabItem tab_collect;
    /**
     * User
     */
    private User user;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_statistics);

        user = (User)getIntent().getSerializableExtra("user_data");
        initView();
    }

    /**
     * initialize View
     */
    private void initView() {

        MySQLite mySQLite = new MySQLite(QuestionBookActivity.this,user.getUserName());
        tabLayout = findViewById(R.id.statistics_tabLayout);
        viewPager = findViewById(R.id.statistics_view_pager);
        tabAdapter = new TabPagerAdapter(getSupportFragmentManager(), this,mySQLite);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        TextView tv_back = findViewById(R.id.tv_statistics_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for (int i = 0; i < 2; i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //Customized View
            tab.setCustomView(tabAdapter.getCustomView(i));

            //
            if (i == 0) {
                (tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                (tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                (tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                (tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                (tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(false);
                (tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(false);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

