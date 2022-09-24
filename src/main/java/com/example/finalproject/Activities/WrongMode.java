package com.example.finalproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.example.finalproject.Data.Questions;
import com.example.finalproject.Data.User;
import com.example.finalproject.Database.MySQLite;
import com.example.finalproject.R;
import java.util.List;

/**
 * Activity which used for checking wrong questions and all questions answered in one mission
 * @author Mengru.Ji
 */
public class WrongMode extends Activity implements View.OnClickListener {

    /**
     * text view: to show the current Index
     */
    private TextView tv_curIndex;
    /**
     * text view: to show the number of all questions
     */
    private TextView tv_totalIndex;
    /**
     * text view: share button
     */
    private TextView tv_share;
    /**
     * text view: favorites button
     */
    private TextView tv_collect;
    /**
     * text view: back button
     */
    private TextView tv_back;
    /**
     * text view； to show questions description
     */
    private TextView tv_question;
    /**
     * text view: to show optionA
     */
    private TextView tv_optionA;
    /**
     * text view: to show optionB
     */
    private TextView tv_optionB;
    /**
     * text view: to show optionC
     */
    private TextView tv_optionC;
    /**
     * text view: to show optionD
     */
    private TextView tv_optionD;
    /**
     * button: Next button
     */
    private Button btn_suc;
    /**
     * button Last button
     */
    private Button btn_pre;
    /**
     * wrong questions list
     */
    private List<Questions> wrongList;
    /**
     * current index
     */
    private int current;
    /**
     * number of all questions list
     */
    private int count;
    //private TextView tv_pointAct;
    /**
     * original question index
     */
    private List<Questions> originalList;
    /**
     * true,then check wrong questions; false,then check all questions
     */
    private boolean wrongMode;
    /**
     * question list
     */
    private List<Questions> tempList;
    /**
     * database
     */
    private MySQLite mySQLite;
    /**
     * table name
     */
    private static final String TABLE_QUESTION = "questions";
    /**
     * table name
     */
    private static final String TABLE_WRONG = "wrongquestions";
    /**
     * table name
     */
    private static final String TABLE_PATH = "filePath";
    /**
     * table name: favorites question
     */
    private static final String TABLE_COLLECT = "collection";
    private User loginUser;

    /**
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_wrong_mode);

        getData();
        initUI();



        //initResultData();

    }

    /**
     * get data from intent
     */
    private void getData() {

        Intent intent = getIntent();
        wrongList = (List<Questions>) intent.getSerializableExtra("wrongList");
        originalList = (List<Questions>) intent.getSerializableExtra("originalList");
        wrongMode = intent.getBooleanExtra("wrongMode",true);
        loginUser = (User)intent.getSerializableExtra("user_data");

        //String curTime = intent.getStringExtra("curTime");
        //String duration = intent.getStringExtra("duration");

        //check if you want to see wrong questions or all questions
        if(wrongMode){
            tempList = wrongList;
        }else{
            tempList = originalList;
        }

        mySQLite = new MySQLite(WrongMode.this,loginUser.getUserName());


    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        //check wrong questions or all questions?
        if(!wrongMode){
            TextView tv_title = findViewById(R.id.tv_wrong_title);
            tv_title.setText("Question List");
        }

        //initialize current index with 0
        current = 0;
        count = tempList.size();

        tv_curIndex = findViewById(R.id.wrong_Mode_current);
        tv_totalIndex = findViewById(R.id.wrong_Mode_total);
        tv_share = findViewById(R.id.tv_wrong_share);
        tv_collect = findViewById(R.id.tv_wrong_collect);
        tv_back = findViewById(R.id.tv_wrong_back);

        tv_question = findViewById(R.id.wrong_Mode_question);
        tv_optionA = findViewById(R.id.wrong_Mode_A);
        tv_optionB = findViewById(R.id.wrong_Mode_B);
        tv_optionC = findViewById(R.id.wrong_Mode_C);
        tv_optionD = findViewById(R.id.wrong_Mode_D);

        btn_suc = findViewById(R.id.wrong_Mode_btn_suc);
        btn_pre = findViewById(R.id.wrong_Mode_btn_pre);

        //tv_pointAct = findViewById(R.id.tv_wrong_Mode_pointAct);

        setListener();


        Questions q = tempList.get(current);

        tv_question.setText(q.getQDescription());
        tv_optionA.setText(q.getOptionA());
        tv_optionB.setText(q.getOptionB());
        tv_optionC.setText(q.getOptionC());
        tv_optionD.setText(q.getOptionD());

        tv_curIndex.setText((current+1)+"");
        tv_totalIndex.setText("/"+count +"");

        //check if this current question is collected or not
        setCollect(q);
        markOption();

    }

