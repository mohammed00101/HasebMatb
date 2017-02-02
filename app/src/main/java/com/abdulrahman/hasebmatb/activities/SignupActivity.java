package com.abdulrahman.hasebmatb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.service.MyInstanceIDService;
import com.abdulrahman.hasebmatb.webTasks.WebConnectionTask;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    Button signupBtn,skiptoHomeBtn;
    TextView loginTxt , userName , email , password;
    SharedPreferences userSharedPreferences;
    //private MyInstanceIDService myInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        skiptoHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        });
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
    void init(){
        signupBtn= (Button) findViewById(R.id.signup_login);
        skiptoHomeBtn= (Button) findViewById(R.id.signup_skipTohome);
        loginTxt= (TextView) findViewById(R.id.signup_backtoLogin);

        userName = (TextView) findViewById(R.id.signup_username_editTxt);
        email = (TextView) findViewById(R.id.signup_email_editTxt);
        password = (TextView) findViewById( R.id.signup_password_editTxt);
        userSharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
    }


   public void send_signup_data(View v){

        new WebConnectionTask(getBaseContext(),"signup.php",new HashMap<String , String>(){{
            put("userName",userName.getText().toString());
            put("email",email.getText().toString());
            put("token", userSharedPreferences.getString("token",""));
            put("password",password.getText().toString());

        }}) {
            @Override
            public void onRespnseComplete(String response) {
                try {
                    int userId = Integer.parseInt(response);
                    if(userId == 0)throw new Exception();
                    userSharedPreferences.edit().putInt("userId", userId).commit();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);

                }catch (Exception ex){

                }

            }
        };
    }
}
