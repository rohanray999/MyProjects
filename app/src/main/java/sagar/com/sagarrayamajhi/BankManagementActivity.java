package sagar.com.sagarrayamajhi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BankManagementActivity extends AppCompatActivity {
    ImageView back_bank,addBank;
    LinearLayout container;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_management);
        container=findViewById(R.id.container);
        databaseHelper=new DatabaseHelper(this);
        databaseHelper.getBankDataList();
        populateData();
        addBank=findViewById(R.id.addBank);
        addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BankManagementActivity.this,AddBankActivity.class);
                startActivity(intent);
            }
        });
        back_bank=findViewById(R.id.back_bankmgmt);
        back_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackpressed();
            }
        });
        }
    public void onbackpressed(){
        super.onBackPressed();
        finish();
    }
    protected void onResume(){
        super.onResume();
        populateData();
    }
   public void populateData(){
        ArrayList<BankInfo>list=new ArrayList<>(databaseHelper.getBankDataList());
        container.removeAllViews();
       for (int i=0;i<list.size();i++){

            final BankInfo info=list.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_bank_list_view, null);
            ImageView bankImage=view.findViewById(R.id.bankImage);
            TextView bankname1=view.findViewById(R.id.bankname1);
            TextView totalBalance=view.findViewById(R.id.totalBalance);
            TextView accNumber=view.findViewById(R.id.accNumber);
            TextView statusBank=view.findViewById(R.id.statusBank);
            TextView delete=view.findViewById(R.id.delete1);
            TextView edit=view.findViewById(R.id.edit1);
           bankname1.setText(info.getBank_name()+"  ("+info.getAccount_type()+")");
           statusBank.setText("Status: "+info.getStatus());
           accNumber.setText("Acc No: "+info.getAccount_number());
           totalBalance.setText("Balance: "+info.getBalance());
           if(info.getImage()!=null) {
               bankImage.setImageBitmap(convertBytetoBitmap(info.getImage()));
           }
           edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("edit click", "testing"+info.getId());
                    Intent intent =new Intent(BankManagementActivity.this,AddBankActivity.class);
                    Log.i("edit click", "testing"+info.getId());

                    intent.putExtra("bankid",Integer.parseInt(info.getId()));
                    startActivity(intent);
                }
            });
           delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   AlertDialog.Builder dialog=new AlertDialog.Builder(BankManagementActivity.this);
                   dialog.setTitle("Delete User!");
                   dialog.setMessage("Are You Sure?");
                   dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           databaseHelper.deleteBankData(info.id);
                           databaseHelper.deleteBankAccountData(info.transactionid);
                           Toast.makeText(BankManagementActivity.this,"Deleted",Toast.LENGTH_LONG).show();
                           onResume();

                       }
                   });
                   dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   dialog.show();
                   }
           });
           container.addView(view);


        }

    }


   public static Bitmap convertBytetoBitmap(byte[] byteArray){
       return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

   }

}
