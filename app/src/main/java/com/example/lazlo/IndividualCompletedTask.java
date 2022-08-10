package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class IndividualCompletedTask extends AppCompatActivity {
    long selectedTaskId;
    SharedPreferences spf;
    Double randUserId;
    MaterialTextView completedTaskTitle,completedTaskCategory,completedTaskCreationDate, completedTaskDeadline,completedTaskStartDate,completedTaskCompletionDate,completedTaskPredictedDuration,completedTaskActualDuration,completedTaskPredictedSpending,completedTaskActualSpending,completedTaskDescription;
    DBHelper dbHelper;
    Double randTaskId;
    String completedTaskTitle_str,completedTaskDescription_str,completedTaskCategory_str,completedTaskDeadline_str;
    long completedTaskStartDate_long, completedTaskCompletionDate_long,completedTaskActualDuration_long,completedTaskPredictedSpending_long, completedTaskCreationDate_long;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_completed_task);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Completed task");



        completedTaskTitle = findViewById(R.id.completedTaskTitle);
        completedTaskDescription = findViewById(R.id.completedTaskDescription);
        completedTaskCategory = findViewById(R.id.completedTaskCategory);
        completedTaskCreationDate = findViewById(R.id.completedTaskCreationDate);
        completedTaskDeadline = findViewById(R.id.completedTaskDeadlineDate);
        completedTaskStartDate = findViewById(R.id.completedTaskStartDate);
        completedTaskCompletionDate = findViewById(R.id.completedTaskCompletionDate);
        completedTaskPredictedDuration = findViewById(R.id.completedTaskPredictedDuration);
        completedTaskActualDuration = findViewById(R.id.completedTaskActualDuration);
        completedTaskPredictedSpending = findViewById(R.id.completedTaskPredictedSpending);
        completedTaskActualSpending = findViewById(R.id.completedTaskActualSpending);
        completedTaskDescription = findViewById(R.id.completedTaskDescription);


        dbHelper = new DBHelper(this);

        spf = getSharedPreferences("user_details",MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        selectedTaskId = getIntent().getLongExtra("selectedTaskId",-1);
        System.out.println("Current id:" + selectedTaskId + " "+ randUserId);
        if (selectedTaskId < 0){
            //do something as invalid id passed
            finish();
        }else {
            try {
                populateViewsWithData(selectedTaskId,randUserId);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }




    }
    private void populateViewsWithData(long selectedTaskId,Double randUserId){
        Cursor cursor = null;
        try {
            cursor  = dbHelper.getAllTasksById(selectedTaskId,randUserId);
        }catch (Exception e){
            System.out.println("Error: " + e);
        }

        if (cursor.moveToFirst()){
            randTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("completedTaskRandomId"));
            System.out.println("Error: " + 0);
            completedTaskTitle_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskTitle"));
            System.out.println(completedTaskTitle_str + 0);
            completedTaskTitle.setText(completedTaskTitle_str);
            System.out.println("Error: " + 1);
            completedTaskDescription_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskDescription"));
            completedTaskDescription.setText(completedTaskDescription_str);
            completedTaskCategory_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskCategory"));
            completedTaskCategory.setText(new StringBuilder().append(getString(R.string.hash_tag)).append(completedTaskCategory_str).toString());
            System.out.println("Error: " + 2);
            completedTaskPredictedSpending_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskPredictedSpending"));
            completedTaskPredictedSpending.setText(new StringBuilder().append("").append(completedTaskPredictedSpending_long).toString());
            System.out.println("Error: " + 3);
            completedTaskDeadline_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskDeadline"));
            completedTaskDeadline.setText(completedTaskDeadline_str);
            System.out.println("Error: " + 4);
            completedTaskCreationDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskCreationDate"));
            completedTaskCreationDate.setText(returnDate(completedTaskCreationDate_long));
            System.out.println("Error: " + 5);
            completedTaskStartDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskStartDate"));
            completedTaskStartDate.setText(returnDate(completedTaskStartDate_long));
            System.out.println("Error: " + 6);
            completedTaskCompletionDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskCompletionDate"));
            completedTaskCompletionDate.setText(returnDate(completedTaskCompletionDate_long));
            System.out.println("Error: " + 7);
            completedTaskActualDuration_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskActualDuration"));
            completedTaskActualDuration.setText(returnDuration(completedTaskActualDuration_long));
            System.out.println("Error: " + 8);
        }else{
            System.out.println("Error populating view");
        }
    }
    private String returnDate(long epochDate){
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "KE"));
        return simpleDateFormat.format(new Date(epochDate));
    }
    /*
    * long hours = totalTaskDuration/3600000;
      long minutes = totalTaskDuration/60000;
      long seconds = totalTaskDuration/1000;
    * */
    private String returnDuration(long duration){
        String formattedDuration;
        if (duration > 3600000){
            long hours = duration/3600000;
            long minutes = duration/60000;
            long seconds = duration/1000;
            formattedDuration  = hours + "hr " + minutes + " min " + seconds + " secs";

        }else if(duration > 600000 ){
            long minutes = duration/60000;
            long seconds = duration/1000;
            formattedDuration  = minutes + " min " + seconds + " secs";
        }else if(duration > 1000){
            long seconds = duration/1000;
            formattedDuration  = seconds + " secs";
        }else {
            formattedDuration = "< 1s";
        }
        return formattedDuration;
    }
}