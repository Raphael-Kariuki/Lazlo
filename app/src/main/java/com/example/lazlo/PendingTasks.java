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


public class PendingTasks extends AppCompatActivity {

    //instantiate variables
    ListView tasks_listView;
    FloatingActionButton btn_addTasks,hamburger_menu;
    String user_name;
    SharedPreferences session_prefs;
    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AppCompatTextView uname;
    AppCompatButton Home, School, Work, Business, Shopping;
    String categoryToPopulate;
    Double randomUserId;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), TasksHomePage.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_tasks);



        //initialize views
        tasks_listView = this.findViewById(R.id.task_listView);

        Home = findViewById(R.id.homeTask);
        Work = findViewById(R.id.workTasks);
        School =findViewById(R.id.schoolTasks);
        Business = findViewById(R.id.businessTasks);
        Shopping = findViewById(R.id.shoppingTasks);

        hamburger_menu = findViewById(R.id.hamburger_menu);

        uname = findViewById(R.id.userName);



        //initialize db class
        dbHelper = new DBHelper(this);





        //obtain username value from sharedPreferences stored on login and set it on a textview
        session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        user_name = session_prefs.getString("username",null);
        randomUserId = Double.parseDouble(session_prefs.getString("randomUserId", null));
        uname.setText(user_name);


        // click to add task
        addNewTasks();


        //process hamburger menu action
        hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Account.class);
            startActivity(intent);
        });

        //TODO:Simplify this background setup with 9-patch drawables

        //process Home button
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of Home button. Black for Home, white for the rest
                Home.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                School.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateHomeTasks();
            }
        });

        //process the Shopping click
        Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of Home button. Black for Shopping, white for the rest
                Shopping.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                School.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateShoppingTasks();
            }
        });

        //process the Work click
        Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of Home button. Black for Work, white for the rest
                Work.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                School.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateWorkTasks();
            }
        });
        //process the School click
        School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of Home button. Black for School, white for the rest
                School.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Business.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


                //set text color to white while clicked, black when not
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateSchoolTasks();
            }
        });

        //process the Business click
        Business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set background on click of Home button. Black for Business, white for the rest
                Business.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Shopping.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Work.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                School.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

                //set text color to white while clicked, black when not
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                populateBusinessTasks();
            }
        });


    }


    //This is called when a task is deleted from the list view
    //receives a category string obtained from the deleted task, why? A deleted task will have a similar category with the rest that are rendered then.
    //We don't want  that when a task is deleted, the view jumps to a different category. User experience is key, no matter the lines of code required
    //that is then used to retrieve content from db of that specific category
    // and call a function to repopulate the task list
    //TODO:find a way to shorten this preceding bulk of code

    private void SetOrRefreshListView(){
        cursor = dbHelper.getAllByCategories(user_name,categoryToPopulate);
        System.out.println(categoryToPopulate);
        taskListPopulate();

    }

    //this function on the other hand receives a category string from addTasks. This is to ensure once a task is added, it's category is automatically
    // rendered on the list view, not some different category. The specific category button is also highlighted . UX matters
    private void SetOrRefreshListView2(){
        String tempCategory = this.getIntent().getStringExtra("tempCategory");
        if (tempCategory != null){
            cursor = dbHelper.getAllByCategories(user_name,tempCategory);

            //highlight the obtained category button
            if (tempCategory.equals("Home")){
                Home.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            }else if (tempCategory.equals("Business")){
                Business.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            }else if (tempCategory.equals("School")){
                School.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            }else if (tempCategory.equals("Work")){
                Work.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            }else if (tempCategory.equals("Shopping")){
                Shopping.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
                Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            }
        }else {

            // if the activity is loaded without addTasks preceding it, that means the tempCategory string will be null
            // thus render the default/ first category, home and highlight to guide user
            cursor = dbHelper.getAllByCategories(user_name,"Home");
            Home.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.cinq));
            Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }
        //receives a cursor that is specific by name, any cursor placed before it with the name cursor, works. CAUTION!!
        taskListPopulate();

    }

    //refresh listview when returning to the activity
    @Override
    protected void onResume(){
        super.onResume();
        SetOrRefreshListView2();
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

    //obtain Home category content and populate list view on Home button click
    private  void populateHomeTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Home");
        taskListPopulate();

    }
    //obtain Shopping category content and populate list view on Shopping button click
    private  void populateShoppingTasks(){
        cursor = dbHelper.getAllByCategories(user_name,"Shopping");
        taskListPopulate();

    }
    //obtain Work category content and populate list view on Work button click
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(PendingTasks.this);
                    builder.setCancelable(true);
                    builder.setTitle("Delete task");
                    builder.setMessage("Are you sure you want to delete task " + taskTitle + " ?");


                    //if yes, delete task and repopulate list view
                    builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean b = false;
                            try {

                                b = dbHelper.deleteTask(l, randomUserId);
                                Cursor Cursor = dbHelper.getTaskById(l, randomUserId);
                                categoryToPopulate = cursor.getString(cursor.getColumnIndexOrThrow("TaskCategory"));



                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (b){
                                Toast.makeText(PendingTasks.this, "task delete successfully", Toast.LENGTH_SHORT).show();
                                SetOrRefreshListView();
                            }else{
                                Toast.makeText(PendingTasks.this, "task delete unsuccessful", Toast.LENGTH_SHORT).show();
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
            getTitleForDeletionConfirm = dbHelper.getTaskById(taskIdOnList,randomUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (getTitleForDeletionConfirm.moveToFirst()){
            taskTitle = getTitleForDeletionConfirm.getString(getTitleForDeletionConfirm.getColumnIndexOrThrow("TaskTitle"));
        }
        return taskTitle;
    }

}