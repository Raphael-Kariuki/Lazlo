package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;

import java.util.Calendar;
import java.util.Date;

public class AddTasks extends AppCompatActivity {
    EditText task_title,task_description,select_date;
    DatePickerDialog datePickerDialog;
    ImageButton btn_saveTasks, btn_cancelTaskCreation;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        task_title = (EditText) findViewById(R.id.taskTitle);
        task_description = (EditText) findViewById(R.id.taskDescription);
        select_date = (EditText) findViewById(R.id.select_date);
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(calendar.DAY_OF_MONTH);


                //date picker dialog

                datePickerDialog = new DatePickerDialog(AddTasks.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        select_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear,mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btn_saveTasks = (ImageButton) findViewById(R.id.btn_saveTask);
        dbHelper = new DBHelper(this);
        btn_saveTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                // get the string email broadcasted from login to stand in as the
                //determiner of who enters tasks. Should be replaced by the username or userId
                String userName = intent.getStringExtra("email").toString().trim();
                String taskTitle_String = task_title.getText().toString().trim();
                String taskDescription_String = task_description.getText().toString().trim();
                //will process dates later
                boolean b = dbHelper.insertTasks(userName, taskTitle_String,taskDescription_String);
                if (taskTitle_String.equals("") && taskDescription_String.equals("")){
                    Toast.makeText(getApplicationContext(), "Missing content", Toast.LENGTH_LONG).show();
                }
                if (b){
                    //put out notification of success and redirect to taskview
                }
            }
        });
    }
}