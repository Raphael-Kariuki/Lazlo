package com.example.lazlo.Sql;

/*added code*/
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
        //TODO: remove username from tasklist completely replace with userId
        DB.execSQL("create Table if not exists TaskList(_id INTEGER PRIMARY KEY ,randTaskId DOUBLE UNIQUE NOT NULL,randUserId DOUBLE NOT NULL, UserName TEXT NOT NULL,TaskTitle TEXT NOT NULL,TaskDescription TEXT NOT NULL, TaskCategory TEXT NOT NULL,TaskAssociatedPrice DOUBLE ,TaskDeadline LOCALDATETIME NOT NULL)");
        DB.execSQL("create Table if not exists TaskListDrafts(_id INTEGER PRIMARY KEY , UserName TEXT ,TaskTitle VARCHAR ,TaskDescription VARCHAR , TaskCategory VARCHAR ,TaskAssociatedPrice VARCHAR ,TaskDeadline VARCHAR )");
        DB.execSQL("create Table if not exists userDetails(_id INTEGER PRIMARY KEY ,randUserId DOUBLE UNIQUE NOT NULL, userName TEXT UNIQUE NOT NULL,email VARCHAR UNIQUE NOT NULL, password PASSWORD NOT NULL)");

        //userId, taskId, startTime, pauseTime, resumeTime,stopTime, totalDuration, taskType,trials, taskState
        DB.execSQL("create Table if not exists TaskStatus(_id INTEGER PRIMARY KEY,randUserId DOUBLE NOT NULL, randTaskId DOUBLE NOT NULL,taskDeadline LOCALDATETIME NOT NULL,taskStartTime DATE NOT NULL, taskPauseTime DATE , taskResumeTime DATE, taskCancelTime DATE,taskCompleteTime DATE, taskDuration LONG, taskType TEXT, taskTrial INTEGER NOT NULL, taskState INTEGER NOT NULL )");
        DB.execSQL("create Table if not exists Completed_N_DeletedTasks(_id INTEGER PRIMARY KEY,randUserId DOUBLE NOT NULL, randTaskId DOUBLE NOT NULL,taskStartTime DATE NOT NULL, taskPauseTime DATE , taskResumeTime DATE, taskCancelTime DATE,taskCompleteTime DATE, taskDuration LONG, taskType TEXT, taskTrial INTEGER NOT NULL)");
    }

    //method run when there's a db upgrade
    public void onUpgrade(SQLiteDatabase DB, int i, int i1){
        DB.execSQL("drop Table if exists userDetails");
        DB.execSQL("drop Table if exists TaskList");
        //recreate the db
        onCreate(DB);
    }

    public boolean insertTaskStatus(Double randUserId, Double randTaskId, LocalDateTime taskDeadline,Date taskStartTime, Date taskPauseTime, Date taskResumeTime, Date taskCancelTime, Date taskCompleteTime, Long taskDuration, String taskType, Integer taskTrial, Integer taskState) {
        ContentValues cv = new ContentValues();
        if (randUserId != null && randUserId > 1) cv.put("randUserId", randUserId);
        if (randTaskId != null && randTaskId > 1) cv.put("randTaskId", randTaskId);
        if (taskStartTime != null ) cv.put("taskStartTime", String.valueOf(taskStartTime));
        if (taskDeadline != null ) cv.put("taskDeadline", String.valueOf(taskDeadline));
        cv.put("taskPauseTime", String.valueOf(taskPauseTime));
        cv.put("taskResumeTime", String.valueOf(taskResumeTime));
        cv.put("taskCancelTime", String.valueOf(taskCancelTime));
        cv.put("taskCompleteTime", String.valueOf(taskCompleteTime));
        cv.put("taskDuration", String.valueOf(taskDuration));
        cv.put("taskType", taskType);
        if (taskTrial != null && taskTrial >= 1) cv.put("taskTrial", taskTrial);
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long result = this.getWritableDatabase().insert("TaskStatus",null, cv);
        this.getWritableDatabase().close();
        return result != -1;
    }
    public boolean insertCompleted_N_DeletedTasks(Double randUserId, Double randTaskId, Date taskStartTime, Date taskPauseTime, Date taskResumeTime, Date taskCancelTime, Date taskCompleteTime, Long taskDuration, String taskType, Integer taskTrial) {
        ContentValues cv = new ContentValues();
        if (randUserId != null && randUserId > 1) cv.put("randUserId", randUserId);
        if (randTaskId != null && randTaskId > 1) cv.put("randTaskId", randTaskId);
        if (taskStartTime != null ) cv.put("taskStartTime", String.valueOf(taskStartTime));
        cv.put("taskPauseTime", String.valueOf(taskPauseTime));
        cv.put("taskResumeTime", String.valueOf(taskResumeTime));
        cv.put("taskCancelTime", String.valueOf(taskCancelTime));
        cv.put("taskCompleteTime", String.valueOf(taskCompleteTime));
        cv.put("taskDuration", String.valueOf(taskDuration));
        cv.put("taskType", taskType);
        if (taskTrial != null && taskTrial >= 1) cv.put("taskTrial", taskTrial);
        long result = this.getWritableDatabase().insert("Completed_N_DeletedTasks",null, cv);
        this.getWritableDatabase().close();
        return result != -1;
    }
    public Cursor getTaskTrialsById(Double randomTaskId){
        return this.getWritableDatabase().query("TaskStatus",null,"randTaskId=?",new String[]{String.valueOf(randomTaskId)},null,null,null);
    }
    public Cursor getCompletedTaskById(Double randomTaskId){
        return this.getWritableDatabase().query("TaskStatus",null,"randTaskId=?",new String[]{String.valueOf(randomTaskId)},null,null,null);
    }
    public boolean deleteCompletedTaskByTaskId(Double randomTaskId){
        int returnValue;
        returnValue = this.getWritableDatabase().delete("TaskStatus","randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;
    }
    public boolean deleteTaskByTaskId(Double randomTaskId){
        int returnValue;
        returnValue = this.getWritableDatabase().delete("TaskList","randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;
    }
    public boolean updateTaskStatusOnStartByTaskId(Double randomTaskId, Date taskStartTime,Integer taskTrial){
        ContentValues cv = new ContentValues();
        cv.put("taskStartTime", String.valueOf(taskStartTime));
        if (taskTrial != null && taskTrial >= 1) cv.put("taskTrial", taskTrial);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }
    public boolean updateTaskStatusOnPauseByTaskId(Double randomTaskId, Date taskPauseTime,String taskType, Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskPauseTime", String.valueOf(taskPauseTime));
        cv.put("taskType", taskType);
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }
    public boolean updateTaskStatusOnResumeByTaskId(Double randomTaskId, Date taskResumeTime,Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskResumeTime", String.valueOf(taskResumeTime));
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }
    public boolean updateTaskStatusOnCancelByTaskId(Double randomTaskId, Date taskCancelTime,Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskCancelTime", String.valueOf(taskCancelTime));
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }
    public boolean updateTaskStatusOnCompleteByTaskId(Double randomTaskId, Date taskCompleteTime,long taskDuration,String taskType,Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskCompleteTime", String.valueOf(taskCompleteTime));
        cv.put("taskType", taskType);
        if (taskDuration >= 1) cv.put("taskDuration", taskDuration);
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }

    //Method to insert userdata on sign up, returning true if successful and otherwise
    //executed on signup.java
    public boolean insertUserData(String userName,Double randUserId, String email, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(userName != null && userName.length() > 0) contentValues.put("userName", userName);
        if (randUserId != null && String.valueOf(randUserId).length() > 0) contentValues.put("randUserId", randUserId);
        if(email != null && email.length() > 0) contentValues.put("email", email);
        if (password != null && password.length() > 0) contentValues.put("password", password);
        long result = DB.insert("userDetails", null, contentValues);
        DB.close();
        return result != -1;
    }
    public long deleteTask(long id){
        return this.getWritableDatabase().delete("TaskList","_id=?",new String[]{String.valueOf(id)});
    }
    public long deleteDraftTask(long id){
        return this.getWritableDatabase().delete("TaskListDrafts","_id=?",new String[]{String.valueOf(id)});
    }
    public Cursor getAll(String uname) {
        //return this.getWritableDatabase().query("TaskList",null,null,null,null,null,null,null);
        return this.getWritableDatabase().query("TaskList",null,"UserName=?",new String[]{String.valueOf(uname)},null,null,"TaskDeadline");
        //return this.getWritableDatabase().rawQuery("Select * from TaskList where UserName = ?",new String[]{String.valueOf(uname)});
    }
    public Cursor getByEmail(String email){
        return this.getWritableDatabase().query("userDetails",null,"email=?",new String[]{String.valueOf(email)},null,null,null);
    }
    public boolean updateByEmail(String email, String tempPassword){

        long returnValue;
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", tempPassword);
        returnValue = this.getWritableDatabase().update("userDetails",contentValues,"email=?",new String[]{String.valueOf(email)});
        return returnValue != -1;
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

    //get sum of spending per month for dashboard
    public Cursor getSumPerMonth(Double randUserId,LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().rawQuery("Select sum(TaskAssociatedPrice) as sumTotalSpendingPerMonth from TaskList where randUserId = ? and TaskDeadline > ? and TaskDeadline < ?", new String[]{String.valueOf(startDate),String.valueOf(endDate)});
    }

    public Cursor getSumOfTasksPerMonthForDashBoard(Double randUserId,LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().rawQuery("Select count(randTaskId) as sumTotalTasksPerMonth from TaskList where randUserId = ? and TaskDeadline > ? and TaskDeadline < ?", new String[]{String.valueOf(randUserId),String.valueOf(startDate),String.valueOf(endDate)});
    }

    //used to obtain no of all user tasks
    public Cursor getSumOfTasksForDashBoard(Double randUserId){
        return this.getWritableDatabase().rawQuery("Select count(randTaskId) as sumTotalTasks from TaskList where randUserId = ?", new String[]{String.valueOf(randUserId)});
    }
    public Cursor getSpendingDetails(LocalDate startDate, LocalDate endDate){
        return this.getWritableDatabase().query("TaskList",new String[]{"_id","TaskTitle","TaskAssociatedPrice"},"TaskDeadline > ? and TaskDeadline < ?",new String[]{String.valueOf(startDate),String.valueOf(endDate)},null,null,"TaskAssociatedPrice DESC");
    }


    //method to insert task, executed on addtasks.java
    public boolean insertTasks(Double randTaskId, Double randUserId,String userName, String taskTitle, String taskDescription, String taskCategory,
                               Double taskAssociatedPrice, LocalDateTime taskDeadline){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (randTaskId != null && String.valueOf(randTaskId).length() > 0) contentValues.put("randTaskId", randTaskId);
        if (randUserId != null && String.valueOf(randUserId).length() > 0) contentValues.put("randUserId", randUserId);
        if (userName != null && userName.length() > 0)  contentValues.put("UserName", userName);
        if (taskTitle != null && taskTitle.length() > 0)contentValues.put("TaskTitle", taskTitle);
        if (taskDescription != null && taskDescription.length() > 0) contentValues.put("TaskDescription", taskDescription);
        if (taskCategory != null && taskCategory.length() > 0) contentValues.put("TaskCategory", taskCategory);
        contentValues.put("TaskAssociatedPrice", taskAssociatedPrice);
        if (taskDeadline != null) contentValues.put("TaskDeadline", String.valueOf(taskDeadline));
        long result = DB.insert("TaskList", null, contentValues);
        DB.close();
        return result != -1;
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
        return result != -1;
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
        //cursor.close();
        return DB.rawQuery("Select userName, password, randUserId from UserDetails where userName=? ",new String[]{String.valueOf(login_Uname)});
    }
    public Cursor get_tasks(String userID){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select TaskTitle, TaskDescription from TaskList where UserName = ?",new String[]{userID});
    }

    public boolean update(long id, String UserName, String TaskTitle, String TaskDescription,String TaskCategory,
                          String TaskAssociatedPrice, LocalDateTime TaskDeadline) {
        long rv = 0;
        ContentValues cv = new ContentValues();
            if (UserName != null && UserName.length() > 0) cv.put("UserName",UserName);
            if (TaskTitle != null && TaskTitle.length() > 0) cv.put("TaskTitle",TaskTitle);
            if (TaskDescription != null && TaskDescription.length() > 0) cv.put("TaskDescription",TaskDescription);
            if (TaskCategory != null && TaskCategory.length() > 0 ) cv.put("TaskCategory", TaskCategory);
            if (TaskAssociatedPrice != null && TaskAssociatedPrice.length() > 0 ) cv.put("TaskAssociatedPrice", TaskAssociatedPrice);
            if (TaskDeadline != null && String.valueOf(TaskDeadline).length() > 0 ) cv.put("TaskDeadline", String.valueOf(TaskDeadline));
            if (cv.size() > 0) rv = this.getWritableDatabase().update("TaskList",cv,"_id=?",new String[]{String.valueOf(id)});
            this.getWritableDatabase().close();
        return rv != -1;

    }
    public Cursor getTaskById(long id) {
        return this.getWritableDatabase().query("TaskList",null,"_id=?",new String[]{String.valueOf(id)},null,null,null);
    }
}
