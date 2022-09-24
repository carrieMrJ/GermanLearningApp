package com.example.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.Fragments.PagerFragment;
import com.example.finalproject.R;
import java.util.List;

/**
 * Adapter for Tab Pager in Question Book Activity
 * @author Mengru.Ji
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    /**
     * Number of Page
     */
    final int PAGE_COUNT=2;
    //private String tabTitles[]=new String[]{"Wrong Questions","Favorites"};
    /**
     * Context
     */
    private Context context;
    /**
     * Database
     */
    private MySQLite mySQLite;

    /**
     * Constructor
     * @param fm
     * @param context
     * @param mySQLite
     */
    public TabPagerAdapter(FragmentManager fm, Context context, MySQLite mySQLite) {
        super(fm);
        this.context=context;
        this.mySQLite = mySQLite;
    }

    /**
     * Get Item
     * @param position Which Page
     * @return Instance of Fragment
     */
    @Override
    public Fragment getItem(int position) {
        List<Questions> wrongList = mySQLite.getWrongQuestions();
        List<Questions> collectList = mySQLite.getCollectQuestions();
        return PagerFragment.newInstance(position+1,wrongList,collectList);
    }

    /**
     * Get Number of Page
     * @return
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /*@Override
    public  CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }*/

    /**
     * Get Custom View
     * @param position Which Page
     * @return View of This Page
     */
    //customized tab_item
    public View getCustomView(int position){

        View view = LayoutInflater.from(context).inflate(R.layout.item_tablayout,null);
        ImageView iv = view.findViewById(R.id.tab_iv);
        TextView tv = view.findViewById(R.id.tab_tv);

        switch (position){
            case 0:

                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_wrongbook));
                tv.setText("Wrong Questions");
                break;
            case 1:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_collectbook));
                tv.setText("Favorites");
                break;
        }
        return view;
    }


}

