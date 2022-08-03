package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/*added code*/
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class FinalPage extends AppCompatActivity {

    //instantiate variables
    ListView tasks_listView,task1;
    FloatingActionButton btn_addTasks,hamburger_menu;
    String s2;
    SharedPreferences session_prefs;
    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AppCompatTextView uname;
    AppCompatButton home, school,work,business,shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);



        //initialize views
        tasks_listView = this.findViewById(R.id.task_listView);

        home = findViewById(R.id.homeTask);
        work = findViewById(R.id.workTasks);
        school =findViewById(R.id.schoolTasks);
        business = findViewById(R.id.businessTasks);
        shopping = findViewById(R.id.shoppingTasks);

        hamburger_menu = findViewById(R.id.hamburger_menu);

        uname = findViewById(R.id.userName);



        //initialize db class
        dbHelper = new DBHelper(this);

        //necessary to call this in-order to repopulate list with current set of tasks on deletion of a single tasks
        SetOrRefreshListView();


        //obtain username value from sharedPreferences stored on login and set it on a textview
        session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        s2 = session_prefs.getString("username",null);
        uname.setText(s2);


        // click to add task
        addNewTasks();


        //process hamburger menu action
        hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), myAccount.class);
            startActivity(intent);
        });



        //process home button

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Simplify this with 9patch
                //set background on click of home button. Black for home, white for the rest
                home.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateHomeTasks();
            }
        });
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of home button. Black for shopping, white for the rest
                shopping.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateHomeShopping();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of home button. Black for work, white for the rest
                work.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateWorkTasks();
            }
        });
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of home button. Black for school, white for the rest
                school.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateSchoolTasks();
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of home button. Black for business, white for the rest
                business.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                school.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

                //set text color to white while clicked, black when not

                business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                school.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateBusinessTasks();
            }
        });


    }



    public void SetOrRefreshListView(){
        cursor = dbHelper.getAll(s2);
        taskListPopulate();

    }
    @Override
    protected void onResume(){
        super.onResume();
        SetOrRefreshListView();
        //refresh listview when returning to the activity
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
        //clean up
    }




    private void addNewTasks(){
        btn_addTasks =  findViewById(R.id.btn_addTasks);
        btn_addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTasks.class);
                intent.putExtra("username", s2);
                startActivity(intent);
            }
        });
    }
    private  void populateHomeTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Home");
        taskListPopulate();

    }
    private  void populateHomeShopping(){
        cursor = dbHelper.getAllByCategories(s2,"Shopping");
        taskListPopulate();

    }
    private  void populateWorkTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Work");
        taskListPopulate();

    }
    private  void populateSchoolTasks(){
        cursor = dbHelper.getAllByCategories(s2,"School");
        taskListPopulate();

    }
    private  void populateBusinessTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Business");
        taskListPopulate();

    }

    //function to populate list view, initially on 1st load with all tasks
    private void taskListPopulate(){
        if (simpleCursorAdapter == null){
            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,cursor,new String[]{"TaskTitle","TaskDescription","TaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedPrice},0);
            tasks_listView.setAdapter(simpleCursorAdapter);

            //setup a click listener for a task
            tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(),individualTask.class);
                    intent.putExtra("my_id_extra", l);
                    startActivity(intent);
                }
            });

            //setup a long-click listener for a task
            tasks_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Cursor getTitleForDeletionConfirm = null;
                    String taskTitle = null;
                    try {
                        getTitleForDeletionConfirm = dbHelper.getTaskById(l);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (getTitleForDeletionConfirm.moveToFirst()){
                        taskTitle = getTitleForDeletionConfirm.getString(getTitleForDeletionConfirm.getColumnIndexOrThrow("TaskTitle"));
                    }

                    //create and show a dialog to ensure that the user wants to delete the long-clicked task
                    AlertDialog.Builder builder = new AlertDialog.Builder(FinalPage.this);
                    builder.setCancelable(true);
                    builder.setTitle("Delete task");
                    builder.setMessage("Are you sure you want to delete task " + taskTitle + " ?");


                    //if yes, delete task and repopulate list view
                    builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.deleteTask(l);
                            Toast.makeText(FinalPage.this, "task delete successfully", Toast.LENGTH_SHORT).show();
                            SetOrRefreshListView();
                        }
                    });

                    //if no, close dialog and continue
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }else {
            simpleCursorAdapter.swapCursor(cursor);
        }
    }


}