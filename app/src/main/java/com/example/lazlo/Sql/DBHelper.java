package com.example.lazlo.Sql;

/*added code*/
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    //create database
    public DBHelper(Context context){
        super(context, "UserData", null, 1);
    }
    //method executed on app creation
    @Override
    public void onCreate(SQLiteDatabase DB){
        DB.execSQL("create Table TaskList(UserName TEXT NOT NULL,TaskTitle TEXT NOT NULL,TaskDescription TEXT NOT NULL)");
        DB.execSQL("create Table userDetails(userName TEXT primary key,email TEXT, password PASSWORD )");

    }

    //method run when there's a db upgrade
    public void onUpgrade(SQLiteDatabase DB, int i, int i1){
        DB.execSQL("drop Table if exists userDetails");
        DB.execSQL("drop Table if exists TaskList");
        //recreate the db
        onCreate(DB);
    }
    //Method to insert userdata on sign up, returning true if successful and otherwise
    //executed on signup.java
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
    //method to insert task, executed on addtasks.java
    public boolean insertTasks( String userName, String taskTitle, String taskDescription){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskTitle", taskTitle);
        contentValues.put("TaskDescription", taskDescription);
        contentValues.put("UserName", userName);
        long result = DB.insert("TaskList", null, contentValues);
        DB.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    //create an array from userdata details from db to be used in populating a listview
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
    //method to obtain login credentials which are checked on login
    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails ",null);
        //cursor.close();
        return cursor;
    }
    //method to create an array of user tasks
    public ArrayList<HashMap<String, String>> getTasks(String userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        ArrayList<HashMap<String,String>> tasksList = new ArrayList<>();
        Cursor cursor = DB.rawQuery("Select * from TaskList where UserName = ?",new String[]{userID});
        while (cursor.moveToNext()){
            HashMap<String,String> task = new HashMap<>();
            task.put("task_title", cursor.getString(1));
            task.put("task_description", cursor.getString(2));
            tasksList.add(task);
        }
        return tasksList;
    }
}
