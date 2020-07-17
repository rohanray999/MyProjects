package sagar.com.sagarrayamajhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText login_username,login_password;
    Button loginButton;
    SharedPreferences sp;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper=new DatabaseHelper(this);
        login_username=findViewById(R.id.login_username);
        login_password=findViewById(R.id.login_password);
        loginButton=findViewById(R.id.loginButton);
        sp=getSharedPreferences("Login",Context.MODE_PRIVATE);
        if(sp.getBoolean("Logged",false)){
            Intent intent=new Intent(LoginActivity.this,DashBoardActivity.class);
            startActivity(intent);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue=login_username.getText().toString();
                String passwordValue=login_password.getText().toString();
                if(isEmpty(login_username)&&isEmpty(login_password)) {
                    if (databaseHelper.isLoginSuccess(usernameValue, passwordValue)) {
                        Toast.makeText(LoginActivity.this, "Sucessfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        sp.edit().putBoolean("Logged", true).apply();
                    } else {
                        Toast.makeText(LoginActivity.this, "Username and Password don't match", Toast.LENGTH_SHORT).show();
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
