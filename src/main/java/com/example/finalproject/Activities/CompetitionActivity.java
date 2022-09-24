package com.example.finalproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.Data.User;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Competition Activity
 * @author Junqi.Sun
 */
public class CompetitionActivity extends Activity implements View.OnClickListener {

    /**
     * Red Button A
     */
    private Button btn_red_a;
    /**
     * Red Button B
     */
    private Button btn_red_b;
    /**
     * Red Button C
     */
    private Button btn_red_c;
    /**
     * Red Button D
     */
    private Button btn_red_d;
    /**
     * Blue Button A
     */
    private Button btn_blue_a;
    /**
     * Blue Button B
     */
    private Button btn_blue_b;
    /**
     * Blue Button C
     */
    private Button btn_blue_c;
    /**
     * Blue Button D
     */
    private Button btn_blue_d;
    /**
     * Round Red
     */
    private final static int ROUND_RED = 101;
    /**
     * Round Blue
     */
    private final static int ROUND_BLUE = 202;
    /**
     * Round
     */
    private int round;
    /**
     * Text view: shutdown button
     */
    private TextView tv_shutdown;
    /**
     * Text view: points of Red
     */
    private TextView tv_redP;
    /**
     * Text view: points of Blue
     */
    private TextView tv_blueP;
    /**
     * Text view: current Position of Question List
     */
    private TextView tv_curIndex;
    /**
     * Text View: Title of Current Question
     */
    private TextView tv_queTitle;
    /**
     * Text View: Opotion A
     */
    private TextView tv_a;
    /**
     * Text View: Opotion B
     */
    private TextView tv_b;
    /**
     * Text View: Opotion C
     */
    private TextView tv_c;
    /**
     * Text View: Opotion D
     */
    private TextView tv_d;
    /**
     * Question List
     */
    private List<Questions> randomList;
    /**
     * current position
     */
    private int curIndex;
    /**
     * size of list
     */
    private int count;
    /**
     * countdown
     */
    private CountDownTimer countDownTimer;
    /**
     * Text View: Countdown
     */
    private TextView tv_countDown;
    /**
     * correct?
     */
    private boolean curCorrect;
    /**
     * points od red
     */
    private int redP;
    /**
     * points of Blue
     */
    private int blueP;
    /**
     * User
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

        setContentView(R.layout.activity_competition);

        loadData();
        initUI();


    }

    /**
     * load Question from Database and get User form Intent
     */
    private void loadData() {

        loginUser = (User)getIntent().getSerializableExtra("user_data");
        MySQLite mySQLite = new MySQLite(CompetitionActivity.this, loginUser.getUserName());
        List<Questions> list = mySQLite.getQuestion();
        int[] randomIndex = randomSet(6, list.size());
        randomList = new ArrayList<>();

        for(int i = 0; i < randomIndex.length; i++){
            randomList.add(list.get(randomIndex[i]));
        }

    }


    /**
     * initialize all UI components
     */
    private void initUI() {

        curIndex = 0;
        count = randomList.size();
        redP = 0;
        blueP = 0;

        tv_countDown = findViewById(R.id.tv_competition_countDown);

        countDownTimer = new CountDownTimer(20*1000,1000) {
            @Override
            public void onTick(long l) {
                tv_countDown.setText(formatTime(l));
            }

            @Override
            public void onFinish() {
                tv_countDown.setText("00:10");
                if(round == ROUND_RED){
                    round = ROUND_BLUE;
                }else{
                    round = ROUND_RED;
                }
                changeSide();
            }
        };

        tv_shutdown = findViewById(R.id.tv_competition_shutdown);
        tv_redP = findViewById(R.id.tv_competition_redP);
        tv_blueP = findViewById(R.id.tv_competition_blueP);
        tv_curIndex = findViewById(R.id.tv_competition_curIndex);
        tv_queTitle = findViewById(R.id.tv_competition_title);

        btn_red_a = findViewById(R.id.btn_red_optionA);
        btn_red_b = findViewById(R.id.btn_red_optionB);
        btn_red_c = findViewById(R.id.btn_red_optionC);
        btn_red_d = findViewById(R.id.btn_red_optionD);

        btn_blue_a = findViewById(R.id.btn_blue_optionA);
        btn_blue_b = findViewById(R.id.btn_blue_optionB);
        btn_blue_c = findViewById(R.id.btn_blue_optionC);
        btn_blue_d = findViewById(R.id.btn_blue_optionD);

        tv_a = findViewById(R.id.competition_A);
        tv_b = findViewById(R.id.competition_B);
        tv_c = findViewById(R.id.competition_C);
        tv_d = findViewById(R.id.competition_D);

        setListener();

        Questions q = randomList.get(curIndex);
        tv_queTitle.setText(q.getQDescription());
        tv_a.setText(q.getOptionA());
        tv_b.setText(q.getOptionB());
        tv_c.setText(q.getOptionC());
        tv_d.setText(q.getOptionD());
        tv_curIndex.setText((curIndex+1)+"");

        btn_red_a.setEnabled(true);
        btn_red_b.setEnabled(true);
        btn_red_c.setEnabled(true);
        btn_red_d.setEnabled(true);
        btn_blue_a.setEnabled(false);
        btn_blue_b.setEnabled(false);
        btn_blue_c.setEnabled(false);
        btn_blue_d.setEnabled(false);

        tv_redP.setText(redP+"");
        tv_blueP.setText(blueP+"");

        round = ROUND_RED;
        timerStart();

    }

