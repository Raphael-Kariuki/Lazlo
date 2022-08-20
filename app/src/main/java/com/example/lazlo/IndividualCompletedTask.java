package com.example.lazlo;




import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;


public class IndividualCompletedTask extends AppCompatActivity {
    Long selectedTaskId;
    SharedPreferences spf;
    Double randUserId;
    MaterialTextView completedTaskTitle,completedTaskCategory,completedTaskCreationDate,
            completedTaskDeadline,completedTaskStartDate,completedTaskCompletionDate,
            completedTaskPredictedDuration,completedTaskActualDuration,completedTaskPredictedSpending,
            completedTaskActualSpending,completedTaskDescription;
    DBHelper dbHelper;
    Double randTaskId;
    String completedTaskTitle_str,completedTaskDescription_str,completedTaskCategory_str,completedTaskDeadline_str,completedTaskPredictedDuration_str;
    Long completedTaskStartDate_long, completedTaskCompletionDate_long,completedTaskActualDuration_long,
            completedTaskPredictedSpending_long, completedTaskCreationDate_long;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent back2CompletedTasksView = new Intent(getApplicationContext(), CompletedTasks.class);
            back2CompletedTasksView.putExtra("category2Populate", completedTaskCategory_str);
            startActivity(back2CompletedTasksView);
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
        assert actionBar != null;
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

        if (cursor != null && cursor.moveToFirst()){
            randTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("completedTaskRandomId"));

            completedTaskTitle_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskTitle"));
            completedTaskTitle.setText(completedTaskTitle_str);

            completedTaskDescription_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskDescription"));
            completedTaskDescription.setText(completedTaskDescription_str);

            completedTaskCategory_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskCategory"));
            completedTaskCategory.setText(String.format(new Locale("en","KE"),"%s%s", getString(R.string.hash_tag), completedTaskCategory_str));

            completedTaskPredictedSpending_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskPredictedSpending"));
            completedTaskPredictedSpending.setText(String.format(new Locale("en","KE"),"%d", completedTaskPredictedSpending_long));

            completedTaskDeadline_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskDeadline"));
            completedTaskDeadline.setText(String.format(new Locale("en","KE"),"%s",HouseOfCommons.returnFormattedDeadline(completedTaskDeadline_str)));

            completedTaskCreationDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskCreationDate"));
            completedTaskCreationDate.setText(HouseOfCommons.returnDate(completedTaskCreationDate_long));

            completedTaskStartDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskStartDate"));
            completedTaskStartDate.setText(HouseOfCommons.returnDate(completedTaskStartDate_long));

            completedTaskCompletionDate_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskCompletionDate"));
            completedTaskCompletionDate.setText(HouseOfCommons.returnDate(completedTaskCompletionDate_long));

            completedTaskActualDuration_long = cursor.getLong(cursor.getColumnIndexOrThrow("completedTaskActualDuration"));
            completedTaskActualDuration.setText(HouseOfCommons.returnDuration(completedTaskActualDuration_long));


            completedTaskPredictedDuration_str = cursor.getString(cursor.getColumnIndexOrThrow("completedTaskPredictedDuration"));
            String[] durationPlusUnits = HouseOfCommons.processPredictedTaskDurationForPopulation(completedTaskPredictedDuration_str);
            completedTaskPredictedDuration.setText(String.format(new Locale("en", "KE"),"%s %s",durationPlusUnits[0],durationPlusUnits[1]));


        }else{
            System.out.println("Error populating view");
        }
    }




}