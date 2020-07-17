package sagar.com.sagarrayamajhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {
    TextView bankmanagement,make_transaction,reports;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nav;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView sett_ings;
    TextView txtBalance;
    DatabaseHelper bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        setUpToolbar();

        bd=new DatabaseHelper(this);

        bankmanagement=findViewById(R.id.bank_management);
        bankmanagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashBoardActivity.this,BankManagementActivity.class);
                startActivity(i);
            }
        });
        make_transaction=findViewById(R.id.make_transaction);
        make_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(DashBoardActivity.this,ViewTranscationActivity.class);
                startActivity(j);
            }
        });
        reports=findViewById(R.id.reports);
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k=new Intent(DashBoardActivity.this,ReportsActivity.class);
                startActivity(k);
            }
        });

        sett_ings=findViewById(R.id.sett_ing);
        sett_ings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this,SettingsActivity.class);
                ArrayList<UserInfo> list=bd.getUserList();
                for(UserInfo in:list) {
                    intent.putExtra("id", Integer.parseInt(in.getId()));
                }
                startActivity(intent);
            }
        });
        nav=findViewById(R.id.navigationView);
        showHeader();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.logout_nav:
                        SharedPreferences sp=getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent=new Intent(DashBoardActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                        default:
                            return true;
                }
                return false;

            }
        });


    }
    private void setUpToolbar(){
        mDrawer=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar_nav);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.app_name,R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }
    boolean twice;
    @Override
    public void onBackPressed() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice=false;
            }
        },2000);
        twice=true;
        if(twice==true) {
             Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }

    public void showHeader(){
        ArrayList<UserInfo>list=new ArrayList<>(bd.getUserList());
        for(int i=0;i<list.size();i++) {
            UserInfo infos=list.get(i);
            View navHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
            TextView txtName = navHeader.findViewById(R.id.txtName);
            TextView txtAddress = navHeader.findViewById(R.id.txtAddress);
            TextView txtBusiness = navHeader.findViewById(R.id.txtBusiness);
            txtBalance =navHeader.findViewById(R.id.txtBalance);
            txtName.setText(infos.getFirstname()+"  "+infos.getLastname());
            txtAddress.setText(infos.getAddress());
            txtBusiness.setText(infos.getBusiness());
            txtBalance.setText(bd.getTotalBalance());

            nav.addHeaderView(navHeader);
            }
        super.onResume();
        }
        @Override
        protected void onStart() {
        super.onStart();
        txtBalance.setText(bd.getTotalBalance());
    }
}
