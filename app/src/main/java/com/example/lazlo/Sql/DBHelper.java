package com.example.lazlo.Sql;

/*added code*/
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    //create database
    public DBHelper(Context context){
        super(context, "UserData", null, 1);
    }
    //method executed on app creation
    @Override
    public void onCreate(SQLiteDatabase DB){
        DB.execSQL("create Table if not exists TaskList(_id INTEGER PRIMARY KEY , UserName TEXT NOT NULL,TaskTitle TEXT NOT NULL,TaskDescription TEXT NOT NULL, TaskCategory TEXT NOT NULL,TaskAssociatedPrice DOUBLE ,TaskDeadline DATE NOT NULL)");
        DB.execSQL("create Table if not exists TaskListDrafts(_id INTEGER PRIMARY KEY , UserName TEXT ,TaskTitle VARCHAR ,TaskDescription VARCHAR , TaskCategory VARCHAR ,TaskAssociatedPrice VARCHAR ,TaskDeadline VARCHAR )");
        DB.execSQL("create Table if not exists userDetails(_id INTEGER PRIMARY KEY ,userName TEXT UNIQUE NOT NULL,email VARCHAR UNIQUE NOT NULL, password PASSWORD NOT NULL)");

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
        if(userName != null && userName.length() > 0) contentValues.put("userName", userName);
        if(email != null && email.length() > 0) contentValues.put("email", email);
        if (password != null && password.length() > 0) contentValues.put("password", password);
        long result = DB.insert("userDetails", null, contentValues);
        DB.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public long deleteTask(long id){
        return this.getWritableDatabase().delete("TaskList","_id=?",new String[]{String.valueOf(id)});
    }
    public long deleteDraftTask(long id){
        return this.getWritableDatabase().delete("TaskListDrafts","_id=?",new String[]{String.valueOf(id)});
    }
    public Cursor getAll(String uname) {
        //return this.getWritableDatabase().query("TaskList",null,null,null,null,null,null,null);
        return this.getWritableDatabase().query("TaskList",null,"UserName=?",new String[]{String.valueOf(uname)},null,null,null);
        //return this.getWritableDatabase().rawQuery("Select * from TaskList where UserName = ?",new String[]{String.valueOf(uname)});
    }
    public Cursor getAllDrafts(String uname) {
        //return this.getWritableDatabase().query("TaskListDrafts",null,null,null,null,null,null,null);
        return this.getWritableDatabase().query("TaskListDrafts",null,"UserName=?",new String[]{String.valueOf(uname)},null,null,null);
        //return this.getWritableDatabase().rawQuery("Select * from TaskList where UserName = ?",new String[]{String.valueOf(uname)});
    }
    public Cursor getAllByCategories(String uname, String category) {
        //return this.getWritableDatabase().query("TaskList",null,null,null,null,null,null,null);
        return this.getWritableDatabase().query("TaskList",null,"UserName=? and TaskCategory = ? ",new String[]{String.valueOf(uname),String.valueOf(category)},null,null,null);
        //return this.getWritableDatabase().rawQuery("Select * from TaskList where UserName = ?",new String[]{String.valueOf(uname)});
    }

    public Cursor getSum(LocalDate startDate, LocalDate endDate){
        return this.getWritableDatabase().rawQuery("Select sum(TaskAssociatedPrice) as sumTotal from TaskList where TaskDeadline > ? and TaskDeadline < ?", new String[]{String.valueOf(startDate),String.valueOf(endDate)});
    }

    //method to insert task, executed on addtasks.java
    public boolean insertTasks(String userName, String taskTitle, String taskDescription,String taskCategory,
                               Double taskAssociatedPrice, LocalDate taskDeadline){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (userName != null && userName.length() > 0)  contentValues.put("UserName", userName);
        if (taskTitle != null && taskTitle.length() > 0)contentValues.put("TaskTitle", taskTitle);
        if (taskDescription != null && taskDescription.length() > 0) contentValues.put("TaskDescription", taskDescription);
        if (taskCategory != null && taskCategory.length() > 0) contentValues.put("TaskCategory", taskCategory);
        contentValues.put("TaskAssociatedPrice", taskAssociatedPrice);
        if (taskDeadline != null) contentValues.put("TaskDeadline", String.valueOf(taskDeadline));
        long result = DB.insert("TaskList", null, contentValues);
        DB.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean insertDraftTasks(String userName, String taskTitle, String taskDescription,String taskCategory,
                               String taskAssociatedPrice, String taskDeadline){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", userName);
        contentValues.put("TaskTitle", taskTitle);
        contentValues.put("TaskDescription", taskDescription);
        contentValues.put("TaskCategory", taskCategory);
        contentValues.put("TaskAssociatedPrice", taskAssociatedPrice);
        contentValues.put("TaskDeadline", String.valueOf(taskDeadline));
        long result = DB.insert("TaskListDrafts", null, contentValues);
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
    public Cursor getData(String login_Uname){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select userName, password from UserDetails where userName=? ",new String[]{String.valueOf(login_Uname)});
        //cursor.close();
        return cursor;
    }
    public Cursor get_tasks(String userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select TaskTitle, TaskDescription from TaskList where UserName = ?",new String[]{userID});
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
    public long update(long id, String UserName, String TaskTitle, String TaskDescription) {
        long rv = 0;
        ContentValues cv = new ContentValues();
            if (UserName != null && UserName.length() > 0) cv.put("UserName",UserName);
            if (TaskTitle != null && TaskTitle.length() > 0) cv.put("TaskTitle",TaskTitle);
            if (TaskDescription != null && TaskDescription.length() > 0) cv.put("TaskDescription",TaskDescription);
            if (cv.size() > 0) rv = this.getWritableDatabase().update("TaskList",cv,"_id=?",new String[]{String.valueOf(id)});
            return rv;

    }
    public Cursor getTaskById(long id) {
        return this.getWritableDatabase().query("TaskList",null,"_id=?",new String[]{String.valueOf(id)},null,null,null);
    }
}
