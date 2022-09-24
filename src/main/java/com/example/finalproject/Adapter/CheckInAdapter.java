package com.example.finalproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.finalproject.Data.User;
import com.example.finalproject.R;


/**
 * Adapter for Daily Attendance
 * Draw Calendar
 * @author Mengru.Ji
 */
public class CheckInAdapter extends BaseAdapter {
    /**
     * Context
     */
    private Context context;
    /**
     * number of Day in this Month
     */
    private final int days;
    /**
     * Weekday of this day
     */
    private final int week;
    /**
     * ID of Day
     */
    private int[] dayNumber;
    /**
     * day of Month
     */
    private final int day;
    /**
     * User
     */
    private User user;
    /**
     * View Holder
     */
    private ViewHolder viewHolder;
    /**
     * Result Code for current Check-in State
     */
    private int checkResult;

    /**
     * Constructor
     * @param context
     * @param days
     * @param week
     * @param day
     * @param user
     * @param checkResult
     */
    public CheckInAdapter(Context context, int days, int week, int day, User user, int checkResult) {

        this.context = context;
        this.days = days;
        this.week = week;
        this.day = day;
        this.user = user;
        this.checkResult=checkResult;
        getEveryDay();
    }


    /**
     * Get count of square
     * @return
     */
    @Override
    public int getCount() {
        return 42;
    }

    /**
     * Get Item
     * @param i
     * @return
     */
    @Override
    public String getItem(int i) {

        return null;
    }

    /**
     * Get Item of Id
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return dayNumber[i];
    }


    /**
     * Get View
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null==view){

            view= LayoutInflater.from(context).inflate(R.layout.item_checkinadapter,null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);

        }else{

            viewHolder= (ViewHolder) view.getTag();
        }

        viewHolder.day.setText(dayNumber[i]==0? "" : dayNumber[i]+"");

        int checkInDays = user.getCheckInDays();

        //
        if(dayNumber[i] == day) {
            //not consecutive and not sign in today yet
            if (checkResult == 2) {

                viewHolder.day.setText("T");
                view.setBackgroundResource(R.color.colorLightPurpleBlue);
                viewHolder.day.setTextColor(Color.parseColor("#ffffff"));

            }

            //already sign-in today
            else if (checkResult == 1) {
                viewHolder.day.setBackgroundResource(R.drawable.icon_ok);
            }
            //consecutive but not sign in today
            else if (checkResult == 0) {

                viewHolder.day.setText("T");
                //viewHolder.day.setTextSize(10);
                view.setBackgroundResource(R.color.colorLightPurpleBlue);
                viewHolder.day.setTextColor(Color.parseColor("#ffffff"));
            }
        }else {
            if(checkResult == 1){
                if(dayNumber[i] > day - checkInDays && dayNumber[i] < day){
                    viewHolder.day.setBackgroundResource(R.drawable.icon_ok);
                }
            }else if(checkResult == 0){
                if(dayNumber[i] >= day - checkInDays && dayNumber[i] < day){
                    viewHolder.day.setBackgroundResource(R.drawable.icon_ok);
                }

            }
        }

        return view;
    }

    /**
     * View Holder for Adapter
     */
    private class ViewHolder{

        /**
         * Text View: Day of Month
         */
        private TextView day;

        /**
         * Constructor
         * @param view
         */
        public ViewHolder(View view) {
            this.day = view.findViewById(R.id.day);
        }
    }

    /**
     * Get 42 Grids and the Text of each Grids
     */
    private void getEveryDay(){
        dayNumber=new int[42];
        //text of Grid: before 1. and last day is 0
        for (int i=0;i<42;i++){
            if (i < days+week && i >= week){
                dayNumber[i]=i-week+1;
            }else{
                dayNumber[i]=0;
            }
        }
    }

}