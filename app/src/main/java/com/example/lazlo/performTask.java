package com.example.lazlo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class performTask extends AppCompatActivity {

    AppCompatImageButton btnStartTask2,btnPauseTask, btnResumeTask;
    AppCompatButton btnCancelDoingTask, btnCompleteDoingTask;
    int taskState;
    AlertDialog.Builder builder;
    long totalTaskDuration;
    DBHelper dbHelper;
    MaterialTextView runningTaskTitle,runningTaskDescription,runningTaskCategory,runningTaskBills,runningTaskDeadline;
    SharedPreferences spf;
    Double randomUserId;
    Double randomTaskId;
    String Title ,Description, Category, Bills,Deadline;
    LocalDateTime formattedLocalDateTime,startTaskDate,pauseTaskDate,resumeTaskDate, completeTaskDate,cancelTaskDate;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), individualTask.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_task);

        //obtain randomTaskId from Intent passed from IndividualTask. Here coz of obtaining context
        randomTaskId = this.getIntent().getDoubleExtra("randomTaskId", -1);

        //obtain randomUserId from sharedPreferences. Obtained as string, converted to double
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randomUserId = Double.parseDouble(spf.getString("randomUserId",null));

        dbHelper = new DBHelper(this);

        runningTaskTitle = findViewById(R.id.runningTaskTitle);
        runningTaskDescription = findViewById(R.id.runningTaskDescription);
        runningTaskCategory = findViewById(R.id.runningTaskCategory);
        runningTaskBills = findViewById(R.id.runningTaskBills);
        runningTaskDeadline = findViewById(R.id.runningTaskDeadline);


        //Handle button to process task durations
        btnStartTask2 = findViewById(R.id.btnStartTask2);
        btnPauseTask = findViewById(R.id.btnPauseTask);
        btnResumeTask = findViewById(R.id.btnResumeTask);
        btnCancelDoingTask = findViewById(R.id.btnCancelDoingTask);
        btnCompleteDoingTask = findViewById(R.id.btnCompleteDoingTask);

        btnStartTask2.setVisibility(View.VISIBLE);
        btnPauseTask.setVisibility(View.INVISIBLE);
        btnResumeTask.setVisibility(View.INVISIBLE);
        btnCancelDoingTask.setVisibility(View.VISIBLE);
        btnCompleteDoingTask.setVisibility(View.INVISIBLE);





        try {
            populateViews();
        }catch (Exception e){
            e.printStackTrace();
        }






        btnStartTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.VISIBLE);
                btnResumeTask.setVisibility(View.INVISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.VISIBLE);
                taskState = 1;

                startTaskDate = getDateTimeNow();
                Cursor cursor = null;
                Integer trial;
                boolean b = false;
                boolean c = false;

                //check for previous trial in performing the task
                try {
                    cursor = dbHelper.getTaskTrialsById(randomTaskId);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (cursor.moveToFirst()){
                    trial = cursor.getInt(cursor.getColumnIndexOrThrow("taskTrial"));
                    System.out.println("trial " + trial);
                    Integer trials = trial + 1;
                    b = updateTaskStatusOnStartButtonPress(randomTaskId,startTaskDate,trials );
                    System.out.println("trials " + trials);
                }else{
                    //insert to db

                    //have to format deadline from string to localDatTime coz it was sent via intent as string
                    c = insertDetailsOnTaskStart(randomUserId, randomTaskId,LocalDateTimeFormat(Deadline),startTaskDate,null,null,null,null,null,null,1,1);
                }
                if (b){
                    Toast.makeText(performTask.this, "update success", Toast.LENGTH_SHORT).show();
                }else if(c){
                    Toast.makeText(performTask.this, "Insert success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(performTask.this, "Fail", Toast.LENGTH_SHORT).show();
                }





            }
        });
        btnPauseTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.INVISIBLE);
                btnResumeTask.setVisibility(View.VISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.INVISIBLE);
                taskState = 2;
                pauseTaskDate = getDateTimeNow();

                //update db

                boolean b = updateTaskStatusOnPauseButtonPress(randomTaskId,pauseTaskDate,"doubleShot",2);
                if (b){
                    Toast.makeText(performTask.this, "success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(performTask.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnResumeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.INVISIBLE);
                btnResumeTask.setVisibility(View.INVISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.VISIBLE);
                taskState = 3;
                resumeTaskDate = getDateTimeNow();

                //update db
                boolean b = updateTaskStatusOnResumeButtonPress(randomTaskId,resumeTaskDate,3);
                if (b){
                    Toast.makeText(performTask.this, "success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(performTask.this, "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //requires context and when placed inside setOnClickListener, it picks that as the context thus needs to be outside
        builder = new AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat);

        btnCancelDoingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTaskDate = getDateTimeNow();
                taskState = 4;

                builder.setCancelable(true);
                builder.setTitle("Cancel working on a task");
                builder.setMessage("Are you sure you want to cancel working on this task?");
                builder.setPositiveButton("Yes,Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                        //update db
                        boolean b = updateTaskStatusOnCancelButtonPress(randomTaskId,cancelTaskDate,taskState);
                        if (b){
                            Toast.makeText(performTask.this, "success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(performTask.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        btnCompleteDoingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //obtain current date and time
                completeTaskDate = getDateTimeNow();

                //setup to determine whether the task was completed in completedTaskCreationDate run or not
                String typeOfCompletion = null;


                //taskState one is obtained when the start button is clicked, if the state remains so to completion, that means
                // the activity wasn't paused to completion, thus assign it as oneShot kinda task. Its duration is simple, completeTme minus startTime
                //LocalDateTime has to be converted to Date so as to obtain the epoch time in long format which can the be used to obtain duration
                if(taskState == 1){
                    typeOfCompletion = "oneShot";
                    totalTaskDuration = getDateFromLocalDateTime(completeTaskDate).getTime() - getDateFromLocalDateTime(startTaskDate).getTime();

                    /*
                    * 1hr = 60min
                    * 1min = 60 sec
                    * 1 sec = 1000ms
                    *       =
                    * 1hr = 3,600,000 ms
                    * 1 min = 60,000 ms
                    *
                    *
                    * */
                    long hours = totalTaskDuration/3600000;
                    long minutes = totalTaskDuration/60000;
                    long seconds = totalTaskDuration/1000;

                    /*
                    * Task State 3 signifies the resume task button was clicked, thus assign the completion type as doubleShot
                    * */
                }else if(taskState == 3){
                    typeOfCompletion = "doubleShot";
                    totalTaskDuration = (getDateFromLocalDateTime(pauseTaskDate).getTime() - getDateFromLocalDateTime(startTaskDate).getTime()) + (getDateFromLocalDateTime(completeTaskDate).getTime() - getDateFromLocalDateTime(resumeTaskDate).getTime());
                    long hours = totalTaskDuration/3600000;
                    long minutes = totalTaskDuration/60000;
                    long seconds = totalTaskDuration/1000;
                }
                //setup taskState for completed tasks
                taskState = 5;


                /*
                * taskStatus table has to be updated with new values of completionTime, Duration typeOfCompletion and the new state which is 5 = completed
                * If successful in updating the taskStatus table, obtain values
                * */

                boolean b = updateTaskStatusOnCompleteButtonPress(randomTaskId,completeTaskDate,totalTaskDuration,typeOfCompletion,taskState);
                if (b){
                    Toast.makeText(performTask.this, "Completion success", Toast.LENGTH_SHORT).show();
                    Cursor cursor = dbHelper.getCompletedTaskById(randomTaskId);
                    if (cursor.moveToFirst() ){
                        Double randTaskId,randUserId;
                        LocalDateTime taskStartTime = null,taskPauseTime = null,taskResumeTime = null,taskCancelTime = null,taskCompleteTime = null;
                        long taskDuration;
                        String taskType;
                        Integer taskTrial;
                        if (taskState == 3){
                            taskPauseTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskPauseTime")));
                            taskResumeTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskResumeTime")));
                            taskCancelTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskCancelTime")));
                            taskCompleteTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskCompleteTime")));
                        }else if(taskState == 1){
                            taskCompleteTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskCompleteTime")));
                        }

                        taskStartTime = LocalDateTimeFormatPlusSecondsFromDB(cursor.getString(cursor.getColumnIndexOrThrow("taskStartTime")));
                        randUserId = cursor.getDouble(cursor.getColumnIndexOrThrow("randUserId"));
                        randTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("randTaskId"));
                        taskDuration = cursor.getLong(cursor.getColumnIndexOrThrow("taskDuration"));
                        taskType = cursor.getString(cursor.getColumnIndexOrThrow("taskType"));
                        taskTrial = cursor.getInt(cursor.getColumnIndexOrThrow("taskTrial"));
                        


                        boolean d = insertCompletedTaskOnComplete(randUserId,randTaskId,LocalDateTimeFormat(Deadline),taskStartTime,taskPauseTime,
                                taskResumeTime,taskCancelTime,taskCompleteTime,taskDuration,taskType,taskTrial);

                                if (d){
                                    Toast.makeText(performTask.this, "Properly transferred", Toast.LENGTH_SHORT).show();

                                    //update taskList table with new taskState = 5 instead of deleting the task which reduces the count of total
                                    // tasks which is required on the dashboard
                                    boolean g = updateTaskListWithTaskState(randomTaskId, taskState);

                                    ////delete from taskStatus:cool
                                    boolean e = deleteTaskOnCompletedNButtonPress(randomTaskId);



                                    if (g){
                                        Toast.makeText(performTask.this, "Properly update with task state", Toast.LENGTH_SHORT).show();
                                    }else if(e){
                                        Toast.makeText(performTask.this, "Properly trashed ", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(performTask.this, "Trashing failure", Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Toast.makeText(performTask.this, "transfer Fail", Toast.LENGTH_SHORT).show();
                                }

                    }
                }else{
                    Toast.makeText(performTask.this, "Completion Fail", Toast.LENGTH_SHORT).show();
                }

                //currentTaskId from up above
                //userId, taskId, startTime, pauseTime, resumeTime,stopTime, totalDuration, taskType,trials, taskState


                Intent backToTasks = new Intent(getApplicationContext(), PendingTasks.class);
                backToTasks.putExtra("tempCategory", Category);
                startActivity(backToTasks);
            }
        });



    }


    public void populateViews(){
        Title = this.getIntent().getStringExtra("taskTitle");
        Description = this.getIntent().getStringExtra("taskDescription");
        Category = this.getIntent().getStringExtra("taskCategory");
        Bills = this.getIntent().getStringExtra("taskBills");
        Deadline = this.getIntent().getStringExtra("taskDeadline");

        runningTaskTitle.setText(Title);
        runningTaskDescription.setText(Description);
        runningTaskCategory.setText(Category);
        runningTaskBills.setText(getString(R.string.money) + " " + Bills);
        runningTaskDeadline.setText(Deadline);
    }

    public boolean insertDetailsOnTaskStart(Double randUserId, Double randTaskId, LocalDateTime taskDeadline, LocalDateTime taskStartTime,
                                            LocalDateTime taskPauseTime, LocalDateTime taskResumeTime, LocalDateTime taskCancelTime,
                                            LocalDateTime taskCompleteTime, Long taskDuration, String taskType,
                                            Integer taskTrial, Integer taskState){
        boolean success = false;
        try {
            success = dbHelper.insertTaskStatus(randUserId,randTaskId,taskDeadline,taskStartTime,taskPauseTime,
                    taskResumeTime,taskCancelTime,taskCompleteTime,taskDuration,taskType,taskTrial,taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }
    public boolean insertCompletedTaskOnComplete(Double randUserId, Double randTaskId, LocalDateTime taskDeadline,LocalDateTime taskStartTime, LocalDateTime taskPauseTime,
                                                 LocalDateTime taskResumeTime, LocalDateTime taskCancelTime,
                                                 LocalDateTime taskCompleteTime, Long taskDuration, String taskType, Integer taskTrial){
        boolean success = false;
        try {
            success = dbHelper.insertCompleted_N_DeletedTasks(randUserId,randTaskId,taskDeadline,taskStartTime,taskPauseTime,
                    taskResumeTime,taskCancelTime,taskCompleteTime,taskDuration,taskType,taskTrial);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }
    //delete from taskStatus
    public boolean deleteTaskOnCompletedNButtonPress(Double randomTaskId){
        boolean success = false;
        try {
            success = dbHelper.deleteCompletedTaskByTaskId(randomTaskId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  success;
    }
    public boolean updateTaskListWithTaskState(Double randTaskId, int taskState){
        boolean success = false;
        try {
            success = dbHelper.updateTaskListWithTaskStateOnDashBoardCompleteBtnPress(randomTaskId, taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  success;
    }

    public boolean updateTaskStatusOnStartButtonPress(Double randTaskId,LocalDateTime taskStartTime,Integer taskTrial){
        boolean success = false;
        try {
            success = dbHelper.updateTaskStatusOnStartByTaskId(randTaskId,taskStartTime,taskTrial);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }
    public boolean updateTaskStatusOnPauseButtonPress(Double randTaskId,LocalDateTime taskPauseTime, String taskType, Integer taskState){
        boolean success = false;
        try {
            success = dbHelper.updateTaskStatusOnPauseByTaskId(randTaskId,taskPauseTime, taskType,taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }
    public boolean updateTaskStatusOnResumeButtonPress(Double randTaskId,LocalDateTime taskResumeTime, Integer taskState){
        boolean success = false;
        try {
            success = dbHelper.updateTaskStatusOnResumeByTaskId(randTaskId,taskResumeTime,taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }
    public boolean updateTaskStatusOnCancelButtonPress(Double randTaskId,LocalDateTime taskCancelTime, Integer taskState){
        boolean success = false;
        try {
            success = dbHelper.updateTaskStatusOnCancelByTaskId(randTaskId,taskCancelTime,taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }

    public boolean updateTaskStatusOnCompleteButtonPress(Double randTaskId,LocalDateTime taskCompleteTime, long taskDuration,String taskType,Integer taskState){
        boolean success = false;
        try {
            success = dbHelper.updateTaskStatusOnCompleteByTaskId(randTaskId,taskCompleteTime,taskDuration,taskType,taskState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;

    }

    public Date stringToDate(String date){
        Date newDate = null;
        DateFormat format = new SimpleDateFormat("EEE LLL dd HH:mm:ss z yyyy");
        try{
           newDate = format.parse(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return newDate;
    }
    private boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            formattedLocalDateTime = getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }
//TODO:obtain deadline from db as can be changed, then task perform in which case the task will still show the old deadline

    //function that receives a date string returns a localDateTime
    private LocalDateTime LocalDateTimeFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-L-d HH:mm");
        try {
            formattedLocalDateTime = getDateFromString(selectedDate, dateTimeFormatter);
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
        }
        return formattedLocalDateTime;
    }
    //function that receives a date string returns a localDateTime
    private LocalDateTime LocalDateTimeFormatPlusSeconds(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm:ss");
        try {
            formattedLocalDateTime = getDateFromString(selectedDate, dateTimeFormatter);
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
        }
        return formattedLocalDateTime;
    }
    //function that receives a date string returns a localDateTime
    private LocalDateTime LocalDateTimeFormatPlusSecondsFromDB(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-L-d HH:mm:ss");
        try {
            formattedLocalDateTime = getDateFromString(selectedDate, dateTimeFormatter);
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
        }
        return formattedLocalDateTime;
    }
    public LocalDateTime getDateTimeNow(){
        Calendar calendar = Calendar.getInstance(new Locale("en","KE"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String formattedHour = null, formattedMinute = null;        String formattedMonth = null,formattedDay = null,formattedSecond =null;

        if (hour < 10){
            formattedHour = "0" + hour;
        }else{
            formattedHour = "" + hour;
        }
        if (minute < 10 ){
            formattedMinute = "0" + minute;
        }else{
            formattedMinute = "" + minute;
        }

        if (month + 1 <= 9){
            formattedMonth = "0" + (month + 1) ;
        }else{
            formattedMonth = String.valueOf(month + 1);
        }
        if(day < 10){
            formattedDay = "0" + day;
        }else{
            formattedDay = String.valueOf(day);
        }
        if(second < 10){
            formattedSecond = "0" + second;
        }else{
            formattedSecond = String.valueOf(second);
        }
        String dateNow = formattedDay + "-" + formattedMonth + "-" + year + " " + formattedHour +":" + formattedMinute + ":" + formattedSecond;
        return LocalDateTimeFormatPlusSeconds(dateNow);

    }
    public static LocalDateTime getDateFromString(String string,DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(string, dateTimeFormatter);
    }
    private Date getDateFromLocalDateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.now(ZoneId.of("GMT+3")).atZone(ZoneId.systemDefault()).toInstant());
    }

}