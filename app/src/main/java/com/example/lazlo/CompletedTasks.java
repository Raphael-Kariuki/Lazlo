package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CompletedTasks extends AppCompatActivity {

    ListView completedTasks_tasks_listView;
    FloatingActionButton completedTasks_hamburger_menu;
    String completedTasks_user_name;
    SharedPreferences completedTasks_session_prefs;
    DBHelper completedTasks_dbHelper;
    Cursor completedTasks_cursor;
    SimpleCursorAdapter completedTasks_simpleCursorAdapter;
    AppCompatTextView completedTasks_uname;
    AppCompatButton completedTasks_home, completedTasks_school,completedTasks_work,completedTasks_business,completedTasks_shopping;
    Double completedTasks_randUserId;


    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), TasksHomePage.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);


        //initialize views
        completedTasks_tasks_listView = this.findViewById(R.id.completedTasks_task_listView);

        completedTasks_home = findViewById(R.id.completedTasks_homeTask);
        completedTasks_work = findViewById(R.id.completedTasks_workTasks);
        completedTasks_school =findViewById(R.id.completedTasks_schoolTasks);
        completedTasks_business = findViewById(R.id.completedTasks_businessTasks);
        completedTasks_shopping = findViewById(R.id.completedTasks_shoppingTasks);

        completedTasks_hamburger_menu = findViewById(R.id.completedTasks_hamburger_menu);

        completedTasks_uname = findViewById(R.id.completedTasks_userName);

        //initialize db class
        completedTasks_dbHelper = new DBHelper(this);

        //obtain username value from sharedPreferences stored on login and set it on a textview
        completedTasks_session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        completedTasks_user_name = completedTasks_session_prefs.getString("username",null);
        completedTasks_uname.setText(completedTasks_user_name);
        completedTasks_randUserId = Double.parseDouble(completedTasks_session_prefs.getString("randomUserId", null));
        System.out.println(completedTasks_randUserId + " "+ completedTasks_user_name);


        //process hamburger menu action
        completedTasks_hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Account.class);
            startActivity(intent);

        });
            //TODO:Simplify this background setup with 9-patch drawables

            //process completedTasks_home button
            completedTasks_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(completedTasks_randUserId + " "+ completedTasks_user_name);
                    //set background on click of completedTasks_home button. Black for completedTasks_home, white for the rest
                    completedTasks_home.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                    completedTasks_work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                    //set text color to white while clicked, black when not
                    completedTasks_home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    populateCompletedTasks_HomeTasks();
                }
            });

            //process the completedTasks_shopping click
            completedTasks_shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //set background on click of completedTasks_home button. Black for completedTasks_shopping, white for the rest
                    completedTasks_shopping.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                    completedTasks_work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                    //set text color to white while clicked, black when not
                    completedTasks_shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    populateCompletedTasks_ShoppingTasks();
                }
            });

            //process the completedTasks_work click
            completedTasks_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //set background on click of completedTasks_home button. Black for completedTasks_work, white for the rest
                    completedTasks_work.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                    completedTasks_shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                    //set text color to white while clicked, black when not
                    completedTasks_work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    populateCompletedTasks_WorkTasks();
                }
            });
            //process the completedTasks_school click
            completedTasks_school.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //set background on click of completedTasks_home button. Black for completedTasks_school, white for the rest
                    completedTasks_school.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                    completedTasks_shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                    //set text color to white while clicked, black when not
                    completedTasks_school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    populateCompletedTasks_SchoolTasks();
                }
            });

            //process the completedTasks_business click
            completedTasks_business.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //set background on click of completedTasks_home button. Black for completedTasks_business, white for the rest
                    completedTasks_business.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                    completedTasks_shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

                    //set text color to white while clicked, black when not
                    completedTasks_business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    completedTasks_shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    completedTasks_home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                    populateCompletedTasks_BusinessTasks();
                }
            });


        
        
    }

    //obtain home category content and populate list view on home button click
    private  void populateCompletedTasks_HomeTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Home");
        taskListPopulate();

    }
    //obtain shopping category content and populate list view on shopping button click
    private  void populateCompletedTasks_ShoppingTasks(){
        completedTasks_cursor = completedTasks_dbHelper.getAllByCategories(completedTasks_randUserId,"Shopping");
        taskListPopulate();

    }
    //obtain work category content and populate list view on work button click
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
            completedTasks_simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,completedTasks_cursor,new String[]{"completedTaskTitle","completedTaskDescription","completedTaskDeadline","completedTaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedDates,R.id.TaskAssociatedPrice},0);
            completedTasks_tasks_listView.setAdapter(completedTasks_simpleCursorAdapter);

            //setup a click listener for a task
            completedTasks_tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }else {
            //necessary to re-obtain updated task list from the db
            completedTasks_simpleCursorAdapter.swapCursor(completedTasks_cursor);
        }
    }
}