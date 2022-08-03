package com.example.lazlo.Sql;

/*added code*/
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.time.LocalDateTime;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    //create database
    public DBHelper(Context context){
        super(context, "UserData", null, 1);
    }

    //method executed on app creation
    @Override
    public void onCreate(SQLiteDatabase DB){
        //TODO: remove username from taskList completely replace with userId
        DB.execSQL("create Table if not exists TaskList(_id INTEGER PRIMARY KEY ,randTaskId DOUBLE UNIQUE NOT NULL,randUserId DOUBLE NOT NULL, UserName TEXT NOT NULL,TaskTitle TEXT NOT NULL,TaskDescription TEXT NOT NULL, TaskCategory TEXT NOT NULL,TaskAssociatedPrice DOUBLE ,TaskDeadline LOCALDATETIME NOT NULL, taskState INTEGER NOT NULL)");
        DB.execSQL("create Table if not exists TaskListDrafts(_id INTEGER PRIMARY KEY , UserName TEXT ,TaskTitle VARCHAR ,TaskDescription VARCHAR , TaskCategory VARCHAR ,TaskAssociatedPrice VARCHAR ,TaskDeadline VARCHAR )");
        DB.execSQL("create Table if not exists userDetails(_id INTEGER PRIMARY KEY ,randUserId DOUBLE UNIQUE NOT NULL, userName TEXT UNIQUE NOT NULL,email VARCHAR UNIQUE NOT NULL, password PASSWORD NOT NULL, Status VARCHAR)");

        //userId, taskId, startTime, pauseTime, resumeTime,stopTime, totalDuration, taskType,trials, taskState
        DB.execSQL("create Table if not exists TaskStatus(_id INTEGER PRIMARY KEY,randUserId DOUBLE NOT NULL, randTaskId DOUBLE NOT NULL,taskDeadline LOCALDATETIME NOT NULL,taskStartTime DATE NOT NULL, taskPauseTime DATE , taskResumeTime DATE, taskCancelTime DATE,taskCompleteTime DATE, taskDuration LONG, taskType TEXT, taskTrial INTEGER NOT NULL, taskState INTEGER NOT NULL )");
        DB.execSQL("create Table if not exists Completed_N_DeletedTasks(_id INTEGER PRIMARY KEY,randUserId DOUBLE NOT NULL, randTaskId DOUBLE NOT NULL,taskDeadline LOCALDATETIME NOT NULL,taskStartTime DATE NOT NULL, taskPauseTime DATE , taskResumeTime DATE, taskCancelTime DATE,taskCompleteTime DATE, taskDuration LONG, taskType TEXT, taskTrial INTEGER NOT NULL)");
    }

    //method run when there's a db upgrade
    public void onUpgrade(SQLiteDatabase DB, int i, int i1){
        DB.execSQL("drop Table if exists userDetails");
        DB.execSQL("drop Table if exists TaskList");
        DB.execSQL("drop Table if exists TaskListDrafts");
        DB.execSQL("drop Table if exists TaskStatus");
        DB.execSQL("drop Table if exists Completed_N_DeletedTasks");
        //recreate the db
        onCreate(DB);
    }

    //function to obtain completed tasks per month
    public Cursor getCountOfCompletedTasksPerMonth(Double randUserId,LocalDateTime startDate, LocalDateTime endDate ){
        return this.getWritableDatabase().rawQuery("select count(randTaskId) as completedTasksPerMonth from Completed_N_DeletedTasks where randUserId = ? and taskDeadline > ? and taskDeadline < ?", new String[]{String.valueOf(randUserId), String.valueOf(startDate), String.valueOf(endDate)});
    }

    //function to obtain completed tasks
    public Cursor getCountOfCompletedTasks(Double randUserId){
        return this.getWritableDatabase().rawQuery("select count(randTaskId) as completedTasks from Completed_N_DeletedTasks where randUserId = ?", new String[]{String.valueOf(randUserId)});
    }
    //function to obtain no of pending tasks per month
    public Cursor getCountOfPendingTasksPerMonth(Double randUserId,LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().rawQuery("select count(randTaskId) as pendingTasksPerMonth from TaskList where randUserId = ? and taskDeadline > ? and taskDeadline < ? and taskState = 0 " , new String[]{String.valueOf(randUserId),String.valueOf(startDate),String.valueOf(endDate)});
    }

    //function to obtain no of pending tasks from TaskList
    public Cursor getCountOfPendingTasks(Double randUserId){
        return this.getWritableDatabase().rawQuery("select count(randTaskId) as totalPendingTasks from TaskList where randUserId = ? and taskState = 0", new String[]{String.valueOf(randUserId)});
    }

    //function to inert taskStatus
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

    //function to insert completed tasks
    public boolean insertCompleted_N_DeletedTasks(Double randUserId, Double randTaskId,LocalDateTime taskDeadline, Date taskStartTime, Date taskPauseTime, Date taskResumeTime, Date taskCancelTime, Date taskCompleteTime, Long taskDuration, String taskType, Integer taskTrial) {
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
        long result = this.getWritableDatabase().insert("Completed_N_DeletedTasks",null, cv);
        this.getWritableDatabase().close();
        return result != -1;
    }

    //function to obtain no of trial so as to know the number of trials in trying to start and complete task
    public Cursor getTaskTrialsById(Double randomTaskId){
        return this.getWritableDatabase().query("TaskStatus",null,"randTaskId=?",new String[]{String.valueOf(randomTaskId)},null,null,null);
    }

    //get details of a task by Id so as to update it's details on it's completion
    public Cursor getCompletedTaskById(Double randomTaskId){
        return this.getWritableDatabase().query("TaskStatus",null,"randTaskId=?",new String[]{String.valueOf(randomTaskId)},null,null,null);
    }

    //delete task on it's completion from taskStatus table
    public boolean deleteCompletedTaskByTaskId(Double randomTaskId){
        int returnValue;
        returnValue = this.getWritableDatabase().delete("TaskStatus","randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;
    }

    //update taskTrial and startTime values
    public boolean updateTaskStatusOnStartByTaskId(Double randomTaskId, Date taskStartTime,Integer taskTrial){
        ContentValues cv = new ContentValues();
        cv.put("taskStartTime", String.valueOf(taskStartTime));
        if (taskTrial != null && taskTrial >= 1) cv.put("taskTrial", taskTrial);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }

    //function to update pause time, type and state on pause
    public boolean updateTaskStatusOnPauseByTaskId(Double randomTaskId, Date taskPauseTime,String taskType, Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskPauseTime", String.valueOf(taskPauseTime));
        cv.put("taskType", taskType);
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }

    //function to update time and state on resume
    public boolean updateTaskStatusOnResumeByTaskId(Double randomTaskId, Date taskResumeTime,Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskResumeTime", String.valueOf(taskResumeTime));
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }

    //function to update task state to state 4 repin cancelled tasks and cancel time
    public boolean updateTaskStatusOnCancelByTaskId(Double randomTaskId, Date taskCancelTime,Integer taskState){
        ContentValues cv = new ContentValues();
        cv.put("taskCancelTime", String.valueOf(taskCancelTime));
        if (taskState != null && taskState >= 1) cv.put("taskState", taskState);
        long returnValue;
        returnValue = this.getWritableDatabase().update("TaskStatus", cv,"randTaskId=?",new String[]{String.valueOf(randomTaskId)});
        return returnValue != -1;

    }

    //function to update task details with taskType, duration, status and completion time
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
    public boolean insertUserData(String userName,Double randUserId, String email, String password, String Status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(userName != null && userName.length() > 0) contentValues.put("userName", userName);
        if (randUserId != null && String.valueOf(randUserId).length() > 0) contentValues.put("randUserId", randUserId);
        if(email != null && email.length() > 0) contentValues.put("email", email);
        if (password != null && password.length() > 0) contentValues.put("password", password);
        contentValues.put("Status", "");
        long result = DB.insert("userDetails", null, contentValues);
        DB.close();
        return result != -1;
    }

    //function to delete task by id when item on listview is clicked
    public boolean deleteTask(long id){
        long returnValue;
        returnValue = this.getWritableDatabase().delete("TaskList","_id=?",new String[]{String.valueOf(id)});
        return returnValue != -1;
    }
    
    //function to delete draft tasks
    public boolean deleteDraftTask(long id){
        long returnValue;
        returnValue = this.getWritableDatabase().delete("TaskListDrafts","_id=?",new String[]{String.valueOf(id)});
        return returnValue != -1;
    }
    
    //function to get all tasks other than those completed to populate on task list
    //task state 5 rep completed tasks
    public Cursor getAll(String uname) {
        return this.getWritableDatabase().query("TaskList",null,"UserName=? and taskState != 5",new String[]{String.valueOf(uname)},null,null,"TaskDeadline");
    }

    //function to reset password by mail
    public boolean updateByEmail(String email, String tempPassword){
        long returnValue;
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", tempPassword);
        returnValue = this.getWritableDatabase().update("userDetails",contentValues,"email=?",new String[]{String.valueOf(email)});
        return returnValue != -1;
    }

    //function to obtain all saved drafts
    public Cursor getAllDrafts(String uname) {
        return this.getWritableDatabase().query("TaskListDrafts",null,"UserName=?",new String[]{String.valueOf(uname)},null,null,null);
    }

    //function to obtain all tasks by categories
    public Cursor getAllByCategories(String uname, String category) {
        return this.getWritableDatabase().query("TaskList",null,"UserName=? and TaskCategory = ? ",new String[]{String.valueOf(uname),String.valueOf(category)},null,null,null);
    }

    //get sum of spending per month for dashboard
    public Cursor getSumPerMonth(Double randUserId,LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().rawQuery("Select sum(TaskAssociatedPrice) as sumTotalSpendingPerMonth from TaskList where randUserId = ? and TaskDeadline > ? and TaskDeadline < ?", new String[]{String.valueOf(randUserId),String.valueOf(startDate),String.valueOf(endDate)});
    }

    //obtain total sum of bills per category
    public Cursor getSumPerCategory(Double randUserId,String taskCategory){
        return this.getWritableDatabase().rawQuery("Select sum(TaskAssociatedPrice) as sumTotalSpendingPerCategory from TaskList where randUserId = ? and TaskCategory =  ?", new String[]{String.valueOf(randUserId),String.valueOf(taskCategory)});
    }

    //used to obtain no of tasks per month to populate dashboard
    public Cursor getSumOfTasksPerMonthForDashBoard(Double randUserId,LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().rawQuery("Select count(randTaskId) as sumTotalTasksPerMonth from TaskList where randUserId = ? and TaskDeadline > ? and TaskDeadline < ?", new String[]{String.valueOf(randUserId),String.valueOf(startDate),String.valueOf(endDate)});
    }

    //used to obtain no of all user tasks to populate view on dashboard
    public Cursor getSumOfTasksForDashBoard(Double randUserId){
        return this.getWritableDatabase().rawQuery("Select count(randTaskId) as sumTotalTasks from TaskList where randUserId = ?", new String[]{String.valueOf(randUserId)});
    }

    //get task details used to populate spending list view on custom view @ dashboard
    public Cursor getSpendingDetails(LocalDateTime startDate, LocalDateTime endDate){
        return this.getWritableDatabase().query("TaskList",new String[]{"_id","TaskTitle","TaskAssociatedPrice"},"TaskDeadline > ? and TaskDeadline < ?",new String[]{String.valueOf(startDate),String.valueOf(endDate)},null,null,"TaskAssociatedPrice DESC");
    }


    //method to insert task, executed on addTasks.java
    public boolean insertTasks(Double randTaskId, Double randUserId,String userName, String taskTitle, String taskDescription, String taskCategory,
                               Double taskAssociatedPrice, LocalDateTime taskDeadline, Integer taskState){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (randTaskId != null && String.valueOf(randTaskId).length() > 0) contentValues.put("randTaskId", randTaskId);
        if (randUserId != null && String.valueOf(randUserId).length() > 0) contentValues.put("randUserId", randUserId);
        if (userName != null && userName.length() > 0)  contentValues.put("UserName", userName);
        if (taskTitle != null && taskTitle.length() > 0)contentValues.put("TaskTitle", taskTitle);
        if (taskDescription != null && taskDescription.length() > 0) contentValues.put("TaskDescription", taskDescription);
        if (taskCategory != null && taskCategory.length() > 0) contentValues.put("TaskCategory", taskCategory);
        if (taskState != null && String.valueOf(taskState).length() > 0) contentValues.put("taskState", taskState);
        contentValues.put("TaskAssociatedPrice", taskAssociatedPrice);
        if (taskDeadline != null) contentValues.put("TaskDeadline", String.valueOf(taskDeadline));
        long result = DB.insert("TaskList", null, contentValues);
        DB.close();
        return result != -1;
    }

    //function to update taskState from the default 0 to 5
    public boolean updateTaskListWithTaskStateOnDashBoardCompleteBtnPress(Double randTaskId, Integer taskState) {
        long rv = 0;
        ContentValues cv = new ContentValues();
        if (taskState != null && String.valueOf(taskState).length() > 0 ) cv.put("taskState", String.valueOf(taskState));
        if (cv.size() > 0) rv = this.getWritableDatabase().update("TaskList",cv,"randTaskId=?",new String[]{String.valueOf(randTaskId)});
        this.getWritableDatabase().close();
        return rv != -1;
    }


    //function to insert draft tasks
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


    //method used to obtain user credentials which are checked on login, matched against user input to know whether the username exists or not
    public Cursor getUserDetailsByUserName(String login_Uname){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select userName,email,password, randUserId,Status from UserDetails where userName=? ",new String[]{String.valueOf(login_Uname)});
    }

    //method to obtain all email in the db which are checked against user input on login
    public Cursor getUserEmail(){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.query("userDetails",new String[]{"email"},null,null,null,null,null);
    }

    //function to check whether there are tasks in the db to decide on redirecting to dashboard or final page
    public Cursor getTasks(Double randUserId){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.query("TaskList",null,"randUserId = ?",new String[]{String.valueOf(randUserId)},null,null,null);
    }

    //function to update task details
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

    //function to obtain task details by id for populating individual task section and while deletion of task when clicked on a list view
    public Cursor getTaskById(long id) {
        return this.getWritableDatabase().query("TaskList",null,"_id=?",new String[]{String.valueOf(id)},null,null,null);
    }

    public boolean updateUserName(String username,String Status, Double randUserId){
        long returnValue;
        ContentValues contentValues = new ContentValues();
        if (!username.isEmpty()) contentValues.put("userName", username);
        if (!Status.isEmpty()) contentValues.put("Status", Status);
        returnValue = this.getWritableDatabase().update("userDetails",contentValues,"randUserId = ?", new String[]{String.valueOf(randUserId)});
        return returnValue != -1;
    }
}
