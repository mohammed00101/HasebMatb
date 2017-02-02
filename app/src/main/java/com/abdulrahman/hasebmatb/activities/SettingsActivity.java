package com.abdulrahman.hasebmatb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.helper.Constants;
import com.abdulrahman.hasebmatb.helper.CustomButton;
import com.abdulrahman.hasebmatb.helper.CustomEditText;
import com.abdulrahman.hasebmatb.helper.CustomTextView;

public class SettingsActivity extends AppCompatActivity {

    CustomButton ringtoneBtn;
    Switch aSwitch ,stopAlarmSwitch;
    CustomEditText distanceFromMtbTxt;
    CustomButton logut;
    private SharedPreferences userSharedPreferences;

    final  int RINGTONE_PICKER_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        setContentView(R.layout.activity_settings);
            init();
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSharedPreferences.edit().putInt("userId",0).commit();
                Intent  intent1=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        distanceFromMtbTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Constants.DISTINATION=Integer.parseInt(distanceFromMtbTxt.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            Constants.DISTINATION=Integer.parseInt(distanceFromMtbTxt.getText().toString());
            }
        });
        ringtoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                        "إختر نغمة تنبية");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                        RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                  Constants.ringtoneUri);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);

                startActivityForResult(intent,
                        RINGTONE_PICKER_REQUEST_CODE);
            }
        });



        if (Constants.isUseVibrate){
            aSwitch.setChecked(true);
        }
        if (Constants.isAlarmOn){
            stopAlarmSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(SettingsActivity.this, "تم تفعيل الأهتزاز", Toast.LENGTH_SHORT).show();
                }
                Constants.isUseVibrate=isChecked;
                Constants.savePreferences(getApplicationContext());
            }
        });
        stopAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //TODO : Check if ALarm is enabled or not
                if(isChecked){
                    Toast.makeText(SettingsActivity.this, "تم تفعيل منبه المطبات", Toast.LENGTH_SHORT).show();
                }
                Constants.isAlarmOn=isChecked;
                Constants.savePreferences(getApplicationContext());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RINGTONE_PICKER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Constants.ringtoneUri = data
                            .getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Constants.savePreferences(getApplicationContext());
                }
                break;
        }
    }

    void init(){
        distanceFromMtbTxt= (CustomEditText) findViewById(R.id.distanceFromMtbTxt);
        logut= (CustomButton) findViewById(R.id.logout);
        ringtoneBtn= (CustomButton) findViewById(R.id.ringtoneBtn);
        aSwitch= (Switch) findViewById(R.id.switchOnOffAlarm);
        stopAlarmSwitch= (Switch) findViewById(R.id.switchStopAlarm);
    }
}
