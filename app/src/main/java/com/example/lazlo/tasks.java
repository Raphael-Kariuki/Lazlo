package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;

import java.util.ArrayList;
import java.util.Locale;

public class tasks extends AppCompatActivity {
DBHelper dbHelper;
tasksAdapter mAdapter;
RecyclerView recyclerView;
Cursor cursor;
SharedPreferences sharedPreferences;
Double randUserId;
ArrayList<taskModel> taskModelArrayList;
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
//TODO:work on search





        return super.onCreateOptionsMenu(menu);
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
        }else{
            mAdapter.filterList(filteredList);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pending tasks");

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(sharedPreferences.getString("randomUserId", null));
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cursor = dbHelper.getAllByCategoriesForPendingTasks(randUserId,"Home");
        taskModelArrayList = new ArrayList<>();
        while(cursor != null && cursor.moveToNext()){
            String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle"));
            String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription"));
            String taskAssociatedPrice = cursor.getString(cursor.getColumnIndexOrThrow("TaskAssociatedPrice"));
            taskModelArrayList.add(new taskModel(taskTitle,taskDescription,taskAssociatedPrice));
        }
        if (cursor != null){
            cursor.close();
        }
        mAdapter = new tasksAdapter(this,taskModelArrayList);
        recyclerView.setAdapter(mAdapter);



    }
}