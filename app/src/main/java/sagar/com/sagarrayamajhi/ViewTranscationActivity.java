package sagar.com.sagarrayamajhi;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewTranscationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner viewTranscation_spin;
    EditText toViewTranscation_date,fromViewTranscation_date;
    private int selectedIndex=0;
    DatePickerDialog datePickerDialog2;
    ImageView viewTranscation_back,viewTranscation_add;
    DatabaseHelper databaseHelper3;
    LinearLayout contentair;
    ArrayList<BankInfo> allbankinfo ;
    Button viewTransButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transcation);

        contentair=findViewById(R.id.contentair);
        databaseHelper3=new DatabaseHelper(this);

        viewTranscation_spin=findViewById(R.id.viewTranscation_spin);
        viewTranscation_spin.setOnItemSelectedListener(this);


        viewTranscation_back=findViewById(R.id.viewTranscation_back);
        viewTranscation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpressing();
            }
        });

        viewTranscation_add=findViewById(R.id.viewTranscation_add);
        viewTranscation_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewTranscationActivity.this,AddTransactionActivity.class);
                startActivity(intent);
            }
        });

        toViewTranscation_date=findViewById(R.id.toViewTransaction_date);

        fromViewTranscation_date=findViewById(R.id.fromViewTransaction_date);
        viewTransButton=findViewById(R.id.viewTransButton);
        viewTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat pattern=new SimpleDateFormat("yyyy-MM-dd");

               String toViewTranscation_datevalue=toViewTranscation_date.getText().toString();
                String fromViewTranscation_datevalue=fromViewTranscation_date.getText().toString();
                String viewTranscation_spinvalue=viewTranscation_spin.getSelectedItem().toString();
                try {
                    Date date1=pattern.parse(String.valueOf(toViewTranscation_datevalue));
                    Date date2=pattern.parse(String.valueOf(fromViewTranscation_datevalue));
                    if(date1.compareTo(date2)>=0) {
                        prepareData(viewTranscation_spinvalue, fromViewTranscation_datevalue, toViewTranscation_datevalue);
                    }
                    else
                    {
                        Toast.makeText(ViewTranscationActivity.this,"Enter valid date",Toast.LENGTH_LONG).show();
                        contentair.removeAllViews();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        List<BankInfo>bankNameList=databaseHelper3.getBankDataList();
        final String[] namelist2=new String[bankNameList.size()+1];
        namelist2[0]="All Banks";
        for(int i=0;i<bankNameList.size();i++){
            namelist2[i+1]=bankNameList.get(i).getBank_name();
            }
        toViewTranscation_date.setText(databaseHelper3.getCurrentDate());
        fromViewTranscation_date.setText(databaseHelper3.getCurrentLastTenDays());
        ArrayAdapter<String> arrayAdapterViewTransaction = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,namelist2) {
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
        arrayAdapterViewTransaction.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        viewTranscation_spin.setAdapter(arrayAdapterViewTransaction);
        toViewTranscation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                final int tYear=calendar.get(Calendar.YEAR);
                final int tMonth=calendar.get(Calendar.MONTH);
                final int tDay=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog2=new DatePickerDialog(ViewTranscationActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        toViewTranscation_date.setText(year + "-"
                                + tTMonth + "-" +day);

                    }
                },tYear,tMonth,tDay);
                datePickerDialog2.show();
            }
        });
        fromViewTranscation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int tYear=calendar.get(Calendar.YEAR);
                int tMonth=calendar.get(Calendar.MONTH);
                int tDay=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog2=new DatePickerDialog(ViewTranscationActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        fromViewTranscation_date.setText(year + "-"
                                + tTMonth + "-" +day);

                    }
                },tYear,tMonth,tDay);
                datePickerDialog2.show();
            }
        });
        prepareData("All Banks",databaseHelper3.getCurrentLastTenDays(),databaseHelper3.getCurrentDate());
        }
        @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void backpressing(){
        super.onBackPressed();
        finish();
    }
    protected void onResume(String name,String fro,String to){
        super.onResume();
        prepareData(name,fro,to);
    }


    public void prepareData(final String name1, final String from, final String to){

        allbankinfo = new ArrayList<>(databaseHelper3.getBankDataList());
                 String bnkId ="All Banks";
                 if( name1!="All Banks"){

             for(BankInfo binfo: allbankinfo){

                 String inputbank = binfo.getBank_name();
                 if( inputbank.equals(name1)){
                     bnkId = binfo.getId();


                 }
        }}
        DatabaseHelper  dbhelper = new DatabaseHelper(this);
            ArrayList<BankInfo> list1 = dbhelper.getBankAccountList(bnkId, from, to);
                if (list1!=null || !list1.isEmpty()){
                    contentair.removeAllViews();
                    for (final BankInfo info :list1) {
                 View view = LayoutInflater.from(this).inflate(R.layout.transaction_list, null);
                 ImageView bankIcon = view.findViewById(R.id.bankIcon);
                 TextView bankNameTrans = view.findViewById(R.id.banknameTrans);
                 TextView dateTrans = view.findViewById(R.id.dateTrans);
                 TextView credit = view.findViewById(R.id.credit);
                 TextView debit = view.findViewById(R.id.debit);
                 TextView remakrs = view.findViewById(R.id.thisRemarks);
                 TextView viewEdit=view.findViewById(R.id.viewEdit);
                 TextView viewDel=view.findViewById(R.id.transDelete);
                 bankNameTrans.setText(info.getBank_name());
                 if (info.getImage() != null) {
                     bankIcon.setImageBitmap(convertBytetoBitmap(info.getImage()));
                 }
                 dateTrans.setText(info.getDate1());
                 credit.setText("Cr: " + info.getCredit());
                 debit.setText("Dr: " + info.getDebit());
                 remakrs.setText(info.getRemarks());
                 viewEdit.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent =new Intent(ViewTranscationActivity.this,AddTransactionActivity.class);
                         intent.putExtra("transactionid",Integer.parseInt(info.getTransactionid()));
                         intent.putExtra("bankid",Integer.parseInt(info.getId()));
                         startActivity(intent);
                     }
                 });
                 viewDel.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         AlertDialog.Builder dialog=new AlertDialog.Builder(ViewTranscationActivity.this);
                         dialog.setTitle("Delete User!");
                         dialog.setMessage("Are You Sure?");
                         dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 databaseHelper3.deleteBankAccountData(info.transactionid);
                                 Toast.makeText(ViewTranscationActivity.this,"Deleted",Toast.LENGTH_LONG).show();
                                 onResume(name1, from, to);


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
                 contentair.addView(view);
             }
             dbhelper.close();

             }
             }
    public static Bitmap convertBytetoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    }
}
