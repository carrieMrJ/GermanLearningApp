package com.example.finalproject.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalproject.Data.User;
import com.example.finalproject.Data.UsersDao;
import com.example.finalproject.Database.DatabaseCreator;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.R;
import com.example.finalproject.Receivers.AlarmReceiver;
import java.util.Calendar;


/**
 * Main Menu Activity
 * @author Junqi.Sun
 */
public class MainActivity extends AppCompatActivity {

    /**
     * request code for check in activity
     */
    private static final int REQUEST_CHECKIN_DAYS = 303;
    /**
     * Timer Activity Button
     */
    private Button timerBT;
    /**
     * Mission Activity Button
     */
    private Button dailyMissionBT;
    /**
     * Competition Activity Button
     */
    private Button competitionBT;
    /**
     * show username
     */
    private TextView tv_username;
    /**
     * show user's points
     */
    private TextView tv_points;
    /**
     * moment login user
     */
    private User loginUser;
    /**
     * Statistics Activity Button
     */
    private Button statisticsBT;
    /**
     * Achievement Activity Button
     */
    private Button achievementBT;
    /**
     * Check In Activity Button
     */
    private Button checkInBT;
    /**
     * consecutive check in days
     */
    private int days = -1;
    /**
     * alarm button
     */
    private TextView ring;
    /**
     * check if alarm is set
     */
    private boolean isRing;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();
        loadQuestionsForUsers();

    }

    /**
     * load questions
     */
    private void loadQuestionsForUsers() {
        MySQLite mySQLite = new MySQLite(MainActivity.this, loginUser.getUserName());
        mySQLite.insertQueListFromFileToDB(MainActivity.this, "questionList");
    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        //curDateTV = findViewById(R.id.current_date);
        timerBT = findViewById(R.id.timer);
        dailyMissionBT = findViewById(R.id.daily_mission);
        competitionBT = findViewById(R.id.competition);
        statisticsBT = findViewById(R.id.statistics);
        tv_username = findViewById(R.id.tv_user_name);
        tv_points = findViewById(R.id.tv_user_points);
        achievementBT = findViewById(R.id.achievement);
        checkInBT = findViewById(R.id.btn_checkin);
        ring = findViewById(R.id.ring);


        //setDate();
        setTimerBT();
        setDailyMissionBT();
        setCompetitionBT();
        setStatisticsBT();
        setAchievementBT();
        setCheckInBT();
        setUser();
        setAlarm();
        setRing();
        isRing = true;
    }

    /**
     * set Alarm button listener
     */
    private void setRing() {

        setAlarm();

        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRing){
                    cancelAlarm();
                    isRing = false;
                }else{
                    setAlarm();
                    isRing = true;
                }
            }
        });

    }

    /**
     * cancel alarm
     */
    private void cancelAlarm() {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        ring.setBackgroundResource(R.drawable.icon_notify_white);
        Toast.makeText(MainActivity.this,"Alarm is canceled",Toast.LENGTH_SHORT).show();
    }


    /**
     * set Check in Button Listener
     */
    private void setCheckInBT() {
        checkInBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkInActivity = new Intent(MainActivity.this, DailyAttendanceActivity.class);
                checkInActivity.putExtra("user_data", loginUser);
                startActivityForResult(checkInActivity, REQUEST_CHECKIN_DAYS);

            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Test Result", "result code" + resultCode + "");
        if (resultCode == 2) {
            if (requestCode == REQUEST_CHECKIN_DAYS) {
                loginUser = (User) data.getSerializableExtra("checkin");
                days = loginUser.getCheckInDays();
                //days = data.getIntExtra("checkin",loginUser.getCheckInDays());
                //Log.i("Test Result", days + "");
                //Log.i("TestDate", loginUser.getLastDate());
                //loginUser.setCheckInDays(days);
                DatabaseCreator databaseCreator = DatabaseCreator.getInstance(getApplicationContext());
                UsersDao usersDao = databaseCreator.usersDao();
                usersDao.updateUser(loginUser);
            }
        } else {
            Log.i("Test Result", "data null!");
        }

    }

    /**
     * set Achievement Button Listener
     */
    private void setAchievementBT() {
        achievementBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent achievementActivity = new Intent(MainActivity.this, AchievementsActivity.class);
                achievementActivity.putExtra("user_data", loginUser);
                startActivity(achievementActivity);
            }
        });
    }

    /**
     * set alarm
     */
    private void setAlarm() {

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",loginUser.getUserName());
        bundle.putString("psw",loginUser.getUserPw());
        bundle.putString("date",loginUser.getLastDate());
        bundle.putInt("checkin",loginUser.getCheckInDays());
        bundle.putInt("points",loginUser.getPoints());
        intent.putExtras(bundle);

        intent.setAction("NotificationAlarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        //alarm.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+2*1000,pendingIntent);

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) > 20){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }else if(calendar.get(Calendar.HOUR_OF_DAY) == 20 && calendar.get(Calendar.MINUTE) > 0){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),24*60*60*1000,pendingIntent);
        ring.setBackgroundResource(R.drawable.icon_notify_black);
        Toast.makeText(this,"Alarm is set",Toast.LENGTH_SHORT).show();


    }

    /**
     * set statistics button listener
     */
    private void setStatisticsBT() {
        statisticsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statisticsActivity = new Intent(MainActivity.this, QuestionBookActivity.class);
                statisticsActivity.putExtra("user_data", loginUser);
                startActivity(statisticsActivity);

            }
        });
    }

    /**
     * set competition button listener
     */
    private void setCompetitionBT() {
        competitionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent competitionStartActivity = new Intent(MainActivity.this, CompetitionStart.class);
                competitionStartActivity.putExtra("user_data", loginUser);
                startActivity(competitionStartActivity);
                //finish();

                overridePendingTransition(android.R.anim.linear_interpolator, android.R.anim.bounce_interpolator);
            }
        });
    }

    /**
     * set current user
     */
    private void setUser() {

        String username = "";
        int points = -1;
        boolean receiverMode = getIntent().getBooleanExtra("receiverMode", false);

        if (receiverMode) {
            String name = getIntent().getStringExtra("username");
            String psw = getIntent().getStringExtra("psw");
            int p = getIntent().getIntExtra("points", 0);
            int checkin = getIntent().getIntExtra("checkin", 0);
            String date = getIntent().getStringExtra("date");

            loginUser = new User(name, psw);
            loginUser.setPoints(p);
            loginUser.setLastDate(date);
            loginUser.setCheckInDays(checkin);

        } else {

            loginUser = (User) getIntent().getSerializableExtra("user_data");
        }

        username = loginUser.getUserName();
        points = loginUser.getPoints();


        tv_username.setText("Hello " + username + " ! ");
        tv_points.setText("Your points : " + points);

    }

    /**
     * set mission button listener
     */
    private void setDailyMissionBT() {
        dailyMissionBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyMissionActivity = new Intent(MainActivity.this, MissionActivity.class);
                dailyMissionActivity.putExtra("user_data", loginUser);
                startActivity(dailyMissionActivity);
                finish();
            }
        });
    }

    /**
     * set timer button listener
     */
    private void setTimerBT() {

        timerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeActivityIntent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(timeActivityIntent);
                //finish();
            }
        });

    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_signout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);


    }


    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();

    }
}
