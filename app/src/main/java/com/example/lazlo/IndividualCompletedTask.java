package com.example.lazlo;




import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;


public class IndividualCompletedTask extends AppCompatActivity {
    SharedPreferences spf;
    Double randUserId;
    MaterialTextView completedTaskTitle,completedTaskCategory,completedTaskCreationDate,
            completedTaskDeadline,completedTaskStartDate,completedTaskCompletionDate,
            completedTaskPredictedDuration,completedTaskActualDuration,completedTaskPredictedSpending,
            completedTaskActualSpending,completedTaskDescription;
    DBHelper dbHelper;
    Double randTaskId;
    String completedTaskTitle_str,completedTaskDescription_str,completedTaskCategory_str,completedTaskDeadline_str,completedTaskPredictedDuration_str;
    Long completedTaskStartDate_long;
    Long completedTaskCompletionDate_long;
    Long completedTaskActualDuration_long;
    String completedTaskPredictedSpending_str;
    Long completedTaskCreationDate_long;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent back2CompletedTasksView = new Intent(getApplicationContext(), completed.class);
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

        try {
            populateViewsWithData();
        }catch(Exception e){
            e.printStackTrace();
        }




    }
    private void populateViewsWithData(){

            Bundle bundle = getIntent().getBundleExtra("individualCompletedTaskDetails");

            randTaskId = bundle.getDouble("randTaskId", -1);

            completedTaskTitle_str = bundle.getString("taskTitle");
            completedTaskTitle.setText(completedTaskTitle_str);

            completedTaskDescription_str = bundle.getString("taskDescription");
            completedTaskDescription.setText(completedTaskDescription_str);

            completedTaskCategory_str = bundle.getString("taskCategory");
            completedTaskCategory.setText(String.format(new Locale("en","KE"),"%s%s", getString(R.string.hash_tag), completedTaskCategory_str));

            completedTaskPredictedSpending_str = bundle.getString("taskAssociatedPrice");
            completedTaskPredictedSpending.setText(String.format(new Locale("en","KE"),"%s %s","Kshs ", completedTaskPredictedSpending_str));

            completedTaskDeadline_str = bundle.getString("taskDeadline");
            completedTaskDeadline.setText(String.format(new Locale("en","KE"),"%s",HouseOfCommons.returnFormattedDeadline(completedTaskDeadline_str)));

            completedTaskCreationDate_long = bundle.getLong("taskCreationTime",-1);
            completedTaskCreationDate.setText(HouseOfCommons.returnDate(completedTaskCreationDate_long));

            completedTaskStartDate_long = bundle.getLong("taskStartTime",-1);
            completedTaskStartDate.setText(HouseOfCommons.returnDate(completedTaskStartDate_long));

            completedTaskCompletionDate_long = bundle.getLong("taskCompleteTime",-1);
            completedTaskCompletionDate.setText(HouseOfCommons.returnDate(completedTaskCompletionDate_long));

            completedTaskActualDuration_long = bundle.getLong("taskDuration",-1);
            completedTaskActualDuration.setText(HouseOfCommons.returnDuration(completedTaskActualDuration_long));


            completedTaskPredictedDuration_str = bundle.getString("taskPredictedDuration");
            String[] durationPlusUnits = HouseOfCommons.processPredictedTaskDurationForPopulation(completedTaskPredictedDuration_str);
            completedTaskPredictedDuration.setText(String.format(new Locale("en", "KE"),"%s %s",durationPlusUnits[0],durationPlusUnits[1]));


    }




}