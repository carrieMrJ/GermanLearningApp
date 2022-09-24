package com.example.finalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.Data.User;
import com.example.finalproject.R;

/**
 * Achievement Activity
 * @author Junqi.Sun
 */
public class AchievementsActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * current Login User
     */
    private User user;
    /**
     * points of user
     */
    private int points;
    /**
     * image view: achievement level 1
     */
    private ImageView iv_achi_a;
    /**
     * image view: achievement level 2
     */
    private ImageView iv_achi_b;
    /**
     * image view: achievement level 3
     */
    private ImageView iv_achi_c;
    /**
     * image view: achievement 3 days in a row
     */
    private ImageView iv_achi_d;
    /**
     * image view: achievement 7 days in a row
     */
    private ImageView iv_achi_e;
    /**
     * text view: first current points
     */
    private TextView tv_curP_a;
    /**
     * text view: second current points
     */
    private TextView tv_curP_b;
    /**
     * text view: third current points
     */
    private TextView tv_curP_c;
    /**
     * text view: first check in days
     */
    private TextView tv_curCheckIn_d;
    /**
     * text view: second check in days
     */
    private TextView tv_curCheckIn_e;
    /**
     * text view: back button
     */
    private TextView tv_back;
    /**
     * consecutive check in days of user
     */
    private int checkIn;
    /**
     * image view: 30 days in a row
     */
    private ImageView iv_achi_f;
    /**
     * text view: third check in days
     */
    private TextView tv_curCheckIn_f;
    /**
     * text view: first share button
     */
    private TextView tv_share_a;
    /**
     * text view: second share button
     */
    private TextView tv_share_b;
    /**
     * text view: third share button
     */
    private TextView tv_share_c;
    /**
     * text view: fourth share button
     */
    private TextView tv_share_d;
    /**
     * text view: fifth share button
     */
    private TextView tv_share_e;
    /**
     * text view: sixth share button
     */
    private TextView tv_share_f;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_achievements);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        getData();

        initUI();
    }

    /**
     * get data from intent
     */
    private void getData() {

        user = (User)getIntent().getSerializableExtra("user_data");
        points = user.getPoints();
        checkIn = user.getCheckInDays();
    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        tv_back = findViewById(R.id.tv_achievement_back);

        iv_achi_a = findViewById(R.id.iv_achievement_a);
        iv_achi_b = findViewById(R.id.iv_achievement_b);
        iv_achi_c = findViewById(R.id.iv_achievement_c);
        iv_achi_d = findViewById(R.id.iv_achievement_d);
        iv_achi_e = findViewById(R.id.iv_achievement_e);
        iv_achi_f = findViewById(R.id.iv_achievement_f);

        tv_curP_a = findViewById(R.id.achievement_curPoints_a);
        tv_curP_b = findViewById(R.id.achievement_curPoints_b);
        tv_curP_c = findViewById(R.id.achievement_curPoints_c);
        tv_curCheckIn_d = findViewById(R.id.achievement_curCheckIn_d);
        tv_curCheckIn_e = findViewById(R.id.achievement_curCheckIn_e);
        tv_curCheckIn_f = findViewById(R.id.achievement_curCheckIn_f);

        tv_share_a = findViewById(R.id.tv_achievement_share_a);
        tv_share_b = findViewById(R.id.tv_achievement_share_b);
        tv_share_c = findViewById(R.id.tv_achievement_share_c);
        tv_share_d = findViewById(R.id.tv_achievement_share_d);
        tv_share_e = findViewById(R.id.tv_achievement_share_e);
        tv_share_f = findViewById(R.id.tv_achievement_share_f);

        tv_curP_a.setText(points+"");
        tv_curP_b.setText(points+"");
        tv_curP_c.setText(points+"");
        tv_curCheckIn_d.setText(checkIn+"");
        tv_curCheckIn_e.setText(checkIn+"");
        tv_curCheckIn_f.setText(checkIn+"");

        tv_share_a.setVisibility(View.INVISIBLE);
        tv_share_b.setVisibility(View.INVISIBLE);
        tv_share_c.setVisibility(View.INVISIBLE);
        tv_share_d.setVisibility(View.INVISIBLE);
        tv_share_e.setVisibility(View.INVISIBLE);
        tv_share_f.setVisibility(View.INVISIBLE);

        tv_share_a.setOnClickListener(this);
        tv_share_b.setOnClickListener(this);
        tv_share_c.setOnClickListener(this);
        tv_share_d.setOnClickListener(this);
        tv_share_e.setOnClickListener(this);
        tv_share_f.setOnClickListener(this);
        



        tv_back.setOnClickListener(this);


        if(points >= 1000){
            iv_achi_a.setImageResource(R.drawable.icon_goal);
            tv_share_a.setVisibility(View.VISIBLE);
        }
        if(points >= 5000){
            iv_achi_b.setImageResource(R.drawable.icon_goal);
            tv_share_b.setVisibility(View.VISIBLE);
        }
        if(points >= 10000){
            iv_achi_c.setImageResource(R.drawable.icon_goal);
            tv_share_c.setVisibility(View.VISIBLE);
        }
        if(checkIn >= 3){
            iv_achi_d.setImageResource(R.drawable.icon_goal);
            tv_share_d.setVisibility(View.VISIBLE);
        }
        if(checkIn >= 7){
            iv_achi_e.setImageResource(R.drawable.icon_goal);
            tv_share_e.setVisibility(View.VISIBLE);
        }
        if(checkIn >= 30){
            iv_achi_f.setImageResource(R.drawable.icon_goal);
            tv_share_f.setVisibility(View.VISIBLE);
        }

    }

    /**
     * onClick
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_achievement_back:
                finish();
                break;
            case R.id.tv_achievement_share_a:
                share("I have already got 1000 points!");
                break;
            case R.id.tv_achievement_share_b:
                share("I have already got 5000 points!");
                break;
            case R.id.tv_achievement_share_c:
                share("I have already got 10000 points!");
                break;
            case R.id.tv_achievement_share_d:
                share("Sign-in 3 days!");
                break;
            case R.id.tv_achievement_share_e:
                share("Sign-in 7 days!");
                break;
            case R.id.tv_achievement_share_f:
                share("Sign-in 30 days!");
                break;
        }
    }

    /**
     * share achievements
     * @param string shared content
     */
    public void share(String string){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, string);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