    /**
     * set all listener
     */
    private void setListener() {
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if(current > 0){

                    clearMark();
                    current--;
                    Questions q = tempList.get(current);
                    setCollect(q);
                    tv_question.setText(q.getQDescription());
                    tv_optionA.setText(q.getOptionA());
                    tv_optionB.setText(q.getOptionB());
                    tv_optionC.setText(q.getOptionC());
                    tv_optionD.setText(q.getOptionD());

                    tv_curIndex.setText((current+1)+"");

                    if(q.isCollect() == 1){
                        tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_collected));
                    }else{
                        tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_noncollect));
                    }

                    markOption();

                }else {
                    new AlertDialog.Builder(WrongMode.this).setTitle(" ! ! ").setMessage("It is already the first question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });


        btn_suc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(current < count - 1){

                    clearMark();
                    current++;
                    Questions q = tempList.get(current);
                    setCollect(q);
                    tv_question.setText(q.getQDescription());
                    tv_optionA.setText(q.getOptionA());
                    tv_optionB.setText(q.getOptionB());
                    tv_optionC.setText(q.getOptionC());
                    tv_optionD.setText(q.getOptionD());

                    tv_curIndex.setText((current+1)+"");

                    if(q.isCollect() == 1){
                        tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_collected));
                    }else{
                        tv_collect.setBackground(getResources().getDrawable(R.drawable.icon_noncollect));
                    }

                    markOption();

                }else {
                    new AlertDialog.Builder(WrongMode.this).setTitle(" ! ! ").setMessage("It is already the last question!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });
        tv_back.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    /**
     * clear marked option
     */
    private void clearMark() {
        tv_optionA.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_optionB.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_optionC.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_optionD.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    /**
     * mark the right answer and users selected answer
     */
    //mark the right Answer and users selected answer
    private void markOption() {

        Questions q = tempList.get(current);
        //Questions temp = wrongList.get(curIndex);
        int selected = q.getSelectedAnswer();
        Log.i("RRR","selected:"+selected);
        int answer = q.getAnswer();
        Log.i("RRR","right:"+answer);

        //use green to mark selected option
        switch(selected){
            case 0:
                tv_optionA.setTextColor(getResources().getColor(R.color.colorGreyGreen));
                break;
            case 1:
                tv_optionB.setTextColor(getResources().getColor(R.color.colorGreyGreen));
                break;
            case 2:
                tv_optionC.setTextColor(getResources().getColor(R.color.colorGreyGreen));
                break;
            case 3:
                tv_optionD.setTextColor(getResources().getColor(R.color.colorGreyGreen));
                break;
            default:
                break;
        }
        //use pink to mark right answer
        switch(answer){
            case 0:
                tv_optionA.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 1:
                tv_optionB.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 2:
                tv_optionC.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            case 3:
                tv_optionD.setTextColor(getResources().getColor(R.color.colorLightRosa));
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_wrong_back:

                finish();
                break;

            case R.id.tv_wrong_collect:

                Questions q = tempList.get(current);
                if(q.isCollect() != 1){
                    tv_collect.setBackgroundResource(R.drawable.icon_collected);
                    q.setCollect(1);
                    mySQLite.insertQuestion(q,TABLE_COLLECT);
                    mySQLite.updateIsCollect(TABLE_QUESTION,q);
                    Toast.makeText(this,"Add Favorites",Toast.LENGTH_SHORT).show();

                }else{
                    tv_collect.setBackgroundResource(R.drawable.icon_noncollect);
                    q.setCollect(0);
                    mySQLite.deleteCollection(q);
                    mySQLite.updateIsCollect(TABLE_QUESTION,q);
                    Toast.makeText(this,"Remove Favorites",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_wrong_share:
                shareText();
                break;

            default:
                break;
        }

    }

    /**
     * share question
     */
    private void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, tempList.get(current).toString()+"\nDo you know the answer?");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    /**
     * to check if the current question is collected or not
     * @param questions
     */
    public void setCollect(Questions questions){

        //Questions questions =tempList.get(current);
        if(questions.isCollect() == 1){
            tv_collect.setBackgroundResource(R.drawable.icon_collected);

        }else{
            tv_collect.setBackgroundResource(R.drawable.icon_noncollect);
        }
    }

}
