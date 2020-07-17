package sagar.com.sagarrayamajhi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name="Finance";
    static int version=1;


    String createUserTable1="CREATE TABLE if not exists `users1` (\n" + "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" + "\t`firstname`\tTEXT,\n" + "\t`lastname`\tTEXT,\n" + "\t`business`\tTEXT,\n" + "\t`username`\tTEXT,\n" + "\t`password`\tTEXT,\n" + "\t`address`\tTEXT\n" + ");";

    String createBankTable="CREATE TABLE if not exists `banks` (\n" + "\t`bankid`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + "\t`bank_name`\tvarchar(100) DEFAULT NULL,\n" + "\t`account_number`\tvarchar(100) DEFAULT NULL,\n" + "\t`image`\tBLOB DEFAULT NULL,\n" + "\t`account_type`\tvarchar(100) DEFAULT NULL,\n" + "\t`status`\tvarchar(30) DEFAULT NULL\n" + ");";


   String createTransactionTable="CREATE TABLE if not exists `bankaccount` (\n" + "\t`transactionid`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + "\t`date1`\tdate DEFAULT NULL,\n" + "\t`bankid`\tint(11) DEFAULT NULL,\n" + "\t`debit`\tdecimal(65,2) DEFAULT NULL,\n" + "\t`credit`\tdecimal(65,2) DEFAULT NULL,\n" + "\t`remarks`\tvarchar(255) DEFAULT NULL\n" + ");";
    public DatabaseHelper(@Nullable Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(createUserTable1);
        getWritableDatabase().execSQL(createBankTable);
        getWritableDatabase().execSQL(createTransactionTable);
    }


    public boolean isLoginSuccess(String username,String password){
        String sql = "Select count(*) from users1 where username='"+username+"'  and password='"+password+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l=statement.simpleQueryForLong();
        if(l>=1){
            return true;
        }
        else {
            return false;
        }

    }
    public void addData(ContentValues cv){
        getWritableDatabase().insert("users1","",cv);

    }
    public void updateUser(String id,ContentValues contentValues){
        getWritableDatabase().update("users1",contentValues,"id=+"+id,null);
    }
    public long insertBankData(ContentValues contentValues){
        long currentID =getWritableDatabase().insert("banks","",contentValues);
        return currentID;
    }


    public long insertBankAccount(ContentValues contentValues){
       long id= getWritableDatabase().insert("bankaccount","",contentValues);
       return id;
    }
    public void updateBankData(String bankid,ContentValues contentValues)
    {
        getWritableDatabase().update("banks", contentValues, "bankid=" + bankid, null);
    }
    public void updateBankAccountData(String transactionid,ContentValues cv){
        getWritableDatabase().update("bankaccount",cv,"transactionid=" +transactionid,null);

    }
    public void deleteBankData(String bankid){
        getWritableDatabase().delete("banks","bankid=" +bankid,null);
    }
    public void deleteBankAccountData(String transactionid){
        getWritableDatabase().delete("bankaccount","transactionid=" +transactionid,null);
    }
    public ArrayList<BankInfo> getBankDataList(){
        String sql="SELECT banks.bankid bankid,SUM(bankaccount.credit)-SUM(bankaccount.debit) as balance,banks.bank_name,bankaccount.transactionid,banks.account_type,banks.image,banks.status,banks.account_number FROM banks,bankaccount where banks.bankid=bankaccount.bankid Group by banks.bankid";

        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<BankInfo>list=new ArrayList<>();
        while (c.moveToNext()){
            BankInfo info=new BankInfo();
            info.setTransactionid(c.getString(c.getColumnIndex("transactionid")));
            info.setId(c.getString(c.getColumnIndex("bankid")));
            info.setBank_name(c.getString(c.getColumnIndex("bank_name")));
            info.setAccount_number(c.getString(c.getColumnIndex("account_number")));
            info.setAccount_type(c.getString(c.getColumnIndex("account_type")));
            info.setImage(c.getBlob(c.getColumnIndex("image")));
            info.setBalance(c.getString(c.getColumnIndex("balance")));
            info.setStatus(c.getString(c.getColumnIndex("status")));
            list.add(info);

        }
        c.close();

        return list;

        }

        public ArrayList<BankInfo>getBankAccountList(String bankid,String startDate,String endDate){
        String sql1="";

      if(bankid=="All Banks") {

            sql1 = "Select * from bankaccount inner join banks on banks.bankid =bankaccount.bankid where date1>='" + startDate  + "'and date1<='" +endDate +"'";

}
       else{
            sql1 = "Select * from bankaccount inner join banks on banks.bankid =bankaccount.bankid where date1>='" + startDate + "' and date1<='" + endDate+"' and banks.bankid="+bankid;
        }

            Cursor c1=getReadableDatabase().rawQuery(sql1,null);
            ArrayList<BankInfo>list1=new ArrayList<>();
                while (c1.moveToNext()) {
                    BankInfo info1 = new BankInfo();

                    info1.setTransactionid(c1.getString(c1.getColumnIndex("transactionid")));
                    info1.setId(c1.getString(c1.getColumnIndex("bankid")));
                    info1.setDate1(c1.getString(c1.getColumnIndex("date1")));
                    info1.setDebit(c1.getString(c1.getColumnIndex("debit")));
                    info1.setCredit(c1.getString(c1.getColumnIndex("credit")));
                    info1.setRemarks(c1.getString(c1.getColumnIndex("remarks")));
                    info1.setBank_name(c1.getString(c1.getColumnIndex("bank_name")));
                    info1.setImage(c1.getBlob(c1.getColumnIndex("image")));
                    info1.setStatus(c1.getString(c1.getColumnIndex("status")));
                    list1.add(info1);
                }

            Log.i("dbhelper",list1.size()+"");
            c1.close();
            return list1;

        }


    public ArrayList<BankReports>getBankReports(String bankid,String startDate,String endDate){
        String sql1="";
        if(bankid=="All Banks") {

            sql1 = "SELECT SUM(bankaccount.credit)-SUM(bankaccount.debit) as balance,SUM(bankaccount.credit) as credit, SUM(bankaccount.debit) as debit,banks.bank_name,banks.account_type,banks.image,banks.status FROM bankaccount,banks WHERE banks.bankid=bankaccount.bankid AND bankaccount.date1 BETWEEN'"+startDate +"'AND'"+endDate+ "' GROUP BY bankaccount.bankid";
        }
        else{
            sql1 = "SELECT SUM(bankaccount.credit)-SUM(bankaccount.debit) as balance,SUM(bankaccount.credit) as credit, SUM(bankaccount.debit) as debit,banks.bank_name,banks.account_type,banks.image,banks.status FROM bankaccount,banks WHERE banks.bankid=bankaccount.bankid AND bankaccount.bankid='"+bankid+"' AND bankaccount.date1 BETWEEN'"+startDate+"' AND '"+endDate+"'";
        }
        Cursor c1=getReadableDatabase().rawQuery(sql1,null);

     ArrayList<BankReports>list1=new ArrayList<>();
       while(c1.moveToNext()) {
           BankReports bR = new BankReports();
            bR.setBalance(c1.getString(c1.getColumnIndex("balance")));
            bR.setAccountType(c1.getString(c1.getColumnIndex("account_type")));
           bR.setBankName(c1.getString(c1.getColumnIndex("bank_name")));
           bR.setCredit(c1.getString(c1.getColumnIndex("credit")));
           bR.setDebit(c1.getString(c1.getColumnIndex("debit")));
           bR.setImage(c1.getBlob(c1.getColumnIndex("image")));
           bR.setStatus(c1.getString(c1.getColumnIndex("status")));
            list1.add(bR);
        }

        Log.i("dbhelper",list1.size()+"");
        c1.close();
        return list1;

    }
    public ArrayList<UserInfo> getUserList(){
        String sql="Select * from users1";


        Cursor c=getReadableDatabase().rawQuery(sql,null);
        ArrayList<UserInfo>list=new ArrayList<>();
        while (c.moveToNext()){
            UserInfo info=new UserInfo();
            info.setId(c.getString(c.getColumnIndex("id")));
            info.setFirstname(c.getString(c.getColumnIndex("firstname")));
            info.setLastname(c.getString(c.getColumnIndex("lastname")));
            info.setBusiness(c.getString(c.getColumnIndex("business")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setUsername(c.getString(c.getColumnIndex("username")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            list.add(info);

        }
        c.close();

        return list;

    }

    public BankInfo getBankInfo1(String bankid) {
        String sql = "select * from banks where bankid= " + bankid;

        Cursor c = getReadableDatabase().rawQuery(sql, null);


        BankInfo info1 = new BankInfo();
        while (c.moveToNext()) {
            info1.id = c.getString(c.getColumnIndex("bankid"));
            info1.bank_name = (c.getString(c.getColumnIndex("bank_name")));
            info1.account_number=(c.getString(c.getColumnIndex("account_number")));
            info1.account_type=(c.getString(c.getColumnIndex("account_type")));
            info1.status=(c.getString(c.getColumnIndex("status")));
            info1.image =  (c.getBlob(c.getColumnIndex("image")));
        }
        c.close();

        return info1;

    }
    public BankInfo getBankAccountinfo1(String transactionid) {
        String sql3 = "select * from bankaccount inner join banks on banks.bankid =bankaccount.bankid where transactionid=" + transactionid;

        Cursor c2 = getReadableDatabase().rawQuery(sql3, null);
        BankInfo info1=new BankInfo();
        while(c2.moveToNext()){
            info1.transactionid= c2.getString(c2.getColumnIndex("transactionid"));
            info1.date1= c2.getString(c2.getColumnIndex("date1"));
            info1.id=c2.getString(c2.getColumnIndex("bankid"));
            info1.debit=c2.getString(c2.getColumnIndex("debit"));
            info1.credit=c2.getString(c2.getColumnIndex("credit"));
            info1.remarks=c2.getString(c2.getColumnIndex("remarks"));
            info1.bank_name=c2.getString(c2.getColumnIndex("bank_name"));
        }


        c2.close();

        return info1;

    }
    public UserInfo getUserInfo(String id){
        String sql4="select * from users1 where id="+id;
        Cursor c12=getReadableDatabase().rawQuery(sql4,null);
        UserInfo userInfo=new UserInfo();
        while(c12.moveToNext()){
            userInfo.id=c12.getString(c12.getColumnIndex("id"));
            userInfo.firstname=c12.getString(c12.getColumnIndex("firstname"));
            userInfo.lastname=c12.getString(c12.getColumnIndex("lastname"));
            userInfo.username=c12.getString(c12.getColumnIndex("username"));
            userInfo.password=c12.getString(c12.getColumnIndex("password"));
            userInfo.business=c12.getString(c12.getColumnIndex("business"));
            userInfo.address=c12.getString(c12.getColumnIndex("address"));

        }
        c12.close();
        return userInfo;
    }
    public String getTotalBalance(){
      String totv="";
      String sql5="select sum(credit)-sum(debit) as total from bankaccount";
      Cursor c13=getReadableDatabase().rawQuery(sql5,null);
      c13.moveToNext();
      String colName=c13.getColumnName(0);
      totv=c13.getString(c13.getColumnIndex(colName));
      c13.close();
      return totv;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getCurrentDate(){
        Calendar calendar=Calendar.getInstance();
        int tYear=calendar.get(Calendar.YEAR);
        int tMonth=calendar.get(Calendar.MONTH)+1;
        int tDay=calendar.get(Calendar.DAY_OF_MONTH);
        String trpp=""+tDay;
        if(tDay<=9) {
        trpp="0"+tDay;
        }

        String currentdate = tYear+"-"+tMonth+"-"+trpp;

       return currentdate;
    }
    public static String getCurrentLastTenDays(){
        String sDate =getCurrentDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DATE, -10);
        String yesterdayAsString = dateFormat.format(calendar1.getTime());
        return yesterdayAsString;
    }
}
