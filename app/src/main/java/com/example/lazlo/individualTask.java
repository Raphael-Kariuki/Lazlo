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
import android.view.Menu;
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
import java.util.Locale;
import java.util.Objects;

public class individualTask extends AppCompatActivity {
  TextInputLayout individualTaskTitle_TextLayout,individualTaskDescription_TextLayout,individualTaskCategory_TextLayout,
          individualTaskBills_TextLayout,individualTaskDateDeadline_TextLayout,individualTaskTimeDeadline_TextLayout
          ,individualTaskPredictedDuration_TextInputLayout,individualTaskPredictedDurationUnits_TextInputLayout;
  TextInputEditText individualTaskTitle_TextInputEdit, individualTaskDescription_TextInputEdit,
          individualTaskBills_TextInputEdit,individualTaskDateDeadline_TextInputEdit,
          individualTaskTimeDeadline_TextInputEdit,individualTaskPredictedDuration_TextInputEditText;
  AutoCompleteTextView individualTaskCategory_TextInputEdit,individualTaskPredictedDurationUnits_AutoCompleteTextView;
  MaterialButton btnSave;
  DBHelper dbHelper;
  long currentId;
  Cursor cursor;
  String selectedCategory,timeDate2update,Titre, Description, Category, Bills, Deadline,predictedDurationUnits,predictedDurationFromDb,predictedDuration;
  LocalDateTime selected_date;
  Double randomTaskId, randUserId;
  SharedPreferences spf;
  boolean f;
  String updateDateTime,updateTitle,updateDescription,updateCategory,updatePrice;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), PendingTasks.class));
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.individual_task_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.startTask:
                startTaskAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_task);


        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("View and edit individual task");

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId",null));

        individualTaskTitle_TextInputEdit = findViewById(R.id.individualTaskTitle_TextInputEdit);
        individualTaskDescription_TextInputEdit = findViewById(R.id.individualTaskDescription_TextInputEdit);
        individualTaskCategory_TextInputEdit = findViewById(R.id.individualTaskCategory_TextInputEdit);
        individualTaskBills_TextInputEdit = findViewById(R.id.individualTaskBills_TextInputEdit);
        individualTaskDateDeadline_TextInputEdit = findViewById(R.id.individualTaskDateDeadline_TextInputEdit);
        individualTaskTimeDeadline_TextInputEdit = findViewById(R.id.individualTaskTimeDeadline_TextInputEdit);
        individualTaskPredictedDuration_TextInputEditText = findViewById(R.id.individualTaskPredictedDuration_TextInputEditText);
        individualTaskPredictedDurationUnits_AutoCompleteTextView = findViewById(R.id.individualTaskPredictedDurationUnits_AutoCompleteTextView);

        individualTaskTitle_TextLayout = findViewById(R.id.individualTaskTitle_TextLayout);
        individualTaskDescription_TextLayout = findViewById(R.id.individualTaskDescription_TextLayout);
        individualTaskCategory_TextLayout = findViewById(R.id.individualTaskCategory_TextLayout);
        individualTaskBills_TextLayout = findViewById(R.id.individualTaskBills_TextLayout);
        individualTaskDateDeadline_TextLayout = findViewById(R.id.individualTaskDateDeadline_TextLayout);
        individualTaskTimeDeadline_TextLayout = findViewById(R.id.individualTaskTimeDeadline_TextLayout);
        individualTaskPredictedDuration_TextInputLayout = findViewById(R.id.individualTaskPredictedDuration_TextInputLayout);
        individualTaskPredictedDurationUnits_TextInputLayout = findViewById(R.id.individualTaskPredictedDurationUnits_TextInputLayout);

        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);







        //populate category dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        individualTaskCategory_TextInputEdit.setAdapter(adapter);
        individualTaskCategory_TextInputEdit.setOnItemClickListener((adapterView, view, i, l) -> selectedCategory = (String) adapterView.getItemAtPosition(i));

        ArrayAdapter<CharSequence> unitsIndividualTaskEditAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.durationUnits, android.R.layout.simple_dropdown_item_1line);
        individualTaskPredictedDurationUnits_AutoCompleteTextView.setAdapter(unitsIndividualTaskEditAdapter);
        individualTaskPredictedDurationUnits_AutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> predictedDurationUnits = (String) adapterView.getItemAtPosition(i));

        currentId = this.getIntent().getLongExtra("my_id_extra",-1);
        if (currentId < 0){
            //do something as invalid id passed
            finish();
        }else {
            try {
                showData();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }



        individualTaskTimeDeadline_TextInputEdit.setOnClickListener(view -> selectTime());
        individualTaskDateDeadline_TextInputEdit.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(individualTask.this, (view1, year, monthOfYear, dayOfMonth) -> {
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
                individualTaskDateDeadline_TextInputEdit.setText(String.format(new Locale("en","KE"),"%s-%s-%s",formattedDay,formattedMonth,year));
            }, mYear,mMonth, mDay);
            datePickerDialog.show();
        });
        btnSave.setOnClickListener(view -> {
            updateTitle = Objects.requireNonNull(individualTaskTitle_TextInputEdit.getText()).toString().trim();
            updateDescription = Objects.requireNonNull(individualTaskDescription_TextInputEdit.getText()).toString().trim();
            updateCategory = individualTaskCategory_TextInputEdit.getText().toString().trim();
            updatePrice = Objects.requireNonNull(individualTaskBills_TextInputEdit.getText()).toString().trim();
            String updateDate = Objects.requireNonNull(individualTaskDateDeadline_TextInputEdit.getText()).toString().trim();
            String updateTime = Objects.requireNonNull(individualTaskTimeDeadline_TextInputEdit.getText()).toString().trim();
            predictedDuration = Objects.requireNonNull(individualTaskPredictedDuration_TextInputEditText.getText()).toString().trim();


            /*
            * Formatting dates are tricky.
            * What the code below does is take the time section HH:ss PM/AM split it first to obtain "HH" and "mm PM".
            * The further split "mm PM/AM" to "mm" and "PM/AM"
            * Format the hour by adding a zero when hour is below 9, then split "mm PM" to obtain minutes
            * */
            String[] timeDeh = updateTime.split(":", 2);

            String new_hour, new_minute;
            if(Integer.parseInt(timeDeh[0]) < 10 && timeDeh[0].length() < 2){
                new_hour = "0" + timeDeh[0];
            }else{
                new_hour = timeDeh[0];
            }
            new_minute = timeDeh[1].split(" ", 2)[0];
            //===============================

            //this is necessary when the user doesn't make a change on the date
            /*
            * The db output of date is yyyy-MM-dd while the dateDialog one is dd-MM-yyyy, so this is there to cater
            * for all situations, if the first digit after stripping the date is less than 31 then format is dd-MM-yyyy meaning user has changed
            * the date , however if the first digit is greater than 31 then the format is yyyy-MM-dd meaning the user hasn't made any change to the date.
            * It is as from the db
            * */
            String new_date = HouseOfCommons.parseDate(updateDate);
            //combine the date and time ready for formatting

            updateDateTime = new_date + " " +new_hour + ":" + new_minute ;
            if (!updateTitle.isEmpty()){
                if (!updateDescription.isEmpty()){
                    if (!updateCategory.isEmpty()){
                        AddTasks addTasks = new AddTasks();
                        if (!updatePrice.isEmpty()){
                            if (HouseOfCommons.priceCheck(updatePrice)){
                                addTasks.willPriceFormat(updatePrice);
                                if (!predictedDuration.isEmpty()){
                                    if (!predictedDurationUnits.isEmpty()){
                                        if (!updateDate.isEmpty()){
                                            if (!updateTime.isEmpty()){
                                                if (willDateFormat(updateDateTime)){
                                                    LocalDateTime date_now = LocalDateTime.now();
                                                    if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0){
                                                        String duration = HouseOfCommons.processPredictedDuration(predictedDuration,predictedDurationUnits);
                                                        try {
                                                            f = dbHelper.updateTask(currentId,randUserId,updateTitle,updateDescription,updateCategory,updatePrice,selected_date, duration);
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                        if (f){
                                                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Update failure", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }else{
                                                    individualTaskTitle_TextLayout.setErrorEnabled(false);
                                                    individualTaskDescription_TextLayout.setErrorEnabled(false);
                                                    individualTaskCategory_TextLayout.setErrorEnabled(false);
                                                    individualTaskBills_TextLayout.setErrorEnabled(false);
                                                    individualTaskDateDeadline_TextLayout.setErrorEnabled(true);
                                                    individualTaskDateDeadline_TextLayout.setError("Select new date");
                                                    individualTaskTimeDeadline_TextLayout.setErrorEnabled(true);
                                                    individualTaskTimeDeadline_TextLayout.setError("Select new time");
                                                    individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                    individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                }
                                            }else{
                                                individualTaskTitle_TextLayout.setErrorEnabled(false);
                                                individualTaskDescription_TextLayout.setErrorEnabled(false);
                                                individualTaskCategory_TextLayout.setErrorEnabled(false);
                                                individualTaskBills_TextLayout.setErrorEnabled(false);
                                                individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                                individualTaskTimeDeadline_TextLayout.setErrorEnabled(true);
                                                individualTaskTimeDeadline_TextLayout.setError("Select time");
                                                individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                                individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                            }
                                        }else{
                                            individualTaskTitle_TextLayout.setErrorEnabled(false);
                                            individualTaskDescription_TextLayout.setErrorEnabled(false);
                                            individualTaskCategory_TextLayout.setErrorEnabled(false);
                                            individualTaskBills_TextLayout.setErrorEnabled(false);
                                            individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                            individualTaskDateDeadline_TextLayout.setError("Select time");
                                            individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                            individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                            individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                        }
                                    }else{
                                        individualTaskTitle_TextLayout.setErrorEnabled(false);
                                        individualTaskDescription_TextLayout.setErrorEnabled(false);
                                        individualTaskCategory_TextLayout.setErrorEnabled(false);
                                        individualTaskBills_TextLayout.setErrorEnabled(false);
                                        individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                        individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                        individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                        individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                        individualTaskPredictedDurationUnits_TextInputLayout.setError("Choose a unit of duration");

                                    }

                                }else{
                                    individualTaskTitle_TextLayout.setErrorEnabled(false);
                                    individualTaskDescription_TextLayout.setErrorEnabled(false);
                                    individualTaskCategory_TextLayout.setErrorEnabled(false);
                                    individualTaskBills_TextLayout.setErrorEnabled(false);
                                    individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                    individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                    individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(true);
                                    individualTaskPredictedDuration_TextInputLayout.setError("Empty predicted duration");
                                    individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                }

                            }else{
                                individualTaskTitle_TextLayout.setErrorEnabled(false);
                                individualTaskDescription_TextLayout.setErrorEnabled(false);
                                individualTaskCategory_TextLayout.setErrorEnabled(false);
                                individualTaskBills_TextLayout.setErrorEnabled(true);
                                individualTaskBills_TextLayout.setError("Wrong price syntax");
                                individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                                individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                                individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                                individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                            }
                        }else{
                            individualTaskTitle_TextLayout.setErrorEnabled(false);
                            individualTaskDescription_TextLayout.setErrorEnabled(false);
                            individualTaskCategory_TextLayout.setErrorEnabled(false);
                            individualTaskBills_TextLayout.setErrorEnabled(true);
                            individualTaskBills_TextLayout.setError("Enter a price figure");
                            individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                            individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                            individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                            individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                        }

                    }else{
                        individualTaskTitle_TextLayout.setErrorEnabled(false);
                        individualTaskDescription_TextLayout.setErrorEnabled(false);
                        individualTaskCategory_TextLayout.setErrorEnabled(true);
                        individualTaskCategory_TextLayout.setError("Choose a category");
                        individualTaskBills_TextLayout.setErrorEnabled(false);
                        individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                        individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                        individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                        individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                    }
                }else {
                    individualTaskTitle_TextLayout.setErrorEnabled(false);
                    individualTaskDescription_TextLayout.setError("Empty description");
                    individualTaskDescription_TextLayout.setErrorEnabled(true);
                    individualTaskCategory_TextLayout.setErrorEnabled(false);
                    individualTaskBills_TextLayout.setErrorEnabled(false);
                    individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                    individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                    individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                    individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                }
            }else {
                individualTaskTitle_TextLayout.setErrorEnabled(true);
                individualTaskTitle_TextLayout.setError("Empty title");
                individualTaskDescription_TextLayout.setErrorEnabled(false);
                individualTaskCategory_TextLayout.setErrorEnabled(false);
                individualTaskBills_TextLayout.setErrorEnabled(false);
                individualTaskDateDeadline_TextLayout.setErrorEnabled(false);
                individualTaskTimeDeadline_TextLayout.setErrorEnabled(false);
                individualTaskPredictedDuration_TextInputLayout.setErrorEnabled(false);
                individualTaskPredictedDurationUnits_TextInputLayout.setErrorEnabled(false);
            }




        });


    }
    public void showData(){
        String regex;
        try {
            cursor = dbHelper.getTaskById(currentId, randUserId);
        }catch (Exception e){
            Toast.makeText(this,"Error " + e + "occurred", Toast.LENGTH_LONG).show();
        }
        System.out.println("done...");
        if (cursor.moveToFirst()){
            randomTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("randTaskId"));

            Titre = cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle"));
            individualTaskTitle_TextInputEdit.setText(Titre);


            Description = cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription"));
            individualTaskDescription_TextInputEdit.setText(Description);

            Category = cursor.getString(cursor.getColumnIndexOrThrow("TaskCategory"));
            individualTaskCategory_TextInputEdit.setText(Category);

            Bills = cursor.getString(cursor.getColumnIndexOrThrow("TaskAssociatedPrice"));
            individualTaskBills_TextInputEdit.setText(Bills);

            String timeDateToFormat = cursor.getString(cursor.getColumnIndexOrThrow("TaskDeadline"));


            if(timeDateToFormat.contains("T")){
                regex = "T";
            }else{
                regex = " ";
            }
            String[] dateTime = timeDateToFormat.split(regex, 2);
            individualTaskDateDeadline_TextInputEdit.setText(dateTime[0]);
            individualTaskTimeDeadline_TextInputEdit.setText(dateTime[1]);

            Deadline = dateTime[0] + " " + dateTime[1];

            predictedDurationFromDb = cursor.getString(cursor.getColumnIndexOrThrow("TaskPredictedDuration"));
            String[] actualDurations = HouseOfCommons.processPredictedTaskDurationForPopulation(predictedDurationFromDb);
            individualTaskPredictedDuration_TextInputEditText.setText(String.format(new Locale("en", "KE"),"%s",actualDurations[0]));
            individualTaskPredictedDurationUnits_AutoCompleteTextView.setText(String.format(new Locale("en", "KE"),"%s",actualDurations[1]));

        }
        cursor.close();


    }
    private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
            if (hour1 < 10){
                timeDate2update = "0" + hour1 + ":" + minute1;
            }else{
                timeDate2update = hour1 + ":" + minute1;
            }
            HouseOfCommons houseOfCommons = new HouseOfCommons();
            individualTaskTimeDeadline_TextInputEdit.setText(houseOfCommons.FormatTime(hour1, minute1));
        },hour, minute,false);
        timePickerDialog.show();
    }

    private boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            selected_date = HouseOfCommons.getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }

private void startTaskAction(){
    Intent startTask = new Intent(getApplicationContext(), performTask.class );
    startTask.putExtra("taskId", currentId);
    startTask.putExtra("randomTaskId", randomTaskId);
    /*
     * Why checks, to counter the situation whereby a task is edited and performed on the fly
     * Without performing this checks, the updated version of the task won't be populated showing wrong facts
     * */
    if (updateDateTime != null){
        startTask.putExtra("taskTitle", updateTitle);
    }else{
        startTask.putExtra("taskTitle", Titre);
    }

    if (updateDateTime != null){
        startTask.putExtra("taskDescription", updateDescription);
    }else{
        startTask.putExtra("taskDescription", Description);
    }

    if (updateDateTime != null){
        startTask.putExtra("taskCategory", updateCategory);
    }else{
        startTask.putExtra("taskCategory", Category);
    }

    if (updateDateTime != null){
        startTask.putExtra("taskBills", updatePrice);
    }else{
        startTask.putExtra("taskBills", Bills);
    }

    if (updateDateTime != null){
        startTask.putExtra("taskDeadline", updateDateTime);
    }else{
        startTask.putExtra("taskDeadline", Deadline);
    }

    startActivity(startTask);
}



    }
