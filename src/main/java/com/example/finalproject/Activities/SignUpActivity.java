package com.example.finalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Data.User;
import com.example.finalproject.Data.UsersDao;
import com.example.finalproject.Database.DatabaseCreator;
import com.example.finalproject.R;

/**
 * Sign up Activity
 * Create your own Account
 * @author JUnqi.Sun
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * wrong code:registered
     */
    private static final int RESULT_REGISTERED = 202;
    /**
     * input username
     */
    private EditText et_userName;
    /**
     * input password
     */
    private EditText et_passWord;
    /**
     * input repeat your password
     */
    private EditText et_repeatEnterPassword;
    /**
     * sign up button
     */
    private Button btn_signup;
    /**
     * back to login activity
     */
    private TextView tv_loginLink;
    /**
     * user
     */
    private User userWithName;
    /**
     * Dao
     */
    private UsersDao usersDao;
    /**
     * typed username
     */
    private String userName;
    /**
     * typed password
     */
    private String password;
    /**
     * user
     */
    private User user;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 设置默认键盘不弹出
        setContentView(R.layout.signup_layout);

        initUI();
    }

    /**
     * initialize all UI components
     */
    private void initUI() {
        et_userName = findViewById(R.id.sign_input_name);
        et_passWord = findViewById(R.id.signup_input_password);
        et_repeatEnterPassword = findViewById(R.id.signup_input_reEnterPassword);
        btn_signup = findViewById(R.id.btn_signup);
        tv_loginLink = findViewById(R.id.link_login);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        tv_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }


    /**
     * sign up
     */
    private void signUp() {
        if(!isValidAccount()){
            unsuccessfulSignUp(0);//incomplete or wrong input
            return;
        }

        btn_signup.setEnabled(false);

        //Get input
        userName = et_userName.getText().toString();
        password = et_passWord.getText().toString();

        //Matching DB store Data
        DatabaseCreator databaseCreator = DatabaseCreator.getInstance(getApplicationContext());
        usersDao = databaseCreator.usersDao();
        userWithName = usersDao.findUserWithName(userName);

        if(userWithName == null || !userWithName.getUserName().equals(userName)){

            user = new User(userName,password);
            usersDao.insertUsers(user);//insert into DB
            successfulSignUp();

        }else {
            unsuccessfulSignUp(RESULT_REGISTERED);//username has already been registered
            btn_signup.setEnabled(true);
        }

    }

    /**
     * successful sign up
     * created a data in corresponding database
     */
    private void successfulSignUp(){

        btn_signup.setEnabled(true);

        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        //intent.putExtra("user_type",NEW_USER);
        intent.putExtra("username",userName);
        intent.putExtra("password",password);

        startActivity(intent);
        finish();
    }

    /**
     * unsuccessful sign up
     * toast error message
     *
     * @param i wrong code
     */
    private void unsuccessfulSignUp(int i) {
        if (i == RESULT_REGISTERED) {
            Toast.makeText(SignUpActivity.this, "This username has been already registered", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SignUpActivity.this,"SignUp failed",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * check if account is valid
     * @return
     */
    private boolean isValidAccount(){

        boolean valid = true;

        String name = et_userName.getText().toString();
        String password = et_passWord.getText().toString();
        String reEnterPsw = et_repeatEnterPassword.getText().toString();

        if(name.isEmpty()){
            et_userName.setError("Username cannot be empty");
            valid = false;
        }else{
            et_userName.setError(null);
        }

        if(password.isEmpty()){
            et_passWord.setError("Please enter your password");
            valid = false;
        }else{
            et_passWord.setError(null);
        }

        if(reEnterPsw.isEmpty()){
            tv_loginLink.setError("Passwords inconsistent");
            valid = false;
        }else{
            tv_loginLink.setError(null);
        }
        return valid;
    }
}
