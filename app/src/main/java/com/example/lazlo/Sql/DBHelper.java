package com.example.lazlo.Sql;

/*added code*/
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, "UserData", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB){
        DB.execSQL("create Table userDetails(userName TEXT primary key,email TEXT, password PASSWORD )");
    }
    public void onUpgrade(SQLiteDatabase DB, int i, int i1){
        DB.execSQL("drop Table if exists userDetails");
        //recreate the db
        onCreate(DB);
    }
    public boolean insertUserData(String userName, String email, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = DB.insert("userDetails", null, contentValues);
        DB.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<HashMap<String, String>> getUserData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        ArrayList<HashMap<String,String>> userList = new ArrayList<>();
        Cursor cursor = DB.rawQuery("Select userName, email, password from userDetails ", null);
        while(cursor.moveToNext()){
            HashMap<String, String> user = new HashMap<>();
            user.put("username",cursor.getString(0));
            user.put("email", cursor.getString(1));
            user.put("password", cursor.getString(2));
            userList.add(user);
                    }
        cursor.close();
        return userList;
    }
    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails ",null);
        //cursor.close();
        return cursor;
    }
}
