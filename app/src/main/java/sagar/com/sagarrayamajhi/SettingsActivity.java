package sagar.com.sagarrayamajhi;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    ImageView back_settings,home_settings;
    EditText settingFirstName,settingLastName,business,addressSetting,passwordSetting,reenterpassword;
    Button submitSetting;
    DatabaseHelper dbuser;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        id=getIntent().getIntExtra("id",0);
        dbuser=new DatabaseHelper(this);
        home_settings=findViewById(R.id.home_settings);
        home_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });


        back_settings=findViewById(R.id.back_settings);
        back_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bcak();
            }
        });

        settingFirstName=findViewById(R.id.settingFirstName);
        settingLastName=findViewById(R.id.SettingLastName);
        business=findViewById(R.id.business);
        addressSetting=findViewById(R.id.addressSetting);
        passwordSetting=findViewById(R.id.passwordSetting);
        reenterpassword=findViewById(R.id.renterpassword);
        submitSetting=findViewById(R.id.settingSubmit);
        if(id!=0) {
            UserInfo inf = dbuser.getUserInfo(id+"");
            settingFirstName.setText(inf.getFirstname());
            settingLastName.setText(inf.getLastname());
            business.setText(inf.business);
            addressSetting.setText(inf.address);
        }

        submitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordSettingValue=passwordSetting.getText().toString();
                String reenterpasswordValue=reenterpassword.getText().toString();
                if(isEmpty(passwordSetting)&&isEmpty(reenterpassword)) {
                    if (passwordSettingValue.equals(reenterpasswordValue)) {
                        ContentValues cv = new ContentValues();
                        cv.put("password", passwordSettingValue);
                        dbuser.updateUser(id + "", cv);
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SettingsActivity.this,"Password do not match ",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
    public void bcak()
    {
        super.onBackPressed();
        finish();

    }
    public boolean isEmpty(EditText check){
        if(check.getText().toString().length()>0){
            return true;
        }
        else{
            check.setError("Enter the value");
            return false;
        }
    }
}
