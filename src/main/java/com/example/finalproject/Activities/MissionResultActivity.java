package com.example.finalproject.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.Data.User;
import com.example.finalproject.Data.UsersDao;
import com.example.finalproject.Database.DatabaseCreator;
import com.example.finalproject.R;
import com.example.finalproject.View.MyProgressBar;
import java.io.Serializable;
import java.util.List;

/**
 * Activity to show the result
 * @author Mengru.Ji
 */
public class MissionResultActivity extends Activity implements View.OnClickListener {

    //LocalBroadcastManager localBroadcastManager;
    int count = MissionActivity.randomList.size();
    int[] mIds = new int[count];
    /**
     * text view: show your submission time
     */
    private TextView tv_submitTime;
    /**
     * text view: show the number of all questions
     */
    private TextView tv_total_que;
    /**
     * text view: jump to check all wrong questions
     */
    private TextView tv_wrongQue_analysis;
    /**
     * relative layout; panel to show the result
     */
    private RelativeLayout result_panel;
    /**
     * text view: back button
     */
    private TextView tv_back;
    /**
     * text view: jump to check all questions
     */
    private TextView tv_allQue_analysis;
    /**
     * text view: to show the number of all correct questions
     */
    private TextView tv_correct;
    /**
     * number of correct questions
     */
    private int correct;
    /**
     * number of all questions
     */
    private int total;
    /**
     * wrong questions list
     */
    private List<Questions> wrongList;
    /**
     * current time point(submission time)
     */
    private String curTime;
    /**
     * duration of test
     */
    private String duration;
    /**
     * original questions list
     */
    private List<Questions> originalList;
    /**
     * text view: show your test duration
     */
    private TextView tv_duration;
    /**
     * true,then check wrong questions; false,then check all questions
     */
    private boolean wrongMode;
    /**
     * text view: share button
     */
    private TextView tv_share;
    /**
     * progressbar: show points with animation
     */
    private MyProgressBar myProgressBar;
    /**
     * points(correct*300/total)
     */
    private float correctRate;
    /**
     * moment logged User
     */
    private User loginUser;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_mission_result);

        getData();
        //initResultData();
        initUI();



    }

    /**
     * get useful data from intent
     */
    private void getData() {

        Intent intent = getIntent();
        correct = intent.getIntExtra("correct",0);
        total = intent.getIntExtra("total",5);
        curTime = intent.getStringExtra("curTime");
        duration = intent.getStringExtra("duration");
        wrongList = (List<Questions>) intent.getSerializableExtra("wrongList");
        originalList = (List<Questions>)intent.getSerializableExtra("originalList");

        for(int i = 0; i < originalList.size(); i++){
            Log.i("RRR","original"+i+": "+originalList.get(i).toString());
        }

        loginUser = (User) intent.getSerializableExtra("user_data");


    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        //GridView gridView = findViewById(R.id.result_gv);

        tv_total_que = findViewById(R.id.tv_report_total_question);
        tv_wrongQue_analysis = findViewById(R.id.tv_wrong_analysis);
        result_panel = findViewById(R.id.rl_result_panel);
        tv_back = findViewById(R.id.tv_result_back);
        tv_allQue_analysis = findViewById(R.id.tv_all_analysis);
        tv_correct = findViewById(R.id.tv_result_correct);
        tv_submitTime = findViewById(R.id.tv_submit_curTime);
        tv_duration = findViewById(R.id.tv_result_duration);
        tv_share = findViewById(R.id.tv_result_share);

        //calculate points
        correctRate = (correct * 300)/ total;
        loginUser.setPoints(loginUser.getPoints()+(int) (correctRate));

        //update current user in database
        DatabaseCreator databaseCreator = DatabaseCreator.getInstance(getApplicationContext());
        UsersDao usersDao = databaseCreator.usersDao();
        usersDao.updateUser(loginUser);

        //customized progressbar
        myProgressBar = findViewById(R.id.result_progress_view);
        myProgressBar.setProgressWithAnimation((int) (correctRate)).setProgressListener(new MyProgressBar.ProgressListener() {
            @Override
            public void currentProgressListener(float currentProgress) {

            }
        });
        myProgressBar.startProgressAnimation();

        tv_back.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_wrongQue_analysis.setOnClickListener(this);
        tv_allQue_analysis.setOnClickListener(this);

        result_panel.setFocusable(true);
        result_panel.setFocusableInTouchMode(true);
        result_panel.requestFocus();
        result_panel.setFocusable(true);

        tv_total_que.setText("/"+total);
        tv_correct.setText(correct+"");
        tv_submitTime.setText(curTime);
        tv_duration.setText(duration);

    }

    /**
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_result_back:

                Intent intent3 = new Intent(MissionResultActivity.this,MainActivity.class);
                intent3.putExtra("user_data",loginUser);
                startActivity(intent3);

                finish();

                break;

            case R.id.tv_all_analysis:

                wrongMode = false;
                Intent intent2 = new Intent(MissionResultActivity.this, WrongMode.class);
                intent2.putExtra("originalList",(Serializable)originalList);
                intent2.putExtra("user_data",loginUser);
                intent2.putExtra("wrongMode",wrongMode);
                startActivity(intent2);
                break;

            case R.id.tv_wrong_analysis:

                if(wrongList != null){
                    wrongMode = true;
                    Intent intent = new Intent(MissionResultActivity.this,WrongMode.class);
                    intent.putExtra("wrongList",(Serializable)wrongList);
                    intent.putExtra("user_data",loginUser);
                    intent.putExtra("wrongMode", wrongMode);
                    startActivity(intent);

                }else{
                    new AlertDialog.Builder(MissionResultActivity.this).setTitle("Congratulations").setMessage("All your Answers are correct!").setPositiveButton("Yeah!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }

                break;

            case R.id.tv_result_share:
                shareResult();
                break;
            default:
                break;

        }

    }

    /**
     * share result
     */
    private void shareResult() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I got "+correctRate+" points in today's test!");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    /**
     * onBackPressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent3 = new Intent(MissionResultActivity.this,MainActivity.class);
        intent3.putExtra("user_data",loginUser);
        startActivity(intent3);

        finish();
    }

}
