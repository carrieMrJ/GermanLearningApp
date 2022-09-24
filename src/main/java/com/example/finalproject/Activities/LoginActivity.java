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
 * Login Activity
 * Login with Account
 * @author Junqi.Sun
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * request code for sign up activity
     */
    private static final int REQUESTCODE_SIGNUP = 101;
    /**
     * input username
     */
    private EditText et_userName;
    /**
     * input password
     */
    private EditText et_psw;
    /**
     * login button
     */
    private Button btn_login;
    /**
     * link of sign uo activity
     */
    private TextView link_signup;
    /**
     * Dao for User
     */
    private UsersDao usersDao;
    /**
     * username
     */
    private String userName;
    /**
     * password
     */
    private String psw;
    /**
     * user
     */
    private User userWithName;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 设置默认键盘不弹出

        setContentView(R.layout.login_layout);

        initUI();
    }

    /**
     * initialize UI components
     */
    private void initUI() {

        et_userName = findViewById(R.id.et_login_username);
        et_psw = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);
        link_signup = findViewById(R.id.link_signup);

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");

        et_userName.setText(username);
        et_psw.setText(password);

        //initListener
        btn_login.setOnClickListener(this);
        link_signup.setOnClickListener(this);
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:

                login();
                break;

            case R.id.link_signup:
                //Jump to SignUp Activity
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(intent,REQUESTCODE_SIGNUP);
                finish();

                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    /**
     * login
     */
    private void login() {
        //if unsuccessfully
        if(!isValidAccount()){
            int wrongCode = 1;//Incomplete input (missing username or password)
            unsuccessfulLogin(wrongCode);
            return;
        }

        btn_login.setEnabled(false);

        //Get Input_data
        userName = et_userName.getText().toString().trim();
        psw = et_psw.getText().toString().trim();

        //Matching with DB--->successfulLogin
        DatabaseCreator databaseCreator = DatabaseCreator.getInstance(getApplicationContext());
        usersDao = databaseCreator.usersDao();
        userWithName = usersDao.findUserWithName(userName);

        if(userWithName == null){

            int wrongCode = 2;//Unregistered username
            unsuccessfulLogin(wrongCode);

        }else if(psw.equals(userWithName.getUserPw())){

            successfulLogin();

        }else{
            int wrongCode = 3;//wrong password
            unsuccessfulLogin(wrongCode);
        }

    }

    /**
     * check if account valid
     * @return boolean
     */
    private boolean isValidAccount(){

        boolean valid = true;

        //Get Input_data
        String userName = et_userName.getText().toString().trim();
        String psw = et_psw.getText().toString().trim();

        //
        if(userName.isEmpty()){
            et_userName.setError("Please enter your Username");
            valid = false;
        }else{
            et_userName.setError(null);
        }

        if(psw.isEmpty()){
            et_psw.setError("Please enter your Password");
            valid = false;
        }else{
            et_psw.setError(null);
        }

        return valid;
    }

    /**
     * show error message according to wrong code
     * @param err wrong code
     */
    private void unsuccessfulLogin(int err) {

        switch (err){
            case 1:
                Toast.makeText(LoginActivity.this,"Invalid Login",Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(LoginActivity.this,"Invalid Username",Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
                break;
            default:
                break;

        }


        btn_login.setEnabled(true);
    }

    /**
     * jump to Main Activity
     */
    private void successfulLogin(){
        btn_login.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user_data",userWithName);
        startActivity(intent);
        finish();
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

        if(requestCode == REQUESTCODE_SIGNUP){
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
