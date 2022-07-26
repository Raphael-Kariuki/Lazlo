package com.example.lazlo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textview.MaterialTextView;

import java.util.Date;

public class performTask extends AppCompatActivity {

    AppCompatImageButton btnStartTask2,btnPauseTask, btnResumeTask;
    AppCompatButton btnCancelDoingTask, btnCompleteDoingTask;
    Date startTaskDate, pauseTaskDate,resumeTaskDate,cancelTaskDate, completeTaskDate;
    int taskState;
    AlertDialog.Builder builder;
    long totalTaskDuration, taskId;
    DBHelper dbHelper;
    Cursor cursor;
    MaterialTextView runningTaskTitle,runningTaskDescription,runningTaskCategory,runningTaskBills,runningTaskDeadline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_task);

        dbHelper = new DBHelper(this);

        runningTaskTitle = findViewById(R.id.runningTaskTitle);
        runningTaskDescription = findViewById(R.id.runningTaskDescription);
        runningTaskCategory = findViewById(R.id.runningTaskCategory);
        runningTaskBills = findViewById(R.id.runningTaskBills);
        runningTaskDeadline = findViewById(R.id.runningTaskDeadline);


        //Handle button to process task durations
        btnStartTask2 = findViewById(R.id.btnStartTask2);
        btnPauseTask = findViewById(R.id.btnPauseTask);
        btnResumeTask = findViewById(R.id.btnResumeTask);
        btnCancelDoingTask = findViewById(R.id.btnCancelDoingTask);
        btnCompleteDoingTask = findViewById(R.id.btnCompleteDoingTask);

        btnStartTask2.setVisibility(View.VISIBLE);
        btnPauseTask.setVisibility(View.INVISIBLE);
        btnResumeTask.setVisibility(View.INVISIBLE);
        btnCancelDoingTask.setVisibility(View.VISIBLE);
        btnCompleteDoingTask.setVisibility(View.INVISIBLE);

        try {
            populateViews();
        }catch (Exception e){
            e.printStackTrace();
        }


        taskId = this.getIntent().getLongExtra("taskId", -1);




        btnStartTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.VISIBLE);
                btnResumeTask.setVisibility(View.INVISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.VISIBLE);
                taskState = 1;
                startTaskDate = new Date();
            }
        });
        btnPauseTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.INVISIBLE);
                btnResumeTask.setVisibility(View.VISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.INVISIBLE);
                taskState = 2;
                pauseTaskDate = new Date();
            }
        });
        btnResumeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartTask2.setVisibility(View.INVISIBLE);
                btnPauseTask.setVisibility(View.INVISIBLE);
                btnResumeTask.setVisibility(View.INVISIBLE);
                btnCancelDoingTask.setVisibility(View.VISIBLE);
                btnCompleteDoingTask.setVisibility(View.VISIBLE);
                taskState = 3;
                resumeTaskDate = new Date();
            }
        });

        //requires context and when placed inside setOnClickListener, it picks that as the context thus needs to be outside
        builder = new AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat);

        btnCancelDoingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setCancelable(true);
                builder.setTitle("Cancel working on a task");
                builder.setMessage("Are you sure you want to cancel working on this task?");
                builder.setPositiveButton("Yes,Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();


                taskState = 4;
                cancelTaskDate = new Date();
            }
        });
        btnCompleteDoingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTaskDate = new Date();
                if(taskState == 1){
                    totalTaskDuration = completeTaskDate.getTime() - startTaskDate.getTime();
                    long hours = totalTaskDuration/3600000;
                    long minutes = totalTaskDuration/60000;
                    long seconds = totalTaskDuration/1000;
                    System.out.println("Time taken: " + hours + ":" + minutes + ":" + seconds);
                }else if(taskState == 3){
                    totalTaskDuration = (pauseTaskDate.getTime() - startTaskDate.getTime()) + (completeTaskDate.getTime() - resumeTaskDate.getTime());
                    long hours = totalTaskDuration/3600000;
                    long minutes = totalTaskDuration/60000;
                    long seconds = totalTaskDuration/1000;
                    System.out.println("Time taken: " + hours + ":" + minutes + ":" + seconds);
                }
                Intent backToTasks = new Intent(getApplicationContext(), FinalPage.class);
                startActivity(backToTasks);
            }
        });



    }


    public void populateViews(){
        String Title = this.getIntent().getStringExtra("taskTitle");
        String Description = this.getIntent().getStringExtra("taskDescription");
        String Category = this.getIntent().getStringExtra("taskCategory");
        String Bills = this.getIntent().getStringExtra("taskBills");
        String Deadline = this.getIntent().getStringExtra("taskDeadline");

        runningTaskTitle.setText(Title);
        runningTaskDescription.setText(Description);
        runningTaskCategory.setText(Category);
        runningTaskBills.setText("Kshs " + Bills);
        runningTaskDeadline.setText(Deadline);
    }



}