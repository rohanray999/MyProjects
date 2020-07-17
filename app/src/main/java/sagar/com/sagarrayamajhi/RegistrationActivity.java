package sagar.com.sagarrayamajhi;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    EditText registerFirstName,registerLastName,registerUsername,registerBusiness,registerAddress,registerPassword,renterpassword1;
    Button rRegister;
    DatabaseHelper db1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db1=new DatabaseHelper(this);
        registerFirstName=findViewById(R.id.registerFirstName);
        registerLastName=findViewById(R.id.registerLastName);
        registerUsername=findViewById(R.id.registerUsername);
        registerBusiness=findViewById(R.id.registerBusiness);
        registerAddress=findViewById(R.id.registersAddress);
        registerPassword=findViewById(R.id.registerPassword);
        renterpassword1=findViewById(R.id.renterpassword1);
        sp=getSharedPreferences("Register",Context.MODE_PRIVATE);
        if(sp.getBoolean("Registered",false)){
            Intent intent=new Intent(RegistrationActivity.this,DashBoardActivity.class);
            startActivity(intent);
        }
        rRegister=findViewById(R.id.rRegister);
        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue=registerFirstName.getText().toString();
                String lastNameValue=registerLastName.getText().toString();
                String usernameValue=registerUsername.getText().toString();
                String businessValue=registerBusiness.getText().toString();
                String addressValue=registerAddress.getText().toString();
                String passwordValue=registerPassword.getText().toString();
                String renterPasswordValue=renterpassword1.getText().toString();
                if(isEmpty(registerFirstName)&&isEmpty(registerLastName)&&isEmpty(registerUsername)&&isEmpty(registerBusiness)&&isEmpty(registerAddress)&&isEmpty(registerPassword)&&isEmpty(renterpassword1)) {
                    if (passwordValue.equals(renterPasswordValue)) {
                        ContentValues cv=new ContentValues();
                        cv.put("firstname",firstNameValue);
                        cv.put("lastname",lastNameValue);
                        cv.put("business",businessValue);
                        cv.put("username",usernameValue);
                        cv.put("password",passwordValue);
                        cv.put("address",addressValue);
                        db1.addData(cv);
                        Toast.makeText(RegistrationActivity.this,"Welcome to Bank Assistant",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegistrationActivity.this, DashBoardActivity.class);
                        startActivity(i);
                        sp.edit().putBoolean("Registered", true).apply();

                    }
                    else{
                        Toast.makeText(RegistrationActivity.this,"Password do not match",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
