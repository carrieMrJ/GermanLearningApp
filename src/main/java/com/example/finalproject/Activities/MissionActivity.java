package com.example.finalproject.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.Data.User;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.R;
import com.example.finalproject.View.BackDialog;
import com.example.finalproject.View.TimerDialog;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Activity for Answer Task
 * Five questions are randomly selected from the database each time
 * @author Mengru.Ji
 *
 */
public class MissionActivity extends Activity implements  View.OnClickListener {

    /**
     * size of Random Question List
     */
    private static final int RANDOM_LIST_SIZE = 5;
    /**
     * name of all question table
     */
    private static final String TABLE_QUESTION = "questions";
    /**
     * name of all wrong questions table
     */
    private static final String TABLE_WRONG = "wrongquestions";
    /**
     * name of the table, which store all file path
     */
    private static final String TABLE_PATH = "filePath";
    /**
     * name of all favorites questions table
     */
    private static final String TABLE_COLLECT = "collection";
    /**
     * text view: back button
     */
    private TextView tv_back;
    /**
     * text view: answer card button
     */
    private TextView tv_answerCard;
    /**
     * text view: timer button
     */
    private TextView tv_time;
    /**
     * text view: share button
     */
    private TextView tv_share;
    /**
     * text view: Number of correct questions
     */
    public static int rightCount = 0;
    /**
     * random questions list
     */
    public static List<Questions> randomList;
    /**
     * current index of question list
     */
    private int current = 0;
    /**
     * size of questions list
     */
    private int count;
    /**
     * text view: show description of question
     */
    private TextView tv_qDescription;
    /**
     * group of radio buttons
     */
    private RadioButton[] radioBtns;
    /**
     * Button: last button
     */
    private Button preBtn;
    /**
     * Button: next button
     */
    private Button sucBtn;
    /**
     * radio group
     */
    private RadioGroup radioGroup;
    /**
     * text view: show current Index
     */
    private TextView currIndex;
    /**
     * text view: show size of questions list
     */
    private TextView totalIndex;
    /**
     * SQLite database
     */
    private MySQLite mySQLite;
    /**
     * text view: favorites button
     */
    private TextView tv_collect;
    /**
     * wrong question list
     */
    private List<Integer> wrongList;
    /**
     * already logged User
     */
    private User loginUser;
    /**
     * test view: show your choice
     */
    private TextView tv_yourChoice;

    //private TextView tv_current;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_daily_mission);

        loadData();

        initUI();

    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        current = 0;
        count = RANDOM_LIST_SIZE;
        //wrongMode = false;

        //initTimer();
        tv_back = findViewById(R.id.tv_back);
        tv_answerCard = findViewById(R.id.tv_answerCard);
        tv_time = findViewById(R.id.tv_time);
        tv_share = findViewById(R.id.tv_share);
        tv_collect = findViewById(R.id.tv_collection);
        tv_yourChoice = findViewById(R.id.tv_yourChoice);
        //tv_current = findViewById(R.id.tv_mission_current);

        startTimer();

        tv_back.setOnClickListener(this);
        tv_answerCard.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_collect.setOnClickListener(this);

        tv_qDescription = findViewById(R.id.tv_question);
        radioBtns = new RadioButton[4];
        radioBtns[0] = findViewById(R.id.rb_firstChoice);
        radioBtns[1] = findViewById(R.id.rb_secondChoice);
        radioBtns[2] = findViewById(R.id.rb_thirdChoice);
        radioBtns[3] = findViewById(R.id.rb_fourthChoice);

        preBtn = findViewById(R.id.btn_mission_pre);
        sucBtn = findViewById(R.id.btn_mission_suc);

        currIndex = findViewById(R.id.tv_mission_current);
        totalIndex = findViewById(R.id.tv_mission_total);

        radioGroup = findViewById(R.id.radioGroup);

        current = 0;
        Questions questions = randomList.get(current);

