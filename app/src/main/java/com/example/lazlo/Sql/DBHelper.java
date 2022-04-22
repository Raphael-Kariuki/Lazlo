package com.example.lazlo.Sql;

/*added code*/
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userDetails ", null);
        return cursor;
    }
}
