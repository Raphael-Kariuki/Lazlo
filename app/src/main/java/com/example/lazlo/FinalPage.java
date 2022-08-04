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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FinalPage extends AppCompatActivity {

    //instantiate variables
    ListView tasks_listView,task1;
    FloatingActionButton btn_addTasks,hamburger_menu;
    String user_name;
    SharedPreferences session_prefs;
    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AppCompatTextView uname;
    AppCompatButton home, school,work,business,shopping;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), myAccount.class));
    }
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
        user_name = session_prefs.getString("username",null);
        uname.setText(user_name);


        // click to add task
        addNewTasks();


        //process hamburger menu action
        hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), myAccount.class);
            startActivity(intent);
        });

        //TODO:Simplify this background setup with 9-patch drawables

        //process home button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        //process the shopping click
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
                populateShoppingTasks();
            }
        });

        //process the work click
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
        //process the school click
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

        //process the business click
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


    //retrieve content from db and call a function to repopulate the task list
    private void SetOrRefreshListView(){
        cursor = dbHelper.getAll(user_name);
        taskListPopulate();

    }

    //refresh listview when returning to the activity
    @Override
    protected void onResume(){
        super.onResume();
        SetOrRefreshListView();
    }

    //clean up
    @Override
    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
    }




    //process add task floating button and redirect to add tasks page
    private void addNewTasks(){
        btn_addTasks =  findViewById(R.id.btn_addTasks);
        btn_addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTasks.class);
                intent.putExtra("username", user_name);
                startActivity(intent);
            }
        });
    }

    //obtain home category content and populate list view on home button click
    private  void populateHomeTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Home");
        taskListPopulate();

    }
    //obtain shopping category content and populate list view on shopping button click
    private  void populateShoppingTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Shopping");
        taskListPopulate();

    }
    //obtain work category content and populate list view on work button click
    private  void populateWorkTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Work");
        taskListPopulate();

    }
    //obtain School category content and populate list view on School button click
    private  void populateSchoolTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"School");
        taskListPopulate();

    }
    //obtain Business category content and populate list view on Business button click
    private  void populateBusinessTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Business");
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

                    //call and assign function that return a string : taskTitle
                    String taskTitle = getTaskTitleForDeletion(l);

                    //create and show a dialog to ensure that the user wants to delete the long-clicked task
                    AlertDialog.Builder builder = new AlertDialog.Builder(FinalPage.this);
                    builder.setCancelable(true);
                    builder.setTitle("Delete task");
                    builder.setMessage("Are you sure you want to delete task " + taskTitle + " ?");


                    //if yes, delete task and repopulate list view
                    builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean b = false;
                            try {
                                b = dbHelper.deleteTask(l);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (b){
                                Toast.makeText(FinalPage.this, "task delete successfully", Toast.LENGTH_SHORT).show();
                                SetOrRefreshListView();
                            }else{
                                Toast.makeText(FinalPage.this, "task delete unsuccessful", Toast.LENGTH_SHORT).show();
                            }
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
            //necessary to re-obtain updated task list from the db
            simpleCursorAdapter.swapCursor(cursor);
        }
    }


    //function that returns the taskTitle when a specific task is clicked
    
    private String getTaskTitleForDeletion(long taskIdOnList){
        Cursor getTitleForDeletionConfirm = null;
        String taskTitle = null;
        try {
            getTitleForDeletionConfirm = dbHelper.getTaskById(taskIdOnList);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (getTitleForDeletionConfirm.moveToFirst()){
            taskTitle = getTitleForDeletionConfirm.getString(getTitleForDeletionConfirm.getColumnIndexOrThrow("TaskTitle"));
        }
        return taskTitle;
    }

}