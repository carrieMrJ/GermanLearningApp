package com.example.finalproject.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.Adapter.CheckInAdapter;
import com.example.finalproject.Data.User;
import com.example.finalproject.R;
import com.example.finalproject.View.SpecialCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Daily Attendance Activity
 *
 *  @author Mengru.Ji
 *
 */
public class DailyAttendanceActivity extends AppCompatActivity implements GridView.OnItemClickListener{

    /**
     * special calendar
     */
    private GridView registration_calendar_gv;
    /**
     * show today
     */
    private TextView today;
    /**
     * adapter
     */
    private CheckInAdapter adapter;
    //private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    /**
     * year
     */
    int mYear = 0;//year
    /**
     * month
     */
    int mMonth = 0;//month
    /**
     * day
     */
    int mDay = 0;//day
    /**
     * login user
     */
    private User user;
    /**
     * rule
     */
    private TextView tv_rule;
    /**
     * show check in days
     */
    private TextView tv_checkInDays;
    /**
     * check in days
     */
    private int checkInDays;
    /**
     * back button
     */
    private TextView tv_back;
    private static final int REQUEST_CHECKIN_DAYS = 303;
    /**
     * last check in date
     */
    private String lastDate;
    /**
     * show last check in date
     */
    private TextView tv_lastDate;
    /**
     *check code for continuous sign-in state
     */
    private int checkResult = 2;
    private Date lastCheckDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getData();
        initUI();


    }

    /**
     * get data from main activity
     */
    private void getData() {
        user = (User)getIntent().getSerializableExtra("user_data");
        checkInDays = user.getCheckInDays();
        lastDate = user.getLastDate();

    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        tv_rule = findViewById(R.id.tv_checkin_rule);
        tv_rule.getPaint().setFlags( Paint.UNDERLINE_TEXT_FLAG );
        tv_checkInDays = findViewById(R.id.tv_checkIn_days);
        tv_checkInDays.setText(checkInDays+" Days");
        tv_back = findViewById(R.id.tv_checkin_back);
        tv_lastDate = findViewById(R.id.tv_checkIn_Date);
        tv_lastDate.setText(lastDate);
        tv_rule = findViewById(R.id.tv_checkin_rule);
        //tv_rule.setOnClickListener();

        registration_calendar_gv = findViewById(R.id.registration_calendar_gv);
        today = findViewById(R.id.today);

        Calendar calendar=Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // get current year
        mMonth = calendar.get(Calendar.MONTH) ;// get current year（Start with 0）
        mDay = calendar.get(Calendar.DAY_OF_MONTH) ;// get current day（Start with 0）

        SpecialCalendar mCalendar = new SpecialCalendar();
        boolean isLeapYear = mCalendar.isLeapYear(mYear);
        int mDays = mCalendar.getDaysOfMonth(isLeapYear,mMonth+1);
        int week = mCalendar.getWeekdayOfMonth(mYear,mMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            lastCheckDate = sdf.parse(user.getLastDate());
            checkResult = checkAllotSigIn(lastCheckDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //update consecutive check in days
        if(checkResult == 2){
            checkInDays = 0;
        }

        //Zero at the beginning of the month
        if(mDay == 1){
            if(checkResult == 1){
                checkInDays = 1;
            }else{
                checkInDays = 0;
            }
        }
        user.setCheckInDays(checkInDays);
        Log.i("TestDate","checkInDays:"+checkInDays);

        adapter = new CheckInAdapter(this,mDays,week,mDay,user,checkResult);
        registration_calendar_gv.setAdapter(adapter);
        registration_calendar_gv.setOnItemClickListener(this);
        today.setText(mDay+"."+(mMonth+1)+"."+mYear);

        tv_checkInDays.setText(checkInDays+" Days");


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setCheckInDays(checkInDays);
                Intent intent = new Intent(DailyAttendanceActivity.this,MainActivity.class);
                intent.putExtra("checkin",user);
                setResult(2,intent);
                Log.i("Test Result","successful back");
                finish();
                //onBackPressed();
            }
        });

        tv_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DailyAttendanceActivity.this).setTitle("Rule of Daily Attendance")
                        .setMessage("1.Only mark Continuous Sign-in Date\n2.Continuous Sign-in Date will be cleared on the First Day of each Month\n")
                        .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    /**
     *WHen click on Item
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String today = mYear + "-" + mMonth + "-" + l;

        if (l!=0){
            if (l == mDay){
                Log.i("TestDate","checkResult"+checkResult);
                if(checkResult != 1) {
                    checkInDays++;
                    user.setCheckInDays(checkInDays);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String time = sdf.format(date);
                    user.setLastDate(time);
                    try {
                        checkResult = checkAllotSigIn(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TextView today_tv = view.findViewById(R.id.day);
                    today_tv.setBackgroundResource(R.drawable.icon_ok);
                    today_tv.setTextColor(Color.BLACK);
                    today_tv.setText(l + "");

                    tv_checkInDays.setText(checkInDays + " Days");
                    tv_lastDate.setText(time);
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                    Toast.makeText(view.getContext(), "Successful Sign in", Toast.LENGTH_SHORT).show();
                    //checkResult = 1;
                }else{
                    Toast.makeText(view.getContext(),"Already signed-in today",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(view.getContext(),"You choose ："+today,Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * Check if user already signed in today
     * 1: already signed in，
     * 0: have not signed in yet, but in continuous signed in state
     * 2: not signed in yet, and continuous signed in state is interrupted
     *
     * @param date last signed in date
     * @return int
     */
    public int checkAllotSigIn(Date date) throws Exception {

        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String time = sdf.format(date);
        Log.i("Test Date","time in Format:" + time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar local = Calendar.getInstance();
        Log.i("Test Date","current localTime:" + local);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),23,59,59);

        Log.i("Test Date","startTime:" + calendar1);
        Log.i("Test Date","endTime:" + calendar2);

        //before today 00:00:00
        if (calendar.before(calendar1)) {

            /*Determine whether it is before yesterday,
             * less than yesterday proves that the check-in is not continuous,
             * and the number of continuous check-in records is set to 0*/
            Date newTime = new Date();
            //yyyy-MM-dd 00：00：00
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = format.format(newTime);
            Date today = format.parse(todayStr);
            //one day = 86400000=24*60*60*1000  so it's the day before yesterday
            if ((today.getTime() - date.getTime()) > 86400000) {
                result = 2;
                Log.i("Test Date","before today's start time, at least the day before yesterday, continuous check-in stop");
            } else {
                result = 0;
                Log.i("Test Date","before today's start time, last sign in time is yesterday, continuous check in");
            }
        }
        //before 23:59:59 today after 00:00:00
        if (calendar.after(calendar1) && local.before(calendar2)) {
            Log.i("Test Date","before 23:59 today after 00:00");
            result = 1;
        }
        //after 23:59:59 today
        if (calendar.after(calendar2)) {
            Log.i("Test Date","after today 23:59");
            result = 1;
        }

        return result;
    }
}