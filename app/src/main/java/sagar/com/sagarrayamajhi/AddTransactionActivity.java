package sagar.com.sagarrayamajhi;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner addTrans_spin,getAddTrans_spin_transType;
    private int selectedIndex=0;
    EditText datepicking,remarks,amount;
    DatePickerDialog datePickerDialog;
    ImageView addTrans_back,addTrans_home;
    DatabaseHelper databaseHelper2;
    Button transSubmit;
    ArrayList<BankInfo> info = new ArrayList<>();
    int transactionid,bankid;
    TextView addTransaction_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        databaseHelper2=new DatabaseHelper(this);
        transactionid=getIntent().getIntExtra("transactionid",0);
        bankid=getIntent().getIntExtra("bankid",0);
        remarks=findViewById(R.id.remarks);
        amount=findViewById(R.id.amount);
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10,2)});
        transSubmit=findViewById(R.id.transSubmit);
        addTransaction_title=findViewById(R.id.adddTransaction_title);

        addTrans_back=findViewById(R.id.addtrans_back);
        addTrans_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        addTrans_home=findViewById(R.id.addTrans_home);
        addTrans_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddTransactionActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });

        addTrans_spin = findViewById(R.id.addTrans_spin);
        addTrans_spin.setOnItemSelectedListener(this);
        getAddTrans_spin_transType = findViewById(R.id.addTrans_spin_transType);
        getAddTrans_spin_transType.setOnItemSelectedListener(this);
        List<String> transcationTransactionTypoe = new ArrayList<>();
        transcationTransactionTypoe.add("Deposit");
        transcationTransactionTypoe.add("Withdrawal");
        List<BankInfo>bankNameList=databaseHelper2.getBankDataList();
        final String[] namelist1=new String[bankNameList.size()];
        for(int i=0;i<bankNameList.size();i++){
            BankInfo inf= new BankInfo();
            inf.setId(bankNameList.get(i).getId());
            inf.setBank_name(bankNameList.get(i).getBank_name());
            namelist1[i]=bankNameList.get(i).getBank_name();
            info.add(inf);


        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,namelist1) {
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.GRAY);
                return tv;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(Color.DKGRAY);
                if (position == selectedIndex) {
                    tv.setTextColor(Color.GRAY);
                }
                return tv;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        addTrans_spin.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,transcationTransactionTypoe) {
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.GRAY);
                return tv;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(Color.DKGRAY);
                if (position == selectedIndex) {
                    tv.setTextColor(Color.GRAY);
                }
                return tv;
            }
        };
        arrayAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        getAddTrans_spin_transType.setAdapter(arrayAdapter1);


        datepicking=findViewById(R.id.transaction_date);
        datepicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int tYear=calendar.get(Calendar.YEAR);
                int tMonth=calendar.get(Calendar.MONTH);
                int tDay=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day =""+dayOfMonth;
                        String tTMonth=""+(month+1);
                        if(dayOfMonth<=9){
                            day ="0"+dayOfMonth;

                        }
                        if ((month+1)<=9){
                            tTMonth="0"+(month+1);
                        }
                        datepicking.setText(year + "-"
                                + tTMonth + "-" +day);

                    }
                },tYear,tMonth,tDay);
                datePickerDialog.show();
            }
        });
        if(transactionid!=0)
        {
            BankInfo infor=databaseHelper2.getBankAccountinfo1(transactionid+"");
            info=databaseHelper2.getBankDataList();
            addTransaction_title.setText("Edit Transaction");
            for(BankInfo in:info){
                if(in.getId().equals(infor.getId())){
                    Log.i("index",info.indexOf(in)+"");
                    addTrans_spin.setSelection(info.indexOf(in));
                }
            }

            datepicking.setText(infor.getDate1());
            if(infor.getDebit().equals("0")){
                getAddTrans_spin_transType.setSelection(0);
                amount.setText(infor.getCredit());


            }
            else
            {
                getAddTrans_spin_transType.setSelection(1);
                amount.setText(infor.getDebit());

            }


            remarks.setText(infor.getRemarks());
        }
        transSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String amountValue=amount.getText().toString();
                    String transcationtypeValue=getAddTrans_spin_transType.getSelectedItem().toString();
                    String transbanknameValue=addTrans_spin.getSelectedItem().toString();
                    String remarksValue=remarks.getText().toString();
                    String dateValue=datepicking.getText().toString();
                    if(isEmpty(datepicking)&&isEmpty(amount)&&isEmpty(remarks)) {
                        ContentValues cv = new ContentValues();
                        //Need to pass id value for each transaction
                        for (BankInfo in : info) {
                            if (in.getBank_name().equals(transbanknameValue)) {
                                cv.put("bankid", in.getId());
                            }
                        }


                        cv.put("date1", dateValue);
                        cv.put("remarks", remarksValue);

                        if (transcationtypeValue.equals("Withdrawal")) {
                            cv.put("debit", amountValue);
                            cv.put("credit", "0");

                        } else {
                            cv.put("credit", amountValue);
                            cv.put("debit", "0");
                        }

                        if (transactionid == 0) {
                            long id = databaseHelper2.insertBankAccount(cv);
                            //databaseHelper2.insertBankAccount(cv);
                            if (id != -1) {
                                Toast.makeText(AddTransactionActivity.this, "Bank added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddTransactionActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            databaseHelper2.updateBankAccountData(transactionid + "", cv);
                            Toast.makeText(AddTransactionActivity.this, "Bank details updated successfully", Toast.LENGTH_SHORT).show();

                        }
                        Intent intent = new Intent(AddTransactionActivity.this, ViewTranscationActivity.class);
                        startActivity(intent);


                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void back(){
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
