package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;

public class IndividualCompletedTask extends AppCompatActivity {
    long selectedTaskId;
    SharedPreferences spf;
    Double randUserId;
    MaterialTextView completedTaskTitle,completedTaskCategory,completedTaskCreationDate, completedTaskDeadline,completedTaskStartDate,completedTaskCompletionDate,completedTaskPredictedDuration,completedTaskActualDuration,completedTaskPredictedSpending,completedTaskActualSpending,completedTaskDescription;
    DBHelper dbHelper;
    Double randTaskId;
    String completedTaskTitle_str,completedTaskCategory_str, completedTaskPredictedSpending_str,completedTaskDeadline_str, completedTaskCreationDate_str, completedTaskStartDate_str, completedTaskCompletionDate_str,completedTaskActualDuration_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_completed_task);

        dbHelper = new DBHelper(this);

        spf = getSharedPreferences("user_details",MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        selectedTaskId = getIntent().getLongExtra("selectedTaskId",-1);
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


        completedTaskTitle = findViewById(R.id.completedTaskTitle);
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
            completedTaskTitle_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskTitle"));
            completedTaskTitle.setText(completedTaskTitle_str);

            completedTaskCategory_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskCategory"));
            completedTaskCategory.setText(completedTaskCategory_str);

            completedTaskPredictedSpending_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskPredictedSpending"));
            completedTaskPredictedSpending.setText(completedTaskPredictedSpending_str);

            completedTaskDeadline_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskDeadline"));
            completedTaskDeadline.setText(completedTaskDeadline_str);

            completedTaskCreationDate_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskCreationDate"));
            completedTaskCreationDate.setText(completedTaskCreationDate_str);

            completedTaskStartDate_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskStartDate"));
            completedTaskStartDate.setText(completedTaskStartDate_str);

            completedTaskCompletionDate_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskCompletionDate"));
            completedTaskCompletionDate.setText(completedTaskCompletionDate_str);

            completedTaskActualDuration_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskActualDuration"));
            completedTaskActualDuration.setText(completedTaskActualDuration_str);

        }else{
            System.out.println("Error populating view");
        }
    }
}