        count = randomList.size();
        //String total = count+"";

        tv_qDescription.setText(questions.getQDescription());
        radioBtns[0].setText(questions.getOptionA());
        radioBtns[1].setText(questions.getOptionB());
        radioBtns[2].setText(questions.getOptionC());
        radioBtns[3].setText(questions.getOptionD());

        currIndex.setText(current+1+"");
        totalIndex.setText("/"+count);

        //set last button listener
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current > 0) {

                    current--;
                    //Log.i("PPP","currIndex2:"+current+"");
                    Questions questions = randomList.get(current);
                    currIndex.setText((current+1)+"");
                    setCollect(questions);
                    tv_qDescription.setText(questions.getQDescription());
                    radioBtns[0].setText(questions.getOptionA());
                    radioBtns[1].setText(questions.getOptionB());
                    radioBtns[2].setText(questions.getOptionC());
                    radioBtns[3].setText(questions.getOptionD());

                    if (questions.isCollect() == 1)
                        tv_collect.setBackgroundResource(R.drawable.icon_collected);
                    else
                        tv_collect.setBackgroundResource(R.drawable.icon_noncollect);

                    //Already chosen?
                    radioGroup.clearCheck();
                    if (questions.getSelectedAnswer() != -1) {
                        radioBtns[questions.getSelectedAnswer()].setChecked(true);
                    }
                    if(questions.getSelectedAnswer() != -1){
                        int i = questions.getSelectedAnswer();
                        String str = "";
                        switch (i){
                            case 0:
                                str = "A";
                                break;
                            case 1:
                                str = "B";
                                break;
                            case 2:
                                str = "C";
                                break;
                            case 3:
                                str = "D";
                                break;
                        }
                        tv_yourChoice.setText("Selected: "+str);
                    }else{
                        tv_yourChoice.setText("");
                    }
                }else{
                    new AlertDialog.Builder(MissionActivity.this).setTitle(" ! ! ").setMessage("It is already the first question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });

        //set last button listener
        sucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (current < count - 1) {

                    current++;
                    //int cur = current;
                    //Log.i("QQQ","+CurrentIndex2:"+(current-1)+"");
                    Questions questions = randomList.get(current);
                    //Log.i("DDD",questions.toString());
                    currIndex.setText((current+1)+"");
                    //Log.i("QQQ",currIndex.getText().toString());

                    setCollect(questions);
                    tv_qDescription.setText(questions.getQDescription());
                    radioBtns[0].setText(questions.getOptionA());
                    radioBtns[1].setText(questions.getOptionB());
                    radioBtns[2].setText(questions.getOptionC());
                    radioBtns[3].setText(questions.getOptionD());

                    if (questions.isCollect() == 1)
                        tv_collect.setBackgroundResource(R.drawable.icon_collected);
                    else
                        tv_collect.setBackgroundResource(R.drawable.icon_noncollect);

                    //already chosen?Then should remember the selected answer
                    radioGroup.clearCheck();
                    if (questions.getSelectedAnswer() != -1) {
                        radioBtns[questions.getSelectedAnswer()].setChecked(true);
                    }

                    if(questions.getSelectedAnswer() != -1){
                        int i = questions.getSelectedAnswer();
                        String str = "";
                        switch (i){
                            case 0:
                                str = "A";
                                break;
                            case 1:
                                str = "B";
                                break;
                            case 2:
                                str = "C";
                                break;
                            case 3:
                                str = "D";
                                break;
                        }
                        tv_yourChoice.setText("Selected: "+str);
                    }else{
                        tv_yourChoice.setText("");
                    }
                } else {

                    stopTimer();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
                    builder.setMessage("Are you sure you want to submit ? ");
                    builder.setTitle("Last Question");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //dialog.dismiss();

                            wrongList = checkAnswer(randomList);
                            for (int i = 0; i < wrongList.size(); i++) {
                                Log.i("WWW",wrongList.get(i)+"\n");
                            }

                            if (wrongList.size() == 0) {
                                Intent intent = new Intent(MissionActivity.this, MissionResultActivity.class);
                                SimpleDateFormat myFmt2 = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");
                                Date date = new Date();
                                String time = myFmt2.format(date);
                                intent.putExtra("curTime", time);
                                intent.putExtra("duration", tv_time.getText());
                                intent.putExtra("correct", randomList.size());
                                intent.putExtra("total", randomList.size());
                                intent.putExtra("user_data",loginUser);
                                intent.putExtra("originalList",(Serializable)randomList);

                                startActivity(intent);
                                finish();

                            } else {

                                //wrong questions
                                List<Questions> newList = new ArrayList<>();

                                //copy the wrong questions to newList
                                for (int i = 0; i < wrongList.size(); i++) {
                                    newList.add(randomList.get(wrongList.get(i)));
                                }

                                for (int i = 0; i < newList.size(); i++) {
                                   Log.i("WWW",newList.get(i).toString());
                                }


                                //add wrong questions into db
                                //SimpleDateFormat myFmt = new SimpleDateFormat("yy-MM-dd");
                                SimpleDateFormat myFmt2 = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");
                                Date date = new Date();
                                //String now = myFmt.format(date);
                                String time = myFmt2.format(date);

                                for (int i = 0; i < newList.size(); i++) {
                                    int wrong = newList.get(i).getWrongTime()+1;
                                    newList.get(i).setWrongTime(wrong);
                                    mySQLite.insertQuestion(newList.get(i),TABLE_WRONG);
                                }

                                Intent intent = new Intent(MissionActivity.this, MissionResultActivity.class);
                                intent.putExtra("curTime", time);
                                intent.putExtra("wrongList", (Serializable) newList);
                                intent.putExtra("originalList",(Serializable) randomList);
                                intent.putExtra("duration", tv_time.getText());
                                intent.putExtra("correct",randomList.size()-wrongList.size());
                                intent.putExtra("total",randomList.size());
                                intent.putExtra("user_data",loginUser);

                                startActivity(intent);
                                finish();
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    startTimer();

                                }
                            });

                    builder.create().show();
                }


            }
        });


        //set radio group listener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (radioBtns[i].isChecked() == true) {
                        randomList.get(current).setSelectedAnswer(i);
                        String str = "";
                        switch (i){
                            case 0:
                                str = "A";
                                break;
                            case 1:
                                str = "B";
                                break;
                            case 2:
                                str = "C";
                                break;
                            case 3:
                                str = "D";
                                break;
                        }
                        tv_yourChoice.setText("Selected : "+ str);
                        break;
                    }
                }
            }
        });


        loginUser = (User)getIntent().getSerializableExtra("user_data");


    }

    //customized count down timer
    int time = 0;
    int second = 0;
    int minute = 0;
    //String timeStr = "00:00";
    int[] mTime = new int[]{0, 0, 0, 0};
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 1:
                    time++;
                    second = time % 60;
                    minute = time / 60;
                    if (minute > 99) {
                        break;
                    }
                    if (second < 10 && minute < 10) {
                        mTime[0] = 0;
                        mTime[1] = minute;
                        mTime[2] = 0;
                        mTime[3] = second;
                    } else if (second >= 10 && minute < 10) {
                        mTime[0] = 0;
                        mTime[1] = minute;
                        mTime[2] = (second + "").charAt(0) - 48;
                        mTime[3] = (second + "").charAt(1) - 48;
                    } else if (second >= 10 && minute >= 10) {
                        mTime[0] = (minute + "").charAt(0) - 48;
                        mTime[1] = (minute + "").charAt(1) - 48;
                        mTime[2] = (second + "").charAt(0) - 48;
                        mTime[3] = (second + "").charAt(1) - 48;
                    }

                    tv_time.setText("" + mTime[0] + mTime[1] + ":" + mTime[2] + mTime[3]);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * start Timer
     */
    private void startTimer() {
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    /**
     * Timer pause
     */
    private void stopTimer() {
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * load questions from database
     */
    private void loadData() {

        loginUser = (User)getIntent().getSerializableExtra("user_data");

        mySQLite = new MySQLite(MissionActivity.this,loginUser.getUserName());

        //mySQLite.deleteAllQuestions(TABLE_COLLECT);
        //mySQLite.deleteAllQuestions(TABLE_WRONG);
        //mySQLite.deleteAllQuestions(TABLE_QUESTION);
        //mySQLite.deleteAllQuestions(TABLE_PATH);

        //mySQLite.insertQueListFromFileToDB(MissionActivity.this,"questionList1");


        //mySQLite.deleteAllQuestions();

        /*Questions que = new Questions();
        que.setqDescription("________ denken Sie gerade?");
        que.setOptionA("Was an");
        que.setOptionB("Woran");
        que.setOptionC("An wen");
        que.setOptionD("An");
        que.setAnswer(1);
        que.setQType(1);
        mySQLite.insertQuestion(que,TABLE_QUESTION);

        Questions que1 = new Questions();
        que1.setqDescription("________ fahren Sie morgen nach Nuernberg? -Weil ich dort meinen Freund besuchen will.");
        que1.setOptionA("Wann");
        que1.setOptionB("Mit wen");
        que1.setOptionC("Warum");
        que1.setOptionD("Womit");
        que1.setAnswer(2);
        que1.setQType(1);
        mySQLite.insertQuestion(que1,TABLE_QUESTION);

        Questions que2 = new Questions();
        que2.setqDescription("________ Stunden Aufenthalt hat der Zug in Frankfurt.");
        que2.setOptionA("Wie viele Uhr");
        que2.setOptionB("Wie viel");
        que2.setOptionC("Um wie viel");
        que2.setOptionD("WIe lange");
        que2.setAnswer(1);
        que2.setQType(1);
        mySQLite.insertQuestion(que2,TABLE_QUESTION);

        Questions que3 = new Questions();
        que3.setqDescription("________ habe ich nur meinen Fueller hingelegt?");
        que3.setOptionA("Wo");
        que3.setOptionB("Wonach");
        que3.setOptionC("Woher");
        que3.setOptionD("Wohin");
        que3.setAnswer(0);
        que3.setQType(1);
        mySQLite.insertQuestion(que3,TABLE_QUESTION);

        Questions que4 = new Questions();
        que4.setqDescription("Kann ich bitte ________ Kugelschreiber einen Augenblick haben, Herr Braun?");
        que4.setOptionA("seinen");
        que4.setOptionB("Ihren");
        que4.setOptionC("sein");
        que4.setOptionD("ihr");
        que4.setAnswer(1);
        que4.setQType(1);
        mySQLite.insertQuestion(que4,TABLE_QUESTION);

        Questions que5 = new Questions();
        que5.setqDescription("Habt ihr ________ Gast mit dem Taxi zum Bahnhof gebracht?");
        que5.setOptionA("euer");
        que5.setOptionB("eure");
        que5.setOptionC("euren");
        que5.setOptionD("eurem");
        que5.setAnswer(2);
        que5.setQType(1);
        mySQLite.insertQuestion(que5,TABLE_QUESTION);*/


        final List<Questions> list = mySQLite.getQuestion();

        int[] randomIndex = randomSet(5, list.size());

        randomList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomList.add(list.get(randomIndex[i]));
        }

        /*for(int i = 0 ; i < randomList.size(); i++){
            Log.i("QQQ","Random:"+randomList.get(i).getQDescription()+"!");
        }*/


    }

    /**
     * set some click listeners
     * @param v view
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_collection:


                if (randomList.get(current).isCollect() == 1) {
                    tv_collect.setBackgroundResource(R.drawable.icon_noncollect);
                    mySQLite.deleteCollection(randomList.get(current));
                    randomList.get(current).setCollect(0);
                    mySQLite.updateIsCollect(TABLE_QUESTION,randomList.get(current));
                    Toast.makeText(this,"Remove Favorites",Toast.LENGTH_SHORT).show();

                } else {
                    tv_collect.setBackgroundResource(R.drawable.icon_collected);
                    randomList.get(current).setCollect(1);
                    mySQLite.insertQuestion(randomList.get(current),TABLE_COLLECT);
                    mySQLite.updateIsCollect(TABLE_QUESTION,randomList.get(current));
                    Toast.makeText(this,"Add Favorites",Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.tv_share:
                shareText();
                break;

            case R.id.tv_back:
                stopTimer();
                final BackDialog backDialog = new BackDialog(this);
                backDialog.setCancelable(false);
                backDialog.show();
                backDialog.setBackDialogClickListner(new BackDialog.BackDialogClickListener() {
                    @Override
                    public void goBack() {

                        Intent intent = new Intent(MissionActivity.this,MainActivity.class);
                        intent.putExtra("user_data",loginUser);
                        stopTimer();
                        startActivity(intent);
                        backDialog.dismiss();
                        finish();

                    }

                    @Override
                    public void proceed() {
                        backDialog.dismiss();
                        startTimer();
                    }
                });
                break;

            case R.id.tv_time:
                stopTimer();
                final TimerDialog confirmDialog = new TimerDialog(this);
                confirmDialog.setCancelable(false);
                confirmDialog.show();
                confirmDialog.setClickListner(new TimerDialog.ClickListener() {

                    @Override
                    public void proceed() {
                        //TODO计时器继续计时
                        confirmDialog.dismiss();
                        startTimer();
                    }

                });
                break;

            case R.id.tv_answerCard:
                //jumpToPage(randomList.size());
        }
    }

    /**
     * share question item
     */
    private void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, randomList.get(current).toString()+"\nDo you know the answer?");
        shareIntent.setType("text/plain");
        stopTimer();
        startActivity(shareIntent);
    }

    /**
     * check all Questions
     * @param randomList Random question list
     * @return index of wrong questions stored in a integer list
     */
        private List<Integer> checkAnswer (List < Questions > randomList) {
            List<Integer> wrongList = new ArrayList<>();
            for (int i = 0; i < randomList.size(); i++) {
                if (randomList.get(i).getAnswer() != randomList.get(i).getSelectedAnswer()) {
                    wrongList.add(i);
                }
            }
            return wrongList;
        }


    /*public void jumpToPage(int index) {
        viewPager.setCurrentItem(index);
    }

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.finalProject.jumpToPage")){
                int index = intent.getIntExtra("index",0);
                jumpToPage(index);
            }else if(intent.getAction().equals("com.finalProject.jumpToNext")){
                jumpToNext();
            }
            rightCount = intent.getIntExtra("rightCount",0);
        }
    };

    private void jumpToNext() {
        int pos = viewPager.getCurrentItem();
        viewPager.setCurrentItem(pos+1);
    }*/

    /**
     * get a list stored randomly selected index
     * @param n number of selected index
     * @param f Range
     * @return int array
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
     * check if this question has already been collected
     * @param questions
     */
    public void setCollect(Questions questions){

        //Questions questions =tempList.get(current);
        if(questions.isCollect() != 1){
            tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_collected));

        }else{
            tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_noncollect));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopTimer();
        final BackDialog backDialog = new BackDialog(this);
        backDialog.setCancelable(false);
        backDialog.show();
        backDialog.setBackDialogClickListner(new BackDialog.BackDialogClickListener() {
            @Override
            public void goBack() {
                Intent intent = new Intent(MissionActivity.this,MainActivity.class);
                intent.putExtra("user_data",loginUser);
                finish();
            }

            @Override
            public void proceed() {
                backDialog.dismiss();
                startTimer();
            }
        });
    }
}





