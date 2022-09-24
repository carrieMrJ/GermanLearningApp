package com.example.finalproject.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.R;


/**
 * Timer Activity
 * @author Junqi.Sun
 */
public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * input(Minutes) max.120min
     */
    private EditText inputTime;
    /**
     * show input time
     */
    private TextView time;
    /**
     * start button
     */
    private Button startTime;
    /**
     * stop button
     */
    private Button stopTime;
    /**
     * get Time button
     */
    private Button getTime;
    /**
     * show the count down timer
     */
    private TextView countDown;
    /**
     * Timer object
     */
    private CountDownTimer countDownTimer;
    /**
     * back button
     */
    private TextView back;
    /**
     * Acquired time
     */
    private int time1;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
    }

    /**
     * initialize view
     */
    private void initView() {
        countDown = findViewById(R.id.tv_timer_countdown);
        inputTime = findViewById(R.id.input);
        startTime = findViewById(R.id.starttime);
        stopTime = findViewById(R.id.stoptime);
        getTime = findViewById(R.id.get);
        time = findViewById(R.id.time);
        back = findViewById(R.id.tv_timer_back);
        getTime.setOnClickListener(this);
        startTime.setOnClickListener(this);
        stopTime.setOnClickListener(this);
        back.setOnClickListener(this);

        startTime.setEnabled(false);
        stopTime.setEnabled(false);
        time1 = 0;

        inputTime.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });



    }

    /**
     * Formatter: convert millisecond to minutes
     * @param millisecond
     * @return
     */
    public String formatTime(long millisecond) {
        int minute;
        int second;
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        }else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    /**
     * onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:

                String str = inputTime.getText().toString().trim();
                if(str.isEmpty()){
                    new AlertDialog.Builder(TimerActivity.this).setTitle("Time Empty").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }else {

                    time.setText(inputTime.getText().toString());


                    if (Integer.parseInt(time.getText().toString()) > 120 || Integer.parseInt(time.getText().toString()) <= 0) {
                        new AlertDialog.Builder(TimerActivity.this).setTitle("Invalid Input").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                inputTime.setText("");
                                time.setText("");
                            }
                        }).create().show();
                    }else{
                        time1 = Integer.parseInt(inputTime.getText().toString());
                        startTime.setEnabled(true);
                        countDownTimer = new CountDownTimer(time1*60*1000,1000) {
                            @Override
                            public void onTick(long l) {
                                countDown.setText(formatTime(l));
                            }

                            @Override
                            public void onFinish() {
                                countDown.setText("00:00");
                                getTime.setEnabled(true);
                                startTime.setEnabled(true);
                                inputTime.setEnabled(true);
                                stopTime.setEnabled(false);
                            }
                        };
                    }


                }
                //i = Integer.parseInt(inputTime.getText().toString());
                break;
            case R.id.starttime:
                timerStart();
                break;
            case R.id.stoptime:
                timerCancel();
                break;
            case R.id.tv_timer_back:
                //timerCancel();
                finish();
            default:
                break;
        }
    }

    /**
     * cancel Timer
     */
    public void timerCancel() {
        countDownTimer.cancel();
        countDown.setText("00:00");
        time.setText("");
        inputTime.setEnabled(true);
        getTime.setEnabled(true);
        startTime.setEnabled(false);
        stopTime.setEnabled(false);
    }

    /**
     * start Timer
     */
    public void timerStart() {
        countDownTimer.start();
        inputTime.setText("");
        stopTime.setEnabled(true);
        inputTime.setEnabled(false);
        startTime.setEnabled(false);
        getTime.setEnabled(false);
    }
}
