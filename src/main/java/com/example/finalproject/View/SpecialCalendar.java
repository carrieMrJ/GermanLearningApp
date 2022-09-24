package com.example.finalproject.View;

import java.util.Calendar;

/**
 *  Dedicated calendar for daily attendance
 */

public class SpecialCalendar {

    /**
     * check if it is leap year or not
     * @param year which year
     * @return
     */
    public boolean isLeapYear(int year){
        if (year % 100 == 0 && year % 400 == 0){
            return true;
        }else if (year % 100 != 0 && year % 4==0){
            return true;
        }
        return false;
    }

    /**
     * get how many days in each month
     * @param isLeapYear
     * @param month which month
     * @return number of days
     */
    public int getDaysOfMonth(boolean isLeapYear,int month){
        int days=0;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days=31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days=30;
                break;
            case 2:
                if (isLeapYear){
                    days=29;
                }else{
                    days=28;
                }
        }
        return days;
    }

    /**
     * What day is the first day of the month
     * 0-6: Sun.-Sat.
     * @param mYear which year
     * @param mMonth which month
     * @return weekday
     */
    public int getWeekdayOfMonth( int mYear, int mMonth){
        Calendar cal=Calendar.getInstance();
        cal.set(mYear,mMonth,1);
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }
}