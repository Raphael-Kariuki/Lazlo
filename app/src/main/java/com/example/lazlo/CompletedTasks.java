package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lazlo.Sql.DBHelper;

public class CompletedTasks extends AppCompatActivity {

    ListView completedTasks_tasks_listView;
    String completedTasks_user_name;
    SharedPreferences completedTasks_session_prefs;
    DBHelper completedTasks_dbHelper;
    Cursor completedTasks_cursor;
    SimpleCursorAdapter completedTasks_simpleCursorAdapter;
    AppCompatButton completedTasks_home, completedTasks_school,completedTasks_work,completedTasks_business,completedTasks_shopping;
    Double completedTasks_randUserId;
    TextView completedTasks_homeUnder,completedTasks_schoolUnder,completedTasks_workUnder,completedTasks_businessUnder,completedTasks_shoppingUnder;


    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), TasksHomePage.class));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), TasksHomePage.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Completed tasks");

        completedTasks_homeUnder = findViewById(R.id.completedTasks_homeUnder);
        completedTasks_schoolUnder = findViewById(R.id.completedTasks_schoolUnder);
        completedTasks_workUnder = findViewById(R.id.completedTasks_workUnder);
        completedTasks_businessUnder = findViewById(R.id.completedTasks_businessUnder);
        completedTasks_shoppingUnder = findViewById(R.id.completedTasks_shoppingUnder);

        //initialize views
        completedTasks_tasks_listView = this.findViewById(R.id.completedTasks_task_listView);

        completedTasks_home = findViewById(R.id.completedTasks_homeTask);
        completedTasks_work = findViewById(R.id.completedTasks_workTasks);
        completedTasks_school =findViewById(R.id.completedTasks_schoolTasks);
        completedTasks_business = findViewById(R.id.completedTasks_businessTasks);
        completedTasks_shopping = findViewById(R.id.completedTasks_shoppingTasks);


        //initialize db class
        completedTasks_dbHelper = new DBHelper(this);

        //obtain username value from sharedPreferences stored on login and set it on a textview
        completedTasks_session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        completedTasks_user_name = completedTasks_session_prefs.getString("username",null);

        completedTasks_randUserId = Double.parseDouble(completedTasks_session_prefs.getString("randomUserId", null));


        populateCompletedTasks_HomeTasks();

            //TODO:Simplify this background setup with 9-patch drawables

            //process completedTasks_home button
            completedTasks_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    populateCompletedTasks_HomeTasks();
                }
            });

            //process the completedTasks_shopping click
            completedTasks_shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                    populateCompletedTasks_ShoppingTasks();
                }
            });

            //process the completedTasks_work click
            completedTasks_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    populateCompletedTasks_WorkTasks();
                }
            });
            //process the completedTasks_school click
            completedTasks_school.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    populateCompletedTasks_SchoolTasks();
                }
            });

            //process the completedTasks_business click
            completedTasks_business.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.white));
                    populateCompletedTasks_BusinessTasks();
                }
            });


        
        
    }
    //obtain Home category content and populate list view on Home button click
    private  void populateCompletedTasks_HomeTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Home");
        taskListPopulate();

    }
    //obtain Shopping category content and populate list view on Shopping button click
    private  void populateCompletedTasks_ShoppingTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Shopping");
        taskListPopulate();

    }
    //obtain Work category content and populate list view on Work button click
    private  void populateCompletedTasks_WorkTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Work");
        taskListPopulate();

    }
    //obtain School category content and populate list view on School button click
    private  void populateCompletedTasks_SchoolTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"School");
        taskListPopulate();

    }
    //obtain Business category content and populate list view on Business button click
    private  void populateCompletedTasks_BusinessTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Business");
        taskListPopulate();

    }

    //function to populate list view, initially on 1st load with all tasks
    private void taskListPopulate(){
        if (completedTasks_simpleCursorAdapter == null){
            completedTasks_simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,completedTasks_cursor,new String[]{"completedTaskTitle","completedTaskDescription","completedTaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedPrice},0);
            completedTasks_tasks_listView.setAdapter(completedTasks_simpleCursorAdapter);

            //setup a click listener for a task
            completedTasks_tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent toIndividualCompletedTask = new Intent(getApplicationContext(),IndividualCompletedTask.class);
                    toIndividualCompletedTask.putExtra("selectedTaskId", l);
                    startActivity(toIndividualCompletedTask);
                }
            });
            completedTasks_tasks_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent sendToIndividualCompletedTaskEdit = new Intent(getApplicationContext(), rescheduleCompletedTask.class);
                    sendToIndividualCompletedTaskEdit.putExtra("task2RescheduleId", l);
                    startActivity(sendToIndividualCompletedTaskEdit);
                    return true;
                }
            });
        }else {
            //necessary to re-obtain updated task list from the db
            completedTasks_simpleCursorAdapter.swapCursor(completedTasks_cursor);
        }
    }

    //this function on the other hand receives a category string from IndividualCompletedTask and rescheduleCompletedTask. This is to ensure once a task is rendered or opened for viewing or rescheduling,
    // it's category is automatically rendered on the list view, not some different category.
    // The specific category button is also highlighted . UX matters
    private void SetOrRefreshListView2(){
        String tempCategory = this.getIntent().getStringExtra("category2Populate");
        if (tempCategory != null){
            completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,tempCategory);

            //highlight the obtained category button
            switch (tempCategory) {
                case "Home":
                    completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Business":
                    completedTasks_businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "School":
                    completedTasks_schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Work":
                    completedTasks_workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Shopping":
                    completedTasks_shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    //  Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
            }
        }else {

            // if the activity is loaded without IndividualCompletedTask and rescheduleCompletedTask preceding it,
            // that means the tempCategory string will be null
            // thus render the default/ first category, home and highlight to guide user
            completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Home");
            completedTasks_homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
            // Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }
        //receives a cursor that is specific by name, any cursor placed before it with the name cursor, works. CAUTION!!
        taskListPopulate();

    }
    @Override
    protected void onResume() {
        super.onResume();
        SetOrRefreshListView2();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}