package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TasksHomePage extends AppCompatActivity {

    AppCompatButton btnPendingTasks, btnCompletedTasks, btnDraftTasks, btnAddTasks;

    @Override
    public  void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_home_page);
        btnAddTasks = findViewById(R.id.btnAddTasks);
        btnPendingTasks = findViewById(R.id.btnPendingTasks);
        btnCompletedTasks = findViewById(R.id.btnCompletedTasks);
        btnDraftTasks = findViewById(R.id.btnDraftTasks);

        btnAddTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTasks.class));
            }
        });
        btnCompletedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CompletedTasks.class));
            }
        });

        btnPendingTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingTasks.class));
            }
        });

        btnDraftTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DraftTasks.class));
            }
        });
    }
}