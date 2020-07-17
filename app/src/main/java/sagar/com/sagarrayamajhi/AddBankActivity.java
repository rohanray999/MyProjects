package sagar.com.sagarrayamajhi;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddBankActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner getGetAddBank_spin_status;
    static Spinner getAddBank_spin_accountType;
    private int selectedIndex=0;
    ImageView back_addbank,home_addBank;
    EditText bankName,accountNumber,openingBalance;
    TextView browse;
    TextView addBank_title;
    Button submitBank;
    ImageView browseImage;
    DatabaseHelper databaseHelper1;
    final int REQUEST_CODE_GALLERY = 999;
    int bankid;
    ArrayList<BankInfo>infort=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        databaseHelper1=new DatabaseHelper(this);
        browseImage=findViewById(R.id.browseImage);
        addBank_title=findViewById(R.id.addBank_title);

        bankid=getIntent().getIntExtra("bankid",0);
        Log.i("AddBankActivity",bankid+"");
        browse=findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddBankActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        bankName=findViewById(R.id.bankName);
        accountNumber=findViewById(R.id.accountNumber);
        openingBalance=findViewById(R.id.openingBalance);
        openingBalance.setFilters(new InputFilter[]{
                new DecimalDigitsInputFilter(10,2)

        });
        getAddBank_spin_accountType = findViewById(R.id.addBank_spin_accountType);
        getAddBank_spin_accountType.setOnItemSelectedListener(this);
        getGetAddBank_spin_status=findViewById(R.id.addBank_spin_status1);
        getGetAddBank_spin_status.setOnItemSelectedListener(this);
        submitBank=findViewById(R.id.submitBank);
        submitBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String banknameValue = bankName.getText().toString();
                    String accountnumberValue = accountNumber.getText().toString();
                    String spin_account = getAddBank_spin_accountType.getSelectedItem().toString();
                    String spin_status = getGetAddBank_spin_status.getSelectedItem().toString();
                    String openingbalanceValue = openingBalance.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("bank_name", banknameValue);
                        contentValues.put("account_number", accountnumberValue);
                        contentValues.put("account_type", spin_account);
                        contentValues.put("status", spin_status);
                        contentValues.put("image", imageViewToByte(browseImage));

                        if (bankid == 0) {
                            if (isEmpty(bankName) && isEmpty(accountNumber) && isEmpty(openingBalance)) {
                                long id = databaseHelper1.insertBankData(contentValues);
                                ArrayList<BankInfo> in = new ArrayList<>(databaseHelper1.getBankDataList());

                                ContentValues contentValues1 = new ContentValues();
                                contentValues1.put("remarks", "Opening Balance");
                                contentValues1.put("credit", openingbalanceValue);
                                contentValues1.put("debit", 0);
                                contentValues1.put("date1", databaseHelper1.getCurrentDate());
                                contentValues1.put("bankid", id + "");
                                databaseHelper1.insertBankAccount(contentValues1);
                                Toast.makeText(AddBankActivity.this, "Sucessfully Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddBankActivity.this, BankManagementActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            databaseHelper1.updateBankData(bankid + "", contentValues);
                            Toast.makeText(AddBankActivity.this, "Update Done", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddBankActivity.this, BankManagementActivity.class);
                            startActivity(intent);
                            }

                        bankName.setText("");
                        accountNumber.setText("");
                        openingBalance.setText("");
                        browseImage.setImageResource(R.drawable.browse);
                        getAddBank_spin_accountType.setSelection(0);
                        getGetAddBank_spin_status.setSelection(0);



                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        back_addbank=findViewById(R.id.back_addBank);
        back_addbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpressed();
            }
        });
        home_addBank=findViewById(R.id.home_addBank);
        home_addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddBankActivity.this,DashBoardActivity.class);
                startActivity(intent);

            }
        });

        List<String> accountTypeName = new ArrayList<>();
        accountTypeName.add("Saving");
        accountTypeName.add("Fixed");
        accountTypeName.add("Current");
        List<String> statusList = new ArrayList<>();
        statusList.add("Active");
        statusList.add("Matured");
        statusList.add("Closed");

        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, accountTypeName){
            public View getView(int position,View convertView,ViewGroup parent){
                TextView tv=(TextView)super.getView(position,convertView,parent);
                tv.setTextColor(Color.GRAY);
                return tv;
            }

            @Override
            public  View getDropDownView(int position,View convertView,ViewGroup parent){
                TextView tv=(TextView)super.getDropDownView(position,convertView,parent);
                tv.setTextColor(Color.DKGRAY);
                if(position==selectedIndex){
                    tv.setTextColor(Color.GRAY);
                }
                return tv;
            }
    };
        arrayAdapter5.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        getAddBank_spin_accountType.setAdapter(arrayAdapter5);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, statusList) {
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
        arrayAdapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        getGetAddBank_spin_status.setAdapter(arrayAdapter3);
        if(bankid!=0)
        {
            BankInfo infor=databaseHelper1.getBankInfo1(bankid+"");
            addBank_title.setText("Edit Bank");
            openingBalance.setVisibility(View.INVISIBLE);
            bankName.setText(infor.getBank_name());
            accountNumber.setText(infor.getAccount_number());
            browseImage.setImageBitmap(BankManagementActivity.convertBytetoBitmap(infor.getImage()));
            if(infor.getAccount_type().equals("Fixed")){
                Log.i("asdfg",infor.getAccount_type().equals("Fixed")+" ");
                getAddBank_spin_accountType.setSelection(1);

            }
            else if(infor.getAccount_type().equals("Current")){
                getAddBank_spin_accountType.setSelection(2);
            }
            else {
                getAddBank_spin_accountType.setSelection(0);
            }

            if(infor.getStatus().equals("Matured")){
                getGetAddBank_spin_status.setSelection(1);
            }
            else if(infor.getStatus().equals("Closed")){
                getGetAddBank_spin_status.setSelection(2);
            }
            else {
                getGetAddBank_spin_status.setSelection(0);
            }

        }

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                browseImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void backpressed(){
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
