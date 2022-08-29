package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

public class tasks extends AppCompatActivity {
DBHelper dbHelper;
public tasksAdapter mAdapter;
RecyclerView recyclerView;
Cursor cursor;
SharedPreferences sharedPreferences;
Double randUserId;
ArrayList<taskModel> taskModelArrayList;
TabLayout tabLayout;
FloatingActionButton btn_addTasks;
String user_name,categoryToPopulateOnSort;
Integer stateToDetermineSortDeadlines,stateToDetermineSortPrice,stateToDetermineSortDuration,stateToDetermineSortCreation;
DrawerLayout drawerLayout;
ActionBarDrawerToggle actionBarDrawerToggle;
NavigationView navigationView;
@Override
public void onBackPressed(){}
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_bar_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        if (actionBarDrawerToggle.onOptionsItemSelected(menuItem)){
            return true;
        }
        switch (menuItem.getItemId()){
            case R.id.sorting:
                return true;

            case R.id.sortByDates:
                if(stateToDetermineSortDeadlines == null){
                    taskModelArrayList.sort(taskModel.tasksDeadlineComparatorDesc);
                    stateToDetermineSortDeadlines = 1;
                }else if (stateToDetermineSortDeadlines == 1){
                    taskModelArrayList.sort(taskModel.tasksDeadlineComparatorAsc);
                    stateToDetermineSortDeadlines = null;
                }
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.sortByCreationTime:
                if(stateToDetermineSortCreation == null){
                    taskModelArrayList.sort(taskModel.tasksCreationComparatorDesc);
                    stateToDetermineSortCreation = 1;
                }else if (stateToDetermineSortCreation == 1){
                    taskModelArrayList.sort(taskModel.tasksCreationComparatorAsc);
                    stateToDetermineSortCreation = null;
                }
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.sortByPrice:
                if(stateToDetermineSortPrice == null){
                    taskModelArrayList.sort(taskModel.tasksPriceComparatorDesc);
                    stateToDetermineSortPrice = 1;
                }else if (stateToDetermineSortPrice == 1){
                    taskModelArrayList.sort(taskModel.tasksPriceComparatorAsc);
                    stateToDetermineSortPrice = null;
                }
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.sortByDuration:
                if(stateToDetermineSortDuration == null){
                    taskModelArrayList.sort(taskModel.tasksDurationComparatorDesc);
                    stateToDetermineSortDuration = 1;
                }else if (stateToDetermineSortDuration == 1){
                    taskModelArrayList.sort(taskModel.tasksDurationComparatorAsc);
                    stateToDetermineSortDuration = null;
                }
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }
    private void filter(String s) {
        ArrayList<taskModel> filteredList = new ArrayList<>();
        for (taskModel item: taskModelArrayList){
            Locale locale = new Locale("en","KE");
            if (item.getTaskTitle().toLowerCase(locale).contains(s.toLowerCase(locale))
                    || item.getTaskDescription().toLowerCase(locale).contains(s.toLowerCase(locale))){
                filteredList.add(item);

            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        }
        mAdapter.filterList(filteredList);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //initialize database class helper
        dbHelper = new DBHelper(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        ///setup action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pending tasks");


        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_account:
                    startActivity(new Intent(getApplicationContext(), editAccount.class));
                    return true;
                case R.id.nav_dashboard:
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                    return true;
                case R.id.nav_timeTracker:
                    startActivity(new Intent(getApplicationContext(), dailyReview.class));
                    return true;
                case R.id.nav_addTasks:
                    startActivity(new Intent(getApplicationContext(),AddTasks.class));
                    return true;
                case R.id.nav_pendingTasks:
                    startActivity(new Intent(getApplicationContext(), tasks.class));
                    return true;
                case R.id.nav_completedTasks:
                    startActivity(new Intent(getApplicationContext(), completed.class));
                    break;
                case R.id.nav_draftTasks:
                    startActivity(new Intent(getApplicationContext(),DraftTasks.class));
                    break;
                case R.id.nav_security:
                    startActivity(new Intent(getApplicationContext(), inHousePasswordReset.class));
                    break;
                case R.id.nav_logout:
                    SharedPreferences prf;
                    prf = getSharedPreferences("user_details",MODE_PRIVATE);
                    Intent i = new Intent(getApplicationContext(),Login.class);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(i);
                    break;

            }
            return false;
        });
        tabLayout = findViewById(R.id.menu);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        populateHomeTasks();
                        break;
                    case 1:
                        populateShoppingTasks();

