package com.example.lazlo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class rescheduleCompletedTask extends AppCompatActivity {


TextInputLayout completedIndividualTaskTitle_TextLayout,completedIndividualTaskDescription_TextLayout,completedIndividualTaskCategory_TextLayout
        ,completedIndividualTaskBills_TextLayout,completedIndividualTaskDateDeadline_TextLayout,completedIndividualTaskTimeDeadline_TextLayout
        ,completedIndividualTaskPredictedDuration_TextInputLayout,completedIndividualTaskPredictedDurationUnits_TextInputLayout;
TextInputEditText completedIndividualTaskTitle_TextInputEdit,completedIndividualTaskDescription_TextInputEdit,
        completedIndividualTaskBills_TextInputEdit,completedIndividualTaskDateDeadline_TextInputEdit,
        completedIndividualTaskTimeDeadline_TextInputEdit,completedIndividualTaskPredictedDuration_TextInputEditText;
String completedIndividualTaskTitle_str,completedIndividualTaskDescription_str,completedIndividualTaskCategory_str,
        completedIndividualTaskBills_str,completedIndividualTaskDateDeadline_str,completedIndividualTaskTimeDeadline_str,
        completedIndividualTaskPredictedDurationUnits_str,completedIndividualTaskPredictedDuration_str;
MaterialButton btnCompletedTaskSave;
AutoCompleteTextView completedIndividualTaskCategory_AutoCompleteTextView,completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView;

long task2RescheduleId;
SharedPreferences spf;
Double randUserId, randTaskId;
DBHelper dbHelper;
String time2Insert, parentTaskId;
LocalDateTime completedTaskDateDeadline2Insert;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent back2CompletedTasksView = new Intent(getApplicationContext(), completed.class);
            back2CompletedTasksView.putExtra("category2Populate",completedIndividualTaskCategory_str);
            startActivity(back2CompletedTasksView);
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
        completedIndividualTaskPredictedDuration_TextInputLayout = findViewById(R.id.completedIndividualTaskPredictedDuration_TextInputLayout);
        completedIndividualTaskPredictedDurationUnits_TextInputLayout = findViewById(R.id.completedIndividualTaskPredictedDurationUnits_TextInputLayout);


        completedIndividualTaskTitle_TextInputEdit = findViewById(R.id.completedIndividualTaskTitle_TextInputEdit);
        completedIndividualTaskDescription_TextInputEdit = findViewById(R.id.completedIndividualTaskDescription_TextInputEdit);
        completedIndividualTaskCategory_AutoCompleteTextView = findViewById(R.id.completedIndividualTaskCategory_TextInputEdit);
        completedIndividualTaskBills_TextInputEdit = findViewById(R.id.completedIndividualTaskBills_TextInputEdit);
        completedIndividualTaskDateDeadline_TextInputEdit = findViewById(R.id.completedIndividualTaskDateDeadline_TextInputEdit);
        completedIndividualTaskTimeDeadline_TextInputEdit = findViewById(R.id.completedIndividualTaskTimeDeadline_TextInputEdit);
        completedIndividualTaskPredictedDuration_TextInputEditText = findViewById(R.id.completedIndividualTaskPredictedDuration_TextInputEditText);
        completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView = findViewById(R.id.completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView);

        btnCompletedTaskSave = findViewById(R.id.btnCompletedTaskSave);


        task2RescheduleId = getIntent().getLongExtra("task2RescheduleId", -1);

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId",null));

        dbHelper = new DBHelper(this);

        //obtain taskId
        randTaskId = returnTaskId(task2RescheduleId,randUserId);



        //populate the views with details from db on task to reschedule
        setTextOnViews();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        completedIndividualTaskCategory_AutoCompleteTextView.setAdapter(adapter);
        ArrayAdapter<CharSequence> unitsCompletedTasksAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.durationUnits, android.R.layout.simple_dropdown_item_1line);
        completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView.setAdapter(unitsCompletedTasksAdapter);
        completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> completedIndividualTaskPredictedDurationUnits_str = (String) adapterView.getItemAtPosition(i));

        completedIndividualTaskTimeDeadline_TextInputEdit.setOnClickListener(view -> selectTime());
        completedIndividualTaskDateDeadline_TextInputEdit.setOnClickListener(view -> selectDate());



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
        btnCompletedTaskSave.setOnClickListener(view -> {

            completedIndividualTaskTitle_str = Objects.requireNonNull(completedIndividualTaskTitle_TextInputEdit.getText()).toString().trim();
            completedIndividualTaskDescription_str = Objects.requireNonNull(completedIndividualTaskDescription_TextInputEdit.getText()).toString().trim();
            completedIndividualTaskCategory_str = completedIndividualTaskCategory_AutoCompleteTextView.getText().toString().trim();
            completedIndividualTaskBills_str = Objects.requireNonNull(completedIndividualTaskBills_TextInputEdit.getText()).toString().trim();
            completedIndividualTaskDateDeadline_str = Objects.requireNonNull(completedIndividualTaskDateDeadline_TextInputEdit.getText()).toString().trim();
            completedIndividualTaskTimeDeadline_str = Objects.requireNonNull(completedIndividualTaskTimeDeadline_TextInputEdit.getText()).toString().trim();
            completedIndividualTaskPredictedDuration_str = Objects.requireNonNull(completedIndividualTaskPredictedDuration_TextInputEditText.getText()).toString().trim();
            completedIndividualTaskPredictedDurationUnits_str = completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView.getText().toString().trim();

            if (!completedIndividualTaskTitle_str.isEmpty()){
                if (!completedIndividualTaskDescription_str.isEmpty()){
                    if (!completedIndividualTaskCategory_str.isEmpty() ){
                        if ( (completedIndividualTaskCategory_str.equals("Shopping") || completedIndividualTaskCategory_str.equals("Work") || completedIndividualTaskCategory_str.equals("School") || completedIndividualTaskCategory_str.equals("Business") || completedIndividualTaskCategory_str.equals("Home") )){
                            if (!completedIndividualTaskBills_str.isEmpty()){
                                if (HouseOfCommons.priceCheck(completedIndividualTaskBills_str)){
                                    AddTasks addTasks = new AddTasks();
                                    addTasks.willPriceFormat(completedIndividualTaskBills_str);
                                    if (!completedIndividualTaskPredictedDuration_str.isEmpty()){
                                        if(HouseOfCommons.durationCheck(completedIndividualTaskPredictedDuration_str)){
                                            if (!completedIndividualTaskPredictedDurationUnits_str.isEmpty()){
                                                if (!completedIndividualTaskDateDeadline_str.isEmpty()){
                                                    if (HouseOfCommons.dateCheck(completedIndividualTaskDateDeadline_str)){
                                                        if (!completedIndividualTaskTimeDeadline_str.isEmpty()){
                                                            if (HouseOfCommons.timeCheck(completedIndividualTaskTimeDeadline_str)){

                                                                String formattedDate = returnFormattedDateTime(completedIndividualTaskDateDeadline_str,completedIndividualTaskTimeDeadline_str);

                                                                if (willDateFormat(formattedDate)){

                                                                    LocalDateTime date_now = LocalDateTime.now();

                                                                    if (completedTaskDateDeadline2Insert.compareTo(date_now) > 0 || completedTaskDateDeadline2Insert.compareTo(date_now) == 0){

                                                                        String duration = HouseOfCommons.processPredictedDuration(completedIndividualTaskPredictedDuration_str,completedIndividualTaskPredictedDurationUnits_str);
                                                                        boolean b =false;
                                                                        Double randomTaskId = HouseOfCommons.generateRandomId();
                                                                        Integer defaultTaskState = 0;
                                                                        String newParentTaskId = String.format(new Locale("en", "KE"),"%s:%s",parentTaskId,randTaskId);
                                                                        try {
                                                                            b = dbHelper.insertTasks(randomTaskId,randUserId,completedIndividualTaskTitle_str,completedIndividualTaskDescription_str,completedIndividualTaskCategory_str
                                                                                    , addTasks.Price, completedTaskDateDeadline2Insert,new Date().getTime(),duration,defaultTaskState,newParentTaskId);
                                                                        }catch (Exception e){
                                                                            e.printStackTrace();
                                                                        }
                                                                        if (b){
                                                                            Toast.makeText(rescheduleCompletedTask.this, "Rescheduled successfully", Toast.LENGTH_SHORT).show();
                                                                            Intent back2CompletedTasksView = new Intent(getApplicationContext(), tasks.class);
                                                                            back2CompletedTasksView.putExtra("tempCategory",completedIndividualTaskCategory_str);
                                                                            startActivity(back2CompletedTasksView);
                                                                        }else{
                                                                            Toast.makeText(rescheduleCompletedTask.this, "Rescheduling failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }else {
                                                                        completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(true);
                                                                        completedIndividualTaskTimeDeadline_TextLayout.setError("Choose a later time");
                                                                        completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                                        completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                                    }

                                                                }else{
                                                                    Toast.makeText(rescheduleCompletedTask.this, "Error formatting date", Toast.LENGTH_LONG).show();
                                                                }
                                                            }else{
                                                                completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                                completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                                completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                                completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                                completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                                                completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(true);
                                                                completedIndividualTaskTimeDeadline_TextLayout.setError("Enter a proper date");
                                                                completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                                completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                            }
                                                        }else{
                                                            completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                            completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                            completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                            completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                            completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                                            completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(true);
                                                            completedIndividualTaskTimeDeadline_TextLayout.setError("Choose a time");
                                                            completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                            completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);

                                                        }
                                                    }else{
                                                        completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                        completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                        completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                        completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                        completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(true);
                                                        completedIndividualTaskDateDeadline_TextLayout.setError("Choose a date");
                                                        completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                                        completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                        completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                    }
                                                }else{
                                                    completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                    completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                    completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                    completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                    completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(true);
                                                    completedIndividualTaskDateDeadline_TextLayout.setError("Tap to choose a date");
                                                    completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                                    completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                    completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                }
                                            }else{
                                                completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                                completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(true);
                                                completedIndividualTaskPredictedDurationUnits_TextInputLayout.setError("Choose a unit of duration");

                                            }

                                        }else{
                                            completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                            completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(true);
                                            completedIndividualTaskPredictedDuration_TextInputLayout.setError("Enter a valid duration amount");
                                            completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                        }

                                    }else{
                                        completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                        completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(true);
                                        completedIndividualTaskPredictedDuration_TextInputLayout.setError("Empty predicted duration");
                                        completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                    }


                                }else{
                                    completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                    completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                    completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                    completedIndividualTaskBills_TextLayout.setErrorEnabled(true);
                                    completedIndividualTaskBills_TextLayout.setError("Enter a money figure");
                                    completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                    completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                    completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                    completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                }
                            }else{
                                completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                                completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                                completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                                completedIndividualTaskBills_TextLayout.setErrorEnabled(true);
                                completedIndividualTaskCategory_TextLayout.setError("Enter a price");
                                completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                            }
                        }else{
                            completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                            completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                            completedIndividualTaskCategory_TextLayout.setErrorEnabled(true);
                            completedIndividualTaskCategory_TextLayout.setError("Choose from the dropdown");
                            completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                            completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                            completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                            completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                            completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                        }

                    }else{
                        completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                        completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                        completedIndividualTaskCategory_TextLayout.setErrorEnabled(true);
                        completedIndividualTaskCategory_TextLayout.setError("Choose a category");
                        completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                        completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                        completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                        completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                        completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                    }
                }else{
                    completedIndividualTaskTitle_TextLayout.setErrorEnabled(false);
                    completedIndividualTaskDescription_TextLayout.setError("Enter a description");
                    completedIndividualTaskDescription_TextLayout.setErrorEnabled(true);
                    completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                    completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                    completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                    completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                    completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                    completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                }
            }else{
                completedIndividualTaskTitle_TextLayout.setErrorEnabled(true);
                completedIndividualTaskTitle_TextLayout.setError("Enter a task title");
                completedIndividualTaskDescription_TextLayout.setErrorEnabled(false);
                completedIndividualTaskCategory_TextLayout.setErrorEnabled(false);
                completedIndividualTaskBills_TextLayout.setErrorEnabled(false);
                completedIndividualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                completedIndividualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                completedIndividualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                completedIndividualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
            }


        });



    }
    private boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        try {
            completedTaskDateDeadline2Insert = HouseOfCommons.getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
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

    private void setTextOnViews(){
        //instantiate variables
        String taskTitle, taskDescription, taskCategory, taskAssociatedPrice,taskDeadline,taskPredictedDuration;


            try {
                //assign variables with data
                Bundle bundle = getIntent().getBundleExtra("rescheduleCompletedTaskDetails");
                taskTitle = bundle.getString("taskTitle");
                taskDescription = bundle.getString("taskDescription");
                taskCategory = bundle.getString("taskCategory");
                taskAssociatedPrice = bundle.getString("taskAssociatedPrice");
                taskDeadline = bundle.getString("taskDeadline");
                taskPredictedDuration = bundle.getString("taskPredictedDuration");
                parentTaskId = bundle.getString("parentTaskId");


                String[] deadline = taskDeadline.split("T",2 );
                String[] duration = HouseOfCommons.processPredictedTaskDurationForPopulation(taskPredictedDuration);

                completedIndividualTaskTitle_TextInputEdit.setText(taskTitle);
                completedIndividualTaskDescription_TextInputEdit.setText(taskDescription);
                completedIndividualTaskCategory_AutoCompleteTextView.setText(taskCategory);
                completedIndividualTaskBills_TextInputEdit.setText(taskAssociatedPrice);
                completedIndividualTaskDateDeadline_TextInputEdit.setText(deadline[0]);
                completedIndividualTaskTimeDeadline_TextInputEdit.setText(deadline[1]);
                completedIndividualTaskPredictedDuration_TextInputEditText.setText(duration[0]);
                completedIndividualTaskPredictedDurationUnits_AutoCompleteTextView.setText(duration[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
            if (hour1 < 10){
                time2Insert = "0" + hour1 + ":" + minute1;
            }else{
                time2Insert = hour1 + ":" + minute1;
            }
            HouseOfCommons houseOfCommons = new HouseOfCommons();
            completedIndividualTaskTimeDeadline_TextInputEdit.setText(houseOfCommons.FormatTime(hour1, minute1));
        },hour, minute,false);
        timePickerDialog.show();
    }
    private void selectDate(){
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(rescheduleCompletedTask.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
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
            completedIndividualTaskDateDeadline_TextInputEdit.setText(String.format(new Locale("en","KE"),"%s-%s-%s",formattedDay,formattedMonth,year));
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    private String returnFormattedDateTime(String Date, String Time){
        //this is necessary when the user doesn't make a change on the date
        /*
         * The db output of date is yyyy-MM-dd while the dateDialog one is dd-MM-yyyy, so this is there to cater
         * for all situations, if the first digit after stripping the date is less than 31 then format is dd-MM-yyyy meaning user has changed
         * the date , however if the first digit is greater than 31 then the format is yyyy-MM-dd meaning the user hasn't made any change to the date.
         * It is as from the db
         * */
        String new_date = HouseOfCommons.parseDate(Date);

        /*
         * Formatting dates are tricky.
         * What the code below does is take the time section HH:ss PM/AM split it first to obtain "HH" and "mm PM".
         * The further split "mm PM/AM" to "mm" and "PM/AM"
         * Format the hour by adding a zero when hour is below 9, then split "mm PM" to obtain minutes
         *
         * From db the time is well formatted however when the user selects a new date, it has to be reformatted
         * */
        String[] timeDeh = Time.split(":", 2);

        String new_hour, new_minute;
        if(Integer.parseInt(timeDeh[0]) < 10 && timeDeh[0].length() < 2){
            new_hour = "0" + timeDeh[0];
        }else{
            new_hour = timeDeh[0];
        }
        if(Integer.parseInt(timeDeh[1].split(" ", 2)[0]) < 10 && timeDeh[1].split(" ", 2)[0].length() < 2){
            new_minute = "0" + timeDeh[1].split(" ", 2)[0];
        }else{
            new_minute = timeDeh[1].split(" ", 2)[0];
        }

        return new_date + " " + new_hour + ":" + new_minute;
    }
}