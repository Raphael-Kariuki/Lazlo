package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class rescheduleCompletedTask extends AppCompatActivity {


TextInputLayout completedIndividualTaskTitle_TextLayout,completedIndividualTaskDescription_TextLayout,completedIndividualTaskCategory_TextLayout
        ,completedIndividualTaskBills_TextLayout,completedIndividualTaskDateDeadline_TextLayout,completedIndividualTaskTimeDeadline_TextLayout;
TextInputEditText completedIndividualTaskTitle_TextInputEdit,completedIndividualTaskDescription_TextInputEdit,completedIndividualTaskBills_TextInputEdit,completedIndividualTaskDateDeadline_TextInputEdit,completedIndividualTaskTimeDeadline_TextInputEdit;
MaterialButton btnCompletedTaskSave;
AutoCompleteTextView completedIndividualTaskCategory_TextInputEdit;

long task2RescheduleId;
SharedPreferences spf;
Double randUserId, randTaskId;
DBHelper dbHelper;
String time2Insert;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_completed_task);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Reschedule task");

        completedIndividualTaskTitle_TextLayout = findViewById(R.id.completedIndividualTaskTitle_TextLayout);
        completedIndividualTaskDescription_TextLayout = findViewById(R.id.completedIndividualTaskDescription_TextLayout);
        completedIndividualTaskCategory_TextLayout = findViewById(R.id.completedIndividualTaskCategory_TextLayout);
        completedIndividualTaskBills_TextLayout = findViewById(R.id.completedIndividualTaskBills_TextLayout);
        completedIndividualTaskDateDeadline_TextLayout = findViewById(R.id.completedIndividualTaskDateDeadline_TextLayout);
        completedIndividualTaskTimeDeadline_TextLayout = findViewById(R.id.completedIndividualTaskTimeDeadline_TextLayout);


        completedIndividualTaskTitle_TextInputEdit = findViewById(R.id.completedIndividualTaskTitle_TextInputEdit);
        completedIndividualTaskDescription_TextInputEdit = findViewById(R.id.completedIndividualTaskDescription_TextInputEdit);
        completedIndividualTaskCategory_TextInputEdit = findViewById(R.id.completedIndividualTaskCategory_TextInputEdit);
        completedIndividualTaskBills_TextInputEdit = findViewById(R.id.completedIndividualTaskBills_TextInputEdit);
        completedIndividualTaskDateDeadline_TextInputEdit = findViewById(R.id.completedIndividualTaskDateDeadline_TextInputEdit);
        completedIndividualTaskTimeDeadline_TextInputEdit = findViewById(R.id.completedIndividualTaskTimeDeadline_TextInputEdit);

        btnCompletedTaskSave = findViewById(R.id.btnCompletedTaskSave);


        task2RescheduleId = getIntent().getLongExtra("task2RescheduleId", -1);

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId",null));

        dbHelper = new DBHelper(this);

        //obtain taskId
        randTaskId = returnTaskId(task2RescheduleId,randUserId);

        //obtain details from taskList using previously obtained taskId
        Cursor task2RescheduleDetailsCursor = obtainTaskDetailsByTaskId(randUserId, randTaskId);

        //populate the views with details from db on task to reschedule
        setTextOnViews(task2RescheduleDetailsCursor);




        completedIndividualTaskTimeDeadline_TextInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });
        completedIndividualTaskDateDeadline_TextInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               selectDate();
            }
        });


        //TODO:process input and prepare for insertion remembering while in inserting use new taskId plus process taskParentId. Check on storing an array in db so as to store trail of parents

        /*
         * Question that pops up is whether to retain the previous task _id or use a new one. I think it's wise to delete the task and only update that it's a rescheduled
         * task by an integer that declares so. If we don't delete the task, rescheduled tasks will increase the count of total tasks, yet it's the same task.
         * Apart from setting up a determiner of scheduled tasks, wwe can also create a table that receives non-unique taskIds and their previous completion dates.
         * We also have to delete the task from Completed_n_deletedTasks. Question 2, why not delete from completed tasks yet delete from task list?
         *
         * Decision: Not delete from either. In inserting the rescheduled task, treat it as a brand new task, assign new taskId.However intro taskParentId to create
         * a traceable link
         * Also not delete from completed task. Rationale behind this , if you work out monday and tuesday, both a re workouts, do you say you worked out
         * once or twice?
         *
         * */



    }
    private Double returnTaskId(long listViewTaskId, Double randUserId){
        //first obtain taskId to use that in later obtaining required task details from taskList table
        Cursor randTaskIdCursor = null;
        double randTaskId = 0.0;
        try {
            randTaskIdCursor =  dbHelper.obtainCompletedTask2RescheduleRandTaskIdByListViewId(listViewTaskId,randUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //obtain taskId from cursor
        if (randTaskIdCursor != null && randTaskIdCursor.moveToFirst()){
            randTaskId = randTaskIdCursor.getDouble(randTaskIdCursor.getColumnIndexOrThrow("randTaskId"));
        }
        return randTaskId;
    }
    private Cursor obtainTaskDetailsByTaskId(Double randUserId, Double randTaskId){
        //obtain details from taskList using previously obtained taskId
        Cursor task2RescheduleDetailsCursor = null;
        try {
            task2RescheduleDetailsCursor = dbHelper.obtainCompletedTaskDetailsByRandomTaskId(randUserId, randTaskId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return task2RescheduleDetailsCursor;
    }
    private void setTextOnViews(Cursor task2RescheduleDetailsCursor){
        //instantiate variables
        String taskTitle, taskDescription, taskCategory, taskAssociatedPrice,taskDeadline;

        if (task2RescheduleDetailsCursor != null && task2RescheduleDetailsCursor.moveToFirst() ){
            try {
                //assign variables with data from cursor
                taskTitle = task2RescheduleDetailsCursor.getString(task2RescheduleDetailsCursor.getColumnIndexOrThrow("TaskTitle"));
                taskDescription = task2RescheduleDetailsCursor.getString(task2RescheduleDetailsCursor.getColumnIndexOrThrow("TaskDescription"));
                taskCategory = task2RescheduleDetailsCursor.getString(task2RescheduleDetailsCursor.getColumnIndexOrThrow("TaskCategory"));
                taskAssociatedPrice = task2RescheduleDetailsCursor.getString(task2RescheduleDetailsCursor.getColumnIndexOrThrow("TaskAssociatedPrice"));
                taskDeadline = task2RescheduleDetailsCursor.getString(task2RescheduleDetailsCursor.getColumnIndexOrThrow("TaskDeadline"));

                String[] deadline = taskDeadline.split("T",2 );

                completedIndividualTaskTitle_TextInputEdit.setText(taskTitle);
                completedIndividualTaskDescription_TextInputEdit.setText(taskDescription);
                completedIndividualTaskCategory_TextInputEdit.setText(taskCategory);
                completedIndividualTaskBills_TextInputEdit.setText(taskAssociatedPrice);
                completedIndividualTaskDateDeadline_TextInputEdit.setText(deadline[0]);
                completedIndividualTaskTimeDeadline_TextInputEdit.setText(deadline[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (hour < 10){
                    time2Insert = "0" + hour + ":" + minute;
                }else{
                    time2Insert = hour + ":" + minute;
                }
                houseOfCommons houseOfCommons = new houseOfCommons();
                completedIndividualTaskTimeDeadline_TextInputEdit.setText(houseOfCommons.FormatTime(hour, minute));
            }
        },hour, minute,false);
        timePickerDialog.show();
    }
    private void selectDate(){
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(rescheduleCompletedTask.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String formattedMonth,formattedDay;
                if (monthOfYear + 1 <= 9){
                    formattedMonth = "0" + (monthOfYear + 1) ;
                }else{
                    formattedMonth = String.valueOf(monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    formattedDay = "0" + dayOfMonth;
                }else{
                    formattedDay = String.valueOf(dayOfMonth);
                }
                completedIndividualTaskDateDeadline_TextInputEdit.setText(formattedDay + "-" + formattedMonth + "-" + year);
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
}