    /**
     * Format time from milisecond to 00:00
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
     * Set Listener
     */
    private void setListener() {

        tv_shutdown.setOnClickListener(this);
        //tv_redP.setOnClickListener(this);
        //tv_blueP.setOnClickListener(this);

        btn_red_a.setOnClickListener(this);
        btn_red_b.setOnClickListener(this);
        btn_red_c.setOnClickListener(this);
        btn_red_d.setOnClickListener(this);

        btn_blue_a.setOnClickListener(this);
        btn_blue_b.setOnClickListener(this);
        btn_blue_c.setOnClickListener(this);
        btn_blue_d.setOnClickListener(this);
        
    }

    /**
     * Click
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_competition_shutdown:
                new AlertDialog.Builder(CompetitionActivity.this).setIcon(getResources().getDrawable(R.drawable.icon_back_yellow))
                        .setTitle("Are you sure you want to leave ? ")
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

                break;
            case R.id.btn_red_optionA:
                timerCancel();
                curCorrect=checkAnswer(0);
                addPoint(round,curCorrect);
                round = ROUND_BLUE;
                changeSide();
                break;
            case R.id.btn_red_optionB:
                timerCancel();
                curCorrect=checkAnswer(1);
                addPoint(round,curCorrect);
                round = ROUND_BLUE;
                changeSide();
                break;
            case R.id.btn_red_optionC:
                timerCancel();
                curCorrect=checkAnswer(2);
                addPoint(round,curCorrect);
                round = ROUND_BLUE;
                changeSide();
                break;
            case R.id.btn_red_optionD:
                timerCancel();
                curCorrect=checkAnswer(3);
                addPoint(round,curCorrect);
                round = ROUND_BLUE;
                changeSide();
                break;
            case R.id.btn_blue_optionA:
                timerCancel();
                curCorrect=checkAnswer(0);
                addPoint(round,curCorrect);
                round = ROUND_RED;
                changeSide();
                break;
            case R.id.btn_blue_optionB:
                timerCancel();
                curCorrect=checkAnswer(1);
                addPoint(round,curCorrect);
                round = ROUND_RED;
                changeSide();
                break;
            case R.id.btn_blue_optionC:
                timerCancel();
                curCorrect=checkAnswer(2);
                addPoint(round,curCorrect);
                round = ROUND_RED;
                changeSide();
                break;
            case R.id.btn_blue_optionD:
                timerCancel();
                curCorrect=checkAnswer(3);
                addPoint(round,curCorrect);
                round = ROUND_RED;
                changeSide();
                break;
            default:
                break;
        }
    }

    /**
     * When Red/Blue finished
     */
    private void changeSide(){
        
        if(curIndex < count-1){
            curIndex++;
            Questions q = randomList.get(curIndex);
            tv_queTitle.setText(q.getQDescription());
            tv_a.setText(q.getOptionA());
            tv_b.setText(q.getOptionB());
            tv_c.setText(q.getOptionC());
            tv_d.setText(q.getOptionD());
            tv_curIndex.setText((curIndex + 1)+"");

            switch(round){
                case ROUND_RED:
                    btn_red_a.setEnabled(true);
                    btn_red_b.setEnabled(true);
                    btn_red_c.setEnabled(true);
                    btn_red_d.setEnabled(true);
                    btn_blue_a.setEnabled(false);
                    btn_blue_b.setEnabled(false);
                    btn_blue_c.setEnabled(false);
                    btn_blue_d.setEnabled(false);
                    break;
                case ROUND_BLUE:
                    btn_red_a.setEnabled(false);
                    btn_red_b.setEnabled(false);
                    btn_red_c.setEnabled(false);
                    btn_red_d.setEnabled(false);
                    btn_blue_a.setEnabled(true);
                    btn_blue_b.setEnabled(true);
                    btn_blue_c.setEnabled(true);
                    btn_blue_d.setEnabled(true);
                    break;
                default:
                    break;

            }
            timerStart();
        }else{

            btn_red_a.setEnabled(false);
            btn_red_b.setEnabled(false);
            btn_red_c.setEnabled(false);
            btn_red_d.setEnabled(false);
            btn_blue_a.setEnabled(false);
            btn_blue_b.setEnabled(false);
            btn_blue_c.setEnabled(false);
            btn_blue_d.setEnabled(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionActivity.this);
            if(redP > blueP){
                builder.setTitle("Congratulations ! RED wins !");
            }else if(redP < blueP){
                builder.setTitle("Congratulations ! BLUE wins !");
            }else{
                builder.setTitle("DRAW!");
            }
            builder.setPositiveButton("Check All Questions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent wrong_mode = new Intent(CompetitionActivity.this,WrongMode.class);
                    wrong_mode.putExtra("wrongMode",false);
                    wrong_mode.putExtra("originalList",(Serializable)randomList);
                    wrong_mode.putExtra("user_data",loginUser);
                    dialogInterface.dismiss();
                    startActivity(wrong_mode);
                    finish();
                }
            }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNeutralButton("Play Again!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    new AlertDialog.Builder(CompetitionActivity.this).setTitle("Start?").setMessage("Click and the Game will start in 1 Seconds").setPositiveButton(getResources().getString(R.string.yes) + "! Start Game!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent competitionActivity = new Intent(CompetitionActivity.this, CompetitionActivity.class);
                            competitionActivity.putExtra("user_data",loginUser);
                            try { Thread.sleep (1000) ;
                            } catch (InterruptedException ie){}
                            dialogInterface.dismiss();
                            startActivity(competitionActivity);
                            finish();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setIcon(getResources().getDrawable(R.drawable.icon_game)).create().show();

                }
            }).create().show();
        }

    }

    /**
     * Check answer
     * @param selected
     * @return
     */
    private boolean checkAnswer(int selected) {
        Questions q = randomList.get(curIndex);
        if(q.getAnswer() != selected){
            return false;
        }else{
            return true;
        }

    }

    /**
     * Add points
     * @param round
     * @param curCorrect
     */
    private void addPoint(int round,boolean curCorrect){

        if(round == ROUND_RED && curCorrect){
            redP++;
        }else if(round == ROUND_BLUE && curCorrect){
            blueP++;
        }
        tv_redP.setText(redP+"");
        tv_blueP.setText(blueP+"");
    }

    /**
     * Get 5 random Index
     * @param n
     * @param f
     * @return
     */
    private static int[] randomSet ( int n, int f){
        Random rdm = new Random(System.currentTimeMillis());
        Set<Integer> intSet = new HashSet<Integer>();

        while (intSet.size() < n) {
            intSet.add(rdm.nextInt(f));
        }


        int intRet[] = new int[n];
        Iterator<Integer> it = intSet.iterator();
        int i = 0;
        while (it.hasNext() & i < n) {
            intRet[i] = Integer.parseInt(it.next().toString());
            i++;
        }

        return intRet;
    }

    /**
     * cancel Timer
     */
    public void timerCancel() {
        countDownTimer.cancel();
    }

    /**
     * start Timer
     */
    public void timerStart() {
        countDownTimer.start();
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
}
