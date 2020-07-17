package sagar.com.sagarrayamajhi;

import android.app.DatePickerDialog;
import android.graphics.Color;
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

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner addReports_spin;
    private int selectedIndex=0;
    EditText toTransaction,fromTransaction;
    DatePickerDialog datePickerDialog;
    ImageView reports_bacck;
    DatabaseHelper databaseHelper4;
    ArrayList<BankInfo> allbankinfo ;
    LinearLayout reportsContainer;
    Button reportsSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        reportsContainer=findViewById(R.id.reportsContainer);
        databaseHelper4=new DatabaseHelper(this);
        reports_bacck=findViewById(R.id.reports_back);
        reports_bacck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backreports();
            }
        });
        toTransaction=findViewById(R.id.totransaction_date);
        fromTransaction=findViewById(R.id.fromtransaction_date);
        addReports_spin = findViewById(R.id.addReports_spin);
        addReports_spin.setOnItemSelectedListener(this);
        List<BankInfo>bankNameList=databaseHelper4.getBankDataList();
        final String[] namelist3=new String[bankNameList.size()+1];
        namelist3[0]="All Banks";
        for(int i=0;i<bankNameList.size();i++){
            namelist3[i+1]=bankNameList.get(i).getBank_name();


        }

        fromTransaction.setText(databaseHelper4.getCurrentLastTenDays());
        toTransaction.setText(databaseHelper4.getCurrentDate());
        ArrayAdapter<String> arrayAdapterreport = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, namelist3) {
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
        arrayAdapterreport.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        addReports_spin.setAdapter(arrayAdapterreport);
        toTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int tYear=calendar.get(Calendar.YEAR);
                int tMonth=calendar.get(Calendar.MONTH);
                int tDay=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(ReportsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day =""+dayOfMonth;
                        String tTMonth=""+(month+1);
                        if(dayOfMonth<=9){
                            day ="0"+dayOfMonth;

                        }
                        if((month+1)<=9){
                            tTMonth="0"+(month+1);
                        }
                        toTransaction.setText(year + "-"
                                + tTMonth + "-" +day);

                    }
                },tYear,tMonth,tDay);
                datePickerDialog.show();
            }
        });
        fromTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int tYear=calendar.get(Calendar.YEAR);
                int tMonth=calendar.get(Calendar.MONTH);
                int tDay=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(ReportsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        fromTransaction.setText(year + "-"
                                + tTMonth + "-" +day);

                    }
                },tYear,tMonth,tDay);
                datePickerDialog.show();
            }
        });
        reportsSubmit=findViewById(R.id.reportsSubmit);
        reportsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat pattern=new SimpleDateFormat("yyyy-MM-dd");

                String toViewTranscation_datevalue=toTransaction.getText().toString();
                String fromViewTranscation_datevalue=fromTransaction.getText().toString();
                String viewTranscation_spinvalue=addReports_spin.getSelectedItem().toString();
                try {
                    Date date1=pattern.parse(String.valueOf(toViewTranscation_datevalue));
                    Date date2=pattern.parse(String.valueOf(fromViewTranscation_datevalue));
                    if(date1.compareTo(date2)>=0){
                        populateReports(viewTranscation_spinvalue,fromViewTranscation_datevalue,toViewTranscation_datevalue);
                    }
                    else {


                        Toast.makeText(ReportsActivity.this,"Enter valid date",Toast.LENGTH_LONG).show();
                        reportsContainer.removeAllViews();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });
        populateReports(addReports_spin.getSelectedItem().toString(),fromTransaction.getText().toString(),toTransaction.getText().toString());


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void backreports(){
        super.onBackPressed();
        finish();
    }
    public void populateReports(final String name1, final String from, final String to) {
        allbankinfo = new ArrayList<>(databaseHelper4.getBankDataList());
        String bnkId = "All Banks";
        if (name1 != "All Banks") {

            for (BankInfo binfo : allbankinfo) {

                String inputbank = binfo.getBank_name();
                if (inputbank.equals(name1)) {
                    bnkId = binfo.getId();


                }
            }
        }

        ArrayList<BankReports> list1 = databaseHelper4.getBankReports(bnkId, from, to);
        if (list1 != null || !list1.isEmpty()) {
            reportsContainer.removeAllViews();
            for (final BankReports info : list1) {
                View view = LayoutInflater.from(this).inflate(R.layout.reports_lay, null);
                ImageView reportsBankIcon = view.findViewById(R.id.reportsBankIcon);
                TextView reports_bankName = view.findViewById(R.id.reports_bankName);
                TextView reportsCredit = view.findViewById(R.id.reportsCredit);
                TextView reportsDebit = view.findViewById(R.id.reportsDebit);
                TextView reoprtsStatus = view.findViewById(R.id.reoprtsStatus);
                TextView balanceReports = view.findViewById(R.id.balanceReports);
                balanceReports.setText("Total Balance: "+info.getBalance());
                reports_bankName.setText(info.getBankName());
                if (info.getImage() != null) {
                    reportsBankIcon.setImageBitmap(ViewTranscationActivity.convertBytetoBitmap(info.getImage()));
                }
                 reportsCredit.setText("Cr: " + info.getCredit());
                reportsDebit.setText("Dr: " + info.getDebit());
                reoprtsStatus.setText("Status: " + info.getStatus());
                reportsContainer.addView(view);

            }
            databaseHelper4.close();
        }
    }


}
