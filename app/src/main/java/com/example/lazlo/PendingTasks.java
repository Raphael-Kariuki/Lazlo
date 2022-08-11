package com.example.lazlo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/*added code*/
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class PendingTasks extends AppCompatActivity {

    //instantiate variables
    ListView tasks_listView;
    FloatingActionButton btn_addTasks;
    String user_name;
    SharedPreferences session_prefs;
    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AppCompatTextView uname;
    MaterialButton Home, School, Work, Business, Shopping;
    String categoryToPopulate;
    Double randomUserId;
    TextView homeUnder, schoolUnder, workUnder, businessUnder, shoppingUnder;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), TasksHomePage.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case android.R.id.home:
        case R.id.myAccount :
            //add the function to perform here
            startActivity(new Intent(this, Account.class));
            return(true);
        case R.id.exit:
            //add the function to perform here
            SharedPreferences prf;
            prf = getSharedPreferences("user_details",MODE_PRIVATE);
            Intent i = new Intent(getApplicationContext(),Login.class);
            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.apply();
            startActivity(i);
            return(true);


    }
        return(super.onOptionsItemSelected(item));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_tasks);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pending tasks");

        //initialize views
        tasks_listView = this.findViewById(R.id.task_listView);

        homeUnder = findViewById(R.id.homeUnder);
        workUnder = findViewById(R.id.workUnder);
        schoolUnder = findViewById(R.id.schoolUnder);
        businessUnder = findViewById(R.id.businessUnder);
        shoppingUnder = findViewById(R.id.shoppingUnder);

        Home = findViewById(R.id.homeTask);
        Work = findViewById(R.id.workTasks);
        School =findViewById(R.id.schoolTasks);
        Business = findViewById(R.id.businessTasks);
        Shopping = findViewById(R.id.shoppingTasks);


        uname = findViewById(R.id.userName);



        //initialize db class
        dbHelper = new DBHelper(this);





        //obtain username value from sharedPreferences stored on login and set it on a textview
        session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        user_name = session_prefs.getString("username",null);
        randomUserId = Double.parseDouble(session_prefs.getString("randomUserId", null));


        // click to add task
        addNewTasks();

        populateHomeTasks();
        tasks_listView.setTextFilterEnabled(true);

        //TODO:Simplify this background setup with 9-patch drawables

        //process Home button
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));

                populateHomeTasks();
            }
        });

        //process the Shopping click
        Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                populateShoppingTasks();
            }
        });

        //process the Work click
        Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                populateWorkTasks();
            }
        });
        //process the School click
        School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                populateSchoolTasks();
            }
        });

        //process the Business click
        Business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.black));
                businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
                populateBusinessTasks();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_bar_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
//TODO:work on search





        return super.onCreateOptionsMenu(menu);
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
            switch (tempCategory) {
                case "Home":
                    homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Business":
                    businessUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Business.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "School":
                    schoolUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // School.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Work":
                    workUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    // Work.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
                case "Shopping":
                    shoppingUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));
                    //  Shopping.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    break;
            }
        }else {

            // if the activity is loaded without addTasks preceding it, that means the tempCategory string will be null
            // thus render the default/ first category, home and highlight to guide user
            cursor = dbHelper.getAllByCategories(user_name,"Home");
            homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
           // Home.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
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
    public void taskListPopulate(){
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