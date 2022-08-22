package com.example.lazlo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lazlo.Sql.DBHelper;

import java.util.ArrayList;

public class completed extends AppCompatActivity {

    DBHelper dbHelper;
    AppCompatButton completed_Home, completed_School, completed_Work, completed_Business, completed_Shopping;
    TextView completed_homeUnder, completed_schoolUnder, completed_workUnder, completed_businessUnder, completed_shoppingUnder;
    SharedPreferences sharedPreferences;
    Double randUserId;
    RecyclerView completed_recyclerView;
    ArrayList<completedTaskModel> completedTaskModelArrayList;
    String categoryToPopulateOnSort;
    Cursor completedTasksCursor;
    completedTasksAdapter completedTasksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        //initialize database class helper
        dbHelper = new DBHelper(this);

        ///setup action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Completed tasks");

        //initialize views
        completed_homeUnder = findViewById(R.id.completed_homeUnder);
        completed_workUnder = findViewById(R.id.completed_workUnder);
        completed_schoolUnder = findViewById(R.id.completed_schoolUnder);
        completed_businessUnder = findViewById(R.id.completed_businessUnder);
        completed_shoppingUnder = findViewById(R.id.completed_shoppingUnder);

        completed_Home = findViewById(R.id.completed_homeTask);
        completed_Work = findViewById(R.id.completed_workTasks);
        completed_School =findViewById(R.id.completed_schoolTasks);
        completed_Business = findViewById(R.id.completed_businessTasks);
        completed_Shopping = findViewById(R.id.completed_shoppingTasks);

        //obtain user id and name from sharedPreferences
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        //if randUserId doesn't exist in sharedPreferences, then exit to login page, this is unauthorized access
        try {
            randUserId = Double.parseDouble(sharedPreferences.getString("randomUserId", null));
        }catch (NullPointerException e){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        completed_recyclerView = findViewById(R.id.completed_recyclerView);
        completed_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        completed_recyclerView.setHasFixedSize(true);


        //TODO:Simplify this background setup with 9-patch drawables

        //process Home button
        completed_Home.setOnClickListener(view -> {
            completed_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            completed_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            categoryToPopulateOnSort = "Home";
            populateHomeTasks();
        });

        //process the Shopping click
        completed_Shopping.setOnClickListener(view -> {
            completed_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            completed_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            categoryToPopulateOnSort = "Shopping";
            populateShoppingTasks();
        });

        //process the Work click
        completed_Work.setOnClickListener(view -> {
            completed_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            completed_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            categoryToPopulateOnSort = "Work";
            populateWorkTasks();
        });
        //process the School click
        completed_School.setOnClickListener(view -> {
            completed_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            completed_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            categoryToPopulateOnSort = "School";
            populateSchoolTasks();
        });

        //process the Business click
        completed_Business.setOnClickListener(view -> {
            completed_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
            completed_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            categoryToPopulateOnSort = "Business";
            populateBusinessTasks();
        });
    }
    //obtain Home category content and populate list view on Home button click
    private  void populateHomeTasks(){
        completedTasksCursor = dbHelper.getAllByCategories(randUserId,"Home");
        completedTasksPopulate();

    }
    //obtain Shopping category content and populate list view on Shopping button click
    private  void populateShoppingTasks(){
        completedTasksCursor = dbHelper.getAllByCategories(randUserId,"Shopping");
        completedTasksPopulate();

    }
    //obtain Work category content and populate list view on Work button click
    private  void populateWorkTasks(){
        completedTasksCursor = dbHelper.getAllByCategories(randUserId,"Work");
        completedTasksPopulate();

    }
    //obtain School category content and populate list view on School button click
    private  void populateSchoolTasks(){
        completedTasksCursor = dbHelper.getAllByCategories(randUserId,"School");
        completedTasksPopulate();

    }
    //obtain Business category content and populate list view on Business button click
    private  void populateBusinessTasks(){
        completedTasksCursor = dbHelper.getAllByCategories(randUserId,"Business");
        completedTasksPopulate();

    }
    private void completedTasksPopulate(){
        completedTaskModelArrayList = new ArrayList<>();


        while(completedTasksCursor != null && completedTasksCursor.moveToNext()){
            Double completedTaskRandTaskId = completedTasksCursor.getDouble(completedTasksCursor.getColumnIndexOrThrow("completedTaskRandTaskId"));
            Double completedTaskRandUserId = completedTasksCursor.getDouble(completedTasksCursor.getColumnIndexOrThrow("completedTaskRandUserId"));
            String completedTaskTitle = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskTitle"));
            String completedTaskDescription = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskDescription"));
            String completedTaskDeadline = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskDeadline"));
            String completedTaskAssociatedPrice = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskAssociatedPrice"));
            String completedTaskCategory = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskCategory"));
            long completedTaskCreationDate = completedTasksCursor.getLong(completedTasksCursor.getColumnIndexOrThrow("completedTaskCreationDate"));
            String completedTaskPredictedDuration = completedTasksCursor.getString(completedTasksCursor.getColumnIndexOrThrow("completedTaskPredictedDuration"));
            long completedTaskStartDate = completedTasksCursor.getLong(completedTasksCursor.getColumnIndexOrThrow("completedTaskStartDate"));
            long completedTaskCompletionDate = completedTasksCursor.getLong(completedTasksCursor.getColumnIndexOrThrow("completedTaskCompletionDate"));
            long completedTaskActualDuration = completedTasksCursor.getLong(completedTasksCursor.getColumnIndexOrThrow("completedTaskActualDuration"));
            int completedTaskTrials = completedTasksCursor.getInt(completedTasksCursor.getColumnIndexOrThrow("completedTaskTrials"));
            int completedTaskState = completedTasksCursor.getInt(completedTasksCursor.getColumnIndexOrThrow("completedTaskState"));


            completedTaskModelArrayList.add(new completedTaskModel(completedTaskRandTaskId,completedTaskRandUserId,completedTaskTitle,
                    completedTaskDescription,completedTaskAssociatedPrice,completedTaskCategory,completedTaskDeadline,completedTaskCreationDate
                    ,completedTaskStartDate,completedTaskCompletionDate,completedTaskActualDuration,completedTaskPredictedDuration,
                    completedTaskTrials,completedTaskState));
        }
        completedTasksAdapter = new completedTasksAdapter(this, completedTaskModelArrayList);
        completed_recyclerView.setAdapter(completedTasksAdapter);
    }
}