                        break;
                    case 2:
                        populateSchoolTasks();

                        break;
                    case 3:
                        populateBusinessTasks();

                        break;
                    case 4:
                        populateWorkTasks();

                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        //obtain user id and name from sharedPreferences
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        //if randUserId doesn't exist in sharedPreferences, then exit to login page, this is unauthorized access
        try {
            randUserId = Double.parseDouble(sharedPreferences.getString("randomUserId", null));
        }catch (NullPointerException e){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        user_name = sharedPreferences.getString("username",null);

        // process add task
        btn_addTasks =  findViewById(R.id.btn_addTasks);
        btn_addTasks.setOnClickListener(view -> addNewTasks());



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        populateHomeTasks();

        //homeUnder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.orange));
        categoryToPopulateOnSort = "Home";







    }
    //obtain Home category content and populate list view on Home button click
    private  void populateHomeTasks(){
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"Home");
        taskListPopulate();

    }
    //obtain Shopping category content and populate list view on Shopping button click
    private  void populateShoppingTasks(){
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"Shopping");
        taskListPopulate();

    }
    //obtain Work category content and populate list view on Work button click
    private  void populateWorkTasks(){
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"Work");
        taskListPopulate();

    }
    //obtain School category content and populate list view on School button click
    private  void populateSchoolTasks(){
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"School");
        taskListPopulate();

    }
    //obtain Business category content and populate list view on Business button click
    private  void populateBusinessTasks(){
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"Business");
        taskListPopulate();

    }
    //process add task floating button and redirect to add tasks page
    private void addNewTasks(){
        Intent intent = new Intent(getApplicationContext(),AddTasks.class);
        intent.putExtra("username", user_name);
        startActivity(intent);
    }
    private void taskListPopulate(){
        taskModelArrayList = new ArrayList<>();

        while(cursor != null && cursor.moveToNext()){
            Double randTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("randTaskId"));
            Double randUserId = cursor.getDouble(cursor.getColumnIndexOrThrow("randUserId"));
            String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle"));
            String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription"));
            String taskAssociatedPrice = cursor.getString(cursor.getColumnIndexOrThrow("TaskAssociatedPrice"));
            String taskCategory = cursor.getString(cursor.getColumnIndexOrThrow("TaskCategory"));
            String taskCreationTime = cursor.getString(cursor.getColumnIndexOrThrow("TaskCreationTime"));
            String taskDeadline = cursor.getString(cursor.getColumnIndexOrThrow("TaskDeadline"));
            String taskPredictedDuration = cursor.getString(cursor.getColumnIndexOrThrow("TaskPredictedDuration"));
            String taskState = cursor.getString(cursor.getColumnIndexOrThrow("taskState"));
            String parentTaskId = cursor.getString(cursor.getColumnIndexOrThrow("parentTaskId"));
            taskModelArrayList.add(new taskModel(randTaskId, randUserId,taskTitle,taskDescription,taskAssociatedPrice,taskCategory,
                    taskCreationTime,taskDeadline,taskPredictedDuration, taskState , parentTaskId));
        }


        //Collections.sort(taskModelArrayList,taskModel.tasksComparator);
        mAdapter = new tasksAdapter(this,taskModelArrayList);
        recyclerView.setAdapter(mAdapter);

    }


    //this function on the other hand receives a category string from addTasks. This is to ensure once a task is added, it's category is automatically
    // rendered on the list view, not some different category. The specific category button is also highlighted . UX matters
    @SuppressLint("NotifyDataSetChanged")
    private void SetOrRefreshListView2(){
        String tempCategory = this.getIntent().getStringExtra("tempCategory");
        if (tempCategory != null){
            cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,tempCategory);
            //call this so that new cursor is picked up
            mAdapter.notifyDataSetChanged();
            taskListPopulate();
        }


    }
    @Override
    public void onResume() {
        SetOrRefreshListView2();
        super.onResume();
    }
    //clean up
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}