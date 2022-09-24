package com.example.finalproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

import com.example.finalproject.Data.User;
import com.example.finalproject.R;

/**
 * Activity before Competition start
 * Introduce the rules of the game
 * @author Junqi.Sun
 */
public class CompetitionStart extends Activity {

    /**
     * Button to start Game
     */
    private Button btn_game;
    /**
     * Button back
     */
    private Button btn_back;
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
        setContentView(R.layout.activity_competition_start);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        loginUser = (User)getIntent().getSerializableExtra("user_data");
        initUI();

    }

    /**
     * initialize all UI components
     */
    private void initUI() {

        btn_game = findViewById(R.id.btn_game_start);
        btn_back = findViewById(R.id.competition_start_back);

        setListener();


    }

    /**
     * set Listener for all Buttons
     */
    private void setListener() {

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CompetitionStart.this).setTitle("Already Known the Rules?").setMessage("Click to Start!").setPositiveButton(getResources().getString(R.string.yes) + "! Start Game!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent competitionActivity = new Intent(CompetitionStart.this, CompetitionActivity.class);
                        competitionActivity.putExtra("user_data",loginUser);
                        startActivity(competitionActivity);
                        dialogInterface.dismiss();
                        finish();

                    }
                }).setNegativeButton("Have a Check!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setIcon(getResources().getDrawable(R.drawable.icon_game)).create().show();
            }
        });

        btn_game.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        //when press and hold
                        btn_game.setBackground(getResources().getDrawable(R.drawable.icon_videogame));

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //when move
                        btn_game.setBackground(getResources().getDrawable(R.drawable.icon_vg_nocolor));
                        break;
                    case MotionEvent.ACTION_UP:
                        //when release
                        btn_game.setBackground(getResources().getDrawable(R.drawable.icon_vg_nocolor));
                        break;
                    default:
                        break;

                }

                return false;
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
