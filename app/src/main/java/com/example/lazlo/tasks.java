package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;

import java.util.ArrayList;

public class tasks extends AppCompatActivity {
DBHelper dbHelper;
tasksAdapter mAdapter;
RecyclerView recyclerView;
Cursor cursor;
SharedPreferences sharedPreferences;
Double randUserId;
ArrayList<taskModel> taskModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

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
        cursor.close();
        mAdapter = new tasksAdapter(this,taskModelArrayList);
        recyclerView.setAdapter(mAdapter);



    }
}